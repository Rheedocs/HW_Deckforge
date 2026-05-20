package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.domain.TradeCard;
import dk.zealand.hw_deckforge.domain.enums.TradeRole;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
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

    @Override
    public List<TradeCard> findByTradeId(int tradeId) {
        try {
            String sql = "SELECT id, trade_id, player_card_id, role FROM trade_card WHERE trade_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new TradeCard(
                    rs.getInt("id"),
                    rs.getInt("trade_id"),
                    rs.getInt("player_card_id"),
                    TradeRole.valueOf(rs.getString("role"))
            ), tradeId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kort for trade!", e);
        }
    }

    // --- Skriveoperationer ---

    @Override
    public void save(TradeCard tradeCard) {
        try {
            String sql = "INSERT INTO trade_card (trade_id, player_card_id, role) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    tradeCard.getTradeId(),
                    tradeCard.getPlayerCardId(),
                    tradeCard.getRole().name());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke gemme trade kort", e.getCause());
        }
    }

    @Override
    public void deleteByTradeId(int tradeId) {
        try {
            String sql = "DELETE FROM trade_card WHERE trade_id = ?";
            jdbcTemplate.update(sql, tradeId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke fjerne kort fra trade", e);
        }
    }
}