package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IDeckRepository;
import dk.zealand.hw_deckforge.domain.Deck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final IDeckRepository deckRepository;

    public DeckService(IDeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> getAll() { return deckRepository.findAll(); }
    public Deck getById(int id) { return deckRepository.findById(id); }
    public List<Deck> getByPlayerId(int playerId) { return deckRepository.findByPlayerId(playerId); }
    public void create(Deck deck) { deckRepository.save(deck); }
    public void update(Deck deck) { deckRepository.update(deck); }
    public void delete(int id) { deckRepository.delete(id); }
}