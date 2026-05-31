package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.CardService;
import dk.zealand.hw_deckforge.application.service.DeckService;
import dk.zealand.hw_deckforge.application.service.EventService;
import dk.zealand.hw_deckforge.application.service.PlayerCardService;
import dk.zealand.hw_deckforge.application.service.PlayerService;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Modtager HTTP-anmodninger for spillerprofiler og samling og delegerer til PlayerService og PlayerCardService.
 * Ingen forretningslogik. */
@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerCardService playerCardService;
    private final DeckService deckService;
    private final EventService eventService;
    private final CardService cardService;

    public PlayerController(PlayerService playerService, PlayerCardService playerCardService, DeckService deckService,
                            EventService eventService, CardService cardService) {
        this.playerService = playerService;
        this.playerCardService = playerCardService;
        this.deckService = deckService;
        this.eventService = eventService;
        this.cardService = cardService;
    }

    // --- Liste ---

    @GetMapping
    public String getAllPlayers(Model model, HttpSession session) {
        int loggedInId = AuthHelper.getLoggedIn(session).getId();
        boolean isAdmin = AuthHelper.isAdmin(session);
        model.addAttribute("players", playerService.getAllSortedByLoggedIn(loggedInId, isAdmin));
        model.addAttribute("isAdmin", isAdmin);
        return "players/player-list";
    }

    // --- Registrering ---

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        playerService.create(username, email, password);
        return "redirect:/login";
    }

    // --- Profil og samling ---

    @GetMapping("/{id}")
    public String showProfile(@PathVariable int id, Model model, HttpSession session) {
        Player player = playerService.getById(id);
        boolean isSelf = AuthHelper.isSelf(session, id);
        boolean isAdmin = AuthHelper.isAdmin(session);
        int cardCount = playerCardService.getVisibleCount(id, player.getCollectionVisibility(), isSelf, isAdmin);
        int deckCount = deckService.getDeckCount(id);
        int eventCount = eventService.getEventCount(id);
        model.addAttribute("player", player);
        model.addAttribute("isSelf", isSelf);
        model.addAttribute("isPrivate", !isSelf && !isAdmin && player.getCollectionVisibility().name().equals("PRIVATE"));
        model.addAttribute("cardCount", cardCount);
        model.addAttribute("deckCount", deckCount);
        model.addAttribute("eventCount", eventCount);
        return "players/player-profile";
    }

    @GetMapping("/{id}/collection")
    public String showCollection(@PathVariable int id, Model model, HttpSession session) {
        Player owner = playerService.getById(id);
        boolean isSelf = AuthHelper.isSelf(session, id);
        boolean isAdmin = AuthHelper.isAdmin(session);
        playerService.checkCollectionAccess(id, isSelf, isAdmin);
        List<PlayerCard> playerCards = playerCardService.getVisibleCards(id, owner.getCollectionVisibility(), isSelf, isAdmin);
        boolean canManage = isSelf || isAdmin;
        model.addAttribute("playerCards", playerCards);
        model.addAttribute("cardMap", cardService.getCardMap());
        model.addAttribute("owner", owner);
        model.addAttribute("isSelf", isSelf);
        model.addAttribute("canManage", canManage);
        if (canManage) {
            model.addAttribute("allCards", cardService.getAll());
            model.addAttribute("collectionMap", playerCardService.getPlayerCardMap(id));
        }
        return "players/player-collection";
    }

    // --- Redigering ---

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        model.addAttribute("player", playerService.getById(id));
        model.addAttribute("roles", Role.values());
        model.addAttribute("visibilities", CollectionVisibility.values());
        return "players/edit-player";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Player player,
                         @RequestParam(required = false) String newPassword,
                         @RequestParam(required = false, defaultValue = "true") boolean active,
                         HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        player.setId(id);
        player.setActive(active);
        playerService.update(player, newPassword, AuthHelper.isAdmin(session), AuthHelper.isSelf(session, id));
        if (AuthHelper.isSelf(session, id)) session.setAttribute("player", playerService.getById(id));
        return "redirect:/players";
    }

    // --- Slet ---

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session,
                                    @RequestHeader(value = "Referer", required = false) String referer) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        Player player = playerService.getById(id);
        model.addAttribute("navn", player.getUsername());
        model.addAttribute("deleteUrl", "/players/" + id + "/delete");
        model.addAttribute("tilbage", referer != null ? referer : "/players");
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String deletePlayer(@PathVariable int id, HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        playerService.delete(id);
        if (AuthHelper.isSelf(session, id)) {
            session.invalidate();
            return "redirect:/login";
        }
        return "redirect:/players";
    }
}

