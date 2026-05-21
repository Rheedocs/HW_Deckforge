package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.enums.EventStatus;
import dk.zealand.hw_deckforge.domain.enums.Format;
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

    @Override
    public List<Event> findAll() {
        String sql = "SELECT id, name, location, date, format, max_players, status FROM event";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public Event findById(int id) {
        String sql = "SELECT id, name, location, date, format, max_players, status FROM event WHERE id = ?";
        List<Event> events = jdbcTemplate.query(sql, this::mapRow, id);
        return events.isEmpty() ? null : events.getFirst();
    }

    @Override
    public List<Event> findUpcoming() {
        String sql = "SELECT id, name, location, date, format, max_players, status FROM event WHERE status = ?";
        return jdbcTemplate.query(sql, this::mapRow, EventStatus.UPCOMING.name());
    }

    // --- Skriveoperationer ---

    @Override
    public void save(Event event) {
        String sql = "INSERT INTO event (name, location, date, format, max_players, status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                event.getName(),
                event.getLocation(),
                event.getDate(),
                event.getFormat().name(),
                event.getMaxPlayers(),
                event.getStatus().name()
        );
    }

    @Override
    public void update(Event event) {
        String sql = """
                UPDATE event
                SET name = ?, location = ?, date = ?, format = ?, max_players = ?, status = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                event.getName(),
                event.getLocation(),
                event.getDate(),
                event.getFormat().name(),
                event.getMaxPlayers(),
                event.getStatus().name(),
                event.getId()
        );
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM event WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // --- Tilmelding ---

    @Override
    public void registerPlayer(int playerId, int eventId, int deckId) {
        String sql = """
            INSERT INTO event_registration (player_id, event_id, deck_id, registration_date)
            VALUES (?, ?, ?, CURRENT_DATE)
            """;

        jdbcTemplate.update(sql, playerId, eventId, deckId);
    }

    @Override
    public int countRegistrations(int eventId) {
        String sql = "SELECT COUNT(*) FROM event_registration WHERE event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, eventId);
        return count != null ? count : 0;
    }

    // --- Intern mapping ---

    private Event mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getDate("date").toLocalDate(),
                rs.getInt("max_players"),
                EventStatus.valueOf(rs.getString("status")),
                Format.valueOf(rs.getString("format"))
        );
    }
}