package dk.zealand.hw_deckforge.presentation.helpers;

import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.Role;
import jakarta.servlet.http.HttpSession;

/**
 * Statisk hjælpeklasse til adgangskontrol via session.
 * Bruges af controllers til rolletjek og ejerskabsvalidering.
 */
public class AuthHelper {

    // --- Session ---

    /** @return den loggede spiller, eller null hvis ingen er logget ind */
    public static Player getLoggedIn(HttpSession session) {
        return (Player) session.getAttribute("player");
    }

    // --- Adgangstjek ---

    /** @return true hvis spilleren har ADMIN-rollen */
    public static boolean isAdmin(HttpSession session) {
        Player player = getLoggedIn(session);
        return player != null && player.getRole() == Role.ADMIN;
    }

    /** @return true hvis spilleren har det givne id */
    public static boolean isSelf(HttpSession session, int id) {
        Player player = getLoggedIn(session);
        return player != null && player.getId() == id;
    }

    public static boolean isAdminOrSelf(HttpSession session, int id) {
        return isAdmin(session) || isSelf(session, id);
    }

    /** Admin kan handle på vegne af andre. Normal spiller returnerer altid eget id. */
    public static int resolveOwnerId(HttpSession session, int resourceOwnerId) {
        return isAdmin(session) ? resourceOwnerId : getLoggedIn(session).getId();
    }
}
