package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
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

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public Player getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul");
        Player player = playerRepository.findById(id);
        if (player == null) throw new IllegalArgumentException("Spiller med id " + id + " findes ikke");
        return player;
    }

    /**
     * Validerer email og adgangskode mod databasen.
     * Fejlbeskeden er bevidst generisk for ikke at afsløre
     * om det er email eller adgangskode der er forkert.
     */
    public Player login(String email, String password) {
        Player player = playerRepository.findByEmail(email);
        if (player == null || !passwordEncoder.matches(password, player.getPassword()))
            throw new IllegalArgumentException("Forkert email eller adgangskode");
        return player;
    }

    /**
     * Opretter en ny spiller med BCrypt-hashet adgangskode.
     * Tjekker at email ikke allerede er i brug.
     */
    public void create(String username, String email, String password) {
        if (playerRepository.findByEmail(email) != null)
            throw new IllegalArgumentException("Email er allerede i brug");
        validatePassword(password);
        Player player = new Player(0, username, email, password, Role.PLAYER, CollectionVisibility.TRADE_ONLY);
        validatePlayer(player);
        player.changePassword(passwordEncoder.encode(player.getPassword()));
        playerRepository.save(player);
    }

    public void update(Player player, String newPassword) {
        Player existing = getById(player.getId());
        existing.setUsername(player.getUsername());
        existing.setEmail(player.getEmail());
        existing.changeVisibility(player.getCollectionVisibility());
        if (player.isAdmin()) existing.promoteToAdmin(); else existing.demoteToPlayer();
        if (newPassword != null && !newPassword.isBlank()) {
            validatePassword(newPassword);
            existing.changePassword(passwordEncoder.encode(newPassword));
        }
        validatePlayer(existing);
        playerRepository.update(existing);
    }

    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        getById(id);
        playerRepository.delete(id);
    }

    /**
     * Tjekker om en given spiller er den eneste admin i systemet.
     * Bruges til at forhindre sletning af den sidste admin.
     */
    public boolean isOnlyAdmin(int id) {
        int adminCount = 0;
        for (Player player : getAll()) if (player.getRole() == Role.ADMIN) adminCount++;
        return adminCount == 1 && getById(id).getRole() == Role.ADMIN;
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Adgangskode må ikke være tom");
        if (password.length() < 8)
            throw new IllegalArgumentException("Adgangskode skal være mindst 8 tegn");
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isBlank() &&
                email.matches("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");
    }

    private void validatePlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Spiller må ikke være null");
        if (player.getUsername() == null || player.getUsername().isBlank())
            throw new IllegalArgumentException("Brugernavn må ikke være tomt");
        if (!isValidEmail(player.getEmail()))
            throw new IllegalArgumentException("Email er ikke gyldig");
        if (player.getRole() == null)
            throw new IllegalArgumentException("Rolle må ikke være tom");
    }
}