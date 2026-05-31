package dk.zealand.hw_deckforge.domain.enums;

/** Magic-formater med tilhørende størrelsesbegrænsninger. Bruges af FormatValidator og EventService. */
public enum Format {
    COMMANDER("Commander", 100, 100),
    STANDARD("Standard", 60, 0),
    DRAFT("Draft", 40, 0),
    CASUAL("Casual", 60, 0);

    private final String displayName;
    private final int minSize;
    private final int maxSize;

    Format(String displayName, int minSize, int maxSize) {
        this.displayName = displayName;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public String getDisplayName() { return displayName; }

    /** @return minimumsstørrelse for et gyldigt deck i dette format */
    public int getMinSize() { return minSize; }
    public int getMaxSize() { return maxSize; }
    public boolean hasMaxSize() { return maxSize > 0; }
}

