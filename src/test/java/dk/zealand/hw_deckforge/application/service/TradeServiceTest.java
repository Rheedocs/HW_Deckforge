package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.infrastructure.PlayerCardRepository;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private ITradeRepository tradeRepository;

    @Mock
    private IPlayerCardRepository playerCardRepository;

    @InjectMocks
    private TradeService tradeService;

    private Trade testTrade;

    @BeforeEach
    void setup() {
        testTrade = new Trade(1,20,30, TradeStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusHours(24));
    }

    @Test
    void accept_shouldSetStatusToAccepted() {
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        tradeService.accept(1);

        assertEquals(TradeStatus.ACCEPTED, testTrade.getStatus());
        verify(tradeRepository).update(testTrade);
    }
    @Test
    void cancel_shouldUpdateStatus() {
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        tradeService.cancel(1);

        assertEquals(TradeStatus.CANCELLED, testTrade.getStatus());
        verify(tradeRepository).update(testTrade);
    }
    @Test // Tjekker om ugyldig trade ikke bliver gemt i databasen hvor proposeren ikke har et valid card
    void propose_shouldFailIfProposerCardNotTradeable() {
        when(playerCardRepository.findForTradeByPlayerId(20))
                .thenReturn(List.of()); // empty

        assertThrows(IllegalArgumentException.class,
                () -> tradeService.propose(testTrade, List.of(10), List.of(20)));

        verify(tradeRepository, never()).save(any());
    }
    @Test
    void getByPlayerId_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.getByPlayerId(0));
    }

    @Test
    void getByPlayerId_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.getByPlayerId(-1));
    }


}