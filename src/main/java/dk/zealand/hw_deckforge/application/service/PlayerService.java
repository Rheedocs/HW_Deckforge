package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;

import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import dk.zealand.hw_deckforge.domain.validation.PlayerValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        validateId(id, "Id");
        Player player = playerRepository.findById(id);
        if (player == null) throw new NotFoundException("Spiller med id " + id + " findes ikke");
        return player;
    }

    public List<Player> getAllSortedByLoggedIn(int loggedInId, boolean isAdmin) {
        List<Player> all = isAdmin ? playerRepository.findAllIncludingInactive() : playerRepository.findAll();
        List<Player> sorted = new ArrayList<>();
        Player self = null;
        for (Player player : all) {
            if (player.getId() == loggedInId) self = player;
            else sorted.add(player);
        }
        if (self != null) sorted.addFirst(self);
        return sorted;
    }

    // --- Auth og livscyklus ---

    public Player login(String email, String password) {
        Player player = playerRepository.findByEmail(email);
        validateLogin(player, password);
        return player;
    }

    public void create(String username, String email, String password) {
        validateNewEmail(email);
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
        validateId(id, "Id");
        Player player = getById(id);
        validateCanDelete(player);
        playerRepository.delete(id);
    }

    public boolean isOnlyAdmin(int id) {
        int adminCount = 0;
        for (Player player : getAll()) if (player.getRole() == Role.ADMIN) adminCount++;
        return adminCount == 1 && getById(id).getRole() == Role.ADMIN;
    }

    public void checkCollectionAccess(int playerId, boolean isSelf, boolean isAdmin) {
        Player owner = getById(playerId);
        if (!canAccessCollection(owner, isSelf, isAdmin)) throw new AccessDeniedException("Denne spillers profil er privat");
    }

    // --- Samling ---

    public void addToCollection(int playerId, int cardId) {
        validateId(playerId, "Spiller-id");
        validateId(cardId, "Kort-id");
        getById(playerId);
        playerCardRepository.save(new PlayerCard(0, playerId, cardId, 1, false));
    }

    public void removeFromCollection(int id) {
        validateId(id, "Samlings-id");
        playerCardRepository.delete(id);
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

    // --- Validering ---

    private void validatePlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Spiller må ikke være null");
        if (player.getUsername() == null || player.getUsername().isBlank()) throw new IllegalArgumentException("Brugernavn må ikke være tomt");
        if (!PlayerValidator.isValidEmail(player.getEmail())) throw new IllegalArgumentException("Email er ikke gyldig");
        if (player.getCollectionVisibility() == null) throw new IllegalArgumentException("Samlingssynlighed skal angives");
        if (player.getRole() == null) throw new IllegalArgumentException("Rolle skal angives");
    }

    private void validateLogin(Player player, String password) {
        if (player == null || !passwordEncoder.matches(password, player.getPassword()))
            throw new IllegalArgumentException("Forkert email eller adgangskode");
        if (!player.isActive()) throw new IllegalArgumentException("Denne konto er deaktiveret");
    }

    private void validateNewEmail(String email) {
        if (playerRepository.findByEmail(email) != null) throw new ValidationException("Email er allerede i brug");
    }

    private void validateCanDelete(Player player) {
        if (isOnlyAdmin(player.getId())) throw new ValidationException("Du kan ikke slette den eneste admin");
    }

    private boolean canAccessCollection(Player owner, boolean isSelf, boolean isAdmin) {
        return isSelf || isAdmin || owner.getCollectionVisibility() != CollectionVisibility.PRIVATE;
    }

    private void validateId(int id, String fieldName) {
        if (id <= 0) throw new IllegalArgumentException(fieldName + " skal være større end nul");
    }
}




