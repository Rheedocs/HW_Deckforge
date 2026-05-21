package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.application.validation.TradeValidator;
import dk.zealand.hw_deckforge.domain.Trade;
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

    @Mock
    private ITradeCardRepository tradeCardRepository;

    @Mock
    private IPlayerRepository playerRepository;

    @Mock
    private TradeValidator tradeValidator;

    @InjectMocks
    private TradeService tradeService;

    private Trade testTrade;

    @BeforeEach
    void setup() {
        testTrade = new Trade(1, 20, 30, TradeStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusHours(24), false, false);
    }

    // --- Forespørgsler ---

    @Test
    void getAllByPlayerId_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.getAllByPlayerId(0));
    }

    @Test
    void getAllByPlayerId_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.getAllByPlayerId(-1));
    }

    // --- Livscyklus ---

    @Test
    void accept_pendingTrade_setsStatusAccepted() {
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        tradeService.accept(1);

        assertEquals(TradeStatus.ACCEPTED, testTrade.getStatus());
        verify(tradeRepository).update(testTrade);
    }

    @Test
    void cancel_pendingTrade_setsStatusCancelled() {
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        tradeService.cancel(1);

        assertEquals(TradeStatus.CANCELLED, testTrade.getStatus());
        verify(tradeRepository).update(testTrade);
    }

    @Test
    void decline_pendingTrade_setsStatusDeclined() {
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        tradeService.decline(1);

        assertEquals(TradeStatus.DECLINED, testTrade.getStatus());
        verify(tradeRepository).update(testTrade);
    }

    @Test
    void accept_alreadyAcceptedTrade_throwsIllegalArgument() {
        testTrade.accept();
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        assertThrows(IllegalArgumentException.class, () -> tradeService.accept(1));
        verify(tradeRepository, never()).update(any());
    }

    @Test
    void cancel_alreadyCancelledTrade_throwsIllegalArgument() {
        testTrade.cancel();
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        assertThrows(IllegalArgumentException.class, () -> tradeService.cancel(1));
        verify(tradeRepository, never()).update(any());
    }

    @Test
    void complete_notAcceptedTrade_throwsIllegalArgument() {
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        assertThrows(IllegalArgumentException.class, () -> tradeService.complete(1, 20));
        verify(tradeRepository, never()).update(any());
    }

    @Test
    void complete_notPartOfTrade_throwsAccessDenied() {
        testTrade.accept();
        when(tradeRepository.findById(1)).thenReturn(testTrade);

        assertThrows(dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException.class,
                () -> tradeService.complete(1, 99));
        verify(tradeRepository, never()).update(any());
    }

    // --- Validering ---

    @Test
    void propose_proposerCardNotTradeable_throwsIllegalArgument() {
        doThrow(new IllegalArgumentException("Proposerens kort er ikke markeret til bytte"))
                .when(tradeValidator)
                .validateProposerCard(20, 10);

        assertThrows(IllegalArgumentException.class,
                () -> tradeService.propose(20, 30, List.of(10), List.of(20)));

        verify(tradeRepository, never()).save(any());
        verify(tradeCardRepository, never()).save(any());
    }

    // --- Scheduler ---

    @Test
    void expireOldTrades_kalderRepository() {
        tradeService.expireOldTrades();

        verify(tradeRepository).expireOldTrades();
    }
}