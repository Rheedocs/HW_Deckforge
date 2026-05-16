package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.CardService;
import dk.zealand.hw_deckforge.application.service.DeckService;
import dk.zealand.hw_deckforge.application.service.PlayerCardService;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckService deckService;
    private final CardService cardService;
    private final PlayerCardService playerCardService;

    public DeckController(DeckService deckService, CardService cardService, PlayerCardService playerCardService) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.playerCardService = playerCardService;
    }

    // --- Deck liste og detalje ---

    @GetMapping("/player/{playerId}")
    public String getDecksByPlayer(@PathVariable int playerId, Model model) {
        List<Deck> decks = deckService.getByPlayerId(playerId);
        model.addAttribute("decks", decks);
        return "decks/deck-list";
    }

    @GetMapping
    public String getAllDecks(HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        return "redirect:/decks/player/" + playerId;
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable int id, Model model, HttpSession session) {
        Deck deck = deckService.getById(id);
        model.addAttribute("deck", deck);
        model.addAttribute("deckCards", deckService.getDeckCards(id));
        model.addAttribute("cardMap", cardService.getCardMap());
        model.addAttribute("allCards", cardService.getAll());
        model.addAttribute("totalCardCount", deckService.getTotalCardCount(id));
        model.addAttribute("playerCardMap", playerCardService.getPlayerCardMap(deck.getPlayerId()));
        model.addAttribute("isSelf", AuthHelper.isAdmin(session) || AuthHelper.isSelf(session, deck.getPlayerId()));
        return "decks/deck-detail";
    }

    // --- Opret og rediger deck ---

    @GetMapping("/create")
    public String showCreateForm() {
        return "decks/add-deck";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Deck deck, HttpSession session) {
        if (!AuthHelper.isAdmin(session) && !AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        deckService.create(deck);
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        Deck deck = deckService.getById(id);
        if (!AuthHelper.isAdmin(session) && !AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        model.addAttribute("deck", deck);
        return "decks/edit-deck";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Deck deck, HttpSession session) {
        if (!AuthHelper.isAdmin(session) && !AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        deck.setId(id);
        deckService.update(deck);
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    // --- Slet deck ---

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session,
                                    @RequestHeader(value = "Referer", required = false) String referer) {
        Deck deck = deckService.getById(id);
        if (!AuthHelper.isAdmin(session) && !AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        model.addAttribute("navn", deck.getName());
        model.addAttribute("deleteUrl", "/decks/" + id + "/delete");
        model.addAttribute("tilbage", referer != null ? referer : "/decks/player/" + deck.getPlayerId());
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        Deck deck = deckService.getById(id);
        deckService.delete(id, AuthHelper.resolveOwnerId(session, deck.getPlayerId()));
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    // --- Synlighed ---

    @PostMapping("/{id}/make-public")
    public String makePublic(@PathVariable int id, HttpSession session) {
        Deck deck = deckService.getById(id);
        deckService.makePublic(id, AuthHelper.resolveOwnerId(session, deck.getPlayerId()));
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    @PostMapping("/{id}/make-private")
    public String makePrivate(@PathVariable int id, HttpSession session) {
        Deck deck = deckService.getById(id);
        deckService.makePrivate(id, AuthHelper.resolveOwnerId(session, deck.getPlayerId()));
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    // --- Kort i deck ---

    @PostMapping("/{id}/cards/add")
    public String addCard(@PathVariable int id,
                          @RequestParam int cardId,
                          @RequestParam int quantity,
                          HttpSession session) {
        Deck deck = deckService.getById(id);
        deckService.addCard(id, cardId, quantity, AuthHelper.resolveOwnerId(session, deck.getPlayerId()));
        return "redirect:/decks/" + id;
    }

    @PostMapping("/{id}/cards/{deckCardId}/remove")
    public String removeCard(@PathVariable int id,
                             @PathVariable int deckCardId,
                             @RequestParam(defaultValue = "1") int quantity,
                             HttpSession session) {
        Deck deck = deckService.getById(id);
        deckService.removeCard(deckCardId, id, quantity, AuthHelper.resolveOwnerId(session, deck.getPlayerId()));
        return "redirect:/decks/" + id;
    }
}
