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

    public boolean isCollectionVisible(CollectionVisibility visibility) {
        return this.collectionVisibility == visibility;
    }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setCollectionVisibility(CollectionVisibility collectionVisibility) {
        this.collectionVisibility = collectionVisibility; }

    @Override
    public String toString() {
        return "Player{id=" + id + ", username='" + username + "', email='" + email + "', role=" + role
                + ", password='[hidden]'}";
    }
}