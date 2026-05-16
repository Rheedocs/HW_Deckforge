package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Card;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.DeckCard;
import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Format;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private static final String[] BASIC_LAND_NAMES = {"Forest", "Island", "Mountain", "Plains", "Swamp"};

    private final IDeckRepository deckRepository;
    private final IDeckCardRepository deckCardRepository;
    private final ICardRepository cardRepository;

    public DeckService(IDeckRepository deckRepository, IDeckCardRepository deckCardRepository,
                       ICardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.deckCardRepository = deckCardRepository;
        this.cardRepository = cardRepository;
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
        int alreadyInDeck = existing != null ? existing.getQuantity() : 0;

        // Tjek format-begrænsning
        Card card = cardRepository.findById(cardId);
        validateFormatLimit(deck.getFormat(), card, alreadyInDeck, quantity);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            deckCardRepository.update(existing);
        } else {
            deckCardRepository.save(new DeckCard(0, deckId, cardId, quantity));
        }
    }

    private void validateFormatLimit(Format format, Card card, int alreadyInDeck, int quantity) {
        if (format == Format.COMMANDER && !isBasicLand(card)) {
            if (alreadyInDeck + quantity > 1)
                throw new IllegalArgumentException(
                        "Commander tillader max 1 eksemplar af hvert kort (undtagen basic lands)");
        } else if (format == Format.STANDARD) {
            if (alreadyInDeck + quantity > 4)
                throw new IllegalArgumentException(
                        "Standard tillader max 4 eksemplarer af hvert kort");
        }
    }

    private boolean isBasicLand(Card card) {
        if (card == null || card.getCardType() != CardType.LAND) return false;
        for (String name : BASIC_LAND_NAMES) {
            if (name.equals(card.getName())) return true;
        }
        return false;
    }

    public void removeCard(int deckCardId, int deckId, int quantity, int requestingPlayerId) {
        if (deckCardId <= 0) throw new IllegalArgumentException("Ugyldigt deck-kort-id");
        if (quantity < 1) throw new IllegalArgumentException("Antal skal være mindst 1");
        Deck deck = getById(deckId);
        if (deck.getPlayerId() != requestingPlayerId) throw new IllegalArgumentException("Du har ikke adgang til dette deck");
        DeckCard deckCard = deckCardRepository.findById(deckCardId);
        if (deckCard == null) throw new IllegalArgumentException("Kortet findes ikke i decket");
        if (quantity >= deckCard.getQuantity()) {
            deckCardRepository.delete(deckCardId);
        } else {
            deckCard.setQuantity(deckCard.getQuantity() - quantity);
            deckCardRepository.update(deckCard);
        }
    }

    public int getTotalCardCount(int deckId) {
        int total = 0;
        for (DeckCard dc : getDeckCards(deckId)) {
            total += dc.getQuantity();
        }
        return total;
    }

    public int getDeckCount(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("PlayerId skal være større end nul");
        return deckRepository.findByPlayerId(playerId).size();
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
