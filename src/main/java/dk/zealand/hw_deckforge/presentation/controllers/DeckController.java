package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.CardService;
import dk.zealand.hw_deckforge.application.service.DeckService;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.DeckCard;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckService deckService;
    private final CardService cardService;

    public DeckController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    // --- Deck liste og detalje ---

    @GetMapping("/player/{playerId}")
    public String getDecksByPlayer(@PathVariable int playerId, Model model, HttpSession session) {
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
        List<DeckCard> deckCards = deckService.getDeckCards(id);
        List<Card> allCards = cardService.getAll();
        Map<Integer, Card> cardMap = new HashMap<>();
        for (Card card : allCards) {
            cardMap.put(card.getId(), card);
        }
        model.addAttribute("deck", deck);
        model.addAttribute("deckCards", deckCards);
        model.addAttribute("cardMap", cardMap);
        model.addAttribute("allCards", allCards);
        model.addAttribute("isSelf", AuthHelper.isSelf(session, deck.getPlayerId()));
        return "decks/deck-detail";
    }

    // --- Opret og rediger deck ---

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        return "decks/add-deck";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Deck deck, HttpSession session) {
        if (!AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        deckService.create(deck);
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        Deck deck = deckService.getById(id);
        if (!AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        model.addAttribute("deck", deck);
        return "decks/edit-deck";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Deck deck, HttpSession session) {
        if (!AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        deckService.update(deck);
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    // --- Slet deck ---

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        Deck deck = deckService.getById(id);
        if (!AuthHelper.isSelf(session, deck.getPlayerId())) return "redirect:/access-denied";
        model.addAttribute("deck", deck);
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        deckService.delete(id, playerId);
        return "redirect:/decks";
    }

    // --- Synlighed ---

    @PostMapping("/{id}/make-public")
    public String makePublic(@PathVariable int id, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        deckService.makePublic(id, playerId);
        return "redirect:/decks/player/" + playerId;
    }

    @PostMapping("/{id}/make-private")
    public String makePrivate(@PathVariable int id, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        deckService.makePrivate(id, playerId);
        return "redirect:/decks/player/" + playerId;
    }

    // --- Kort i deck ---

    @PostMapping("/{id}/cards/add")
    public String addCard(@PathVariable int id,
                          @RequestParam int cardId,
                          @RequestParam int quantity,
                          HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        deckService.addCard(id, cardId, quantity, playerId);
        return "redirect:/decks/" + id;
    }

    @PostMapping("/{id}/cards/{deckCardId}/remove")
    public String removeCard(@PathVariable int id,
                             @PathVariable int deckCardId,
                             HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        deckService.removeCard(deckCardId, id, playerId);
        return "redirect:/decks/" + id;
    }
}
