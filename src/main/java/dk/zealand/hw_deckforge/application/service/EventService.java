package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.EventRegistration;
import dk.zealand.hw_deckforge.domain.validation.FormatValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final IEventRepository eventRepository;
    private final DeckService deckService;

    public EventService(IEventRepository eventRepository, DeckService deckService) {
        this.eventRepository = eventRepository;
        this.deckService = deckService;
    }

    // --- Forespørgsler ---

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(int id) {
        validateId(id, "Id");
        Event event = eventRepository.findById(id);
        if (event == null) throw new NotFoundException("Ingen event fundet med id: " + id);
        return event;
    }

    public List<Event> getUpcoming() {
        return eventRepository.findUpcoming();
    }

    public List<EventRegistration> getRegistrationsByEventId(int eventId) {
        validateId(eventId, "EventId");
        return eventRepository.findRegistrationsByEventId(eventId);
    }

    public int countRegistrations(int eventId) {
        validateId(eventId, "EventId");
        return eventRepository.countRegistrations(eventId);
    }

    public boolean isFull(int eventId) {
        Event event = getById(eventId);
        return eventRepository.countRegistrations(eventId) >= event.getMaxPlayers();
    }

    public boolean isPlayerRegistered(int playerId, int eventId) {
        validateId(playerId, "PlayerId");
        validateId(eventId, "EventId");
        return eventRepository.existsRegistration(playerId, eventId);
    }

    public int getEventCount(int playerId) {
        validateId(playerId, "PlayerId");
        return eventRepository.countRegistrationsByPlayerId(playerId);
    }

    // --- Livscyklus ---

    public void create(Event event) {
        validateEvent(event);
        eventRepository.save(event);
    }

    public void update(Event event) {
        validateEvent(event);
        if (eventRepository.findById(event.getId()) == null) throw new NotFoundException("Ingen event fundet med id: " + event.getId());
        eventRepository.update(event);
    }

    public void delete(int id) {
        getById(id);
        eventRepository.delete(id);
    }

    public void registerPlayer(int playerId, int eventId, int deckId) {
        validateRegistrationIds(playerId, eventId, deckId);
        Event event = getById(eventId);
        Deck deck = deckService.getById(deckId);
        validateRegistration(playerId, event, deck);
        eventRepository.registerPlayer(playerId, eventId, deckId);
    }

    // --- Validering ---

    private void validateEvent(Event event) {
        if (event == null) throw new IllegalArgumentException("Event må ikke være null");
        event.validateOrThrow();
    }

    private void validateRegistrationIds(int playerId, int eventId, int deckId) {
        validateId(playerId, "PlayerId");
        validateId(eventId, "EventId");
        validateId(deckId, "DeckId");
    }

    private void validateRegistration(int playerId, Event event, Deck deck) {
        if (deck.getPlayerId() != playerId) throw new ValidationException("Du kan kun tilmelde dig med dit eget deck");
        if (!event.isUpcoming()) throw new ValidationException("Du kan kun tilmelde dig kommende events");
        if (deck.getFormat() != event.getFormat()) throw new ValidationException("Deckets format matcher ikke eventets format");
        FormatValidator.validateDeckSize(deck.getFormat(), deckService.getTotalCardCount(deck.getId()));
        if (eventRepository.existsRegistration(playerId, event.getId())) throw new ValidationException("Du er allerede tilmeldt dette event");
        if (eventRepository.countRegistrations(event.getId()) >= event.getMaxPlayers()) throw new ValidationException("Eventet er fuldt");
    }

    private void validateId(int id, String fieldName) {
        if (id <= 0) throw new IllegalArgumentException(fieldName + " skal være større end nul");
    }
}




