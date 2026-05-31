package dk.zealand.hw_deckforge.domain.enums;

/** Farver i Magic: The Gathering inkl. COLORLESS til farveløse kort. */
public enum Color {
    WHITE("White"),
    BLUE("Blue"),
    BLACK("Black"),
    RED("Red"),
    GREEN("Green"),
    COLORLESS("Colorless");

    private final String displayName;

    Color(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

