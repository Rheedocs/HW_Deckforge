package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.TradeRole;

public class TradeCard {
    private int id;
    private int tradeId;
    private int playerCardId;
    private TradeRole role;

    public TradeCard(int id, int tradeId, int playerCardId, TradeRole role) {
        this.id = id;
        this.tradeId = tradeId;
        this.playerCardId = playerCardId;
        this.role = role;
    }

    public int getId() { return id; }
    public int getTradeId() { return tradeId; }
    public int getPlayerCardId() { return playerCardId; }
    public TradeRole getRole() { return role; }
}