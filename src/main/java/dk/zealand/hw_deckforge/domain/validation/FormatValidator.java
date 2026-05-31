package dk.zealand.hw_deckforge.domain.validation;

import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Format;


/**
 * Magic-formatregler samlet ét sted. Strategy-lignende:
 * reglerne varierer pr. format, logikken er centraliseret.
 */
public class FormatValidator {

    private static final String[] BASIC_LAND_NAMES = {"Forest", "Island", "Mountain", "Plains", "Swamp"};

    private FormatValidator() {}

    // --- Validering ---

    /** Commander max 1 kopi, Standard og Casual max 4, basic lands undtaget. */
    public static void validateFormatLimit(Format format, Card card, int alreadyInDeck, int quantity) {
        if (format == null) throw new IllegalArgumentException("Format skal angives");
        if (card == null) throw new IllegalArgumentException("Kort skal angives");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");

        int totalCopies = alreadyInDeck + quantity;

        if (format == Format.COMMANDER && isNotBasicLand(card) && totalCopies > 1)
            throw new IllegalArgumentException("Commander tillader max 1 eksemplar af hvert kort undtagen basic lands");

        if ((format == Format.STANDARD || format == Format.CASUAL) && isNotBasicLand(card) && totalCopies > 4)
            throw new IllegalArgumentException(format.getDisplayName() + " tillader max 4 eksemplarer af hvert kort undtagen basic lands");
    }

    /** Commander præcis 100 kort, Standard minimum 60. */
    public static void validateDeckSize(Format format, int totalCards) {
        if (format == null) throw new IllegalArgumentException("Format skal angives");
        if (totalCards < format.getMinSize())
            throw new IllegalArgumentException(format.getDisplayName() + " kræver mindst " + format.getMinSize() + " kort");
        if (format.hasMaxSize() && totalCards != format.getMaxSize())
            throw new IllegalArgumentException(format.getDisplayName() + " kræver præcis " + format.getMaxSize() + " kort");
    }

    // --- Intern behandling ---

    private static boolean isNotBasicLand(Card card) {
        if (card == null || card.getCardType() != CardType.LAND) return true;
        for (String name : BASIC_LAND_NAMES) {
            if (name.equals(card.getName())) return false;
        }
        return true;
    }
}


