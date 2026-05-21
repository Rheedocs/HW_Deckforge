package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(TradeRepository.class)
class TradeRepositoryTest {

    @Autowired
    private TradeRepository repository;

    @Test
    void findById_nonExistingId_returnsNull() {
        Trade trade = repository.findById(999);
        assertNull(trade);
    }

    @Test
    void findById_existingId_returnsTrade() {
        Trade trade = repository.findById(1);
        assertNotNull(trade);
        assertEquals(1, trade.getId());
    }

    @Test
    void findIncomingByPlayerId_existingReceiver_returnsOnlyReceiverTrades() {
        List<Trade> trades = repository.findIncomingByPlayerId(3);
        assertFalse(trades.isEmpty());
        for (Trade trade : trades) {
            assertEquals(3, trade.getReceiverId());
            assertEquals(TradeStatus.PENDING, trade.getStatus());
        }
    }

    @Test
    void findByPlayerId_nonExistingPlayer_returnsEmptyList() {
        List<Trade> trades = repository.findByPlayerId(90);
        assertTrue(trades.isEmpty());
    }
}