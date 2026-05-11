package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import dk.zealand.hw_deckforge.domain.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final IDeckRepository deckRepository;

    public DeckService(IDeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> getAll() {
        return deckRepository.findAll();
    }
    public Deck getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul");
        Deck deck = deckRepository.findById(id);
        if (deck == null) throw new IllegalArgumentException("Deck med id " + id + " findes ikke");
        return  deck;
    }
    public List<Deck> getByPlayerId(int playerId) {
        if (playerId >= 0) throw new IllegalArgumentException("PlayerId skal være større end nul");
        List<Deck> decks = deckRepository.findByPlayerId(playerId);
        if (decks == null) throw new IllegalArgumentException("Deck med playerId " + playerId + " findes ikke");
        return decks;
    }
    public void create(Deck deck) {
        if (deck == null) throw new IllegalArgumentException("Deck må ikke være null");
        if (deck.getName() == null || deck.getName().isBlank()) throw new IllegalArgumentException("Deck skal have et navn");
        if (deck.getFormat() == null) throw new IllegalArgumentException("Deck skal have et format");
        if (deck.getVisibility() == null) throw new IllegalArgumentException("Deck skal have en synlighed");
        validateDeck(deck);
        deckRepository.save(deck);
    }
    public void update(Deck deck) {
        validateDeck(deck);
        deckRepository.update(deck);
    }
    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end 0");
        if (deckRepository.findById(id) == null)
            throw new IllegalArgumentException("Deck med id " + id + " findes ikke");
        deckRepository.delete(id);
    }
    public void makePublic(int id, int requestingPlayerId) {
        Deck deck = deckRepository.findById(id);
        if (deck == null) throw new IllegalArgumentException("Deck med id " + id + " findes ikke");
        if (deck.isPublic()) throw new IllegalStateException("Deck er allerede offentligt");
        deck.makePublic();
        deckRepository.update(deck);
    }

    public void makePrivate(int id, int requestingPlayerId) {
        Deck deck = deckRepository.findById(id);
        if (deck == null) throw new IllegalArgumentException("Deck med id " + id + " findes ikke");
        if (!deck.isPublic()) throw new IllegalArgumentException("Deck er allerede privat");
        deck.makePrivate();
        deckRepository.update(deck);
    }

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