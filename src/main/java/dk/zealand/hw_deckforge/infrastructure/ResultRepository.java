package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IResultRepository;
import dk.zealand.hw_deckforge.domain.Result;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Repository
public class ResultRepository implements IResultRepository {
    private final JdbcTemplate jdbcTemplate;

    public ResultRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Result> findByEventId(int eventId) {
        try {
            String sql = "SELECT id, player_id, event_id, placement FROM result WHERE event_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Result(
                    rs.getInt("id"),
                    rs.getInt("playerId"),
                    rs.getInt("eventId"),
                    rs.getInt("placements")
            ), eventId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente resultater!", e);
        }
    }

    @Override
    public List<Result> findByPlayerId(int playerId) {
        try {
            String sql = "SELECT id, playerId, eventId, placements FROM result WHERE playerId = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Result(
                    rs.getInt("id"),
                    rs.getInt("playerId"),
                    rs.getInt("eventId"),
                    rs.getInt("placements")
            ), playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente resultater!", e);
        }
    }
    @Override
    public void save(Result result) {
        try {
            String sql = "INSERT INTO result (playerId, eventId, placements) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    result.getId(),
                    result.getPlayerId(),
                    result.getEventId(),
                    result.getPlacement());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke gemme resultater!", e);
        }
    }
}