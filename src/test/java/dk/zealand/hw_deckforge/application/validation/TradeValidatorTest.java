package dk.zealand.hw_deckforge.application.validation;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeValidatorTest {

    @Mock
    private IPlayerCardRepository playerCardRepository;

    @Test
    void validateProposerCard_cardNotMarkedForTrade_throwsIllegalArgument() {
        when(playerCardRepository.findForTradeByPlayerId(20))
                .thenReturn(List.of());

        TradeValidator validator = new TradeValidator(playerCardRepository);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateProposerCard(20, 10));
    }

    @Test
    void validateProposerCard_cardMarkedForTrade_doesNotThrow() {
        PlayerCard card = mock(PlayerCard.class);
        when(card.getId()).thenReturn(10);

        when(playerCardRepository.findForTradeByPlayerId(20))
                .thenReturn(List.of(card));

        TradeValidator validator = new TradeValidator(playerCardRepository);

        assertDoesNotThrow(() -> validator.validateProposerCard(20, 10));
    }
}