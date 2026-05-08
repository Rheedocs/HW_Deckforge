package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final ICardRepository cardRepository;

    public CardService(ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAll() { return cardRepository.findAll(); }
    public Card getById(int id) { return cardRepository.findById(id); }
    public void create(Card card) { cardRepository.save(card); }
    public void update(Card card) { cardRepository.update(card); }
    public void delete(int id) { cardRepository.delete(id); }
}