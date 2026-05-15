package dk.zealand.hw_deckforge.domain;

public class PlayerCard {
    private int id;
    private int playerId;
    private int cardId;
    private int quantity;
    private boolean forTrade;

    public PlayerCard(int id, int playerId, int cardId, int quantity, boolean forTrade) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (cardId <= 0) throw new IllegalArgumentException("Ugyldigt kort-id");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        this.id = id;
        this.playerId = playerId;
        this.cardId = cardId;
        this.quantity = quantity;
        this.forTrade = forTrade;
    }

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public int getCardId() { return cardId; }
    public int getQuantity() { return quantity; }
    public boolean isForTrade() { return forTrade; }

    public void setQuantity(int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        this.quantity = quantity;
    }

    public void markForTrade() { this.forTrade = true; }
    public void unmarkForTrade() { this.forTrade = false; }
}