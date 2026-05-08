package dk.zealand.hw_deckforge.application.service;

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

    public List<Result> getByEventId(int eventId) { return resultRepository.findByEventId(eventId); }
    public List<Result> getByPlayerId(int playerId) { return resultRepository.findByPlayerId(playerId); }
    public void save(Result result) { resultRepository.save(result); }
}