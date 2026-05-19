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
    private boolean active;

    public Player() {}

    public Player(int id, String username, String email, String password, Role role, CollectionVisibility collectionVisibility) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.collectionVisibility = collectionVisibility;
        this.active = true;
    }

    // --- Getters ---

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public CollectionVisibility getCollectionVisibility() { return collectionVisibility; }
    public boolean isActive() { return active; }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }
    public void setCollectionVisibility(CollectionVisibility collectionVisibility) {
        this.collectionVisibility = collectionVisibility;
    }
    public void setActive(boolean active) { this.active = active; }

    // --- Adfærd ---

    public void deactivate() {
        this.active = false;
    }

    public void changeVisibility(CollectionVisibility visibility) {
        if (visibility == null) throw new IllegalArgumentException("Synlighed må ikke være null");
        this.collectionVisibility = visibility;
    }

    public void changePassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank())
            throw new IllegalArgumentException("Password må ikke være tomt");
        this.password = hashedPassword;
    }

    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }

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
                + ", password='[hidden]', active=" + active + "}";
    }
}
