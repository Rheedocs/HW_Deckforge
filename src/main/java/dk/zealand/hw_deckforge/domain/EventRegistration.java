package dk.zealand.hw_deckforge.domain;

import java.time.LocalDate;

/** Mellemled mellem spiller, event og deck. Registrerer en spillers tilmelding med valgt deck. */
public class EventRegistration {
    private final int id;
    private final int playerId;
    private final int eventId;
    private final int deckId;
    private final LocalDate registrationDate;
    private final String playerName;
    private final String deckName;

    public EventRegistration(int id, int playerId, int eventId, int deckId, LocalDate registrationDate,
                             String playerName, String deckName) {
        this.id = id;
        this.playerId = playerId;
        this.eventId = eventId;
        this.deckId = deckId;
        this.registrationDate = registrationDate;
        this.playerName = playerName;
        this.deckName = deckName;
    }

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getEventId() { return eventId; }
    public int getDeckId() { return deckId; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getPlayerName() { return playerName; }
    public String getDeckName() { return deckName; }
}

