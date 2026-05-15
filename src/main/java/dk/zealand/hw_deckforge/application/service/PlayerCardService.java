package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerCardService {

    private final IPlayerCardRepository playerCardRepository;

    public PlayerCardService(IPlayerCardRepository playerCardRepository) {
        this.playerCardRepository = playerCardRepository;
    }

    public int countByPlayerId(int playerId) {
        return playerCardRepository.countByPlayerId(playerId);
    }

    public List<PlayerCard> getByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        return playerCardRepository.findByPlayerId(playerId);
    }
}