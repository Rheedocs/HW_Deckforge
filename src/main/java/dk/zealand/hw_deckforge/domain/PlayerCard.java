package dk.zealand.hw_deckforge.domain;

public class PlayerCard {
    private int id;
    private int playerId;
    private int cardId;
    private int quantity;
    private boolean forTrade;

    public PlayerCard(int id, int playerId, int cardId, int quantity, boolean forTrade) {
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

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setForTrade(boolean forTrade) { this.forTrade = forTrade; }
}