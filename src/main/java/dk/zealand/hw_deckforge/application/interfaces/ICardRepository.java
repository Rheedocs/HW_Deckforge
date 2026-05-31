package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Card;
import java.util.List;

/** Kontrakt for kortdata. Implementeres af CardRepository i infrastructure-laget. */
public interface ICardRepository {
    List<Card> findAll();
    Card findById(int id);
    void save(Card card);
    void update(Card card);
    void delete(int id);
}
