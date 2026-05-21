package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.PlayerService;
import dk.zealand.hw_deckforge.domain.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final PlayerService playerService;

    public LoginController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // --- Login ---

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        try {
            Player player = playerService.login(email, password);
            session.setAttribute("player", player);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Forkert email eller adgangskode");
            return "login";
        }
    }

    // --- Session ---

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}