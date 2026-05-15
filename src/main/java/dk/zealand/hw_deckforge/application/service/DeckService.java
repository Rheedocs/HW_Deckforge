package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IDeckCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.DeckCard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final IDeckRepository deckRepository;
    private final IDeckCardRepository deckCardRepository;

    public DeckService(IDeckRepository deckRepository, IDeckCardRepository deckCardRepository) {
        this.deckRepository = deckRepository;
        this.deckCardRepository = deckCardRepository;
    }

    // --- Deck CRUD ---

    public List<Deck> getAll() {
        return deckRepository.findAll();
    }

    public Deck getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul");
        Deck deck = deckRepository.findById(id);
        if (deck == null) throw new IllegalArgumentException("Deck med id " + id + " findes ikke");
        return deck;
    }

    public List<Deck> getByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("PlayerId skal være større end nul");
        return deckRepository.findByPlayerId(playerId);
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
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til dette deck");
        if (!deck.isPublic()) throw new IllegalArgumentException("Deck er allerede privat");
        deck.makePrivate();
        deckRepository.update(deck);
    }

    public void makePublic(int id, int requestingPlayerId) {
        Deck deck = getById(id);
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til dette deck");
        if (deck.isPublic()) throw new IllegalStateException("Deck er allerede offentligt");
        deck.makePublic();
        deckRepository.update(deck);
    }

    public void delete(int id, int requestingPlayerId) {
        Deck deck = getById(id);
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til at slette dette deck");
        deckRepository.delete(id);
    }

    // --- Deck-kort ---

    public List<DeckCard> getDeckCards(int deckId) {
        if (deckId <= 0) throw new IllegalArgumentException("Ugyldigt deck-id");
        return deckCardRepository.findByDeckId(deckId);
    }

    public void addCard(int deckId, int cardId, int quantity, int requestingPlayerId) {
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        Deck deck = getById(deckId);
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til dette deck");
        DeckCard existing = deckCardRepository.findByDeckIdAndCardId(deckId, cardId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            deckCardRepository.update(existing);
        } else {
            deckCardRepository.save(new DeckCard(0, deckId, cardId, quantity));
        }
    }

    public void removeCard(int deckCardId, int deckId, int requestingPlayerId) {
        if (deckCardId <= 0) throw new IllegalArgumentException("Ugyldigt deck-kort-id");
        Deck deck = getById(deckId);
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til dette deck");
        deckCardRepository.delete(deckCardId);
    }

    // --- Validering ---

    private void validateDeck(Deck deck) {
        if (deck == null) throw new IllegalArgumentException("Deck må ikke være null");
        if (deck.getName() == null || deck.getName().isBlank())
            throw new IllegalArgumentException("Deck navn må ikke være tomt");
        if (deck.getName().length() > 50)
            throw new IllegalArgumentException("Deck navn må ikke være længere end 50 tegn");
        if (deck.getFormat() == null)
            throw new IllegalArgumentException("Format må ikke være tomt");
        if (deck.getVisibility() == null)
            throw new IllegalArgumentException("Synlighed må ikke være tom");
        if (deck.getPlayerId() <= 0)
            throw new IllegalArgumentException("Deck skal tilhøre en gyldig spiller");
    }
}
