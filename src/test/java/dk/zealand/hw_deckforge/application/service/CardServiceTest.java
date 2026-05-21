package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;
import dk.zealand.hw_deckforge.infrastructure.external.ScryfallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private ICardRepository cardRepository;

    @Mock
    private ScryfallService scryfallService;

    @InjectMocks
    private CardService cardService;

    private Card testCard;

    @BeforeEach
    void setUp() {
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
    void getById_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.getById(0));
    }

    @Test
    void getById_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.getById(-1));
    }

    @Test
    void getById_notFound_throwsIllegalArgument() {
        when(cardRepository.findById(99)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cardService.getById(99));
    }

    // --- create ---
    @Test
    void create_validCard_callsSave() {
        cardService.create(testCard, null);
        verify(cardRepository).save(testCard);
    }

    @Test
    void create_nullCard_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.create(null, null));
    }

    @Test
    void create_blankName_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(0, "", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON, null, null));
    }

    @Test
    void create_nullCardType_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(0, "Lightning Bolt", null, "Alpha", Color.RED, Rarity.COMMON, null, null));
    }

    @Test
    void create_nullColor_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(0, "Lightning Bolt", CardType.INSTANT, "Alpha", null, Rarity.COMMON, null, null));
    }

    @Test
    void create_nullRarity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(0, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, null, null, null));
    }

    @Test
    void create_withScryfallUrl_setsImageUrl() {
        String scryfallUrl = "https://scryfall.com/card/tla/4/aang-the-last-airbender";
        String imageUrl = "https://cards.scryfall.io/normal/front/a/b/abc.jpg";
        when(scryfallService.fetchImageUrlByScryfallLink(scryfallUrl)).thenReturn(imageUrl);
        cardService.create(testCard, scryfallUrl);
        assertEquals(imageUrl, testCard.getImageUrl());
        verify(cardRepository).save(testCard);
    }

    @Test
    void create_withInvalidScryfallUrl_doesNotChangeImageUrl() {
        cardService.create(testCard, "ugyldig");
        assertEquals("url", testCard.getImageUrl());
        verify(cardRepository).save(testCard);
    }

    // --- update ---
    @Test
    void update_validCard_callsUpdate() {
        cardService.update(testCard, null);
        verify(cardRepository).update(testCard);
    }

    @Test
    void update_nullCard_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.update(null, null));
    }

    @Test
    void update_idIsZero_throwsIllegalArgument() {
        Card card = new Card(0, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON, null, null);
        assertThrows(IllegalArgumentException.class, () -> cardService.update(card, null));
    }

    @Test
    void update_blankName_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(1, "", CardType.INSTANT, "Alpha", Color.RED, Rarity.COMMON, null, null));
    }

    @Test
    void update_nullRarity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(1, "Lightning Bolt", CardType.INSTANT, "Alpha", Color.RED, null, null, null));
    }

    @Test
    void update_withScryfallUrl_setsImageUrl() {
        String scryfallUrl = "https://scryfall.com/card/tla/4/aang-the-last-airbender";
        String imageUrl = "https://cards.scryfall.io/normal/front/a/b/abc.jpg";
        when(scryfallService.fetchImageUrlByScryfallLink(scryfallUrl)).thenReturn(imageUrl);
        cardService.update(testCard, scryfallUrl);
        assertEquals(imageUrl, testCard.getImageUrl());
        verify(cardRepository).update(testCard);
    }

    // --- delete ---
    @Test
    void delete_validId_callsDelete() {
        cardService.delete(1);
        verify(cardRepository).delete(1);
    }

    @Test
    void delete_idIsZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.delete(0));
    }

    @Test
    void delete_idIsNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> cardService.delete(-1));
    }
}