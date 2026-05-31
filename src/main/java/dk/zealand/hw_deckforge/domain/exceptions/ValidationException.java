package dk.zealand.hw_deckforge.domain.exceptions;

/** Kastes ved fejl i forretningslogik og brugerinput der ikke opfylder domæneregler. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

