package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.ITradeRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.Trade;
import dk.zealand.hw_deckforge.domain.TradeCard;
import dk.zealand.hw_deckforge.domain.enums.TradeRole;
import dk.zealand.hw_deckforge.domain.enums.TradeStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    private final ITradeRepository tradeRepository;
    private final IPlayerCardRepository playerCardRepository;
    private final ITradeCardRepository tradeCardRepository;

    public TradeService(ITradeRepository tradeRepository, IPlayerCardRepository playerCardRepository, ITradeCardRepository tradeCardRepository) {
        this.tradeRepository = tradeRepository;
        this.playerCardRepository = playerCardRepository;
        this.tradeCardRepository = tradeCardRepository;
    }

    public Trade getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end 0");
        return tradeRepository.findById(id);
    }

    public List<Trade> getByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Player id  skal være større end 0");
        return tradeRepository.findByPlayerId(playerId);
    }

    public List<Trade> getIncomingByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Player id  skal være større end 0");
        return tradeRepository.findIncomingByPlayerId(playerId);
    }

    public void accept(int tradeId) {
        Trade trade = getById(tradeId);
        if (trade.getStatus() != TradeStatus.PENDING) {
            throw new IllegalArgumentException("Kun trades som er pending kan accepteres");
        }
        trade.accept();
        tradeRepository.update(trade);
    }

    public void decline(int tradeId) {
        Trade trade = getById(tradeId);
        if (trade.getStatus() != TradeStatus.PENDING) {
            throw new IllegalArgumentException("Kun trades som er pending kan afvises");
        }
        trade.decline();
        tradeRepository.update(trade);
    }

    public void cancel(int tradeId) {
        Trade trade = getById(tradeId);
        if (trade.getStatus() != TradeStatus.PENDING) {
            throw new IllegalArgumentException("Kun trades som er pending kan annuleres");
        }
        trade.cancel();
        tradeRepository.update(trade);
    }

    @Scheduled(fixedRate = 86400000)
    public void expireOldTrades() {
        tradeRepository.expireOldTrades();
    }

    public void propose(Trade trade, List<Integer> proposerCardIds, List<Integer> receiverCardIds) {
        if (proposerCardIds == null || proposerCardIds.size() != 1) {
            throw new IllegalArgumentException("Et trade skal indholde et kort fra begge parter");
        }
        if (receiverCardIds == null || receiverCardIds.size() != 1) {
            throw new IllegalArgumentException("Et trade skal indholde et kort fra begge parter");
        }

        int proposerCardId = proposerCardIds.getFirst();
        int receiverCardId = receiverCardIds.getFirst();

        boolean proposerCardValid = false;
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(trade.getProposerId())) {
            if (pc.getId() == proposerCardId) {
                proposerCardValid = true;
                break;
            }
        }
        if (!proposerCardValid) {
            throw new IllegalArgumentException("Proposerens kort er ikke markeret til bytte");
        }

        // Validerer at receivers kort er markeret/klar til at bytte
        boolean receiverCardValid = false;
        for (PlayerCard pc : playerCardRepository.findForTradeByPlayerId(trade.getReceiverId())) {
            if (pc.getId() == receiverCardId) {
                receiverCardValid = true;
                break;
            }
        }
        if (!receiverCardValid) {
            throw new IllegalArgumentException("Modtagerens kort er ikke markeret til bytte");
        }

        // Save() sætter det genererede id på trade
        tradeRepository.save(trade);

        tradeCardRepository.save(new TradeCard(0, trade.getId(), proposerCardId, TradeRole.PROPOSER));
        tradeCardRepository.save(new TradeCard(0, trade.getId(), receiverCardId, TradeRole.RECEIVER));
    }
}