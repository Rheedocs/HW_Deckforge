package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    private ICardRepository cardRepository;
    private CardService cardService;
    private Card testCard;

    @BeforeEach
    void setUp() {
        cardRepository = Mockito.mock(ICardRepository.class);
        cardService = new CardService(cardRepository);
        testCard = new Card(1, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON, "Deal 3 damage", "url");
    }

    // --- getAll ---
    @Test
    void getAll_returnsListOfCards() {
        when(cardRepository.findAll()).thenReturn(List.of(testCard));
        List<Card> result = cardService.getAll();
        assertEquals(1, result.size());
    }

    // --- getById ---
    @Test
    void getById_validId_returnsCard() {
        when(cardRepository.findById(1)).thenReturn(testCard);
        assertEquals(testCard, cardService.getById(1));
    }

    @Test
    void getById_invalidId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.getById(0));
    }

    @Test
    void getById_notFound_throwsNoSuchElement() {
        when(cardRepository.findById(99)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> cardService.getById(99));
    }

    // --- create ---
    @Test
    void create_nullCard_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.create(null));
    }

    @Test
    void create_validCard_callsSave() {
        cardService.create(testCard);
        verify(cardRepository, times(1)).save(testCard);
    }

    @Test
    void create_missingName_throwsIllegalArgument() {
        testCard.setName("");
        assertThrows(IllegalArgumentException.class, () -> cardService.create(testCard));
    }

    // --- delete ---
    @Test
    void delete_validId_callsDelete() {
        cardService.delete(1);
        verify(cardRepository, times(1)).delete(1);
    }

    @Test
    void delete_invalidId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.delete(0));
        assertThrows(IllegalArgumentException.class, () -> cardService.delete(-1));
    }
}