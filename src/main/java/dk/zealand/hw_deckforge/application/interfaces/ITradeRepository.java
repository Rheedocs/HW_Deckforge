package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Trade;
import java.util.List;

public interface ITradeRepository {
    Trade findById(int id);
    List<Trade> findByPlayerId(int playerId);
    List <Trade> findIncomingByPlayerId(int playerId);
    void save(Trade trade);
    void update (Trade trade);
    void expireOldTrades();
}
