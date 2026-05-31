package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Håndterer spillerens kortsamling og synlighedsstyring. */
@Service
public class PlayerCardService {

    private final IPlayerCardRepository playerCardRepository;

    public PlayerCardService(IPlayerCardRepository playerCardRepository) {
        this.playerCardRepository = playerCardRepository;
    }

    // --- Forespørgsler ---

    public int countByPlayerId(int playerId) {
        validateId(playerId, "Spiller-id");
        return playerCardRepository.countByPlayerId(playerId);
    }

    public List<PlayerCard> getByPlayerId(int playerId) {
        validateId(playerId, "Spiller-id");
        return playerCardRepository.findByPlayerId(playerId);
    }

    /** @return kun kort markeret til bytning. Bruges i bytteforslags-formularen. */
    public List<PlayerCard> getForTradeByPlayerId(int playerId) {
        validateId(playerId, "Spiller-id");
        return playerCardRepository.findForTradeByPlayerId(playerId);
    }

    /** @return map fra player_card-id til PlayerCard for O(1) opslag i templates. */
    public Map<Integer, PlayerCard> getPlayerCardMap(int playerId) {
        validateId(playerId, "Spiller-id");
        Map<Integer, PlayerCard> map = new HashMap<>();
        for (PlayerCard pc : playerCardRepository.findByPlayerId(playerId)) {
            map.put(pc.getCardId(), pc);
        }
        return map;
    }

    public Map<Integer, PlayerCard> getPlayerCardMapByIds(List<Integer> playerCardIds) {
        Map<Integer, PlayerCard> map = new HashMap<>();
        for (int id : playerCardIds) {
            PlayerCard pc = playerCardRepository.findById(id);
            if (pc != null) map.put(id, pc);
        }
        return map;
    }

    // --- Synlighed ---

    /** Returnerer kort baseret på synlighedsniveau: PRIVATE, TRADE_ONLY eller PUBLIC. */
    public List<PlayerCard> getVisibleCards(int playerId, CollectionVisibility visibility, boolean isSelf, boolean isAdmin) {
        validateId(playerId, "Spiller-id");
        List<PlayerCard> all = playerCardRepository.findByPlayerId(playerId);
        if (canSeeFullCollection(visibility, isSelf, isAdmin)) return all;
        if (visibility == CollectionVisibility.TRADE_ONLY) return getTradeOnlyCards(all);
        return new ArrayList<>();
    }

    public int getVisibleCount(int playerId, CollectionVisibility visibility, boolean isSelf, boolean isAdmin) {
        validateId(playerId, "Spiller-id");
        if (canSeeFullCollection(visibility, isSelf, isAdmin)) return playerCardRepository.countByPlayerId(playerId);
        if (visibility == CollectionVisibility.TRADE_ONLY) return playerCardRepository.countForTradeByPlayerId(playerId);
        return 0;
    }

    // --- Samling ---

    public void addCard(int playerId, int cardId, int quantity) {
        validateId(playerId, "Spiller-id");
        validateId(cardId, "Kort-id");
        validateQuantity(quantity);
        PlayerCard existing = playerCardRepository.findByPlayerIdAndCardId(playerId, cardId);
        saveOrUpdatePlayerCard(playerId, cardId, quantity, existing);
    }

    public void removeCard(int playerCardId, int quantity) {
        validateId(playerCardId, "Spillerkort-id");
        validateQuantity(quantity);
        PlayerCard playerCard = getExistingPlayerCard(playerCardId);
        removeOrReducePlayerCard(playerCard, quantity);
    }

    public void save(PlayerCard playerCard) {
        validatePlayerCard(playerCard);
        playerCardRepository.save(playerCard);
    }

    public void delete(int id) {
        validateId(id, "Id");
        playerCardRepository.delete(id);
    }

    /** Markerer eller afmarkerer et kort som tilgængeligt for bytning. Bruges af bytteflowet til reservation. */
    public void setForTrade(int id, boolean forTrade) {
        validateId(id, "Id");
        PlayerCard playerCard = getExistingPlayerCard(id);
        if (forTrade) playerCard.markForTrade();
        else playerCard.unmarkForTrade();
        playerCardRepository.update(playerCard);
    }

    // --- Intern behandling ---

    private boolean canSeeFullCollection(CollectionVisibility visibility, boolean isSelf, boolean isAdmin) {
        return isSelf || isAdmin || visibility == CollectionVisibility.PUBLIC;
    }

    private List<PlayerCard> getTradeOnlyCards(List<PlayerCard> playerCards) {
        List<PlayerCard> filtered = new ArrayList<>();
        for (PlayerCard pc : playerCards) {
            if (pc.isForTrade()) filtered.add(pc);
        }
        return filtered;
    }

    private PlayerCard getExistingPlayerCard(int id) {
        PlayerCard playerCard = playerCardRepository.findById(id);
        if (playerCard == null) throw new IllegalArgumentException("Kortet findes ikke i samlingen");
        return playerCard;
    }

    private void saveOrUpdatePlayerCard(int playerId, int cardId, int quantity, PlayerCard existing) {
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            playerCardRepository.update(existing);
        } else {
            playerCardRepository.save(new PlayerCard(0, playerId, cardId, quantity, false));
        }
    }

    private void removeOrReducePlayerCard(PlayerCard playerCard, int quantity) {
        if (quantity >= playerCard.getQuantity()) {
            playerCardRepository.delete(playerCard.getId());
        } else {
            playerCard.setQuantity(playerCard.getQuantity() - quantity);
            playerCardRepository.update(playerCard);
        }
    }

    // --- Validering ---

    private void validatePlayerCard(PlayerCard playerCard) {
        if (playerCard == null) throw new IllegalArgumentException("SpillerKort må ikke være null");
        validateId(playerCard.getPlayerId(), "Spiller-id");
        validateId(playerCard.getCardId(), "Kort-id");
        validateQuantity(playerCard.getQuantity());
    }

    private void validateId(int id, String fieldName) {
        if (id <= 0) throw new IllegalArgumentException("Ugyldigt " + fieldName.toLowerCase());
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
    }
}


