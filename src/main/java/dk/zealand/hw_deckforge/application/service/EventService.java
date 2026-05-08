package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final IEventRepository eventRepository;

    public EventService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAll() { return eventRepository.findAll(); }
    public Event getById(int id) { return eventRepository.findById(id); }
    public List<Event> getUpcoming() { return eventRepository.findUpcoming(); }
    public void create(Event event) { eventRepository.save(event); }
    public void update(Event event) { eventRepository.update(event); }
    public void delete(int id) { eventRepository.delete(id); }
    public void registerPlayer(int playerId, int eventId, int deckId) { eventRepository.registerPlayer(playerId, eventId, deckId); }
}