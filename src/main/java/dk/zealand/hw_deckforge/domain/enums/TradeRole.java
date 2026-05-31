package dk.zealand.hw_deckforge.domain.enums;

/** Rolle i et bytte. PROPOSER er forslagsstilleren, RECEIVER er modtageren. */
public enum TradeRole {
    PROPOSER("Forslagsstiller"),
    RECEIVER("Modtager");

    private final String displayName;

    TradeRole(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

