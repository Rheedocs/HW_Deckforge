package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card(1, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON,
                "Deal 3 damage.", "https://cards.scryfall.io/normal/front/test.jpg");
    }

    // --- hasImage ---

    @Test
    void hasImage_withImageUrl_returnsTrue() {
        assertTrue(card.hasImage());
    }

    @Test
    void hasImage_withNullImageUrl_returnsFalse() {
        card.setImageUrl(null);
        assertFalse(card.hasImage());
    }

    @Test
    void hasImage_withBlankImageUrl_returnsFalse() {
        card.setImageUrl("   ");
        assertFalse(card.hasImage());
    }

    // --- getScryfallUrl ---

    @Test
    void getScryfallUrl_withName_returnsSearchUrl() {
        String url = card.getScryfallUrl();
        assertTrue(url.startsWith("https://scryfall.com/search"));
        assertTrue(url.contains("Lightning"));
    }

    @Test
    void getScryfallUrl_withNoName_returnsEmptyString() {
        Card kortUdenNavn = new Card();
        assertEquals("", kortUdenNavn.getScryfallUrl());
    }

    // --- setName ---

    @Test
    void setName_withNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> card.setName(null));
    }

    @Test
    void setName_withBlankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> card.setName(""));
    }

    @Test
    void setName_withValidName_updatesName() {
        card.setName("Counterspell");
        assertEquals("Counterspell", card.getName());
    }
}
