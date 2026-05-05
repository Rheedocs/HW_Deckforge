package dk.zealand.hw_deckforge.presentation.helpers;

import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.Role;
import jakarta.servlet.http.HttpSession;

/**
 * Statisk hjælpeklasse til session-baseret adgangskontrol.
 * Bruges af controllers til at tjekke om en bruger er logget ind,
 * har admin-rolle, eller forsøger at tilgå sine egne data.
 */
public class AuthHelper {

    public static Player getLoggedIn(HttpSession session) {
        return (Player) session.getAttribute("player");
    }

    public static boolean isAdmin(HttpSession session) {
        Player player = getLoggedIn(session);
        return player != null && player.getRole() == Role.ADMIN;
    }

    public static boolean isSelf(HttpSession session, int id) {
        Player player = getLoggedIn(session);
        return player != null && player.getId() == id;
    }

    public static boolean isAdminOrSelf(HttpSession session, int id) {
        return isAdmin(session) || isSelf(session, id);
    }
}