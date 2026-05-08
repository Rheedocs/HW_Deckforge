package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final IPlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PlayerService(IPlayerRepository playerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Player> getAll() { return playerRepository.findAll(); }
    public Player getById(int id) { return playerRepository.findById(id); }
    public Player getByEmail(String email) { return playerRepository.findByEmail(email); }
    public Player login(String email, String password) { return null; }
    public void create(Player player) { playerRepository.save(player); }
    public void update(Player player, String newPassword) { playerRepository.update(player); }
    public void delete(int id) { playerRepository.delete(id); }
    public boolean isOnlyAdmin(int id) { return false; }
}