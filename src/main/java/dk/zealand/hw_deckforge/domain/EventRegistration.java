package dk.zealand.hw_deckforge.domain;

import java.time.LocalDate;

public class EventRegistration {
    private final int id;
    private final int playerId;
    private final int eventId;
    private final int deckId;
    private final LocalDate registrationDate;

    public EventRegistration(int id, int playerId, int eventId, int deckId, LocalDate registrationDate) {
        this.id = id;
        this.playerId = playerId;
        this.eventId = eventId;
        this.deckId = deckId;
        this.registrationDate = registrationDate;
    }

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getEventId() { return eventId; }
    public int getDeckId() { return deckId; }
    public LocalDate getRegistrationDate() { return registrationDate; }
}