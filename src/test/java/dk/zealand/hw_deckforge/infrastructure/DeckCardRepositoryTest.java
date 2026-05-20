package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.domain.DeckCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DeckCardRepository.class)
class DeckCardRepositoryTest {

    @Autowired
    private DeckCardRepository repository;

    // --- findByDeckId ---

    @Test
    void findByDeckId_existingDeck_returnsCards() {
        List<DeckCard> cards = repository.findByDeckId(1);
        assertFalse(cards.isEmpty());
    }

    @Test
    void findByDeckId_nonExistingDeck_returnsEmptyList() {
        List<DeckCard> cards = repository.findByDeckId(999);
        assertTrue(cards.isEmpty());
    }

    // --- findByDeckIdAndCardId ---

    @Test
    void findByDeckIdAndCardId_existingEntry_returnsDeckCard() {
        DeckCard dc = repository.findByDeckIdAndCardId(1, 1);
        assertNotNull(dc);
        assertEquals(1, dc.getDeckId());
        assertEquals(1, dc.getCardId());
    }

    @Test
    void findByDeckIdAndCardId_nonExistingEntry_returnsNull() {
        DeckCard dc = repository.findByDeckIdAndCardId(1, 999);
        assertNull(dc);
    }

    // --- findById ---

    @Test
    void findById_existingId_returnsDeckCard() {
        DeckCard dc = repository.findById(1);
        assertNotNull(dc);
    }

    @Test
    void findById_nonExistingId_returnsNull() {
        DeckCard dc = repository.findById(999);
        assertNull(dc);
    }

    // --- save ---

    @Test
    void save_newDeckCard_canBeFoundAfterwards() {
        DeckCard nytKort = new DeckCard(0, 1, 8, 1);
        repository.save(nytKort);

        DeckCard found = repository.findByDeckIdAndCardId(1, 8);
        assertNotNull(found);
        assertEquals(1, found.getQuantity());
    }

    // --- update ---

    @Test
    void update_existingDeckCard_updatesQuantity() {
        DeckCard dc = repository.findById(1);
        dc.setQuantity(7);
        repository.update(dc);

        DeckCard updated = repository.findById(1);
        assertEquals(7, updated.getQuantity());
    }

    // --- delete ---

    @Test
    void delete_existingDeckCard_cannotBeFoundAfterwards() {
        repository.delete(1);
        DeckCard deleted = repository.findById(1);
        assertNull(deleted);
    }
}
