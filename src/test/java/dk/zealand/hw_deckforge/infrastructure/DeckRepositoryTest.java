package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DeckRepository.class)
class DeckRepositoryTest {

    @Autowired
    private DeckRepository repository;

    @Test
    void findAll() {
        List<Deck> decks = repository.findAll();
        assertFalse(decks.isEmpty());
    }

    @Test
    void findById() {
        Deck deck = repository.findById(0);
        assertNull(deck);
    }

    @Test
    void findByPlayerId() {
        List<Deck> decks = repository.findByPlayerId(10);
        assertNotNull(decks);
    }

    @Test
    void save() {
        Deck newDeck = new Deck(-1, 1, "Defensive Deck", Format.COMMANDER, DeckVisibility.PRIVATE);
        repository.save(newDeck);
        List<Deck> decks = repository.findAll();
        boolean found = false;
        for (Deck d : decks) {
            if (d.getName().equals("Defensive Deck")) {
                found = true;
                break;
            }
        }
        assertTrue(found);

    }
    @Test
    void update() {
        Deck deck = repository.findById(4);
        deck.setName("Updated Deck");
        repository.update(deck);

        Deck updated = repository.findById(4);
        assertEquals("Updated Deck", updated.getName());
    }

    @Test
    void delete() {
        repository.delete(6);
        Deck deleted = repository.findById(6);
        assertNull(deleted);
    }
}