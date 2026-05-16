package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.CardService;
import dk.zealand.hw_deckforge.application.service.PlayerCardService;
import dk.zealand.hw_deckforge.application.service.PlayerService;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
    private final PlayerCardService playerCardService;
    private final PlayerService playerService;

    public CardController(CardService cardService, PlayerCardService playerCardService, PlayerService playerService) {
        this.cardService = cardService;
        this.playerCardService = playerCardService;
        this.playerService = playerService;
    }

    @GetMapping
    public String getAllCards(Model model, HttpSession session) {
        model.addAttribute("cards", cardService.getAll());
        model.addAttribute("isAdmin", AuthHelper.isAdmin(session));
        return "cards/card-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("card", new Card());
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("colors", Color.values());
        model.addAttribute("rarities", Rarity.values());
        return "cards/add-card";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Card card, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        cardService.create(card);
        return "redirect:/cards";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("card", cardService.getById(id));
        model.addAttribute("cardTypes", CardType.values());
        model.addAttribute("colors", Color.values());
        model.addAttribute("rarities", Rarity.values());
        return "cards/edit-card";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Card card, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        card.setId(id);
        cardService.update(card);
        return "redirect:/cards";
    }

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session,
                                    @RequestHeader(value = "Referer", required = false) String referer) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        Card card = cardService.getById(id);
        model.addAttribute("navn", card.getName());
        model.addAttribute("deleteUrl", "/cards/" + id + "/delete");
        model.addAttribute("tilbage", referer != null ? referer : "/cards");
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        cardService.delete(id);
        return "redirect:/cards";
    }

    @GetMapping("/player/{playerId}")
    public String getPlayerCollection(@PathVariable int playerId, Model model, HttpSession session) {
        Player owner = playerService.getById(playerId);
        boolean isSelf = AuthHelper.isSelf(session, playerId);
        boolean isAdmin = AuthHelper.isAdmin(session);

        if (owner.getCollectionVisibility() == CollectionVisibility.PRIVATE && !isSelf && !isAdmin)
            return "redirect:/access-denied";

        model.addAttribute("playerCards", playerCardService.getVisibleCards(
                playerId, owner.getCollectionVisibility(), isSelf, isAdmin));
        model.addAttribute("cardMap", cardService.getCardMap());
        boolean canManage = isSelf || isAdmin;
        model.addAttribute("owner", owner);
        model.addAttribute("isSelf", isSelf);
        model.addAttribute("canManage", canManage);
        if (canManage) {
            model.addAttribute("allCards", cardService.getAll());
            model.addAttribute("collectionMap", playerCardService.getPlayerCardMap(playerId));
        }
        return "cards/player-collection";
    }

    @PostMapping("/player/{playerId}/add")
    public String addToCollection(@PathVariable int playerId,
                                  @RequestParam int cardId,
                                  @RequestParam int quantity,
                                  HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, playerId)) return "redirect:/access-denied";
        playerCardService.addCard(playerId, cardId, quantity);
        return "redirect:/cards/player/" + playerId;
    }

    @PostMapping("/player/{playerId}/{playerCardId}/remove")
    public String removeFromCollection(@PathVariable int playerId,
                                       @PathVariable int playerCardId,
                                       @RequestParam(defaultValue = "1") int quantity,
                                       HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, playerId)) return "redirect:/access-denied";
        playerCardService.removeCard(playerCardId, quantity);
        return "redirect:/cards/player/" + playerId;
    }

    @PostMapping("/player/{playerId}/{playerCardId}/set-trade")
    public String setForTrade(@PathVariable int playerId,
                              @PathVariable int playerCardId,
                              @RequestParam boolean forTrade,
                              HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, playerId)) return "redirect:/access-denied";
        playerCardService.setForTrade(playerCardId, forTrade);
        return "redirect:/cards/player/" + playerId;
    }
}