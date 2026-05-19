package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.Trade;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradeRepository implements ITradeRepository {
    private final JdbcTemplate jdbcTemplate;

    public TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- Forespørgsler ---

    @Override public Trade findById(int id) { return null; }
    @Override public List<Trade> findByPlayerId(int playerId) { return null; }
    @Override public List<Trade> findIncomingByPlayerId(int playerId) { return null; }

    // --- Skriveoperationer ---

    @Override public void save(Trade trade) {}
    @Override public void update(Trade trade) {}
    @Override public void expireOldTrades() {}
}