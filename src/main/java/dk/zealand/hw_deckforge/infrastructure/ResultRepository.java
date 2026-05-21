package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IResultRepository;
import dk.zealand.hw_deckforge.domain.Result;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResultRepository implements IResultRepository {
    private final JdbcTemplate jdbcTemplate;

    public ResultRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- Forespørgsler ---

    @Override
    public List<Result> findByEventId(int eventId) {
        try {
            String sql = "SELECT id, player_id, event_id, placement FROM result WHERE event_id = ?";
            return jdbcTemplate.query(sql, this::mapRow, eventId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente resultater for event: " + eventId, e);
        }
    }

    @Override
    public List<Result> findByPlayerId(int playerId) {
        try {
            String sql = "SELECT id, player_id, event_id, placement FROM result WHERE player_id = ?";
            return jdbcTemplate.query(sql, this::mapRow, playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente resultater for spiller: " + playerId, e);
        }
    }

    // --- Skriveoperationer ---

    @Override
    public void save(Result result) {
        try {
            String sql = "INSERT INTO result (player_id, event_id, placement) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    result.getPlayerId(),
                    result.getEventId(),
                    result.getPlacement()
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke gemme resultat", e);
        }
    }

    // --- Intern mapping ---

    private Result mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Result(
                rs.getInt("id"),
                rs.getInt("player_id"),
                rs.getInt("event_id"),
                rs.getInt("placement")
        );
    }
}