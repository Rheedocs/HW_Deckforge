package dk.zealand.hw_deckforge.domain;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private int id;
    private int playerId;
    private int eventId;
    private int placement;

    public Result() {}

    public Result(int id, int playerId, int eventId, int placement) {
        this.id = id;
        this.playerId = playerId;
        this.eventId = eventId;
        this.placement = placement;
    }

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getEventId() { return eventId; }
    public int getPlacement() { return placement; }

    public void setPlacement(int placement) { this.placement = placement; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (playerId <= 0)
            errors.add("Spiller skal vælges");

        if (eventId <= 0)
            errors.add("Event skal angives");

        if (placement <= 0)
            errors.add("Placering skal være mindst 1");

        return errors;
    }
}