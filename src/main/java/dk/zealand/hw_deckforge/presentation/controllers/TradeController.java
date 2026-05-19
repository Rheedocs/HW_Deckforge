package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.TradeService;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    // --- Oversigt ---

    @GetMapping
    public String showTradePage(Model model, HttpSession session) { return "trades/trade-overview"; }

    @GetMapping("/incoming")
    public String showIncoming(Model model, HttpSession session) { return "trades/trade-incoming"; }

    @GetMapping("/history")
    public String showHistory(Model model, HttpSession session) { return "trades/trade-history"; }

    // --- Forslag ---

    @GetMapping("/propose/{receiverId}")
    public String showProposalForm(@PathVariable int receiverId, Model model, HttpSession session) {
        return "trades/trade-propose"; }

    @PostMapping("/propose/{receiverId}")
    public String propose(@PathVariable int receiverId, @RequestParam List<Integer> proposerCardIds,
                          @RequestParam List<Integer> receiverCardIds, HttpSession session) {
        return "redirect:/trades"; }

    // --- Handlinger ---

    @PostMapping("/{tradeId}/accept")
    public String accept(@PathVariable int tradeId, HttpSession session) { return "redirect:/trades/incoming"; }

    @PostMapping("/{tradeId}/decline")
    public String decline(@PathVariable int tradeId, HttpSession session) { return "redirect:/trades/incoming"; }

    @PostMapping("/{tradeId}/cancel")
    public String cancel(@PathVariable int tradeId, HttpSession session) { return "redirect:/trades"; }
}
