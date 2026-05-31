package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.infrastructure.external.ScryfallService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Håndterer kortadministration inkl. hentning af kortbilleder via ScryfallService.
 * Kun admin kan oprette og redigere. */
@Service
public class CardService {

    private static final String SCRYFALL_CARD_URL_PREFIX = "https://scryfall.com/card/";

    private final ICardRepository cardRepository;
    private final ScryfallService scryfallService;

    public CardService(ICardRepository cardRepository, ScryfallService scryfallService) {
        this.cardRepository = cardRepository;
        this.scryfallService = scryfallService;
    }

    // --- Forespørgsler ---

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    /** @return map fra kort-id til Card for O(1) opslag i templates uden gentagne databasekald. */
    public Map<Integer, Card> getCardMap() {
        Map<Integer, Card> map = new HashMap<>();
        for (Card card : cardRepository.findAll()) {
            map.put(card.getId(), card);
        }
        return map;
    }

    public Card getById(int id) {
        validateId(id);
        Card card = cardRepository.findById(id);
        if (card == null) throw new NotFoundException("Kort med id " + id + " blev ikke fundet");
        return card;
    }

    // --- Livscyklus ---

    /** Opretter kort og henter billedeurl fra Scryfall hvis en Scryfall-url er angivet. */
    public void create(Card card, String scryfallUrl) {
        prepareCardForSave(card, scryfallUrl);
        cardRepository.save(card);
    }

    /** Opdaterer kortdata og henter ny billedeurl fra Scryfall hvis url er ændret. */
    public void update(Card card, String scryfallUrl) {
        validateCardForUpdate(card);
        prepareCardForSave(card, scryfallUrl);
        cardRepository.update(card);
    }

    public void delete(int id) {
        validateId(id);
        cardRepository.delete(id);
    }

    // --- Intern behandling ---

    private void prepareCardForSave(Card card, String scryfallUrl) {
        validateCard(card);
        applyImageFromScryfallLink(card, sanitizeScryfallUrl(scryfallUrl));
    }

    private void applyImageFromScryfallLink(Card card, String scryfallUrl) {
        if (scryfallUrl == null) return;
        String imageUrl = scryfallService.fetchImageUrlByScryfallLink(scryfallUrl);
        if (imageUrl != null) card.setImageUrl(imageUrl);
    }

    private String sanitizeScryfallUrl(String scryfallUrl) {
        if (scryfallUrl == null || scryfallUrl.isBlank()) return null;
        String trimmedUrl = scryfallUrl.strip();
        return trimmedUrl.startsWith(SCRYFALL_CARD_URL_PREFIX) ? trimmedUrl : null;
    }

    // --- Validering ---

    private void validateCard(Card card) {
        if (card == null) throw new IllegalArgumentException("Kort må ikke være null");
        if (card.getName() == null || card.getName().isBlank()) throw new IllegalArgumentException("Kort skal have et navn");
        if (card.getCardType() == null) throw new IllegalArgumentException("Kortet skal have en type");
        if (card.getColor() == null) throw new IllegalArgumentException("Kortet skal have en farve");
        if (card.getRarity() == null) throw new IllegalArgumentException("Kortet skal have en sjældenhed");
    }

    private void validateCardForUpdate(Card card) {
        validateCard(card);
        if (card.getId() == null || card.getId() <= 0) throw new IllegalArgumentException("Ugyldigt kort-id");
    }

    private void validateId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul");
    }
}




