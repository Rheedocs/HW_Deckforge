package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.EventService;
import dk.zealand.hw_deckforge.application.service.PlayerService;
import dk.zealand.hw_deckforge.application.service.ResultService;
import dk.zealand.hw_deckforge.domain.Result;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/results")
public class ResultController {

    private final EventService eventService;
    private final PlayerService playerService;
    private final ResultService resultService;

    public ResultController(EventService eventService, PlayerService playerService, ResultService resultService){
        this.eventService = eventService;
        this.playerService = playerService;
        this.resultService = resultService;
    }
    @GetMapping("/event/{eventId}")
    public String showResults(@PathVariable int eventId, Model model, HttpSession session) {
        model.addAttribute("result", resultService.getByEventId(eventId));
        return "results/result-list";
    }

    @GetMapping("/event/{eventId}/register")
    public String showRegisterForm(@PathVariable int eventId, Model model, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        model.addAttribute("event", eventService.getById(eventId));
        model.addAttribute("players", playerService.getAll());
        model.addAttribute("result", new Result());
        return "results/result-register";
    }

    @PostMapping("/event/{eventId}/register")
    public String save(@PathVariable int eventId, @ModelAttribute Result result, HttpSession session) {
        if (session.getAttribute("player") == null) return "redirect:/login";
        if (!AuthHelper.isAdmin(session)) return "redirect:/access-denied";
        result.setEventId(eventId);
        resultService.save(result);
        return "redirect:/results/event/" + eventId;
    }
}