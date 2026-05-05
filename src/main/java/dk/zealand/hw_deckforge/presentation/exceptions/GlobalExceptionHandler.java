package dk.zealand.hw_deckforge.presentation.exceptions;

import dk.zealand.hw_deckforge.domain.exceptions.DatabaseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Fanger exceptions der bobler op fra alle lag og returnerer en brugervenlig fejlside.
 * Annotationen @ControllerAdvice betyder at handleren gælder globalt for alle controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(DatabaseException.class)
    public String handleDatabaseException(DatabaseException ex, Model model) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("fejl", List.of(ex.getMessage()));
        return "error";
    }
}