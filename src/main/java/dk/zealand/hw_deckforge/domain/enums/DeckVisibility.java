package dk.zealand.hw_deckforge.domain.enums;

/** Synlighedsniveau for decks. PRIVATE skjuler decket for andre spillere, PUBLIC gør det synligt for alle. */
public enum DeckVisibility {
    PRIVATE("Privat"),
    PUBLIC("Offentlig");

    private final String displayName;

    DeckVisibility(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

