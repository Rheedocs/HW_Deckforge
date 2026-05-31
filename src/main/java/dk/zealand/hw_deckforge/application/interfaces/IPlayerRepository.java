package dk.zealand.hw_deckforge.application.interfaces;

import dk.zealand.hw_deckforge.domain.Player;
import java.util.List;

/** Kontrakt for spillerdata. Implementeres af PlayerRepository i infrastructure-laget. */
public interface IPlayerRepository {
    List<Player> findAll();
    Player findById(int id);
    Player findByEmail(String email);
    List<Player> findAllIncludingInactive();
    void save(Player player);
    void update(Player player);
    void delete(int id);
}