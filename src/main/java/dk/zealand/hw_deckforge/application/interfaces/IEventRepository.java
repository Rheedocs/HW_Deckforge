package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Event;
import java.util.List;

public interface IEventRepository {
    List<Event> findAll();
    Event findById(int id);
    List<Event> findUpcoming();
    void save(Event event);
    void update(Event event);
    void delete(int id);
    void registerPlayer(int playerId, int eventId, int deckId);
    int countRegistrations(int eventId);
}
