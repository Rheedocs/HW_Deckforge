package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void addCard(int playerId, int cardId, int quantity) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (cardId <= 0) throw new IllegalArgumentException("Ugyldigt kort-id");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        PlayerCard existing = playerCardRepository.findByPlayerIdAndCardId(playerId, cardId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            playerCardRepository.update(existing);
        } else {
            playerCardRepository.save(new PlayerCard(0, playerId, cardId, quantity, false));
        }
    }

    public void removeCard(int playerCardId, int quantity) {
        if (playerCardId <= 0) throw new IllegalArgumentException("Ugyldigt spillerkort-id");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        PlayerCard playerCard = playerCardRepository.findById(playerCardId);
        if (playerCard == null) throw new IllegalArgumentException("Kortet findes ikke i samlingen");
        if (quantity >= playerCard.getQuantity()) {
            playerCardRepository.delete(playerCardId);
        } else {
            playerCard.setQuantity(playerCard.getQuantity() - quantity);
            playerCardRepository.update(playerCard);
        }
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

    public Set<Integer> getOwnedCardIds(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        Set<Integer> ids = new HashSet<>();
        for (PlayerCard pc : playerCardRepository.findByPlayerId(playerId)) {
            ids.add(pc.getCardId());
        }
        return ids;
    }

    public Map<Integer, PlayerCard> getPlayerCardMap(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        Map<Integer, PlayerCard> map = new HashMap<>();
        for (PlayerCard pc : playerCardRepository.findByPlayerId(playerId)) {
            map.put(pc.getCardId(), pc);
        }
        return map;
    }

    public List<PlayerCard> getVisibleCards(int playerId, CollectionVisibility visibility, boolean isSelf, boolean isAdmin) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        List<PlayerCard> all = playerCardRepository.findByPlayerId(playerId);
        if (isSelf || isAdmin || visibility == CollectionVisibility.PUBLIC) {
            return all;
        }
        if (visibility == CollectionVisibility.TRADE_ONLY) {
            List<PlayerCard> filtered = new ArrayList<>();
            for (PlayerCard pc : all) {
                if (pc.isForTrade()) filtered.add(pc);
            }
            return filtered;
        }
        return new ArrayList<>();
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