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
    public String getAllCards(Model model) {
        model.addAttribute("cards", cardService.getAll());
        return "cards/card-list";
    }

    @GetMapping("/{id}")
    public String getCard(@PathVariable int id, Model model) {
        model.addAttribute("card", cardService.getById(id));
        return "cards/card-detail";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("card", new Card());
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
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("card", cardService.getById(id));
        model.addAttribute("tilbage", "/cards");
        return "cards/delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        cardService.delete(id);
        return "redirect:/cards";
    }
}