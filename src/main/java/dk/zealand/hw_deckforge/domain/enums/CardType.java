package dk.zealand.hw_deckforge.domain.enums;

public enum CardType {
    CREATURE("Creature"),
    LAND("Land"),
    INSTANT("Instant"),
    SORCERY("Sorcery"),
    ARTIFACT("Artifact"),
    ENCHANTMENT("Enchantment"),
    PLANESWALKER("Planeswalker");

    private final String displayName;

    CardType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}