package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerCardRepository implements IPlayerCardRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlayerCardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override public List<PlayerCard> findByPlayerId(int playerId) { return null; }
    @Override public List<PlayerCard> findForTradeByPlayerId(int playerId) { return null; }
    @Override public void save(PlayerCard playerCard) {}
    @Override public void update(PlayerCard playerCard) {}
    @Override public void delete(int id) {}
    @Override public void setForTrade(int id, boolean forTrade) {}

    @Override
    public int countByPlayerId(int playerId) {
        try {
            String sql = "SELECT COUNT(*) FROM player_card WHERE player_id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, playerId);
            return count != null ? count : 0;
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke tælle kort for spiller!", e);
        }
    }
}