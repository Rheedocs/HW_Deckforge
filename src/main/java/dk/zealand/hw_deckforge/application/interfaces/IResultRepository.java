package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Result;
import java.util.List;

public interface IResultRepository {
    List<Result> findByEventId(int eventId);
    List<Result> findByPlayerId(int playerId);
    void save(Result result);
}
