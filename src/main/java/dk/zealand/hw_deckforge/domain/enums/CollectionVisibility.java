package dk.zealand.hw_deckforge.domain.enums;

public enum CollectionVisibility {
    PRIVATE("Privat"),
    TRADE_ONLY("Kun byttekort"),
    PUBLIC("Offentlig");

    private final String displayName;

    CollectionVisibility(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}