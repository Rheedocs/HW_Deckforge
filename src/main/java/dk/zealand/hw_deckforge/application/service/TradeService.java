package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.TradeCard;
import dk.zealand.hw_deckforge.domain.enums.TradeRole;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
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

    public TradeService(ITradeRepository tradeRepository, IPlayerCardRepository playerCardRepository, ITradeCardRepository tradeCardRepository) {
        this.tradeRepository = tradeRepository;
        this.playerCardRepository = playerCardRepository;
        this.tradeCardRepository = tradeCardRepository;
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

    public List<Trade> getSentByPlayerId(List<Trade> all, int playerId) {
        List<Trade> sent = new ArrayList<>();
        for (Trade trade : all) {
            if (trade.getProposerId() == playerId && trade.getStatus() == TradeStatus.PENDING) {
                sent.add(trade);
            }
        }
        return sent;
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
            if (trade.getStatus() != TradeStatus.PENDING) {
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
        if (trade.getStatus() != TradeStatus.PENDING)
            throw new IllegalArgumentException("Kun trades som er pending kan accepteres");
        trade.accept();
        tradeRepository.update(trade);
    }

    public void decline(int tradeId) {
        Trade trade = getById(tradeId);
        if (trade.getStatus() != TradeStatus.PENDING)
            throw new IllegalArgumentException("Kun trades som er pending kan afvises");
        trade.decline();
        tradeRepository.update(trade);
    }

    public void cancel(int tradeId) {
        Trade trade = getById(tradeId);
        if (trade.getStatus() != TradeStatus.PENDING)
            throw new IllegalArgumentException("Kun trades som er pending kan annuleres");
        trade.cancel();
        tradeRepository.update(trade);
    }

    @Scheduled(fixedRate = 10000)
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
                LocalDateTime.now().plusHours(24)
        );
        proposeWithCards(trade, proposerCardIds, receiverCardIds);
    }

    private void proposeWithCards(Trade trade, List<Integer> proposerCardIds, List<Integer> receiverCardIds) {
        if (proposerCardIds == null || proposerCardIds.size() != 1)
            throw new IllegalArgumentException("Et trade skal indeholde et kort fra begge parter");
        if (receiverCardIds == null || receiverCardIds.size() != 1)
            throw new IllegalArgumentException("Et trade skal indeholde et kort fra begge parter");

        int proposerCardId = proposerCardIds.getFirst();
        int receiverCardId = receiverCardIds.getFirst();

        validateProposerCard(trade.getProposerId(), proposerCardId);
        validateReceiverCard(trade.getReceiverId(), receiverCardId);

        int tradeId = tradeRepository.save(trade);
        tradeCardRepository.save(new TradeCard(0, tradeId, proposerCardId, TradeRole.PROPOSER));
        tradeCardRepository.save(new TradeCard(0, tradeId, receiverCardId, TradeRole.RECEIVER));
    }

    private void validateProposerCard(int proposerId, int proposerCardId) {
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(proposerId)) {
            if (pc.getId() == proposerCardId) return;
        }
        throw new IllegalArgumentException("Proposerens kort er ikke markeret til bytte");
    }

    private void validateReceiverCard(int receiverId, int receiverCardId) {
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(receiverId)) {
            if (pc.getId() == receiverCardId) return;
        }
        throw new IllegalArgumentException("Modtagerens kort er ikke markeret til bytte");
    }
}