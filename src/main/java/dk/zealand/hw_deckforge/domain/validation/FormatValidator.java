package dk.zealand.hw_deckforge.domain.validation;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;

import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Format;

public class FormatValidator {

    private static final String[] BASIC_LAND_NAMES = {"Forest", "Island", "Mountain", "Plains", "Swamp"};

    private FormatValidator() {}

    // --- Validering ---

    public static void validateFormatLimit(Format format, Card card, int alreadyInDeck, int quantity) {
        if (format == null) throw new IllegalArgumentException("Format skal angives");
        if (card == null) throw new IllegalArgumentException("Kort skal angives");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");

        int totalCopies = alreadyInDeck + quantity;

        if (format == Format.COMMANDER && !isBasicLand(card) && totalCopies > 1)
            throw new IllegalArgumentException("Commander tillader max 1 eksemplar af hvert kort undtagen basic lands");

        if ((format == Format.STANDARD || format == Format.CASUAL) && !isBasicLand(card) && totalCopies > 4)
            throw new IllegalArgumentException(format.getDisplayName() + " tillader max 4 eksemplarer af hvert kort undtagen basic lands");
    }

    public static void validateDeckSize(Format format, int totalCards) {
        if (format == null) throw new IllegalArgumentException("Format skal angives");
        if (totalCards < format.getMinSize())
            throw new IllegalArgumentException(format.getDisplayName() + " kræver mindst " + format.getMinSize() + " kort");
        if (format.hasMaxSize() && totalCards != format.getMaxSize())
            throw new IllegalArgumentException(format.getDisplayName() + " kræver præcis " + format.getMaxSize() + " kort");
    }

    public static boolean isDeckSizeValid(Format format, int totalCards) {
        if (format == null) return false;
        if (totalCards < format.getMinSize()) return false;
        return !format.hasMaxSize() || totalCards == format.getMaxSize();
    }

    // --- Intern behandling ---

    private static boolean isBasicLand(Card card) {
        if (card == null || card.getCardType() != CardType.LAND) return false;
        for (String name : BASIC_LAND_NAMES) {
            if (name.equals(card.getName())) return true;
        }
        return false;
    }
}


