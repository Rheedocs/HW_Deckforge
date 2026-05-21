package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;

import java.time.LocalDateTime;

public class Trade {
    private int id;
    private int proposerId;
    private int receiverId;
    private TradeStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private boolean proposerConfirmed;
    private boolean receiverConfirmed;

    public Trade(int id, int proposerId, int receiverId, TradeStatus status,
                 LocalDateTime createdAt, LocalDateTime expiresAt, boolean proposerConfirmed, boolean receiverConfirmed) {
        validate(proposerId, receiverId, status, createdAt, expiresAt);
        this.id = id;
        this.proposerId = proposerId;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.proposerConfirmed = proposerConfirmed;
        this.receiverConfirmed = receiverConfirmed;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getProposerId() { return proposerId; }
    public int getReceiverId() { return receiverId; }
    public TradeStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isProposerConfirmed() { return proposerConfirmed; }
    public boolean isReceiverConfirmed() { return receiverConfirmed; }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setProposerId(int proposerId) { this.proposerId = proposerId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    public void setStatus(TradeStatus status) {
        if (status == null) throw new IllegalArgumentException("Status må ikke være null");
        this.status = status;
    }

    // --- Adfærd ---

    public boolean isExpired() { return expiresAt.isBefore(LocalDateTime.now()); }
    public void accept() { this.status = TradeStatus.ACCEPTED; }
    public void decline() { this.status = TradeStatus.DECLINED; }
    public void cancel() { this.status = TradeStatus.CANCELLED; }
    public void complete() { this.status = TradeStatus.COMPLETED; }
    public void confirmProposer() { this.proposerConfirmed = true; }
    public void confirmReceiver() { this.receiverConfirmed = true; }
    public boolean isFullyConfirmed() { return proposerConfirmed && receiverConfirmed; }

    // --- Validering ---

    private void validate(int proposerId, int receiverId, TradeStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        if (proposerId <= 0) throw new IllegalArgumentException("Ugyldigt proposer-id");
        if (receiverId <= 0) throw new IllegalArgumentException("Ugyldigt receiver-id");
        if (proposerId == receiverId) throw new ValidationException("Du kan ikke bytte med dig selv");
        if (status == null) throw new IllegalArgumentException("Status må ikke være null");
        if (createdAt == null) throw new IllegalArgumentException("Oprettelsesdato må ikke være null");
        if (expiresAt == null) throw new IllegalArgumentException("Udløbsdato må ikke være null");
        if (!expiresAt.isAfter(createdAt)) throw new IllegalArgumentException("Udløbsdato skal være efter oprettelsesdato");
    }

    @Override
    public String toString() {
        return "Trade{id=" + id + ", proposerId=" + proposerId + ", receiverId=" + receiverId
                + ", status=" + status + ", expiresAt=" + expiresAt + "}";
    }
}

