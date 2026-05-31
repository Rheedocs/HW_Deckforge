package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.TradeService;
import dk.zealand.hw_deckforge.domain.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Viser forsiden med indikator for indgående bytteforslag og aktive bytter. */
@Controller
public class HomeController {

    private final TradeService tradeService;

    public HomeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Player player = (Player) session.getAttribute("player");
        if (player != null) {
            int incomingTradeCount = tradeService.countIncomingPendingTrades(player.getId());
            int activeTradeCount = tradeService.countActiveAcceptedTrades(player.getId());
            model.addAttribute("incomingTradeCount", incomingTradeCount);
            model.addAttribute("activeTradeCount", activeTradeCount);
        }
        return "index";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}