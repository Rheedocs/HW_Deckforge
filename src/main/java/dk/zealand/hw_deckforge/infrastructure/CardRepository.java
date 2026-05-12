package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepository implements ICardRepository {
    private final JdbcTemplate jdbcTemplate;

    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Card> findAll() {
        String sql = "SELECT id, name, type, color FROM card";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Card(
                rs.getInt("id"),
                rs.getString("name"),
                CardType.valueOf(rs.getString("type")),
                rs.getString("set_name"),
                Color.valueOf(rs.getString("color")),
                Rarity.valueOf(rs.getString("rarity")),
                rs.getString("rule_text"),
                rs.getString("image_url")
        ));
    }
    @Override
    public Card findById(int id) {
        String sql = "SELECT id, name, type, set_name, color, rarity, rule_text, image_url FROM card WHERE id = ?";
        List<Card> card = jdbcTemplate.query(sql, (rs, rowNum) -> new Card(
                rs.getInt("id"),
                rs.getString("name"),
                CardType.valueOf(rs.getString("type")),
                rs.getString("set_name"),
                Color.valueOf(rs.getString("color")),
                Rarity.valueOf(rs.getString("rarity")),
                rs.getString("rule_text"),
                rs.getString("image_url")
        ), id);
        return card.isEmpty() ? null : card.getFirst();
    }
    @Override
    public void save(Card card) {
        String sql = "INSERT INTO card (id, name, color, type) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, card.getId(), card.getName(), card.getColor(), card.getCardType(),
                card.getRuleText(), card.getImageUrl());
    }
    @Override
    public void update(Card card) {
        String sql = "INSERT INTO card SET id = ?, name = ?, color = ?, type = ? WHERE id = ?";
        jdbcTemplate.update(sql, card.getId(), card.getName(), card.getColor(), card.getCardType(),
                card.getRuleText(), card.getImageUrl());
    }
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM card WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}