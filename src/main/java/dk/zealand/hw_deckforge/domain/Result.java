package dk.zealand.hw_deckforge.domain;

import java.util.ArrayList;
import java.util.List;

/** Gemmer en spillers placering i et afsluttet event. */
public class Result {
    private int id;
    private int playerId;
    private int eventId;
    private int placement;
    private String playerName;

    public Result() {}

    public Result(int id, int playerId, int eventId, int placement) {
        this(id, playerId, eventId, placement, null);
    }

    public Result(int id, int playerId, int eventId, int placement, String playerName) {
        this.id = id;
        this.playerId = playerId;
        this.eventId = eventId;
        this.placement = placement;
        this.playerName = playerName;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getEventId() { return eventId; }
    public int getPlacement() { return placement; }
    public String getPlayerName() { return playerName; }
    public String getPlacementText() { return placement + ". plads"; }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setPlacement(int placement) { this.placement = placement; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    // --- Validering ---

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (playerId <= 0) errors.add("Spiller skal vælges");
        if (eventId <= 0) errors.add("Event skal angives");
        if (placement <= 0) errors.add("Placering skal være mindst 1");
        return errors;
    }

    public void validateOrThrow() {
        List<String> errors = validate();
        if (!errors.isEmpty()) throw new IllegalArgumentException(String.join(", ", errors));
    }
}

