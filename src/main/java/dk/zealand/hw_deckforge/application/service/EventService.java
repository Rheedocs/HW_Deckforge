package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.EventRegistration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {

    private final IEventRepository eventRepository;

    public EventService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // --- Forespørgsler ---

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul!");
        Event event = eventRepository.findById(id);
        if (event == null) throw new NoSuchElementException("Ingen event fundet med id: " + id);
        return event;
    }

    public List<Event> getUpcoming() {
        return eventRepository.findUpcoming();
    }

    public List<EventRegistration> getRegistrationsByEventId(int eventId) {
        if (eventId <= 0) throw new IllegalArgumentException("EventId skal være større end nul");
        return eventRepository.findRegistrationsByEventId(eventId);
    }

    // --- Livscyklus ---

    public void create(Event event) {
        if (event == null) throw new IllegalArgumentException("Event må ikke være null!");

        List<String> errors = event.validate();
        if (!errors.isEmpty()) throw new IllegalArgumentException(String.join(", ", errors));

        eventRepository.save(event);
    }

    public void update(Event event) {
        if (event == null) throw new IllegalArgumentException("Event må ikke være null!");
        if (eventRepository.findById(event.getId()) == null)
            throw new NoSuchElementException("Ingen event fundet med id: " + event.getId());

        List<String> errors = event.validate();
        if (!errors.isEmpty()) throw new IllegalArgumentException(String.join(", ", errors));

        eventRepository.update(event);
    }

    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul!");
        if (eventRepository.findById(id) == null)
            throw new NoSuchElementException("Ingen event fundet med id: " + id);
        eventRepository.delete(id);
    }

    public void registerPlayer(int playerId, int eventId, int deckId) {
        if (playerId <= 0 || eventId <= 0 || deckId <= 0)
            throw new IllegalArgumentException("playerId, eventId og deckId skal alle være større end nul!");
        Event event = eventRepository.findById(eventId);
        if (event == null) throw new NoSuchElementException("Ingen event fundet med id: " + eventId);
        if (!event.isUpcoming()) throw new IllegalArgumentException("Du kan kun tilmelde dig kommende events");
        if (eventRepository.existsRegistration(playerId, eventId)) throw new IllegalArgumentException("Du er allerede tilmeldt dette event");
        if (eventRepository.countRegistrations(eventId) >= event.getMaxPlayers()) throw new IllegalArgumentException("Eventet er fuldt");
        eventRepository.registerPlayer(playerId, eventId, deckId);
    }

    public int countRegistrations(int eventId) {
        if (eventId <= 0) throw new IllegalArgumentException("EventId skal være større end nul!");
        return eventRepository.countRegistrations(eventId);
    }
}