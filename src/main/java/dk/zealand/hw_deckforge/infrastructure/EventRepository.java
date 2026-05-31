package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.EventRegistration;
import dk.zealand.hw_deckforge.domain.enums.EventStatus;
import dk.zealand.hw_deckforge.domain.enums.Format;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/** JdbcTemplate-implementering af IEventRepository inkl. kapacitetstjek og automatisk statusopdatering. */
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

    @Override
    public List<EventRegistration> findRegistrationsByEventId(int eventId) {
        String sql = """
                SELECT er.id, er.player_id, er.event_id, er.deck_id, er.registration_date,
                       p.username AS player_name, d.name AS deck_name
                FROM event_registration er
                JOIN player p ON er.player_id = p.id
                JOIN deck d ON er.deck_id = d.id
                WHERE er.event_id = ?
                ORDER BY er.registration_date, p.username
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new EventRegistration(
                rs.getInt("id"),
                rs.getInt("player_id"),
                rs.getInt("event_id"),
                rs.getInt("deck_id"),
                rs.getDate("registration_date").toLocalDate(),
                rs.getString("player_name"),
                rs.getString("deck_name")
        ), eventId);
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
                event.getStatus().name());
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
                event.getId());
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
    public boolean existsRegistration(int playerId, int eventId) {
        String sql = "SELECT COUNT(*) FROM event_registration WHERE player_id = ? AND event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, playerId, eventId);
        return count != null && count > 0;
    }

    @Override
    public int countRegistrations(int eventId) {
        String sql = "SELECT COUNT(*) FROM event_registration WHERE event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, eventId);
        return count != null ? count : 0;
    }

    @Override
    public int countRegistrationsByPlayerId(int playerId) {
        String sql = "SELECT COUNT(*) FROM event_registration WHERE player_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, playerId);
        return count != null ? count : 0;
    }

    /** Opdaterer events fra UPCOMING til ONGOING eller COMPLETED baseret på dato.
     * Kaldes af @Scheduled i EventService. */
    @Override
    public void updateExpiredEvents() {
        String sql = """
        UPDATE event
        SET status = CASE
            WHEN status = 'UPCOMING' AND date = CURRENT_DATE THEN 'ONGOING'
            WHEN status IN ('UPCOMING', 'ONGOING') AND date < CURRENT_DATE THEN 'COMPLETED'
            ELSE status
        END
        WHERE status IN ('UPCOMING', 'ONGOING') AND id > 0
        """;
        jdbcTemplate.update(sql);
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