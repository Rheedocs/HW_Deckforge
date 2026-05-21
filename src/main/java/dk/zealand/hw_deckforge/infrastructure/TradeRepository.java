package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TradeRepository implements ITradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_SQL =
            "SELECT id, proposer_id, receiver_id, status, created_at, expires_at, proposer_confirmed," +
                    " receiver_confirmed FROM trade";

    private final RowMapper<Trade> tradeRowMapper = (rs, rowNum) -> new Trade(
            rs.getInt("id"),
            rs.getInt("proposer_id"),
            rs.getInt("receiver_id"),
            TradeStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("expires_at").toLocalDateTime(),
            rs.getBoolean("proposer_confirmed"),
            rs.getBoolean("receiver_confirmed")
    );

    @Override
    public Trade findById(int id) {
        try {
            List<Trade> trades = jdbcTemplate.query(BASE_SQL + " WHERE id = ?", tradeRowMapper, id);
            return trades.isEmpty() ? null : trades.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke finde trades for denne player" + id, e);
        }
    }

    @Override
    public List<Trade> findByPlayerId(int playerId) {
        try {
            return jdbcTemplate.query(BASE_SQL + " WHERE proposer_id = ? OR receiver_id = ?",
                    tradeRowMapper, playerId, playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente trades for spiller: " + playerId, e);
        }
    }

    @Override
    public List<Trade> findIncomingByPlayerId(int playerId) {
        try {
            return jdbcTemplate.query(BASE_SQL + " WHERE receiver_id = ? AND status = ?", tradeRowMapper,
                    playerId, TradeStatus.PENDING.name());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kommende trades for spiller: " + playerId, e);
        }
    }

    @Override
    public int save(Trade trade) {
        try {
            String sql = "INSERT INTO trade (proposer_id, receiver_id, status, created_at, expires_at," +
                    " proposer_confirmed, receiver_confirmed) VALUES (?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, trade.getProposerId());
                ps.setInt(2, trade.getReceiverId());
                ps.setString(3, trade.getStatus().name());
                ps.setObject(4, trade.getCreatedAt());
                ps.setObject(5, trade.getExpiresAt());
                ps.setBoolean(6, trade.isProposerConfirmed());
                ps.setBoolean(7, trade.isReceiverConfirmed());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new DatabaseException("Ingen nøgle returneret ved gem af trade", null);
            }
            return key.intValue();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke gemme trade", e);
        }
    }

    @Override
    public void update(Trade trade) {
        try {
            String sql = "UPDATE trade SET proposer_id = ?, receiver_id = ?, status = ?, created_at = ?," +
                    " expires_at = ?, proposer_confirmed = ?, receiver_confirmed = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    trade.getProposerId(),
                    trade.getReceiverId(),
                    trade.getStatus().name(),
                    trade.getCreatedAt(),
                    trade.getExpiresAt(),
                    trade.isProposerConfirmed(),
                    trade.isReceiverConfirmed(),
                    trade.getId());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere trades", e);
        }
    }

    @Override
    public void expireOldTrades() {
        try {
            String selectSql = "SELECT id FROM trade WHERE expires_at < ? AND status = ?";
            List<Integer> ids = jdbcTemplate.queryForList(selectSql, Integer.class,
                    LocalDateTime.now(), TradeStatus.PENDING.name());
            if (ids.isEmpty()) return;
            for (int id : ids) {
                String updateSql = "UPDATE trade SET status = ? WHERE id = ?";
                jdbcTemplate.update(updateSql, TradeStatus.EXPIRED.name(), id);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke udløbe gamle trades", e);
        }
    }
}