package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;

import dk.zealand.hw_deckforge.application.interfaces.IResultRepository;
import dk.zealand.hw_deckforge.domain.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final IResultRepository resultRepository;

    public ResultService(IResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    // --- Forespørgsler ---

    public List<Result> getByEventId(int eventId) {
        validateId(eventId, "EventId");
        return resultRepository.findByEventId(eventId);
    }

    public List<Result> getByPlayerId(int playerId) {
        validateId(playerId, "PlayerId");
        List<Result> results = resultRepository.findByPlayerId(playerId);
        if (results == null || results.isEmpty()) throw new NotFoundException("Ingen resultater for spiller fundet");
        return results;
    }

    // --- Livscyklus ---

    public void save(Result result) {
        if (result == null) throw new IllegalArgumentException("Resultat må ikke være null");
        result.validateOrThrow();
        resultRepository.save(result);
    }

    // --- Validering ---

    private void validateId(int id, String fieldName) {
        if (id <= 0) throw new IllegalArgumentException(fieldName + " skal være større end nul");
    }
}



