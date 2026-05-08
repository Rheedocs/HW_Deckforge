package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeckRepository implements IDeckRepository {
    private final JdbcTemplate jdbcTemplate;

    public DeckRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override public List<Deck> findAll() { return null; }
    @Override public Deck findById(int id) { return null; }
    @Override public List<Deck> findByPlayerId(int playerId) { return null; }
    @Override public void save(Deck deck) {}
    @Override public void update(Deck deck) {}
    @Override public void delete(int id) {}
}