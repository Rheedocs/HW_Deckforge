package dk.zealand.hw_deckforge.domain.enums;

/** Brugerroller i systemet. PLAYER har adgang til samling, decks, events og bytning. ADMIN kan oprette kortdata. */
public enum Role {
    PLAYER("Spiller"),
    ADMIN("Admin");

    private final String displayName;

    Role(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}

