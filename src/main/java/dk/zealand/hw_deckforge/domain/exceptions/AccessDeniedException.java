package dk.zealand.hw_deckforge.domain.exceptions;

/** Kastes når en bruger forsøger at tilgå en ressource de ikke har adgang til. */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}

