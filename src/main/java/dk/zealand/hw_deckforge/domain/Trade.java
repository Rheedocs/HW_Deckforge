package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.TradeStatus;

import java.time.LocalDateTime;

public class Trade {
    private int id;
    private int proposerId;
    private int receiverId;
    private TradeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Trade(int id, int proposerId, int receiverId, TradeStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        validate(proposerId, receiverId, status, createdAt, expiresAt);
        this.id = id;
        this.proposerId = proposerId;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getProposerId() { return proposerId; }
    public int getReceiverId() { return receiverId; }
    public TradeStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setProposerId(int proposerId) { this.proposerId = proposerId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    public void setStatus(TradeStatus status) { this.status = status; }

    // --- Adfærd ---

    public boolean isExpired() { return expiresAt.isBefore(LocalDateTime.now()); }
    public void accept() { this.status = TradeStatus.ACCEPTED; }
    public void decline() { this.status = TradeStatus.DECLINED; }
    public void cancel() { this.status = TradeStatus.CANCELLED; }

    // --- Validering ---

    private void validate(int proposerId, int receiverId, TradeStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        if (proposerId <= 0) throw new IllegalArgumentException("Ugyldigt proposer-id");
        if (receiverId <= 0) throw new IllegalArgumentException("Ugyldigt receiver-id");
        if (status == null) throw new IllegalArgumentException("Status må ikke være null");
        if (createdAt == null) throw new IllegalArgumentException("Oprettelsesdato må ikke være null");
        if (expiresAt == null) throw new IllegalArgumentException("Udløbsdato må ikke være null");
    }

    // --- Hjælpemetoder ---

    @Override
    public String toString() {
        return "Trade{id=" + id + ", proposerId=" + proposerId + ", receiverId=" + receiverId
                + ", status=" + status + ", expiresAt=" + expiresAt + "}";
    }
}