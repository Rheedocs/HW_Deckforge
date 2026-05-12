package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.CardService;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public String getAllCards(Model model, HttpSession session) {
        model.addAttribute("cards",cardService.getAll());
        return "cards/card-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        model.addAttribute("card", new Card(0, "", null, "",
                null, null, null, null));
        return "cards/add-card";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Card card, HttpSession session) {
        cardService.create(card);
        return "redirect:/cards";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        model.addAttribute("card", new Card(0, "", null, "",
                null, null, null, null));
        return "cards/edit-card";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Card card, HttpSession session) {
        card.setId(id);
        cardService.update(card);
        return "redirect:/cards";
    }


    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        Card card = cardService.getById(id);
        model.addAttribute("card",card);
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        cardService.delete(id);
        return "redirect:/cards"; }
}