package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerCardService {

    private final IPlayerCardRepository playerCardRepository;

    public PlayerCardService(IPlayerCardRepository playerCardRepository) {
        this.playerCardRepository = playerCardRepository;
    }

    public int countByPlayerId(int playerId) {
        return playerCardRepository.countByPlayerId(playerId);
    }
}