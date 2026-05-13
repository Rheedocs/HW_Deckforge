package dk.zealand.hw_deckforge.domain.enums;

public enum Role {
    PLAYER("Spiller"),
    ADMIN("Admin");

    private final String displayName;

    Role(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}