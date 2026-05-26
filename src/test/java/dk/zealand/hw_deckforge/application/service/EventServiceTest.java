package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.EventStatus;
import dk.zealand.hw_deckforge.domain.enums.Format;
import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;
import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock private IEventRepository eventRepository;
    @Mock private DeckService deckService;

    private EventService eventService;
    private Event validEvent;
    private Deck validDeck;

    @BeforeEach
    void setup() {
        eventService = new EventService(eventRepository, deckService);
        validEvent = new Event(1, "Commander Night", "Næstved", LocalDate.now().plusDays(7), 16, EventStatus.UPCOMING, Format.COMMANDER);
        validDeck = new Deck(5, 20, "Commander Deck", Format.COMMANDER, DeckVisibility.PUBLIC);
    }

    // --- Forespørgsler ---

    @Test
    void getAll_returnsList() {
        when(eventRepository.findAll()).thenReturn(List.of(validEvent));

        List<Event> result = eventService.getAll();

        assertEquals(1, result.size());
        assertEquals(validEvent, result.getFirst());
    }

    @Test
    void getById_returnsEvent_whenFound() {
        when(eventRepository.findById(1)).thenReturn(validEvent);

        Event result = eventService.getById(1);

        assertEquals(validEvent, result);
    }

    @Test
    void getById_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> eventService.getById(0));
    }

    @Test
    void getById_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> eventService.getById(-1));
    }

    @Test
    void getById_notFound_throwsNotFoundException() {
        when(eventRepository.findById(1)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventService.getById(1));
    }

    @Test
    void getUpcoming_returnsList() {
        when(eventRepository.findUpcoming()).thenReturn(List.of(validEvent));

        List<Event> result = eventService.getUpcoming();

        assertEquals(1, result.size());
        assertEquals(validEvent, result.getFirst());
    }

    // --- Livscyklus ---

    @Test
    void create_callsSave() {
        eventService.create(validEvent);

        verify(eventRepository).save(validEvent);
    }

    @Test
    void create_nullEvent_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> eventService.create(null));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void update_callsUpdate() {
        when(eventRepository.findById(1)).thenReturn(validEvent);

        eventService.update(validEvent);

        verify(eventRepository).update(validEvent);
    }

    @Test
    void delete_callsDelete() {
        when(eventRepository.findById(1)).thenReturn(validEvent);

        eventService.delete(1);

        verify(eventRepository).delete(1);
    }

    @Test
    void registerPlayer_callsRegisterPlayer() {
        when(eventRepository.findById(1)).thenReturn(validEvent);
        when(deckService.getById(5)).thenReturn(validDeck);
        when(deckService.getTotalCardCount(5)).thenReturn(100);
        when(eventRepository.existsRegistration(20, 1)).thenReturn(false);
        when(eventRepository.countRegistrations(1)).thenReturn(0);

        eventService.registerPlayer(20, 1, 5);

        verify(eventRepository).registerPlayer(20, 1, 5);
    }

    @Test
    void registerPlayer_fullEvent_throwsValidationException() {
        when(eventRepository.findById(1)).thenReturn(validEvent);
        when(deckService.getById(5)).thenReturn(validDeck);
        when(deckService.getTotalCardCount(5)).thenReturn(100);
        when(eventRepository.existsRegistration(20, 1)).thenReturn(false);
        when(eventRepository.countRegistrations(1)).thenReturn(16);

        assertThrows(ValidationException.class, () -> eventService.registerPlayer(20, 1, 5));
        verify(eventRepository, never()).registerPlayer(anyInt(), anyInt(), anyInt());
    }

    @Test
    void updateEventStatuses_callsRepository() {
        eventService.updateEventStatuses();

        verify(eventRepository).updateExpiredEvents();
    }
}