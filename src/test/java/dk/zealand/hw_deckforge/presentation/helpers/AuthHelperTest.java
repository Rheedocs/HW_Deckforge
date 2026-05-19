package dk.zealand.hw_deckforge.presentation.helpers;

import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class AuthHelperTest {

    private MockHttpSession session;
    private Player admin;
    private Player player;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        admin = new Player(1, "admin", "admin@deckforge.dk", "encoded",
                Role.ADMIN, CollectionVisibility.PUBLIC);
        player = new Player(2, "spiller", "spiller@deckforge.dk", "encoded",
                Role.PLAYER, CollectionVisibility.PUBLIC);
    }

    // --- getLoggedIn ---

    @Test
    void getLoggedIn_withPlayerInSession_returnsPlayer() {
        session.setAttribute("player", player);
        assertEquals(player, AuthHelper.getLoggedIn(session));
    }

    @Test
    void getLoggedIn_withNoSession_returnsNull() {
        assertNull(AuthHelper.getLoggedIn(session));
    }

    // --- isAdmin ---

    @Test
    void isAdmin_withAdminInSession_returnsTrue() {
        session.setAttribute("player", admin);
        assertTrue(AuthHelper.isAdmin(session));
    }

    @Test
    void isAdmin_withPlayerInSession_returnsFalse() {
        session.setAttribute("player", player);
        assertFalse(AuthHelper.isAdmin(session));
    }

    @Test
    void isAdmin_withNoSession_returnsFalse() {
        assertFalse(AuthHelper.isAdmin(session));
    }

    // --- isSelf ---

    @Test
    void isSelf_withMatchingId_returnsTrue() {
        session.setAttribute("player", player);
        assertTrue(AuthHelper.isSelf(session, 2));
    }

    @Test
    void isSelf_withNonMatchingId_returnsFalse() {
        session.setAttribute("player", player);
        assertFalse(AuthHelper.isSelf(session, 99));
    }

    @Test
    void isSelf_withNoSession_returnsFalse() {
        assertFalse(AuthHelper.isSelf(session, 2));
    }

    // --- isAdminOrSelf ---

    @Test
    void isAdminOrSelf_asAdmin_returnsTrue() {
        session.setAttribute("player", admin);
        assertTrue(AuthHelper.isAdminOrSelf(session, 99));
    }

    @Test
    void isAdminOrSelf_asOwner_returnsTrue() {
        session.setAttribute("player", player);
        assertTrue(AuthHelper.isAdminOrSelf(session, 2));
    }

    @Test
    void isAdminOrSelf_asOtherPlayer_returnsFalse() {
        session.setAttribute("player", player);
        assertFalse(AuthHelper.isAdminOrSelf(session, 99));
    }

    // --- resolveOwnerId ---

    @Test
    void resolveOwnerId_asAdmin_returnsResourceOwnerId() {
        session.setAttribute("player", admin);
        assertEquals(5, AuthHelper.resolveOwnerId(session, 5));
    }

    @Test
    void resolveOwnerId_asNonAdmin_returnsLoggedInPlayerId() {
        session.setAttribute("player", player);
        assertEquals(2, AuthHelper.resolveOwnerId(session, 5));
    }
}
