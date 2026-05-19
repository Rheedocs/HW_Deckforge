package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.PlayerCard;
import java.util.List;

public interface IPlayerCardRepository {
    List<PlayerCard> findByPlayerId(int playerId);
    PlayerCard findById(int id);
    PlayerCard findByPlayerIdAndCardId(int playerId, int cardId);
    List<PlayerCard> findForTradeByPlayerId(int playerId);
    void save(PlayerCard playerCard);
    void update(PlayerCard playerCard);
    void delete(int id);
    int countByPlayerId(int playerId);
    int countForTradeByPlayerId(int playerId);

}
