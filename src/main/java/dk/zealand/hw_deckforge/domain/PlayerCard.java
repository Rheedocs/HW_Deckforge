package dk.zealand.hw_deckforge.domain;

/** Mellemled mellem spiller og kort. Gemmer antal og om kortet er markeret til bytning. */
public class PlayerCard {
    private final int id;
    private final int playerId;
    private final int cardId;
    private int quantity;
    private boolean forTrade;

    public PlayerCard(int id, int playerId, int cardId, int quantity, boolean forTrade) {
        validate(playerId, cardId, quantity);
        this.id = id;
        this.playerId = playerId;
        this.cardId = cardId;
        this.quantity = quantity;
        this.forTrade = forTrade;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getCardId() { return cardId; }
    public int getQuantity() { return quantity; }
    public boolean isForTrade() { return forTrade; }

    // --- Adfærd ---

    public void setQuantity(int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        this.quantity = quantity;
    }

    /** Markerer eller afmarkerer kortet som tilgængeligt for bytning. Kaldes af setForTrade i PlayerCardService. */
    public void markForTrade() { this.forTrade = true; }
    public void unmarkForTrade() { this.forTrade = false; }

    // --- Validering ---

    private void validate(int playerId, int cardId, int quantity) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (cardId <= 0) throw new IllegalArgumentException("Ugyldigt kort-id");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
    }
}

