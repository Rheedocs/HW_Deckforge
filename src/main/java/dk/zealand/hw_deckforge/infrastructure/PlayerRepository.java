package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository implements IPlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override public List<Player> findAll() { return null; }
    @Override public Player findById(int id) { return null; }
    @Override public Player findByEmail(String email) { return null; }
    @Override public void save(Player player) {}
    @Override public void update(Player player) {}
    @Override public void delete(int id) {}
}