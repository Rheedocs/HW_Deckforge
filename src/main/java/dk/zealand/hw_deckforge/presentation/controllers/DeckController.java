package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.DeckService;
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

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

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
        return "decks/edit-deck";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Deck deck, HttpSession session) {
        if (!AuthHelper.isSelf(session, id)) return "redirect:/access-denied";
        deckService.update(deck);
        return "redirect:/decks/player/" + deck.getPlayerId();
    }

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        int playerId = AuthHelper.getLoggedIn(session).getId();
        deckService.delete(id, playerId);
        return "redirect:/decks";
    }

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
}