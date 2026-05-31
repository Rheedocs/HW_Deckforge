package dk.zealand.hw_deckforge.domain.validation;

/** Statisk domænevalidator for email-format og adgangskodekrav. */
public class PlayerValidator {

    // --- Validering ---

    /** @return true hvis email matcher gyldigt format */
    public static boolean isValidEmail(String email) {
        return email != null && !email.isBlank() &&
                email.matches("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");
    }

    /** Kaster IllegalArgumentException hvis adgangskode er tom eller under 8 tegn. */
    public static void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Adgangskode må ikke være tom");
        if (password.length() < 8)
            throw new IllegalArgumentException("Adgangskode skal være mindst 8 tegn");
    }
}

