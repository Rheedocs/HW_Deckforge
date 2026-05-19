package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.domain.TradeCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradeCardRepository implements ITradeCardRepository {
    private final JdbcTemplate jdbcTemplate;

    public TradeCardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- Forespørgsler ---

    @Override public List<TradeCard> findByTradeId(int tradeId) { return null; }

    // --- Skriveoperationer ---

    @Override public void save(TradeCard tradeCard) {}
    @Override public void deleteByTradeId(int tradeId) {}
}