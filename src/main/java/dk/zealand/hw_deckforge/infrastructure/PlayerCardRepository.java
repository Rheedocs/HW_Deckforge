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

    @Override
    public List<PlayerCard> findByPlayerId(int playerId) {
        try {
            String sql = "SELECT id, player_id, card_id, quantity, for_trade FROM player_card WHERE player_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new PlayerCard(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getInt("card_id"),
                    rs.getInt("quantity"),
                    rs.getBoolean("for_trade")
            ), playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kort for spiller!", e);
        }
    }
    @Override
    public List<PlayerCard> findForTradeByPlayerId(int playerId) {
        try {
            String sql = "SELECT id, player_id, card_id, quantity, for_trade FROM player_card WHERE player_id = ? AND for_trade = TRUE";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new PlayerCard(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getInt("card_id"),
                    rs.getInt("quantity"),
                    rs.getBoolean("for_trade")
            ), playerId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente byttekort for spiller!", e);
        }
    }
    @Override
    public PlayerCard findById(int id) {
        try {
            String sql = "SELECT id, player_id, card_id, quantity, for_trade FROM player_card WHERE id = ?";
            List<PlayerCard> results = jdbcTemplate.query(sql, (rs, rowNum) -> new PlayerCard(
                    rs.getInt("id"), rs.getInt("player_id"), rs.getInt("card_id"),
                    rs.getInt("quantity"), rs.getBoolean("for_trade")), id);
            return results.isEmpty() ? null : results.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente spillerkort med id: " + id, e);
        }
    }

    @Override
    public PlayerCard findByPlayerIdAndCardId(int playerId, int cardId) {
        try {
            String sql = "SELECT id, player_id, card_id, quantity, for_trade FROM player_card WHERE player_id = ? AND card_id = ?";
            List<PlayerCard> results = jdbcTemplate.query(sql, (rs, rowNum) -> new PlayerCard(
                    rs.getInt("id"), rs.getInt("player_id"), rs.getInt("card_id"),
                    rs.getInt("quantity"), rs.getBoolean("for_trade")), playerId, cardId);
            return results.isEmpty() ? null : results.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente spillerkort", e);
        }
    }

    @Override
    public void save(PlayerCard playerCard) {
        try {
            String sql = "INSERT INTO player_card (player_id, card_id, quantity, for_trade) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, playerCard.getPlayerId(), playerCard.getCardId(),
                    playerCard.getQuantity(), playerCard.isForTrade());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke tilføje kort til samling", e);
        }
    }

    @Override
    public void update(PlayerCard playerCard) {
        try {
            String sql = "UPDATE player_card SET quantity = ?, for_trade = ? WHERE id = ?";
            jdbcTemplate.update(sql, playerCard.getQuantity(), playerCard.isForTrade(), playerCard.getId());
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere spillerkort", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM player_card WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke fjerne kort fra samling", e);
        }
    }

    @Override
    public void setForTrade(int id, boolean forTrade) {
        try {
            String sql = "UPDATE player_card SET for_trade = ? WHERE id = ?";
            jdbcTemplate.update(sql, forTrade, id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere byttemarkering", e);
        }
    }

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

    @Override
    public int countForTradeByPlayerId(int playerId) {
        try {
            String sql = "SELECT COUNT(*) FROM player_card WHERE player_id = ? AND for_trade = TRUE";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, playerId);
            return count != null ? count : 0;
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke tælle byttekort for spiller!", e);
        }
    }
}