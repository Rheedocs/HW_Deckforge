package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.EventService;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.Result;
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

    @GetMapping
    public String getAllEvents(Model model, HttpSession session) {
        model.addAttribute("Events", eventService.getAll());
        return "events/event-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("event", new Event());
        return "events/add-event";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Event event, HttpSession session) {
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        eventService.create(event);
        return "redirect:/events";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable int id, Model model, HttpSession session) {
        return "events/event-detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        model.addAttribute("Event", eventService.getById(id));
        return "events/edit-event";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Event event, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        event.setEventId(id);
        eventService.update(event);
        return "redirect:/events";
    }

    @PostMapping("/{id}/register")
    public String register(@PathVariable int id, @RequestParam int deckId, HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        eventService.registerPlayer(id, player.getId(), deckId);
        return "redirect:/events/" + id;
    }

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        Event event = eventService.getById(id);
        model.addAttribute("event", event);
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        if (!AuthHelper.isAdminOrSelf(session, id)) return "redirect:/access-denied";
        eventService.delete(id);
        if (AuthHelper.isSelf(session, id)) {
            session.invalidate();
            return "redirect:/login";
        }
        return "redirect:/event-list";
    }

}