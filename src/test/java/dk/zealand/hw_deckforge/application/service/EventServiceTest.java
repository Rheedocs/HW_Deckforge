package dk.zealand.hw_deckforge.application.service;
import dk.zealand.hw_deckforge.application.interfaces.IEventRepository;
import dk.zealand.hw_deckforge.domain.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setName("Test Event");
    }

    // --- getAll ---

    @Test
    void getAll_returnsList() {
        when(eventRepository.findAll()).thenReturn(List.of(testEvent));
        List<Event> result = eventService.getAll();
        assertEquals(1, result.size());
        verify(eventRepository).findAll();
    }

    @Test
    void getAll_returnsEmptyList_whenNoEvents() {
        when(eventRepository.findAll()).thenReturn(List.of());
        List<Event> result = eventService.getAll();
        assertTrue(result.isEmpty());
    }

    // --- getById ---

    @Test
    void getById_throwsIllegalArgument_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> eventService.getById(0));
    }

    @Test
    void getById_throwsIllegalArgument_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> eventService.getById(-5));
    }

    @Test
    void getById_returnsEvent_whenFound() {
        when(eventRepository.findById(1)).thenReturn(testEvent);
        Event result = eventService.getById(1);
        assertEquals(testEvent, result);
    }

    @Test
    void getById_throwsNoSuchElement_whenNotFound() {
        when(eventRepository.findById(99)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> eventService.getById(99));
    }

    // --- getUpcoming ---

    @Test
    void getUpcoming_returnsList() {
        when(eventRepository.findUpcoming()).thenReturn(List.of(testEvent));
        List<Event> result = eventService.getUpcoming();
        assertEquals(1, result.size());
        verify(eventRepository).findUpcoming();
    }

    // --- create ---

    @Test
    void create_callsSave() {
        eventService.create(testEvent);
        verify(eventRepository).save(testEvent);
    }

    // --- update ---

    @Test
    void update_callsUpdate() {
        eventService.update(testEvent);
        verify(eventRepository).update(testEvent);
    }

    // --- delete ---

    @Test
    void delete_callsDelete() {
        eventService.delete(1);
        verify(eventRepository).delete(1);
    }

    // --- registerPlayer ---

    @Test
    void registerPlayer_callsRegisterPlayer() {
        eventService.registerPlayer(10, 1, 5);
        verify(eventRepository).registerPlayer(10, 1, 5);
    }
}