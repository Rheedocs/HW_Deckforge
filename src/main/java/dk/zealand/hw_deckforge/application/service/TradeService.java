package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.Trade;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Trade getById(int id) { return tradeRepository.findById(id); }
    public List<Trade> getByPlayerId(int playerId) { return tradeRepository.findByPlayerId(playerId); }
    public List<Trade> getIncomingByPlayerId(int playerId) { return tradeRepository.findIncomingByPlayerId(playerId); }
    public void accept(int tradeId) {}
    public void decline(int tradeId) {}
    public void cancel(int tradeId) {}
    public void expireOldTrades() {}
    public void propose(Trade trade, List<Integer> proposerCardIds, List<Integer> receiverCardIds) {}
}