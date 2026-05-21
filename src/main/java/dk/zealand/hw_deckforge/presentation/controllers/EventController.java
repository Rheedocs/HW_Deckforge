package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.DeckService;
import dk.zealand.hw_deckforge.application.service.EventService;
import dk.zealand.hw_deckforge.application.service.ResultService;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final DeckService deckService;
    private final ResultService resultService;

    public EventController(EventService eventService, DeckService deckService, ResultService resultService) {
        this.eventService = eventService;
        this.deckService = deckService;
        this.resultService = resultService;
    }

    // --- Forespørgsler ---

    @GetMapping
    public String getAllEvents(Model model) {
        model.addAttribute("events", eventService.getAll());
        return "events/event-list";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable int id, Model model, HttpSession session) {
        Event event = eventService.getById(id);
        model.addAttribute("event", event);
        model.addAttribute("registrations", eventService.getRegistrationsByEventId(id));
        model.addAttribute("results", resultService.getByEventId(id));
        model.addAttribute("isFull", eventService.isFull(id));
        Player player = (Player) session.getAttribute("player");
        if (player != null) {
            model.addAttribute("isRegistered", eventService.isPlayerRegistered(player.getId(), id));
            model.addAttribute("decks", deckService.getByPlayerIdAndFormat(player.getId(), event.getFormat()));
        }
        return "events/event-detail";
    }

    // --- Livscyklus ---

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("event", new Event());
        return "events/add-event";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Event event, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        eventService.create(event);
        return "redirect:/events";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("event", eventService.getById(id));
        return "events/edit-event";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, @ModelAttribute Event event, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        event.setId(id);
        eventService.update(event);
        return "redirect:/events";
    }

    @GetMapping("/{id}/delete")
    public String showDeleteConfirm(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("event", eventService.getById(id));
        model.addAttribute("deleteUrl", "/events/" + id + "/delete");
        model.addAttribute("cancelUrl", "/events");
        return "delete-confirm";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        eventService.delete(id);
        return "redirect:/events";
    }

    // --- Tilmelding ---

    @PostMapping("/{id}/register")
    public String register(@PathVariable int id, @RequestParam int deckId, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        Player player = AuthHelper.getLoggedIn(session);
        eventService.registerPlayer(player.getId(), id, deckId);
        return "redirect:/events/" + id;
    }
}