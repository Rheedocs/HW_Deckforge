package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(1, "TestSpiller", "test@mail.dk", "encoded_password",
                Role.PLAYER, CollectionVisibility.TRADE_ONLY);
    }

    // --- isAdmin ---

    @Test
    void isAdmin_ifRoleIsPlayer_returnsFalse() {
        assertFalse(player.isAdmin());
    }

    @Test
    void isAdmin_ifRoleIsAdmin_returnsTrue() {
        player.promoteToAdmin();
        assertTrue(player.isAdmin());
    }

    // --- promoteToAdmin og demoteToPlayer ---

    @Test
    void promoteToAdmin_setsRoleToAdmin() {
        player.promoteToAdmin();
        assertEquals(Role.ADMIN, player.getRole());
    }

    @Test
    void promoteToAdmin_alreadyAdmin_remainsAdmin() {
        player.promoteToAdmin();
        player.promoteToAdmin();
        assertEquals(Role.ADMIN, player.getRole());
    }

    @Test
    void demoteToPlayer_setsRoleToPlayer() {
        player.promoteToAdmin();
        player.demoteToPlayer();
        assertEquals(Role.PLAYER, player.getRole());
    }

    // --- changeVisibility ---

    @Test
    void changeVisibility_withValidVisibility_updatesVisibility() {
        player.changeVisibility(CollectionVisibility.PUBLIC);
        assertEquals(CollectionVisibility.PUBLIC, player.getCollectionVisibility());
    }

    @Test
    void changeVisibility_withNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> player.changeVisibility(null));
    }

    // --- changePassword ---

    @Test
    void changePassword_withValidPassword_updatesPassword() {
        player.changePassword("nyt_encoded_password");
        assertEquals("nyt_encoded_password", player.getPassword());
    }

    @Test
    void changePassword_withBlankPassword_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> player.changePassword(""));
    }

    @Test
    void changePassword_withNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> player.changePassword(null));
    }

    // --- isCollectionVisible ---

    @Test
    void isCollectionVisible_ifMatchingVisibility_returnsTrue() {
        assertTrue(player.isCollectionVisible(CollectionVisibility.TRADE_ONLY));
    }

    @Test
    void isCollectionVisible_ifNotMatchingVisibility_returnsFalse() {
        assertFalse(player.isCollectionVisible(CollectionVisibility.PUBLIC));
    }

    @Test
    void isCollectionVisible_withNull_returnsFalse() {
        assertFalse(player.isCollectionVisible(null));
    }

    // --- toString ---

    @Test
    void toString_doesNotContainPassword() {
        assertFalse(player.toString().contains("encoded_password"));
    }
}