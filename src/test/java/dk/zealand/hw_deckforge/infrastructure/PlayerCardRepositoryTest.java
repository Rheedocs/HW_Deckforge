package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.domain.PlayerCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(PlayerCardRepository.class)
class PlayerCardRepositoryTest {

    @Autowired
    private PlayerCardRepository repository;

    // --- findByPlayerId ---

    @Test
    void findByPlayerId_existingPlayer_returnsCards() {
        List<PlayerCard> cards = repository.findByPlayerId(2);
        assertFalse(cards.isEmpty());
    }

    @Test
    void findByPlayerId_playerWithNoCards_returnsEmptyList() {
        List<PlayerCard> cards = repository.findByPlayerId(1);
        assertTrue(cards.isEmpty());
    }

    // --- findById ---

    @Test
    void findById_existingId_returnsPlayerCard() {
        PlayerCard pc = repository.findById(1);
        assertNotNull(pc);
        assertEquals(2, pc.getPlayerId());
    }

    @Test
    void findById_nonExistingId_returnsNull() {
        PlayerCard pc = repository.findById(999);
        assertNull(pc);
    }

    // --- save ---

    @Test
    void save_newPlayerCard_canBeFoundAfterwards() {
        PlayerCard nyKort = new PlayerCard(0, 1, 1, 2, false);
        repository.save(nyKort);

        List<PlayerCard> cards = repository.findByPlayerId(1);
        assertFalse(cards.isEmpty());
    }

    // --- update ---

    @Test
    void update_existingPlayerCard_updatesQuantity() {
        PlayerCard pc = repository.findById(1);
        pc.setQuantity(10);
        repository.update(pc);

        PlayerCard updated = repository.findById(1);
        assertEquals(10, updated.getQuantity());
    }

    // --- delete ---

    @Test
    void delete_existingPlayerCard_cannotBeFoundAfterwards() {
        repository.delete(1);
        PlayerCard deleted = repository.findById(1);
        assertNull(deleted);
    }
}
