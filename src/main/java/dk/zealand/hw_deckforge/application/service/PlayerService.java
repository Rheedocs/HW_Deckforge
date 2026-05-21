package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.domain.validation.PlayerValidator;
import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final IPlayerRepository playerRepository;
    private final IPlayerCardRepository playerCardRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PlayerService(IPlayerRepository playerRepository, IPlayerCardRepository playerCardRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.playerCardRepository = playerCardRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Forespørgsler ---

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public List<Player> getAllIncludingInactive() {
        return playerRepository.findAllIncludingInactive();
    }

    public Player getById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end nul");
        Player player = playerRepository.findById(id);
        if (player == null) throw new IllegalArgumentException("Spiller med id " + id + " findes ikke");
        return player;
    }

    public List<Player> getAllSortedByLoggedIn(int loggedInId, boolean isAdmin) {
        List<Player> all = isAdmin
                ? playerRepository.findAllIncludingInactive()
                : playerRepository.findAll();
        List<Player> sorted = new java.util.ArrayList<>();
        Player self = null;
        for (Player p : all) {
            if (p.getId() == loggedInId) self = p;
            else sorted.add(p);
        }
        if (self != null) sorted.addFirst(self);
        return sorted;
    }

    // --- Auth og livscyklus ---

    // fejlbesked er bevidst generisk — afslører ikke om email eller adgangskode er forkert
    public Player login(String email, String password) {
        Player player = playerRepository.findByEmail(email);
        if (player == null || !passwordEncoder.matches(password, player.getPassword()))
            throw new IllegalArgumentException("Forkert email eller adgangskode");
        if (!player.isActive())
            throw new IllegalArgumentException("Denne konto er deaktiveret");
        return player;
    }

    public void create(String username, String email, String password) {
        if (playerRepository.findByEmail(email) != null)
            throw new IllegalArgumentException("Email er allerede i brug");
        PlayerValidator.validatePassword(password);
        Player player = new Player(0, username, email, password, Role.PLAYER, CollectionVisibility.TRADE_ONLY);
        validatePlayer(player);
        player.changePassword(passwordEncoder.encode(player.getPassword()));
        playerRepository.save(player);
    }

    public void update(Player player, String newPassword, boolean isAdmin, boolean isSelf) {
        Player existing = getById(player.getId());
        applyProfileChanges(existing, player);
        applyRoleAndStatus(existing, player, isAdmin, isSelf);
        applyPasswordChange(existing, newPassword);
        validatePlayer(existing);
        playerRepository.update(existing);
    }

    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id skal være større end 0");
        if (isOnlyAdmin(id)) throw new IllegalArgumentException("Du kan ikke slette den eneste admin");
        if (playerRepository.findById(id) == null) throw new IllegalArgumentException("Spiller med id " + id
                + " findes ikke");
        playerRepository.delete(id);
    }

    public boolean isOnlyAdmin(int id) {
        int adminCount = 0;
        for (Player player : getAll()) if (player.getRole() == Role.ADMIN) adminCount++;
        return adminCount == 1 && getById(id).getRole() == Role.ADMIN;
    }

    public void checkCollectionAccess(int playerId, boolean isSelf, boolean isAdmin) {
        Player owner = getById(playerId);
        if (!isSelf && !isAdmin && owner.getCollectionVisibility() == CollectionVisibility.PRIVATE)
            throw new AccessDeniedException("Denne spillers profil er privat");
    }

    // --- Opdateringshjælpere ---

    private void applyProfileChanges(Player existing, Player updated) {
        existing.setUsername(updated.getUsername());
        existing.setEmail(updated.getEmail());
        existing.changeVisibility(updated.getCollectionVisibility());
    }

    private void applyRoleAndStatus(Player existing, Player updated, boolean isAdmin, boolean isSelf) {
        if (!isAdmin) {
            existing.demoteToPlayer();
            return;
        }
        if (isSelf && isOnlyAdmin(existing.getId())) existing.promoteToAdmin();
        else if (updated.isAdmin()) existing.promoteToAdmin();
        else existing.demoteToPlayer();
        existing.setActive(updated.isActive());
    }

    private void applyPasswordChange(Player existing, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) return;
        PlayerValidator.validatePassword(newPassword);
        existing.changePassword(passwordEncoder.encode(newPassword));
    }

    // --- Samling ---

    public void addToCollection(int playerId, int cardId) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (cardId <= 0) throw new IllegalArgumentException("Ugyldigt kort-id");
        getById(playerId);
        PlayerCard playerCard = new PlayerCard(0, playerId, cardId, 1, false);
        playerCardRepository.save(playerCard);
    }

    public void removeFromCollection(int id) {
        if (id <= 0) throw new IllegalArgumentException("Ugyldigt samlings-id");
        playerCardRepository.delete(id);
    }

    // --- Validering ---

    private void validatePlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Spiller må ikke være null");
        if (!PlayerValidator.isValidEmail(player.getEmail()))
            throw new IllegalArgumentException("Email er ikke gyldig");
    }
}