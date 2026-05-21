package dk.zealand.hw_deckforge.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCardTest {

    private PlayerCard playerCard;

    @BeforeEach
    void setUp() {
        playerCard = new PlayerCard(1, 2, 3, 2, false);
    }

    // --- markForTrade / unmarkForTrade ---

    @Test
    void markForTrade_setsForTradeTrue() {
        playerCard.markForTrade();
        assertTrue(playerCard.isForTrade());
    }

    @Test
    void unmarkForTrade_setsForTradeFalse() {
        playerCard.markForTrade();
        playerCard.unmarkForTrade();
        assertFalse(playerCard.isForTrade());
    }

    // --- setQuantity ---

    @Test
    void setQuantity_withValidQuantity_updatesQuantity() {
        playerCard.setQuantity(5);
        assertEquals(5, playerCard.getQuantity());
    }

    @Test
    void setQuantity_withZero_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> playerCard.setQuantity(0));
    }

    @Test
    void setQuantity_withNegative_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> playerCard.setQuantity(-1));
    }

    // --- Konstruktør validering ---

    @Test
    void constructor_withInvalidPlayerId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PlayerCard(0, 0, 3, 1, false));
    }

    @Test
    void constructor_withInvalidCardId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PlayerCard(0, 1, 0, 1, false));
    }

    @Test
    void constructor_withInvalidQuantity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PlayerCard(0, 1, 1, 0, false));
    }
}
