package dk.zealand.hw_deckforge.domain.enums;

public enum Rarity {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    MYTHIC_RARE("Mythic Rare");

    private final String displayName;

    Rarity(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

