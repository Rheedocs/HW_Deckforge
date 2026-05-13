package dk.zealand.hw_deckforge.domain.enums;

public enum EventStatus {
    UPCOMING("Kommende"),
    ONGOING("Igangværende"),
    COMPLETED("Afsluttet"),
    CANCELLED("Aflyst");

    private final String displayName;

    EventStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}