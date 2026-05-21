package dk.zealand.hw_deckforge.domain.validation;

import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Format;

public class FormatValidator {

    private static final String[] BASIC_LAND_NAMES = {"Forest", "Island", "Mountain", "Plains", "Swamp"};

    // --- Validering ---

    public static void validateFormatLimit(Format format, Card card, int alreadyInDeck, int quantity) {
        if (format == Format.COMMANDER && !isBasicLand(card)) {
            if (alreadyInDeck + quantity > 1)
                throw new IllegalArgumentException(
                        "Commander tillader max 1 eksemplar af hvert kort (undtagen basic lands)");
        } else if (format == Format.STANDARD) {
            if (alreadyInDeck + quantity > 4)
                throw new IllegalArgumentException(
                        "Standard tillader max 4 eksemplarer af hvert kort");
        }
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