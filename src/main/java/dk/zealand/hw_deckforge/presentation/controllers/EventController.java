package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.EventService;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // --- Liste og detalje ---

    @GetMapping
    public String getAllEvents(Model model, HttpSession session) { return "events/event-list"; }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable int id, Model model, HttpSession session) { return "events/event-detail"; }

    // --- Opret og rediger ---

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        model.addAttribute("event", new Event(0, "", "",
                null, 0, null, null));
        return "events/add-event";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Event event, HttpSession session) { return "redirect:/events"; }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        model.addAttribute("event", new Event(0, "", "",
                null, 0, null, null));
        return "events/edit-event";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Event event, HttpSession session) {
        return "redirect:/events"; }

    // --- Tilmelding og slet ---

    @PostMapping("/{id}/register")
    public String register(@PathVariable int id, @RequestParam int deckId, HttpSession session) {
        return "redirect:/events/" + id; }

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        return "delete-confirm"; }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) { return "redirect:/events"; }
}
