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
    int countRegistrations(int eventId);
    boolean existsRegistration(int playerId, int eventId);
}