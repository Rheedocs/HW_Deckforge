package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.TradeCard;
import java.util.List;

public interface ITradeCardRepository {
    List<TradeCard> findByTradeId(int tradeId);
    void save(TradeCard tradeCard);
    void deleteByTradeId(int tradeId);

}
