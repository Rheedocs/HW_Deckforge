package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(CardRepository.class)
class CardRepositoryTest {

    @Autowired
    private CardRepository repository;

    // --- findAll ---

    @Test
    void findAll_returnsNonEmptyList() {
        List<Card> cards = repository.findAll();
        assertFalse(cards.isEmpty());
    }

    @Test
    void findAll_containsKnownCard() {
        List<Card> cards = repository.findAll();
        boolean found = false;
        for (Card c : cards) {
            if (c.getName().equals("Lightning Bolt")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    // --- findById ---

    @Test
    void findById_existingId_returnsCard() {
        Card card = repository.findById(1);
        assertNotNull(card);
        assertEquals("Shivan Dragon", card.getName());
    }

    @Test
    void findById_nonExistingId_returnsNull() {
        Card card = repository.findById(999);
        assertNull(card);
    }

    // --- save og findById ---

    @Test
    void save_newCard_canBeFoundAfterwards() {
        Card nyKort = new Card(0, "Test Kort", CardType.CREATURE, "Test Sæt",
                Color.RED, Rarity.COMMON, "Test regeltext.", null);
        repository.save(nyKort);

        List<Card> cards = repository.findAll();
        boolean found = false;
        for (Card c : cards) {
            if (c.getName().equals("Test Kort")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    // --- update ---

    @Test
    void update_existingCard_updatesCorrectly() {
        Card card = repository.findById(1);
        card.setName("Opdateret Dragon");
        repository.update(card);

        Card updated = repository.findById(1);
        assertEquals("Opdateret Dragon", updated.getName());
    }

    // --- delete ---

    @Test
    void delete_existingCard_cannotBeFoundAfterwards() {
        repository.delete(27);
        Card deleted = repository.findById(27);
        assertNull(deleted);
    }
}
