package dk.zealand.hw_deckforge.application.validation;

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
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(proposerId)) {
            if (pc.getId() == proposerCardId) return;
        }
        throw new IllegalArgumentException("Proposerens kort er ikke markeret til bytte");
    }

    public void validateReceiverCard(int receiverId, int receiverCardId) {
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(receiverId)) {
            if (pc.getId() == receiverCardId) return;
        }
        throw new IllegalArgumentException("Modtagerens kort er ikke markeret til bytte");
    }
}