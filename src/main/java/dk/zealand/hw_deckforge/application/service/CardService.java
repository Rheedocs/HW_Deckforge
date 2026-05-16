package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.ICardRepository;
import dk.zealand.hw_deckforge.domain.Card;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {

    private final ICardRepository cardRepository;

    public CardService(ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    public Map<Integer, Card> getCardMap() {
        Map<Integer, Card> map = new HashMap<>();
        for (Card card : cardRepository.findAll()) {
            map.put(card.getId(), card);
        }
        return map;
    }

    public Card getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end 0!");
        Card card = cardRepository.findById(id);
        if (card == null) throw new IllegalArgumentException("Kort med id " + id + " blev ikke fundet!");
        return card;
    }

    public void create(Card card) {
        if (card == null) throw new IllegalArgumentException("Kort må ikke være null!");
        validateCard(card);
        cardRepository.save(card);
    }

    public void update(Card card) {
        if (card == null) throw new IllegalArgumentException("Kort må ikke være null!");
        if (card.getId() == null || card.getId() <= 0) throw new IllegalArgumentException("Ugyldigt kort-id!");
        validateCard(card);
        cardRepository.update(card);
    }

    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Ugyldigt id!");
        cardRepository.delete(id);
    }

    private void validateCard(Card card) {
        if (card.getName() == null || card.getName().isBlank())
            throw new IllegalArgumentException("Kort skal have et navn!");
        if (card.getCardType() == null)
            throw new IllegalArgumentException("Kortet skal have en type!");
        if (card.getColor() == null)
            throw new IllegalArgumentException("Kortet skal have en farve!");
        if (card.getRarity() == null)
            throw new IllegalArgumentException("Kortet skal have en sjældenhed!");
    }
}