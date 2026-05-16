package dk.zealand.hw_deckforge.domain.enums;

public enum Format {
    COMMANDER("Commander", 100),
    STANDARD("Standard", 60),
    DRAFT("Draft", 0),
    CASUAL("Casual", 0);

    private final String displayName;
    private final int minSize;

    Format(String displayName, int minSize) {
        this.displayName = displayName;
        this.minSize = minSize;
    }

    public String getDisplayName() { return displayName; }
    public int getMinSize() { return minSize; }
}