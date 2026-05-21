package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerCardRepository;
import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.PlayerCard;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import dk.zealand.hw_deckforge.domain.exceptions.AccessDeniedException;
import dk.zealand.hw_deckforge.domain.exceptions.NotFoundException;
import dk.zealand.hw_deckforge.domain.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock private IPlayerRepository playerRepository;
    @Mock private IPlayerCardRepository playerCardRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    private PlayerService playerService;
    private Player player;
    private Player admin;

    @BeforeEach
    void setup() {
        playerService = new PlayerService(playerRepository, playerCardRepository, passwordEncoder);
        player = new Player(1, "Nicki", "nicki@test.dk", "hashed-password", Role.PLAYER, CollectionVisibility.TRADE_ONLY);
        admin = new Player(2, "Admin", "admin@test.dk", "hashed-password", Role.ADMIN, CollectionVisibility.PUBLIC);
    }

    // --- Forespørgsler ---

    @Test
    void getAll_returnsPlayers() {
        when(playerRepository.findAll()).thenReturn(List.of(player));

        List<Player> result = playerService.getAll();

        assertEquals(1, result.size());
        assertEquals(player, result.getFirst());
    }

    @Test
    void getAllIncludingInactive_returnsPlayers() {
        when(playerRepository.findAllIncludingInactive()).thenReturn(List.of(player, admin));

        List<Player> result = playerService.getAllIncludingInactive();

        assertEquals(2, result.size());
    }

    @Test
    void getById_returnsPlayer_whenFound() {
        when(playerRepository.findById(1)).thenReturn(player);

        Player result = playerService.getById(1);

        assertEquals(player, result);
    }

    @Test
    void getById_withInvalidId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerService.getById(0));
    }

    @Test
    void getById_ifPlayerNotFound_throwsNotFoundException() {
        when(playerRepository.findById(99)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> playerService.getById(99));
    }

    @Test
    void getAllSortedByLoggedIn_putsLoggedInPlayerFirst() {
        when(playerRepository.findAll()).thenReturn(List.of(admin, player));

        List<Player> result = playerService.getAllSortedByLoggedIn(1, false);

        assertEquals(player, result.getFirst());
        assertEquals(admin, result.get(1));
    }

    @Test
    void getAllSortedByLoggedIn_adminGetsInactivePlayersToo() {
        when(playerRepository.findAllIncludingInactive()).thenReturn(List.of(player, admin));

        List<Player> result = playerService.getAllSortedByLoggedIn(2, true);

        assertEquals(admin, result.getFirst());
        verify(playerRepository).findAllIncludingInactive();
    }

    // --- Login ---

    @Test
    void login_withCorrectCredentials_returnsPlayer() {
        when(playerRepository.findByEmail("nicki@test.dk")).thenReturn(player);
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);

        Player result = playerService.login("nicki@test.dk", "password123");

        assertEquals(player, result);
    }

    @Test
    void login_withWrongCredentials_throwsIllegalArgument() {
        when(playerRepository.findByEmail("nicki@test.dk")).thenReturn(player);
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> playerService.login("nicki@test.dk", "wrong"));
    }

    @Test
    void login_withInactivePlayer_throwsIllegalArgument() {
        player.setActive(false);
        when(playerRepository.findByEmail("nicki@test.dk")).thenReturn(player);
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> playerService.login("nicki@test.dk", "password123"));
    }

    // --- Opret ---

    @Test
    void create_withValidData_savesPlayer() {
        when(playerRepository.findByEmail("new@test.dk")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");

        playerService.create("New Player", "new@test.dk", "password123");

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository).save(captor.capture());
        assertEquals("New Player", captor.getValue().getUsername());
        assertEquals("new@test.dk", captor.getValue().getEmail());
        assertEquals("encoded-password", captor.getValue().getPassword());
        assertEquals(Role.PLAYER, captor.getValue().getRole());
        assertEquals(CollectionVisibility.TRADE_ONLY, captor.getValue().getCollectionVisibility());
    }

    @Test
    void create_withExistingEmail_throwsValidationException() {
        when(playerRepository.findByEmail("nicki@test.dk")).thenReturn(player);

        assertThrows(ValidationException.class, () -> playerService.create("Nicki", "nicki@test.dk", "password123"));
        verify(playerRepository, never()).save(any());
    }

    @Test
    void create_withInvalidPassword_throwsIllegalArgument() {
        when(playerRepository.findByEmail("new@test.dk")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> playerService.create("New", "new@test.dk", "short"));
        verify(playerRepository, never()).save(any());
    }

    // --- Opdater ---

    @Test
    void update_asSelf_updatesProfileAndSessionRelevantFields() {
        Player updated = new Player(1, "Updated", "updated@test.dk", "ignored", Role.PLAYER, CollectionVisibility.PUBLIC);
        when(playerRepository.findById(1)).thenReturn(player);

        playerService.update(updated, null, false, true);

        verify(playerRepository).update(player);
        assertEquals("Updated", player.getUsername());
        assertEquals("updated@test.dk", player.getEmail());
        assertEquals(CollectionVisibility.PUBLIC, player.getCollectionVisibility());
        assertEquals(Role.PLAYER, player.getRole());
    }

    @Test
    void update_withNewPassword_hashesPassword() {
        Player updated = new Player(1, "Nicki", "nicki@test.dk", "ignored", Role.PLAYER, CollectionVisibility.TRADE_ONLY);
        when(playerRepository.findById(1)).thenReturn(player);
        when(passwordEncoder.encode("newPassword123")).thenReturn("new-hash");

        playerService.update(updated, "newPassword123", false, true);

        assertEquals("new-hash", player.getPassword());
        verify(playerRepository).update(player);
    }

    @Test
    void update_asAdmin_canPromotePlayer() {
        Player updated = new Player(1, "Nicki", "nicki@test.dk", "ignored", Role.ADMIN, CollectionVisibility.TRADE_ONLY);
        when(playerRepository.findById(1)).thenReturn(player);

        playerService.update(updated, null, true, false);

        assertEquals(Role.ADMIN, player.getRole());
        verify(playerRepository).update(player);
    }

    // --- Slet ---

    @Test
    void delete_withExistingPlayer_deletesPlayer() {
        when(playerRepository.findById(1)).thenReturn(player);
        when(playerRepository.findAll()).thenReturn(List.of(player, admin));

        playerService.delete(1);

        verify(playerRepository).delete(1);
    }

    @Test
    void delete_withNonExistingPlayer_throwsNotFoundException() {
        when(playerRepository.findById(99)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> playerService.delete(99));
        verify(playerRepository, never()).delete(anyInt());
    }

    @Test
    void delete_onlyAdmin_throwsValidationException() {
        when(playerRepository.findById(2)).thenReturn(admin);
        when(playerRepository.findAll()).thenReturn(List.of(admin));

        assertThrows(ValidationException.class, () -> playerService.delete(2));
        verify(playerRepository, never()).delete(anyInt());
    }

    @Test
    void isOnlyAdmin_returnsTrue_whenOnlyAdmin() {
        when(playerRepository.findAll()).thenReturn(List.of(admin));
        when(playerRepository.findById(2)).thenReturn(admin);

        assertTrue(playerService.isOnlyAdmin(2));
    }

    @Test
    void isOnlyAdmin_returnsFalse_whenMoreAdminsExist() {
        Player secondAdmin = new Player(3, "Admin2", "admin2@test.dk", "hashed-password", Role.ADMIN, CollectionVisibility.PUBLIC);
        when(playerRepository.findAll()).thenReturn(List.of(admin, secondAdmin));

        assertFalse(playerService.isOnlyAdmin(2));
    }

    // --- Samling ---

    @Test
    void addToCollection_withValidData_savesPlayerCard() {
        when(playerRepository.findById(1)).thenReturn(player);

        playerService.addToCollection(1, 5);

        ArgumentCaptor<PlayerCard> captor = ArgumentCaptor.forClass(PlayerCard.class);
        verify(playerCardRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getPlayerId());
        assertEquals(5, captor.getValue().getCardId());
        assertEquals(1, captor.getValue().getQuantity());
        assertFalse(captor.getValue().isForTrade());
    }

    @Test
    void addToCollection_playerNotFound_throwsNotFoundException() {
        when(playerRepository.findById(1)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> playerService.addToCollection(1, 1));
        verify(playerCardRepository, never()).save(any());
    }

    @Test
    void removeFromCollection_withValidId_deletesPlayerCard() {
        playerService.removeFromCollection(10);

        verify(playerCardRepository).delete(10);
    }

    @Test
    void removeFromCollection_withInvalidId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> playerService.removeFromCollection(0));
    }

    // --- Adgang ---

    @Test
    void checkCollectionAccess_privateOtherPlayer_throwsAccessDenied() {
        Player privatePlayer = new Player(4, "Private", "private@test.dk", "hashed-password", Role.PLAYER, CollectionVisibility.PRIVATE);
        when(playerRepository.findById(4)).thenReturn(privatePlayer);

        assertThrows(AccessDeniedException.class, () -> playerService.checkCollectionAccess(4, false, false));
    }

    @Test
    void checkCollectionAccess_privateSelf_doesNotThrow() {
        Player privatePlayer = new Player(4, "Private", "private@test.dk", "hashed-password", Role.PLAYER, CollectionVisibility.PRIVATE);
        when(playerRepository.findById(4)).thenReturn(privatePlayer);

        assertDoesNotThrow(() -> playerService.checkCollectionAccess(4, true, false));
    }

    @Test
    void checkCollectionAccess_privateAdmin_doesNotThrow() {
        Player privatePlayer = new Player(4, "Private", "private@test.dk", "hashed-password", Role.PLAYER, CollectionVisibility.PRIVATE);
        when(playerRepository.findById(4)).thenReturn(privatePlayer);

        assertDoesNotThrow(() -> playerService.checkCollectionAccess(4, false, true));
    }
}