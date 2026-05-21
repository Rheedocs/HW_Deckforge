package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import dk.zealand.hw_deckforge.domain.enums.EventStatus;
import dk.zealand.hw_deckforge.domain.enums.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private IEventRepository eventRepository;

    private EventService eventService;
    private Event validEvent;

    @BeforeEach
    void setup() {
        eventService = new EventService(eventRepository);

        validEvent = new Event(
                1,
                "Commander Night",
                "Næstved",
                LocalDate.now().plusDays(7),
                16,
                EventStatus.UPCOMING,
                Format.COMMANDER
        );
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
    void getById_notFound_throwsNoSuchElement() {
        when(eventRepository.findById(1)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> eventService.getById(1));
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

        eventService.registerPlayer(20, 1, 5);

        verify(eventRepository).registerPlayer(20, 1, 5);
    }
}