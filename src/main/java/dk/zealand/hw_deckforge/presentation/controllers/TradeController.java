package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.TradeService;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
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


    @GetMapping
    public String showTradePage(Model model, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        model.addAttribute("trades", tradeService.getByPlayerId(playerId));
        return "trades/trade-overview";
    }

    @GetMapping("/incoming")
    public String showIncoming(Model model, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        model.addAttribute("trades", tradeService.getIncomingByPlayerId(playerId));
        return "trades/trade-incoming";
    }

    @GetMapping("/propose/{receiverId}")
    public String showProposalForm(@PathVariable int receiverId, Model model, HttpSession session) {
        int proposerId = AuthHelper.getLoggedIn(session).getId();
        model.addAttribute("trade", new Trade());
        model.addAttribute("proposerCards", tradeService.getByPlayerId(proposerId));
        model.addAttribute("receiverCards", tradeService.getIncomingByPlayerId(receiverId));
        model.addAttribute("receiverId", receiverId);
        return "trades/trade-propose";
    }

    @PostMapping("/propose/{receiverId}")
    public String propose(@PathVariable int receiverId, @RequestParam List<Integer> proposerCardIds,
                          @RequestParam List<Integer> receiverCardIds, HttpSession session) {
        return "redirect:/trades"; }

    @PostMapping("/{tradeId}/accept")
    public String accept(@PathVariable int tradeId, HttpSession session) {
        tradeService.accept(tradeId);
        return "redirect:/trades/incoming"; }

    @PostMapping("/{tradeId}/decline")
    public String decline(@PathVariable int tradeId, HttpSession session) {
        tradeService.decline(tradeId);
        return "redirect:/trades/incoming"; }

    @PostMapping("/{tradeId}/cancel")
    public String cancel(@PathVariable int tradeId, HttpSession session) {
        tradeService.cancel(tradeId);
        return "redirect:/trades"; }

    @GetMapping("/history")
    public String showHistory(Model model, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        // getLoggedInn returnere alle trades som ikke er i PENDING status
        List<Trade> allTrades = tradeService.getByPlayerId(playerId);
        List<Trade> history = allTrades.stream()
                .filter(t -> t.getStatus() != TradeStatus.PENDING)
                .toList();
        model.addAttribute("trades", history);
        return "trades/trade-history";
    }
}