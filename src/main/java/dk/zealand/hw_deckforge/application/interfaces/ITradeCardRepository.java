package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.TradeCard;
import java.util.List;

/** Kontrakt for kortroller i bytter (PROPOSER/RECEIVER). */
public interface ITradeCardRepository {
    List<TradeCard> findByTradeId(int tradeId);
    void save(TradeCard tradeCard);
}
