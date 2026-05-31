package dk.zealand.hw_deckforge.domain.exceptions;

/** Wrapper for DataAccessException. Abstraherer infrastrukturfejl fra forretningslaget. */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

