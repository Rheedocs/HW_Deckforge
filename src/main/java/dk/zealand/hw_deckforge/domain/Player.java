package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
import dk.zealand.hw_deckforge.domain.enums.Role;

public class Player {
    private int id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private CollectionVisibility collectionVisibility;

    public Player() {}

    public Player(int id, String username, String email, String password, Role role, CollectionVisibility collectionVisibility) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.collectionVisibility = collectionVisibility;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public CollectionVisibility getCollectionVisibility() { return collectionVisibility; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }

    /**
     * Ændrer samlingssynlighed. Null er ikke tilladt.
     */
    public void changeVisibility(CollectionVisibility visibility) {
        if (visibility == null) throw new IllegalArgumentException("Synlighed må ikke være null");
        this.collectionVisibility = visibility;
    }

    /**
     * Sætter et nyt BCrypt-hashet password. Bruges af service-laget efter encoding.
     */
    public void changePassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank())
            throw new IllegalArgumentException("Password må ikke være tomt");
        this.password = hashedPassword;
    }

    /**
     * Giver admin-rolle. Kan kun tildeles af service-laget.
     */
    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }

    /**
     * Fjerner admin-rolle og sætter tilbage til PLAYER.
     */
    public void demoteToPlayer() {
        this.role = Role.PLAYER;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isCollectionVisible(CollectionVisibility visibility) {
        return this.collectionVisibility == visibility;
    }

    @Override
    public String toString() {
        return "Player{id=" + id + ", username='" + username + "', email='" + email + "', role=" + role
                + ", password='[hidden]'}";
    }
}