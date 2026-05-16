package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IDeckCardRepository;
import dk.zealand.hw_deckforge.domain.DeckCard;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeckCardRepository implements IDeckCardRepository {

    private final JdbcTemplate jdbcTemplate;

    public DeckCardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_SQL =
            "SELECT id, deck_id, card_id, quantity FROM deck_card";

    private final RowMapper<DeckCard> deckCardRowMapper = (rs, rowNum) -> new DeckCard(
            rs.getInt("id"),
            rs.getInt("deck_id"),
            rs.getInt("card_id"),
            rs.getInt("quantity")
    );

    @Override
    public List<DeckCard> findByDeckId(int deckId) {
        try {
            return jdbcTemplate.query(BASE_SQL + " WHERE deck_id = ?", deckCardRowMapper, deckId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kort for deck", e);
        }
    }

    @Override
    public DeckCard findById(int id) {
        try {
            List<DeckCard> results = jdbcTemplate.query(BASE_SQL + " WHERE id = ?", deckCardRowMapper, id);
            return results.isEmpty() ? null : results.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente deck-kort med id: " + id, e);
        }
    }

    @Override
    public DeckCard findByDeckIdAndCardId(int deckId, int cardId) {
        try {
            List<DeckCard> results = jdbcTemplate.query(
                    BASE_SQL + " WHERE deck_id = ? AND card_id = ?", deckCardRowMapper, deckId, cardId);
            return results.isEmpty() ? null : results.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kort for deck", e);
        }
    }

    @Override
    public void save(DeckCard deckCard) {
        try {
            String sql = "INSERT INTO deck_card (deck_id, card_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, deckCard.getDeckId(), deckCard.getCardId(), deckCard.getQuantity());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke tilføje kort til deck", e);
        }
    }

    @Override
    public void update(DeckCard deckCard) {
        try {
            String sql = "UPDATE deck_card SET quantity = ? WHERE id = ?";
            jdbcTemplate.update(sql, deckCard.getQuantity(), deckCard.getId());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere kort i deck", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM deck_card WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke fjerne kort fra deck", e);
        }
    }
}
