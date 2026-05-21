package dk.zealand.hw_deckforge.application.service;

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
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end 0");
        return tradeRepository.findById(id);
    }

    public List<Trade> getAllByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Player id skal være større end 0");
        return tradeRepository.findByPlayerId(playerId);
    }

    public List<Trade> getActiveByPlayerId(List<Trade> all, int playerId) {
        List<Trade> active = new ArrayList<>();
        for (Trade trade : all) {
            if ((trade.getProposerId() == playerId || trade.getReceiverId() == playerId)
                    && trade.getStatus() == TradeStatus.ACCEPTED) {
                active.add(trade);
            }
        }
        return active;
    }

    public int countActiveAcceptedTrades(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Player id skal være større end 0");
        return getActiveByPlayerId(tradeRepository.findByPlayerId(playerId), playerId).size();
    }

    public int countIncomingPendingTrades(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Player id skal være større end 0");
        return tradeRepository.findIncomingByPlayerId(playerId).size();
    }

    public List<Trade> getSentByPlayerId(List<Trade> all, int playerId) {
        List<Trade> sent = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getProposerId() == playerId && trade.getStatus() == TradeStatus.PENDING) {
                sent.add(trade);
            }
        }
        return sent;
    }

    public Map<Integer, Player> getPlayerMap(List<Trade> trades) {
        Map<Integer, Player> map = new HashMap<>();
        for (Trade trade : trades) {
            if (!map.containsKey(trade.getProposerId()))
                map.put(trade.getProposerId(), playerRepository.findById(trade.getProposerId()));
            if (!map.containsKey(trade.getReceiverId()))
                map.put(trade.getReceiverId(), playerRepository.findById(trade.getReceiverId()));
        }
        return map;
    }

    public List<Trade> getReceivedByPlayerId(List<Trade> all, int playerId) {
        List<Trade> received = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getReceiverId() == playerId && trade.getStatus() == TradeStatus.PENDING) {
                received.add(trade);
            }
        }
        return received;
    }

    public List<Trade> getHistoryByPlayerId(List<Trade> all) {
        List<Trade> history = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getStatus() != TradeStatus.PENDING && trade.getStatus() != TradeStatus.ACCEPTED) {
                history.add(trade);
            }
        }
        return history;
    }

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

    public void accept(int tradeId) {
        Trade trade = getById(tradeId);
        validatePending(trade, "accepteres");
        trade.accept();
        tradeRepository.update(trade);
    }

    public void decline(int tradeId) {
        Trade trade = getById(tradeId);
        validatePending(trade, "afvises");
        trade.decline();
        tradeRepository.update(trade);
    }

    public void cancel(int tradeId) {
        Trade trade = getById(tradeId);
        validatePending(trade, "annulleres");
        trade.cancel();
        tradeRepository.update(trade);
    }

    public void complete(int tradeId, int requestingPlayerId) {
        Trade trade = getById(tradeId);
        validateComplete(trade, requestingPlayerId);
        if (trade.getProposerId() == requestingPlayerId) {
            trade.confirmProposer();
        } else {
            trade.confirmReceiver();
        }
        if (trade.isFullyConfirmed()) {
            trade.complete();
            fjernFraBytteliste(tradeId);
        }
        tradeRepository.update(trade);
    }

    // 86400000 = 24 timer
    @Scheduled(fixedRate = 86400000)
    public void expireOldTrades() {
        tradeRepository.expireOldTrades();
    }

    // --- Validering ---

    public void propose(int proposerId, int receiverId, List<Integer> proposerCardIds, List<Integer> receiverCardIds) {
        Trade trade = new Trade(
                0,
                proposerId,
                receiverId,
                TradeStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                false,
                false
        );
        proposeWithCards(trade, proposerCardIds, receiverCardIds);
    }

    public void validateProposal(int proposerId, int receiverId) {
        Player receiver = playerRepository.findById(receiverId);
        if (receiver.getCollectionVisibility() == CollectionVisibility.PRIVATE)
            throw new AccessDeniedException("Denne spillers profil er privat");
        if (playerCardRepository.findForTradeByPlayerId(proposerId).isEmpty())
            throw new IllegalArgumentException("Du har ingen kort markeret til bytte");
        if (playerCardRepository.findForTradeByPlayerId(receiverId).isEmpty())
            throw new IllegalArgumentException("Denne spiller har ingen kort markeret til bytte");
    }

    private void validatePending(Trade trade, String handling) {
        if (trade.getStatus() != TradeStatus.PENDING)
            throw new IllegalArgumentException("Kun trades som er pending kan " + handling);
    }

    private void validateComplete(Trade trade, int requestingPlayerId) {
        if (trade.getStatus() != TradeStatus.ACCEPTED)
            throw new IllegalArgumentException("Kun accepterede trades kan bekræftes som gennemført");
        if (trade.getProposerId() != requestingPlayerId && trade.getReceiverId() != requestingPlayerId)
            throw new AccessDeniedException("Du er ikke en del af denne trade");
    }

    private void proposeWithCards(Trade trade, List<Integer> proposerCardIds, List<Integer> receiverCardIds) {
        if (proposerCardIds == null || proposerCardIds.size() != 1)
            throw new IllegalArgumentException("Et trade skal indeholde et kort fra begge parter");
        if (receiverCardIds == null || receiverCardIds.size() != 1)
            throw new IllegalArgumentException("Et trade skal indeholde et kort fra begge parter");

        int proposerCardId = proposerCardIds.getFirst();
        int receiverCardId = receiverCardIds.getFirst();

        tradeValidator.validateProposerCard(trade.getProposerId(), proposerCardId);
        tradeValidator.validateReceiverCard(trade.getReceiverId(), receiverCardId);

        int tradeId = tradeRepository.save(trade);
        tradeCardRepository.save(new TradeCard(0, tradeId, proposerCardId, TradeRole.PROPOSER));
        tradeCardRepository.save(new TradeCard(0, tradeId, receiverCardId, TradeRole.RECEIVER));
    }

    // --- Intern behandling ---

    private void fjernFraBytteliste(int tradeId) {
        for (TradeCard tc : tradeCardRepository.findByTradeId(tradeId)) {
            PlayerCard pc = playerCardRepository.findById(tc.getPlayerCardId());
            if (pc != null) {
                pc.unmarkForTrade();
                playerCardRepository.update(pc);
            }
        }
    }
}