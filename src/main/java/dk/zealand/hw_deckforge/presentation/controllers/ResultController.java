package dk.zealand.hw_deckforge.presentation.controllers;

import dk.zealand.hw_deckforge.application.service.ResultService;
import dk.zealand.hw_deckforge.domain.Result;
import dk.zealand.hw_deckforge.presentation.helpers.AuthHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/event/{eventId}")
    public String showResults(@PathVariable int eventId, Model model, HttpSession session) {
        return "results/result-list"; }

    @GetMapping("/event/{eventId}/register")
    public String showRegisterForm(@PathVariable int eventId, Model model, HttpSession session) {
        return "results/result-register"; }

    @PostMapping("/event/{eventId}/register")
    public String save(@PathVariable int eventId, @ModelAttribute List<Result> results, HttpSession session) {
        return "redirect:/results/event/" + eventId; }
}