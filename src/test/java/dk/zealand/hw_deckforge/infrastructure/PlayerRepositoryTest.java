package dk.zealand.hw_deckforge.infrastructure;

import dk.zealand.hw_deckforge.domain.Player;
import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(PlayerRepository.class)
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository repository;

    @Test
    void findAll_returnsNonEmptyList() {
        List<Player> players = repository.findAll();
        assertFalse(players.isEmpty());
    }

    @Test
    void findAll_containsKnownPlayer() {
        List<Player> players = repository.findAll();
        boolean found = false;
        for (Player p : players) {
            if (p.getUsername().equals("admin")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void findById_existingId_returnsPlayer() {
        Player player = repository.findById(1);
        assertNotNull(player);
        assertEquals("admin", player.getUsername());
    }

    @Test
    void findById_nonExistingId_returnsNull() {
        Player player = repository.findById(999);
        assertNull(player);
    }

    @Test
    void findByEmail_existingEmail_returnsPlayer() {
        Player player = repository.findByEmail("goncalo@deckforge.dk");
        assertNotNull(player);
        assertEquals("goncalo", player.getUsername());
    }

    @Test
    void findByEmail_nonExistingEmail_returnsNull() {
        Player player = repository.findByEmail("ingen@deckforge.dk");
        assertNull(player);
    }

    @Test
    void save_newPlayer_canBeFoundAfterwards() {
        Player player = new Player(0, "nyspiller", "ny@deckforge.dk", "encoded_password",
                Role.PLAYER, CollectionVisibility.TRADE_ONLY);
        repository.save(player);

        Player found = repository.findByEmail("ny@deckforge.dk");
        assertNotNull(found);
        assertEquals("nyspiller", found.getUsername());
    }

    @Test
    void update_existingPlayer_updatesCorrectly() {
        Player player = repository.findById(2);
        player.setUsername("opdateretbrugernavn");
        repository.update(player);

        Player updated = repository.findById(2);
        assertEquals("opdateretbrugernavn", updated.getUsername());
    }

    @Test
    void delete_existingPlayer_isDeactivated() {
        repository.delete(6);
        Player deleted = repository.findById(6);
        assertNotNull(deleted);
        assertFalse(deleted.isActive());
    }
}