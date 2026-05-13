package dk.zealand.hw_deckforge.domain.enums;

public enum Format {
    COMMANDER("Commander"),
    STANDARD("Standard"),
    DRAFT("Draft"),
    CASUAL("Casual");

    private final String displayName;

    Format(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}