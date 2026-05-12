package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeckRepository implements IDeckRepository {
    private final JdbcTemplate jdbcTemplate;

    public DeckRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final String BASE_SQL =
            "SELECT id, player_id, name, format, visibility FROM deck";

    private final RowMapper<Deck> deckRowMapper = (rs, rowNum) -> new Deck(
            rs.getInt("id"),
            rs.getInt("player_id"),
            rs.getString("name"),
            Format.valueOf(rs.getString("format")),
            DeckVisibility.valueOf(rs.getString("visibility"))
    );

    @Override public List<Deck> findAll() {
        try {
            return jdbcTemplate.query(BASE_SQL, deckRowMapper);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente spillere", e);
        }
    }
    @Override public Deck findById(int id) {
        try {
            List<Deck> decks = jdbcTemplate.query(BASE_SQL + " WHERE id = ?", deckRowMapper, id);
            return decks.isEmpty() ? null : decks.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente deck med id: " + id, e);
        }

    }
    @Override public List<Deck> findByPlayerId(int playerId) {
        try {
           return jdbcTemplate.query(BASE_SQL + " WHERE player_id = ?", deckRowMapper, playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente deck med playerId: " + playerId, e);
        }
    }
    @Override public void save(Deck deck) {
            try {
                String sql = "INSERT INTO deck (player_id, name, format, visibility) VALUES (?, ?, ?, ?)";
                jdbcTemplate.update(sql,
                        deck.getPlayerId(),
                        deck.getName(),
                        deck.getFormat().name(),
                        deck.getVisibility().name());
            } catch (DataAccessException e) {
                throw new DatabaseException("Kunne ikke oprette deck", e);
            }
    }
    @Override
    public void update(Deck deck) {
        try {
            String sql = "UPDATE deck SET player_id = ?, name = ?, format = ?, visibility = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    deck.getPlayerId(),
                    deck.getName(),
                    deck.getFormat().name(),
                    deck.getVisibility().name(),
                    deck.getId());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere deck", e);
        }
    }
    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM deck WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            e.getStackTrace();
            throw new DatabaseException("Kunne ikke slette deck med id " + id, e);
        }
    }
}