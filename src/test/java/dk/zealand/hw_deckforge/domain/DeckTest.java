package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck(1, 1, "Test Deck", Format.CASUAL, DeckVisibility.PRIVATE);
    }

    // --- makePublic / makePrivate ---

    @Test
    void makePublic_setsVisibilityToPublic() {
        deck.makePublic();
        assertEquals(DeckVisibility.PUBLIC, deck.getVisibility());
    }

    @Test
    void makePrivate_setsVisibilityToPrivate() {
        deck.makePublic();
        deck.makePrivate();
        assertEquals(DeckVisibility.PRIVATE, deck.getVisibility());
    }

    // --- isPublic ---

    @Test
    void isPublic_whenPublic_returnsTrue() {
        deck.makePublic();
        assertTrue(deck.isPublic());
    }

    @Test
    void isPublic_whenPrivate_returnsFalse() {
        assertFalse(deck.isPublic());
    }

    // --- setName ---

    @Test
    void setName_withNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> deck.setName(null));
    }

    @Test
    void setName_withValidName_updatesName() {
        deck.setName("Nyt Deck Navn");
        assertEquals("Nyt Deck Navn", deck.getName());
    }
}
