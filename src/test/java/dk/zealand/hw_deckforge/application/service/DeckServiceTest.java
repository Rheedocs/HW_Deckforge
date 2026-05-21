package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.DeckCard;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @Mock
    private IDeckRepository deckRepository;

    @Mock
    private IDeckCardRepository deckCardRepository;

    @Mock
    private ICardRepository cardRepository;

    @InjectMocks
    private DeckService deckService;

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck(1, 1, "Test Deck", Format.CASUAL, DeckVisibility.PUBLIC);
    }

    // --- getById ---

    @Test
    void getById_withExistingId_returnsDeck() {
        when(deckRepository.findById(1)).thenReturn(deck);
        Deck result = deckService.getById(1);
        assertEquals(deck, result);
    }

    @Test
    void getById_withNonExistingId_throwsNotFoundException() {
        when(deckRepository.findById(99)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> deckService.getById(99));
    }

    @Test
    void getById_withInvalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> deckService.getById(0));
    }

    // --- create ---

    @Test
    void create_withValidDeck_savesDeck() {
        deckService.create(deck);
        verify(deckRepository).save(deck);
    }

    @Test
    void create_withNullDeck_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> deckService.create(null));
    }

    @Test
    void create_withBlankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Deck(-1, 1, "", Format.CASUAL, DeckVisibility.PUBLIC));
    }

    // --- update ---

    @Test
    void update_withValidDeck_updatesDeck() {
        deckService.update(deck);
        verify(deckRepository).update(deck);
    }

    // --- delete ---

    @Test
    void delete_withValidIdAndOwner_deletesDeck() {
        when(deckRepository.findById(1)).thenReturn(deck);
        deckService.delete(1, 1);
        verify(deckRepository).delete(1);
    }

    @Test
    void delete_withWrongOwner_throwsIllegalArgumentException() {
        when(deckRepository.findById(1)).thenReturn(deck);
        assertThrows(IllegalArgumentException.class, () -> deckService.delete(1, 99));
    }

    // --- getVisibleDecks ---

    @Test
    void getVisibleDecks_forOwner_returnsAllDecks() {
        Deck privatDeck = new Deck(2, 1, "Privat", Format.CASUAL, DeckVisibility.PRIVATE);
        when(deckRepository.findByPlayerId(1)).thenReturn(List.of(deck, privatDeck));
        List<Deck> result = deckService.getVisibleDecks(1, true, false);
        assertEquals(2, result.size());
    }

    @Test
    void getVisibleDecks_forAdmin_returnsAllDecks() {
        Deck privatDeck = new Deck(2, 1, "Privat", Format.CASUAL, DeckVisibility.PRIVATE);
        when(deckRepository.findByPlayerId(1)).thenReturn(List.of(deck, privatDeck));
        List<Deck> result = deckService.getVisibleDecks(1, false, true);
        assertEquals(2, result.size());
    }

    @Test
    void getVisibleDecks_forOtherPlayer_returnsOnlyPublicDecks() {
        Deck privatDeck = new Deck(2, 1, "Privat", Format.CASUAL, DeckVisibility.PRIVATE);
        when(deckRepository.findByPlayerId(1)).thenReturn(List.of(deck, privatDeck));
        List<Deck> result = deckService.getVisibleDecks(1, false, false);
        assertEquals(1, result.size());
        assertTrue(result.getFirst().isPublic());
    }

    // --- checkAccess ---

    @Test
    void checkAccess_publicDeck_noException() {
        assertDoesNotThrow(() -> deckService.checkAccess(deck, false, false));
    }

    @Test
    void checkAccess_privateDeckNonOwner_throwsAccessDeniedException() {
        Deck privatDeck = new Deck(1, 1, "Privat", Format.CASUAL, DeckVisibility.PRIVATE);
        assertThrows(AccessDeniedException.class, () -> deckService.checkAccess(privatDeck, false, false));
    }

    @Test
    void checkAccess_privateDeckOwner_noException() {
        Deck privatDeck = new Deck(1, 1, "Privat", Format.CASUAL, DeckVisibility.PRIVATE);
        assertDoesNotThrow(() -> deckService.checkAccess(privatDeck, true, false));
    }

    // --- addCard format-validering ---

    @Test
    void addCard_commanderFormat_exceedsAllowedQuantity_throwsIllegalArgumentException() {
        Deck commanderDeck = new Deck(1, 1, "Commander", Format.COMMANDER, DeckVisibility.PUBLIC);
        Card kort = new Card(1, "Sol Ring", CardType.ARTIFACT, "Alpha", Color.COLORLESS, Rarity.UNCOMMON, "", "");
        DeckCard eksisterende = new DeckCard(1, 1, 1, 1);
        when(deckRepository.findById(1)).thenReturn(commanderDeck);
        when(deckCardRepository.findByDeckIdAndCardId(1, 1)).thenReturn(eksisterende);
        when(cardRepository.findById(1)).thenReturn(kort);
        assertThrows(IllegalArgumentException.class, () -> deckService.addCard(1, 1, 1, 1));
    }

    @Test
    void addCard_standardFormat_exceedsFourCopies_throwsIllegalArgumentException() {
        Deck standardDeck = new Deck(1, 1, "Standard", Format.STANDARD, DeckVisibility.PUBLIC);
        Card kort = new Card(1, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON, "", "");
        DeckCard eksisterende = new DeckCard(1, 1, 1, 4);
        when(deckRepository.findById(1)).thenReturn(standardDeck);
        when(deckCardRepository.findByDeckIdAndCardId(1, 1)).thenReturn(eksisterende);
        when(cardRepository.findById(1)).thenReturn(kort);
        assertThrows(IllegalArgumentException.class, () -> deckService.addCard(1, 1, 1, 1));
    }

    @Test
    void addCard_newCard_savesNewDeckCard() {
        Card kort = new Card(1, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON, "", "");
        when(deckRepository.findById(1)).thenReturn(deck);
        when(deckCardRepository.findByDeckIdAndCardId(1, 1)).thenReturn(null);
        when(cardRepository.findById(1)).thenReturn(kort);
        deckService.addCard(1, 1, 2, 1);
        verify(deckCardRepository).save(any(DeckCard.class));
    }
}