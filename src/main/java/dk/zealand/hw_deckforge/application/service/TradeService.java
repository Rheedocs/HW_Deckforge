package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.application.validation.TradeValidator;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.TradeCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.TradeRole;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facade for bytteforløbet. Controlleren kalder propose/accept/complete,
 * servicen håndterer repositories, validering og statusflow internt.
 */
@Service
public class TradeService {

    private final ITradeRepository tradeRepository;
    private final IPlayerCardRepository playerCardRepository;
    private final ITradeCardRepository tradeCardRepository;
    private final IPlayerRepository playerRepository;
    private final TradeValidator tradeValidator;

    public TradeService(ITradeRepository tradeRepository, IPlayerCardRepository playerCardRepository,
                        ITradeCardRepository tradeCardRepository, IPlayerRepository playerRepository,
                        TradeValidator tradeValidator) {
        this.tradeRepository = tradeRepository;
        this.playerCardRepository = playerCardRepository;
        this.tradeCardRepository = tradeCardRepository;
        this.playerRepository = playerRepository;
        this.tradeValidator = tradeValidator;
    }

    // --- Forespørgsler ---

    public Trade getById(int id) {
        validateId(id, "Trade-id");
        Trade trade = tradeRepository.findById(id);
        if (trade == null) throw new NotFoundException("Trade med id " + id + " findes ikke");
        return trade;
    }

    public List<Trade> getAllByPlayerId(int playerId) {
        validateId(playerId, "Player-id");
        return tradeRepository.findByPlayerId(playerId);
    }

    /** @return bytter med status ACCEPTED der afventer fysisk gennemførelse. */
    public List<Trade> getActiveByPlayerId(List<Trade> all, int playerId) {
        validateId(playerId, "Player-id");
        List<Trade> active = new ArrayList<>();
        for (Trade trade : all) {
            if (isParticipant(trade, playerId) && trade.getStatus() == TradeStatus.ACCEPTED) active.add(trade);
        }
        return active;
    }

    public int countActiveAcceptedTrades(int playerId) {
        validateId(playerId, "Player-id");
        return getActiveByPlayerId(tradeRepository.findByPlayerId(playerId), playerId).size();
    }

    /** @return antal indgående ubesvarede forslag. Bruges til indikatoren på forsiden. */
    public int countIncomingPendingTrades(int playerId) {
        validateId(playerId, "Player-id");
        return tradeRepository.findIncomingByPlayerId(playerId).size();
    }

