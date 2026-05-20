package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CardRepository implements ICardRepository {
    private final JdbcTemplate jdbcTemplate;

    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- Forespørgsler ---

    @Override
    public List<Card> findAll() {
        try {
            String sql = "SELECT id, name, card_type, color, set_name, rarity, rule_text, image_url FROM card ORDER BY name";
            return jdbcTemplate.query(sql, this::mapRow);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kort!", e);
        }
    }

    @Override
    public Card findById(int id) {
        try {
            String sql = "SELECT id, name, card_type, color, set_name, rarity, rule_text, image_url FROM card WHERE id = ?";
            List<Card> cards = jdbcTemplate.query(sql, this::mapRow, id);
            return cards.isEmpty() ? null : cards.getFirst();
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke hente kort med id " + id + "!", e);
        }
    }

    // --- Skriveoperationer ---

    @Override
    public void save(Card card) {
        try {
            String sql = "INSERT INTO card (name, card_type, color, set_name, rarity, rule_text, image_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    card.getName(),
                    card.getCardType().name(),
                    card.getColor().name(),
                    card.getSetName(),
                    card.getRarity().name(),
                    card.getRuleText(),
                    card.getImageUrl()
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke gemme kort!", e);
        }
    }

    @Override
    public void update(Card card) {
        try {
            String sql = "UPDATE card SET name = ?, card_type = ?, color = ?, set_name = ?, rarity = ?, " +
                    "rule_text = ?, image_url = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    card.getName(),
                    card.getCardType().name(),
                    card.getColor().name(),
                    card.getSetName(),
                    card.getRarity().name(),
                    card.getRuleText(),
                    card.getImageUrl(),
                    card.getId()
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke opdatere kort!", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM card WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Kunne ikke slette kort med id " + id + "!", e);
        }
    }

    // --- Mapping ---

    private Card mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Card(
                rs.getInt("id"),
                rs.getString("name"),
                CardType.valueOf(rs.getString("card_type")),
                rs.getString("set_name"),
                Color.valueOf(rs.getString("color")),
                Rarity.valueOf(rs.getString("rarity")),
                rs.getString("rule_text"),
                rs.getString("image_url")
        );
    }
}