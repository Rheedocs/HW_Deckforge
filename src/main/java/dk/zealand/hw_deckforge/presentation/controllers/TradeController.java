package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.CardService;
import dk.zealand.hw_deckforge.application.service.PlayerCardService;
import dk.zealand.hw_deckforge.application.service.TradeService;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.TradeCard;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trades")
public class TradeController {

    private final TradeService tradeService;
    private final PlayerCardService playerCardService;
    private final CardService cardService;

    public TradeController(TradeService tradeService, PlayerCardService playerCardService,
                           CardService cardService) {
        this.tradeService = tradeService;
        this.playerCardService = playerCardService;
        this.cardService = cardService;
    }

    // --- Oversigt ---

    @GetMapping
    public String showTradePage(Model model, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        List<Trade> all = tradeService.getAllByPlayerId(playerId);
        Map<Integer, List<TradeCard>> tradeCardMap = tradeService.getTradeCardMap(all);
        model.addAttribute("loggedInId", playerId);
        model.addAttribute("received", tradeService.getReceivedByPlayerId(all, playerId));
        model.addAttribute("sent", tradeService.getSentByPlayerId(all, playerId));
        model.addAttribute("history", tradeService.getHistoryByPlayerId(all));
        model.addAttribute("playerMap", tradeService.getPlayerMap(all));
        model.addAttribute("tradeCardMap", tradeCardMap);
        model.addAttribute("playerCardMap", playerCardService.getPlayerCardMapByIds(tradeService.getPlayerCardIds(tradeCardMap)));
        model.addAttribute("cardMap", cardService.getCardMap());
        return "trades/trade-overview";
    }

    // --- Foreslå bytte ---

    @GetMapping("/propose/{receiverId}")
    public String showProposalForm(@PathVariable int receiverId,
                                   @RequestParam(required = false) Integer receiverCardId,
                                   Model model, HttpSession session) {
        int proposerId = AuthHelper.getLoggedIn(session).getId();
        tradeService.validateProposal(proposerId, receiverId);
        model.addAttribute("proposerCards", playerCardService.getForTradeByPlayerId(proposerId));
        model.addAttribute("receiverCards", playerCardService.getForTradeByPlayerId(receiverId));
        model.addAttribute("cardMap", cardService.getCardMap());
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("preselectedReceiverCardId", receiverCardId);
        return "trades/trade-propose";
    }

    @PostMapping("/propose/{receiverId}")
    public String propose(@PathVariable int receiverId,
                          @RequestParam List<Integer> proposerCardIds,
                          @RequestParam List<Integer> receiverCardIds,
                          HttpSession session) {
        int proposerId = AuthHelper.getLoggedIn(session).getId();
        tradeService.propose(proposerId, receiverId, proposerCardIds, receiverCardIds);
        return "redirect:/trades";
    }

    // --- Handlinger ---

    @PostMapping("/{tradeId}/accept")
    public String accept(@PathVariable int tradeId, HttpSession session) {
        tradeService.accept(tradeId);
        return "redirect:/trades";
    }

    @PostMapping("/{tradeId}/decline")
    public String decline(@PathVariable int tradeId, HttpSession session) {
        tradeService.decline(tradeId);
        return "redirect:/trades";
    }

    @PostMapping("/{tradeId}/cancel")
    public String cancel(@PathVariable int tradeId, HttpSession session) {
        tradeService.cancel(tradeId);
        return "redirect:/trades";
    }

    @PostMapping("/{tradeId}/complete")
    public String complete(@PathVariable int tradeId, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        tradeService.complete(tradeId, playerId);
        return "redirect:/trades";
    }

}