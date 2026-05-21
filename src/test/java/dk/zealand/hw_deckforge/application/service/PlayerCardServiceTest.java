package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerCardServiceTest {

    @Mock
    private IPlayerCardRepository playerCardRepository;

    @InjectMocks
    private PlayerCardService playerCardService;

    private PlayerCard playerCard;

    @BeforeEach
    void setUp() {
        playerCard = new PlayerCard(1, 1, 1, 2, false);
    }

    // --- countByPlayerId ---
    @Test
    void countByPlayerId_validId_returnsCount() {
        when(playerCardRepository.countByPlayerId(1)).thenReturn(3);
        assertEquals(3, playerCardService.countByPlayerId(1));
    }

    @Test
    void countByPlayerId_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.countByPlayerId(0));
    }

    @Test
    void countByPlayerId_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.countByPlayerId(-1));
    }

    // --- getByPlayerId ---
    @Test
    void getByPlayerId_validId_returnsList() {
        when(playerCardRepository.findByPlayerId(1)).thenReturn(List.of(playerCard));
        List<PlayerCard> result = playerCardService.getByPlayerId(1);
        assertEquals(1, result.size());
    }

    @Test
    void getByPlayerId_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.getByPlayerId(0));
    }

    @Test
    void getByPlayerId_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.getByPlayerId(-1));
    }

    // --- getForTradeByPlayerId ---
    @Test
    void getForTradeByPlayerId_validId_returnsList() {
        PlayerCard forTrade = new PlayerCard(2, 1, 2, 1, true);
        when(playerCardRepository.findForTradeByPlayerId(1)).thenReturn(List.of(forTrade));
        List<PlayerCard> result = playerCardService.getForTradeByPlayerId(1);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isForTrade());
    }

    @Test
    void getForTradeByPlayerId_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.getForTradeByPlayerId(0));
    }

    // --- save ---
    @Test
    void save_validPlayerCard_callsSave() {
        playerCardService.save(playerCard);
        verify(playerCardRepository).save(playerCard);
    }

    @Test
    void save_nullPlayerCard_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.save(null));
    }

    // --- delete ---
    @Test
    void delete_validId_callsDelete() {
        playerCardService.delete(1);
        verify(playerCardRepository).delete(1);
    }

    @Test
    void delete_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.delete(0));
    }

    @Test
    void delete_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.delete(-1));
    }

    // --- setForTrade ---
    @Test
    void setForTrade_true_marksForTrade() {
        PlayerCard pc = new PlayerCard(1, 1, 1, 1, false);
        when(playerCardRepository.findById(1)).thenReturn(pc);
        playerCardService.setForTrade(1, true);
        assertTrue(pc.isForTrade());
        verify(playerCardRepository).update(pc);
    }

    @Test
    void setForTrade_false_unmarksForTrade() {
        PlayerCard pc = new PlayerCard(1, 1, 1, 1, true);
        when(playerCardRepository.findById(1)).thenReturn(pc);
        playerCardService.setForTrade(1, false);
        assertFalse(pc.isForTrade());
        verify(playerCardRepository).update(pc);
    }

    @Test
    void setForTrade_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerCardService.setForTrade(0, true));
    }
}
