package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerCardService {

    private final IPlayerCardRepository playerCardRepository;

    public PlayerCardService(IPlayerCardRepository playerCardRepository) {
        this.playerCardRepository = playerCardRepository;
    }

    public int countByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        return playerCardRepository.countByPlayerId(playerId);
    }

    public List<PlayerCard> getByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        return playerCardRepository.findByPlayerId(playerId);
    }

    public List<PlayerCard> getForTradeByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        return playerCardRepository.findForTradeByPlayerId(playerId);
    }

    public void save(PlayerCard playerCard) {
        if (playerCard == null) throw new IllegalArgumentException("SpillerKort må ikke være null");
        playerCardRepository.save(playerCard);
    }

    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Ugyldigt id");
        playerCardRepository.delete(id);
    }

    public void setForTrade(int id, boolean forTrade) {
        if (id <= 0) throw new IllegalArgumentException("Ugyldigt id");
        playerCardRepository.setForTrade(id, forTrade);
    }

    public int getVisibleCount(int playerId, CollectionVisibility visibility, boolean isSelf, boolean isAdmin) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (isSelf || isAdmin || visibility == CollectionVisibility.PUBLIC) {
            return playerCardRepository.countByPlayerId(playerId);
        } else if (visibility == CollectionVisibility.TRADE_ONLY) {
            return playerCardRepository.countForTradeByPlayerId(playerId);
        } else {
            return 0;
        }
    }
}