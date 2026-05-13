package dk.zealand.hw_deckforge.domain.enums;

public enum DeckVisibility {
    PRIVATE("Privat"),
    PUBLIC("Offentlig");

    private final String displayName;

    DeckVisibility(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}