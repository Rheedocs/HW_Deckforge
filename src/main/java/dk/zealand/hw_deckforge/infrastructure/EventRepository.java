package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepository implements IEventRepository {
    private final JdbcTemplate jdbcTemplate;

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- Forespørgsler ---

    @Override public List<Event> findAll() { return null; }
    @Override public Event findById(int id) { return null; }
    @Override public List<Event> findUpcoming() { return null; }

    // --- Skriveoperationer ---

    @Override public void save(Event event) {}
    @Override public void update(Event event) {}
    @Override public void delete(int id) {}

    // --- Tilmelding ---

    @Override public void registerPlayer(int playerId, int eventId, int deckId) {}
    @Override public int countRegistrations(int eventId) { return 0; }
}