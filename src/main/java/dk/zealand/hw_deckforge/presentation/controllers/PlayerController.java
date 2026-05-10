package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.PlayerService;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public String getAllPlayers(Model model) {
        model.addAttribute("players", playerService.getAll());
        return "players/player-list";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password) {
        playerService.create(username, email, password);
        return "redirect:/login";
    }

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
                         HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        player.setId(id);
        if (!AuthHelper.isAdmin(session)) {
            player.demoteToPlayer();
        } else if (AuthHelper.isSelf(session, id) && playerService.isOnlyAdmin(id)) {
            player.promoteToAdmin();
        }
        playerService.update(player, newPassword);
        if (AuthHelper.isSelf(session, id)) {
            session.setAttribute("player", playerService.getById(id));
        }
        return "redirect:/players";
    }

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        if (playerService.isOnlyAdmin(id))
            throw new IllegalArgumentException("Du kan ikke slette den eneste admin");
        Player player = playerService.getById(id);
        model.addAttribute("navn", player.getUsername());
        model.addAttribute("deleteUrl", "/players/" + id + "/delete");
        model.addAttribute("tilbage", "/players");
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String deletePlayer(@PathVariable int id, HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        if (playerService.isOnlyAdmin(id))
            throw new IllegalArgumentException("Du kan ikke slette den eneste admin");
        playerService.delete(id);
        // Hvis spilleren slettede sig selv, log ud
        if (AuthHelper.isSelf(session, id)) {
            session.invalidate();
            return "redirect:/login";
        }
        return "redirect:/players";
    }
}