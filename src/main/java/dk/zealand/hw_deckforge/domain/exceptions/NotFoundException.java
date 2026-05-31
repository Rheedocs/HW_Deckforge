package dk.zealand.hw_deckforge.domain.exceptions;

/** Kastes når en entitet ikke findes i databasen. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

