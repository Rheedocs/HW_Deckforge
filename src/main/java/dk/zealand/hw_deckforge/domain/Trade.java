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

    // --- Statushandlinger ---

    public boolean isExpired() { return expiresAt.isBefore(LocalDateTime.now()); }
    public void accept() { this.status = TradeStatus.ACCEPTED; }
    public void decline() { this.status = TradeStatus.DECLINED; }
    public void cancel() { this.status = TradeStatus.CANCELLED; }
    public void setStatus(TradeStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Trade{id=" + id + ", proposerId=" + proposerId + ", receiverId=" + receiverId
                + ", status=" + status + ", expiresAt=" + expiresAt + "}";
    }
}