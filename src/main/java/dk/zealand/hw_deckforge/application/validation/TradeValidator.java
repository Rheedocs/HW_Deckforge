package dk.zealand.hw_deckforge.application.validation;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import org.springframework.stereotype.Component;

@Component
public class TradeValidator {

    private final IPlayerCardRepository playerCardRepository;

    public TradeValidator(IPlayerCardRepository playerCardRepository) {
        this.playerCardRepository = playerCardRepository;
    }

    // --- Validering ---

    public void validateProposerCard(int proposerId, int proposerCardId) {
        validateTradeCard(proposerId, proposerCardId, "Proposerens kort er ikke markeret til bytte");
    }

    public void validateReceiverCard(int receiverId, int receiverCardId) {
        validateTradeCard(receiverId, receiverCardId, "Modtagerens kort er ikke markeret til bytte");
    }

    private void validateTradeCard(int playerId, int playerCardId, String errorMessage) {
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(playerId)) {
            if (pc.getId() == playerCardId) return;
        }
        throw new IllegalArgumentException(errorMessage);
    }
}


