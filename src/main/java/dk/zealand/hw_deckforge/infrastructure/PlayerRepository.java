package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository implements IPlayerRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SQL =
            "SELECT id, username, email, password, role, collection_visibility FROM player";

    private final RowMapper<Player> playerRowMapper = (rs, rowNum) -> new Player(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            Role.valueOf(rs.getString("role")),
            CollectionVisibility.valueOf(rs.getString("collection_visibility"))
    );

    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Player> findAll() {
        try {
            return jdbcTemplate.query(BASE_SQL, playerRowMapper);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente spillere", e);
        }
    }

    @Override
    public Player findById(int id) {
        try {
            List<Player> players = jdbcTemplate.query(BASE_SQL + " WHERE id = ?", playerRowMapper, id);
            return players.isEmpty() ? null : players.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente spiller med id " + id, e);
        }
    }

    @Override
    public Player findByEmail(String email) {
        try {
            List<Player> players = jdbcTemplate.query(BASE_SQL + " WHERE email = ?", playerRowMapper, email);
            return players.isEmpty() ? null : players.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente spiller med email " + email, e);
        }
    }

    @Override
    public void save(Player player) {
        try {
            String sql = "INSERT INTO player (username, email, password, role, collection_visibility) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    player.getUsername(),
                    player.getEmail(),
                    player.getPassword(),
                    player.getRole().name(),
                    player.getCollectionVisibility().name());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke oprette spiller", e);
        }
    }

    @Override
    public void update(Player player) {
        try {
            String sql = "UPDATE player SET username = ?, email = ?, password = ?, role = ?," +
                    " collection_visibility = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    player.getUsername(),
                    player.getEmail(),
                    player.getPassword(),
                    player.getRole().name(),
                    player.getCollectionVisibility().name(),
                    player.getId());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere spiller", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM player WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke slette spiller med id " + id, e);
        }
    }
}