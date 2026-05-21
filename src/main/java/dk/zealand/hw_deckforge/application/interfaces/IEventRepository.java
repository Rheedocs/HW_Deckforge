package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.EventRegistration;
import java.util.List;

public interface IEventRepository {
    List<Event> findAll();
    Event findById(int id);
    List<Event> findUpcoming();
    List<EventRegistration> findRegistrationsByEventId(int eventId);
    void save(Event event);
    void update(Event event);
    void delete(int id);
    void registerPlayer(int playerId, int eventId, int deckId);
    boolean existsRegistration(int playerId, int eventId);
    int countRegistrations(int eventId);
    int countRegistrationsByPlayerId(int playerId);
}