package dk.zealand.hw_deckforge.presentation.exceptions;

import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;
import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    // --- Fejlhåndtering ---

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex) {
        return "redirect:/access-denied";
    }

    @ExceptionHandler(DatabaseException.class)
    public String handleDatabaseException(DatabaseException ex, Model model) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        return "error";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model, HttpServletRequest request) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        model.addAttribute("tilbage", request.getHeader("Referer"));
        return "error";
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidation(ValidationException ex, Model model, HttpServletRequest request) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        model.addAttribute("tilbage", request.getHeader("Referer"));
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model, HttpServletRequest request) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        model.addAttribute("tilbage", request.getHeader("Referer"));
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        return "error";
    }
}


