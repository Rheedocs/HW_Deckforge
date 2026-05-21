package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IResultRepository;
import dk.zealand.hw_deckforge.domain.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ResultService {

    private final IResultRepository resultRepository;

    public ResultService(IResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    // --- Forespørgsler ---

    public List<Result> getByEventId(int eventId) {
        if (eventId <= 0) throw new IllegalArgumentException("Id skal være større end nul!");
        return resultRepository.findByEventId(eventId);
    }

    public List<Result> getByPlayerId(int playerId) {
        if (playerId <= 0) throw new IllegalArgumentException("Id skal være større end nul!");
        List<Result> results = resultRepository.findByPlayerId(playerId);
        if (results == null || results.isEmpty()) throw new NoSuchElementException("Ingen resultater for spiller fundet!");
        return results;
    }

    // --- Livscyklus ---

    public void save(Result result) {
        if (result == null) throw new IllegalArgumentException("Resultat må ikke være nul!");
        List<String> errors = result.validate();
        if (!errors.isEmpty()) throw new IllegalArgumentException(String.join(", ", errors));
        resultRepository.save(result);
    }
}