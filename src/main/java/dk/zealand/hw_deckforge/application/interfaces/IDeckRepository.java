package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Deck;
import java.util.List;

/** Kontrakt for deck-data inkl. synlighedsstyring. */
public interface IDeckRepository {
    Deck findById(int id);
    List <Deck> findByPlayerId(int playerId);
    void save(Deck deck);
    void update (Deck deck);
    void delete(int id);
}
