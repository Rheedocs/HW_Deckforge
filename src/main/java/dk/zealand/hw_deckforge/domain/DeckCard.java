package dk.zealand.hw_deckforge.domain;

public class DeckCard {
    private final int id;
    private final int deckId;
    private final int cardId;
    private int quantity;

    public DeckCard(int id, int deckId, int cardId, int quantity) {
        validate(deckId, cardId, quantity);
        this.id = id;
        this.deckId = deckId;
        this.cardId = cardId;
        this.quantity = quantity;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getDeckId() { return deckId; }
    public int getCardId() { return cardId; }
    public int getQuantity() { return quantity; }

    // --- Adfærd ---

    public void setQuantity(int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        this.quantity = quantity;
    }

    // --- Validering ---

    private void validate(int deckId, int cardId, int quantity) {
        if (deckId <= 0) throw new IllegalArgumentException("Ugyldigt deck-id");
        if (cardId <= 0) throw new IllegalArgumentException("Ugyldigt kort-id");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
    }
}

