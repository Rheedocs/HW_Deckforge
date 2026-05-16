package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.DeckCard;
import java.util.List;

public interface IDeckCardRepository {
    List<DeckCard> findByDeckId(int deckId);
    DeckCard findById(int id);
    DeckCard findByDeckIdAndCardId(int deckId, int cardId);
    void save(DeckCard deckCard);
    void update(DeckCard deckCard);
    void delete(int id);
}
