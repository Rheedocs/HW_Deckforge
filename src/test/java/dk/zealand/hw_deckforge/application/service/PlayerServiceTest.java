package dk.zealand.hw_deckforge.application.service;

import dk.zealand.hw_deckforge.application.interfaces.IPlayerRepository;
import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private IPlayerRepository playerRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(1, "TestSpiller", "test@mail.dk", "encoded_password",
                Role.PLAYER, CollectionVisibility.TRADE_ONLY);
    }

    // getById
    @Test
    void getById_ifValidId_returnsPlayer() {
        when(playerRepository.findById(1)).thenReturn(player);
        Player result = playerService.getById(1);
        assertEquals(player, result);
    }

    @Test
    void getById_ifPlayerNotFound_throwsIllegalArgumentException() {
        when(playerRepository.findById(99)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> playerService.getById(99));
    }

    @Test
    void getById_ifIdIsZero_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> playerService.getById(0));
    }

    // getAll
    @Test
    void getAll_returnsAllPlayers() {
        when(playerRepository.findAll()).thenReturn(List.of(player));
        List<Player> result = playerService.getAll();
        assertEquals(1, result.size());
    }

    // login
    @Test
    void login_withCorrectCredentials_returnsPlayer() {
        when(playerRepository.findByEmail("test@mail.dk")).thenReturn(player);
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
        Player result = playerService.login("test@mail.dk", "password123");
        assertEquals(player, result);
    }

    @Test
    void login_withWrongPassword_throwsIllegalArgumentException() {
        when(playerRepository.findByEmail("test@mail.dk")).thenReturn(player);
        when(passwordEncoder.matches("forkert", "encoded_password")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> playerService.login("test@mail.dk", "forkert"));
    }

    @Test
    void login_withUnknownEmail_throwsIllegalArgumentException() {
        when(playerRepository.findByEmail("ingen@mail.dk")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> playerService.login("ingen@mail.dk", "password123"));
    }

    // create
    @Test
    void create_withValidData_savesPlayer() {
        when(playerRepository.findByEmail("test@mail.dk")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        playerService.create("TestSpiller", "test@mail.dk", "password123");
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void create_withExistingEmail_throwsIllegalArgumentException() {
        when(playerRepository.findByEmail("test@mail.dk")).thenReturn(player);
        assertThrows(IllegalArgumentException.class,
                () -> playerService.create("TestSpiller", "test@mail.dk", "password123"));
    }

    @Test
    void create_withShortPassword_throwsIllegalArgumentException() {
        when(playerRepository.findByEmail("test@mail.dk")).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> playerService.create("TestSpiller", "test@mail.dk", "kort"));
    }

    @Test
    void create_withInvalidEmail_throwsIllegalArgumentException() {
        when(playerRepository.findByEmail("ikkeengyldigemail")).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> playerService.create("TestSpiller", "ikkeengyldigemail", "password123"));
    }

    // delete
    @Test
    void delete_withValidId_deletesPlayer() {
        when(playerRepository.findById(1)).thenReturn(player);
        playerService.delete(1);
        verify(playerRepository).delete(1);
    }

    @Test
    void delete_withInvalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> playerService.delete(0));
    }

    // isOnlyAdmin
    @Test
    void isOnlyAdmin_ifOneAdmin_returnsTrue() {
        player.promoteToAdmin();
        when(playerRepository.findAll()).thenReturn(List.of(player));
        when(playerRepository.findById(1)).thenReturn(player);
        assertTrue(playerService.isOnlyAdmin(1));
    }

    @Test
    void isOnlyAdmin_ifMultipleAdmins_returnsFalse() {
        Player otherAdmin = new Player(2, "AndenAdmin", "admin2@mail.dk", "encoded",
                Role.ADMIN, CollectionVisibility.TRADE_ONLY);
        player.promoteToAdmin();
        when(playerRepository.findAll()).thenReturn(List.of(player, otherAdmin));
        assertFalse(playerService.isOnlyAdmin(1));
    }

    @Test
    void isOnlyAdmin_ifPlayerIsNotAdmin_returnsFalse() {
        when(playerRepository.findAll()).thenReturn(List.of(player));
        assertFalse(playerService.isOnlyAdmin(1));
    }
}