    /** @return afventende forslag sendt af spilleren (PENDING). */
    public List<Trade> getSentByPlayerId(List<Trade> all, int playerId) {
        validateId(playerId, "Player-id");
        List<Trade> sent = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getProposerId() == playerId && trade.getStatus() == TradeStatus.PENDING) sent.add(trade);
        }
        return sent;
    }

    /** @return afventende forslag modtaget af spilleren (PENDING). */
    public List<Trade> getReceivedByPlayerId(List<Trade> all, int playerId) {
        validateId(playerId, "Player-id");
        List<Trade> received = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getReceiverId() == playerId && trade.getStatus() == TradeStatus.PENDING) received.add(trade);
        }
        return received;
    }

    /** @return afsluttede bytter med status COMPLETED, DECLINED, CANCELLED eller EXPIRED. */
    public List<Trade> getHistoryByPlayerId(List<Trade> all) {
        List<Trade> history = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getStatus() != TradeStatus.PENDING && trade.getStatus() != TradeStatus.ACCEPTED) history.add(trade);
        }
        return history;
    }

    /** Bygger map fra spiller-id til Player for O(1) opslag i templates uden databasekald pr. trade. */
    public Map<Integer, Player> getPlayerMap(List<Trade> trades) {
        Map<Integer, Player> map = new HashMap<>();
        for (Trade trade : trades) {
            addPlayerToMap(map, trade.getProposerId());
            addPlayerToMap(map, trade.getReceiverId());
        }
        return map;
    }

    /** Bygger map fra trade-id til liste af TradeCard for effektiv visning af byttedetaljer. */
    public Map<Integer, List<TradeCard>> getTradeCardMap(List<Trade> trades) {
        Map<Integer, List<TradeCard>> map = new HashMap<>();
        for (Trade trade : trades) {
            map.put(trade.getId(), tradeCardRepository.findByTradeId(trade.getId()));
        }
        return map;
    }

    public List<Integer> getPlayerCardIds(Map<Integer, List<TradeCard>> tradeCardMap) {
        List<Integer> ids = new ArrayList<>();
        for (List<TradeCard> cards : tradeCardMap.values()) {
            for (TradeCard tc : cards) {
                ids.add(tc.getPlayerCardId());
            }
        }
        return ids;
    }

    // --- Livscyklus ---

    /** Opretter forslag og reserverer begge parters kort. Udløber automatisk efter 24 timer. */
    public void propose(int proposerId, int receiverId, List<Integer> proposerCardIds, List<Integer> receiverCardIds) {
        validateProposal(proposerId, receiverId);
        validateTradeCards(proposerCardIds, receiverCardIds);
        Trade trade = new Trade(0, proposerId, receiverId, TradeStatus.PENDING, LocalDateTime.now(),
                LocalDateTime.now().plusHours(24), false, false);
        proposeWithCards(trade, proposerCardIds.getFirst(), receiverCardIds.getFirst());
    }

    /** Accepterer forslag. Byttet gennemføres først når begge bekræfter via complete(). */
    public void accept(int tradeId) {
        Trade trade = getById(tradeId);
        validatePending(trade, "accepteres");
        trade.accept();
        tradeRepository.update(trade);
    }

    /** Afviser forslag og frigiver kortreservationer. */
    public void decline(int tradeId) {
        Trade trade = getById(tradeId);
        validatePending(trade, "afvises");
        trade.decline();
        tradeRepository.update(trade);
    }

    /** Annullerer forslag og frigiver kortreservationer. */
    public void cancel(int tradeId) {
        Trade trade = getById(tradeId);
        validatePending(trade, "annulleres");
        trade.cancel();
        tradeRepository.update(trade);
    }

    /** Dobbelt bekræftelse. Sætter COMPLETED og opdaterer samlinger kun når begge har bekræftet. */
    public void complete(int tradeId, int requestingPlayerId) {
        Trade trade = getById(tradeId);
        validateComplete(trade, requestingPlayerId);
        confirmTradeSide(trade, requestingPlayerId);
        if (trade.isFullyConfirmed()) {
            trade.complete();
            removeFromTradeList(tradeId);
        }
        tradeRepository.update(trade);
    }

    /** Frigiver reservationer på forslag ældre end 24 timer. Køres dagligt via @Scheduled. */
    @Scheduled(fixedRate = 86400000)
    public void expireOldTrades() {
        tradeRepository.expireOldTrades();
    }

    // --- Validering ---

    public void validateProposal(int proposerId, int receiverId) {
        validateId(proposerId, "Proposer-id");
        validateId(receiverId, "Receiver-id");
        if (proposerId == receiverId) throw new ValidationException("Du kan ikke bytte med dig selv");

        Player proposer = playerRepository.findById(proposerId);
        Player receiver = playerRepository.findById(receiverId);
        if (receiver == null) throw new IllegalArgumentException("Modtager findes ikke");
        if (proposer == null) throw new IllegalArgumentException("Afsender findes ikke");
        if (receiver.getCollectionVisibility() == CollectionVisibility.PRIVATE)
            throw new AccessDeniedException("Denne spillers profil er privat");
        if (playerCardRepository.findForTradeByPlayerId(proposerId).isEmpty())
            throw new ValidationException("Du har ingen kort markeret til bytte");
        if (playerCardRepository.findForTradeByPlayerId(receiverId).isEmpty())
            throw new ValidationException("Denne spiller har ingen kort markeret til bytte");
    }
    
    private void validateTradeCards(List<Integer> proposerCardIds, List<Integer> receiverCardIds) {
        if (proposerCardIds == null || proposerCardIds.size() != 1)
            throw new IllegalArgumentException("Et trade skal indeholde et kort fra begge parter");
        if (receiverCardIds == null || receiverCardIds.size() != 1)
            throw new IllegalArgumentException("Et trade skal indeholde et kort fra begge parter");
    }

    private void validatePending(Trade trade, String handling) {
        if (trade.getStatus() != TradeStatus.PENDING) throw new IllegalArgumentException("Kun trades som er pending kan " + handling);
    }

    private void validateComplete(Trade trade, int requestingPlayerId) {
        validateId(requestingPlayerId, "Player-id");
        if (trade.getStatus() != TradeStatus.ACCEPTED) throw new IllegalArgumentException("Kun accepterede trades kan bekræftes som gennemført");
        if (!isParticipant(trade, requestingPlayerId)) throw new AccessDeniedException("Du er ikke en del af denne trade");
    }

    private void validateId(int id, String fieldName) {
        if (id <= 0) throw new IllegalArgumentException(fieldName + " skal være større end nul");
    }

    // --- Intern behandling ---

    private void proposeWithCards(Trade trade, int proposerCardId, int receiverCardId) {
        tradeValidator.validateProposerCard(trade.getProposerId(), proposerCardId);
        tradeValidator.validateReceiverCard(trade.getReceiverId(), receiverCardId);

        int tradeId = tradeRepository.save(trade);
        tradeCardRepository.save(new TradeCard(0, tradeId, proposerCardId, TradeRole.PROPOSER));
        tradeCardRepository.save(new TradeCard(0, tradeId, receiverCardId, TradeRole.RECEIVER));
    }

    private boolean isParticipant(Trade trade, int playerId) {
        return trade.getProposerId() == playerId || trade.getReceiverId() == playerId;
    }

    private void confirmTradeSide(Trade trade, int requestingPlayerId) {
        if (trade.getProposerId() == requestingPlayerId) trade.confirmProposer();
        else trade.confirmReceiver();
    }

    private void addPlayerToMap(Map<Integer, Player> map, int playerId) {
        if (!map.containsKey(playerId)) map.put(playerId, playerRepository.findById(playerId));
    }

    private void removeFromTradeList(int tradeId) {
        for (TradeCard tc : tradeCardRepository.findByTradeId(tradeId)) {
            PlayerCard pc = playerCardRepository.findById(tc.getPlayerCardId());
            if (pc != null) {
                pc.unmarkForTrade();
                playerCardRepository.update(pc);
            }
        }
    }
}




