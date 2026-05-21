package dk.zealand.hw_deckforge.domain.enums;

public enum TradeStatus {
    PENDING("Afventer"),
    ACCEPTED("Accepteret"),
    COMPLETED("Gennemført"),
    DECLINED("Afvist"),
    CANCELLED("Annulleret"),
    EXPIRED("Udløbet");

    private final String displayName;

    TradeStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

