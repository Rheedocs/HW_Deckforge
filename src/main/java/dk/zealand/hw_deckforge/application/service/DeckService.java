package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.DeckCard;
import dk.zealand.hw_deckforge.domain.enums.Format;
import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import dk.zealand.hw_deckforge.domain.validation.FormatValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService {

    private final IDeckRepository deckRepository;
    private final IDeckCardRepository deckCardRepository;
    private final ICardRepository cardRepository;

    public DeckService(IDeckRepository deckRepository, IDeckCardRepository deckCardRepository, ICardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.deckCardRepository = deckCardRepository;
        this.cardRepository = cardRepository;
    }

    // --- Deck CRUD ---

    public Deck getById(int id) {
        validateId(id, "Id");
        Deck deck = deckRepository.findById(id);
        if (deck == null) throw new NotFoundException("Deck med id " + id + " findes ikke");
        return deck;
    }

    public List<Deck> getByPlayerId(int playerId) {
        validateId(playerId, "PlayerId");
        return deckRepository.findByPlayerId(playerId);
    }

    public List<Deck> getByPlayerIdAndFormat(int playerId, Format format) {
        validateId(playerId, "PlayerId");
        if (format == null) throw new IllegalArgumentException("Format skal angives");
        List<Deck> matchingDecks = new ArrayList<>();
        for (Deck deck : deckRepository.findByPlayerId(playerId)) {
            if (deck.getFormat() == format) matchingDecks.add(deck);
        }
        return matchingDecks;
    }

    public List<Deck> getVisibleDecks(int playerId, boolean isSelf, boolean isAdmin) {
        validateId(playerId, "PlayerId");
        List<Deck> all = deckRepository.findByPlayerId(playerId);
        if (isSelf || isAdmin) return all;
        List<Deck> visible = new ArrayList<>();
        for (Deck deck : all) {
            if (deck.isPublic()) visible.add(deck);
        }
        return visible;
    }

    public void checkAccess(Deck deck, boolean isSelf, boolean isAdmin) {
        if (!deck.isPublic() && !isSelf && !isAdmin) throw new AccessDeniedException("Du har ikke adgang til dette deck");
    }

    public void checkOwnerAccess(Deck deck, boolean isSelf, boolean isAdmin) {
        if (!isSelf && !isAdmin) throw new AccessDeniedException("Du har ikke adgang til dette deck");
    }

    public void create(Deck deck) {
        validateDeck(deck);
        deckRepository.save(deck);
    }

    public void update(Deck deck) {
        validateDeck(deck);
        deckRepository.update(deck);
    }

    public void makePrivate(int id, int requestingPlayerId) {
        Deck deck = getById(id);
        validateDeckOwner(deck, requestingPlayerId);
        if (!deck.isPublic()) throw new IllegalArgumentException("Deck er allerede privat");
        deck.makePrivate();
        deckRepository.update(deck);
    }

    public void makePublic(int id, int requestingPlayerId) {
        Deck deck = getById(id);
        validateDeckOwner(deck, requestingPlayerId);
        if (deck.isPublic()) throw new IllegalStateException("Deck er allerede offentligt");
        deck.makePublic();
        deckRepository.update(deck);
    }

    public void delete(int id, int requestingPlayerId) {
        Deck deck = getById(id);
        validateDeckOwner(deck, requestingPlayerId);
        deckRepository.delete(id);
    }

    // --- Deck-kort ---

    public List<DeckCard> getDeckCards(int deckId) {
        validateId(deckId, "Deck-id");
        return deckCardRepository.findByDeckId(deckId);
    }

    public void addCard(int deckId, int cardId, int quantity, int requestingPlayerId) {
        validateQuantity(quantity);
        Deck deck = getById(deckId);
        validateDeckOwner(deck, requestingPlayerId);
        validateDeckSizeAfterAdd(deck, deckId, quantity);

        DeckCard existing = deckCardRepository.findByDeckIdAndCardId(deckId, cardId);
        int alreadyInDeck = existing != null ? existing.getQuantity() : 0;
        Card card = cardRepository.findById(cardId);
        FormatValidator.validateFormatLimit(deck.getFormat(), card, alreadyInDeck, quantity);
        saveOrUpdateDeckCard(deckId, cardId, quantity, existing);
    }

    public void removeCard(int deckCardId, int deckId, int quantity, int requestingPlayerId) {
        validateId(deckCardId, "Deck-kort-id");
        validateQuantity(quantity);
        Deck deck = getById(deckId);
        validateDeckOwner(deck, requestingPlayerId);
        DeckCard deckCard = deckCardRepository.findById(deckCardId);
        if (deckCard == null) throw new IllegalArgumentException("Kortet findes ikke i decket");
        removeOrReduceDeckCard(deckCard, quantity);
    }

    public int getTotalCardCount(int deckId) {
        int total = 0;
        for (DeckCard dc : getDeckCards(deckId)) {
            total += dc.getQuantity();
        }
        return total;
    }

    public int getDeckCount(int playerId) {
        validateId(playerId, "PlayerId");
        return deckRepository.findByPlayerId(playerId).size();
    }

    // --- Intern behandling ---

    private void saveOrUpdateDeckCard(int deckId, int cardId, int quantity, DeckCard existing) {
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            deckCardRepository.update(existing);
        } else {
            deckCardRepository.save(new DeckCard(0, deckId, cardId, quantity));
        }
    }

    private void removeOrReduceDeckCard(DeckCard deckCard, int quantity) {
        if (quantity >= deckCard.getQuantity()) {
            deckCardRepository.delete(deckCard.getId());
        } else {
            deckCard.setQuantity(deckCard.getQuantity() - quantity);
            deckCardRepository.update(deckCard);
        }
    }

    // --- Validering ---

    private void validateDeck(Deck deck) {
        if (deck == null) throw new IllegalArgumentException("Deck må ikke være null");
        if (deck.getName() == null || deck.getName().isBlank()) throw new IllegalArgumentException("Deck navn må ikke være tomt");
        if (deck.getName().length() > 50) throw new IllegalArgumentException("Deck navn må ikke være længere end 50 tegn");
        if (deck.getFormat() == null) throw new IllegalArgumentException("Format skal angives");
    }

    private void validateDeckOwner(Deck deck, int requestingPlayerId) {
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til dette deck");
    }

    private void validateDeckSizeAfterAdd(Deck deck, int deckId, int quantity) {
        int totalAfterAdd = getTotalCardCount(deckId) + quantity;
        if (deck.getFormat().hasMaxSize() && totalAfterAdd > deck.getFormat().getMaxSize())
            throw new IllegalArgumentException(deck.getFormat().getDisplayName() + " må kun have " + deck.getFormat().getMaxSize() + " kort");
    }

    private void validateId(int id, String fieldName) {
        if (id <= 0) throw new IllegalArgumentException(fieldName + " skal være større end nul");
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
    }
}




