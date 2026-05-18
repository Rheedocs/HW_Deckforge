package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TradeRepository implements ITradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_SQL =
            "SELECT id, proposer_id, receiver_id, status, created_at, expired_at FROM trade";

    private final RowMapper<Trade> tradeRowMapper = (rs, rowNum) -> new Trade(
            rs.getInt("id"),
            rs.getInt("proposer_id"),
            rs.getInt("receiver_id"),
            TradeStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("expired_at").toLocalDateTime()
    );

    @Override
    public Trade findById(int id) {
        try {
            List<Trade> trades = jdbcTemplate.query(BASE_SQL + " WHERE id = ?", tradeRowMapper, id);
            return trades.isEmpty() ? null : trades.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente deck med id: " + id, e);
        }
    }

    @Override
    public List<Trade> findByPlayerId(int playerId) {
        try {
            return jdbcTemplate.query(BASE_SQL + " WHERE proposer_id = ? OR receiver_id = ?", tradeRowMapper, playerId, playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente trades for spiller: " + playerId, e);
        }
    }

    @Override
    public List<Trade> findIncomingByPlayerId(int playerId) {
        try {
            return jdbcTemplate.query(BASE_SQL + " WHERE receiver_id = ?", tradeRowMapper, playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kommende trades for spiller: " + playerId, e);
        }
    }

    @Override
    public void save(Trade trade) {
        try {
            String sql = "INSERT INTO trade (id, proposer_id, receiver_id, status, created_at, expired_at) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    trade.getId(),
                    trade.getProposerId(),
                    trade.getReceiverId(),
                    trade.getStatus().name(),
                    trade.getCreatedAt(),
                    trade.getExpiresAt());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke gemme trades", e);
        }
    }

    @Override
    public void update(Trade trade) {
        try {
            String sql = "UPDATE trade SET id = ?, proposer_id = ?, receiver_id = ?, status = ?, created_at = ?, expired_at = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    trade.getId(),
                    trade.getProposerId(),
                    trade.getReceiverId(),
                    trade.getStatus(),
                    trade.getCreatedAt(),
                    trade.getExpiresAt());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere trades", e);
        }

    }

    @Override
    public void expireOldTrades() {
        try {
            String sql = "UPDATE trade SET status = ? WHERE expired_at < ? AND status = ?";
            jdbcTemplate.update(sql,
                    TradeStatus.CANCELLED.name(),
                    LocalDateTime.now(),
                    TradeStatus.PENDING.name()
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke udløbe gamle trades", e);
        }
    }
}
