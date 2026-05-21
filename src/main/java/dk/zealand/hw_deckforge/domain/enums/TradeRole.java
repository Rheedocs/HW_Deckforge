package dk.zealand.hw_deckforge.domain.enums;

public enum TradeRole {
    PROPOSER("Forslagsstiller"),
    RECEIVER("Modtager");

    private final String displayName;

    TradeRole(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

