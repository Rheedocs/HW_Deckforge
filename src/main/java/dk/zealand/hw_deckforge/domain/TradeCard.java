package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.TradeRole;

/** Mellemled mellem Trade og PlayerCard. Angiver hvilke kort der indgår i byttet og på hvilken side (PROPOSER/RECEIVER). */
public class TradeCard {
    private int id;
    private int tradeId;
    private int playerCardId;
    private TradeRole role;

    public TradeCard(int id, int tradeId, int playerCardId, TradeRole role) {
        validate(tradeId, playerCardId, role);
        this.id = id;
        this.tradeId = tradeId;
        this.playerCardId = playerCardId;
        this.role = role;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getTradeId() { return tradeId; }
    public int getPlayerCardId() { return playerCardId; }
    public TradeRole getRole() { return role; }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setTradeId(int tradeId) { this.tradeId = tradeId; }
    public void setPlayerCardId(int playerCardId) { this.playerCardId = playerCardId; }
    public void setRole(TradeRole role) { this.role = role; }

    // --- Validering ---

    private void validate(int tradeId, int playerCardId, TradeRole role) {
        if (tradeId <= 0) throw new IllegalArgumentException("Ugyldigt trade-id");
        if (playerCardId <= 0) throw new IllegalArgumentException("Ugyldigt spillerkort-id");
        if (role == null) throw new IllegalArgumentException("Rolle må ikke være null");
    }
}

