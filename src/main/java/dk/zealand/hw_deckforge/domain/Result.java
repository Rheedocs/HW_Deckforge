package dk.zealand.hw_deckforge.domain;

public class Result {
    private final int id;
    private final int playerId;
    private final int eventId;
    private int placement;

    public Result(int id, int playerId, int eventId, int placement) {
        validate(playerId, eventId, placement);
        this.id = id;
        this.playerId = playerId;
        this.eventId = eventId;
        this.placement = placement;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getEventId() { return eventId; }
    public int getPlacement() { return placement; }

    // --- Adfærd ---

    public void setPlacement(int placement) {
        if (placement < 1) throw new IllegalArgumentException("Placering skal være mindst 1");
        this.placement = placement;
    }

    // --- Validering ---

    private void validate(int playerId, int eventId, int placement) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (eventId <= 0) throw new IllegalArgumentException("Ugyldigt event-id");
        if (placement < 1) throw new IllegalArgumentException("Placering skal være mindst 1");
    }
}