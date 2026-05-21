package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;

public class Deck {
    private Integer id;
    private int playerId;
    private String name;
    private Format format;
    private DeckVisibility visibility;

    public Deck(Integer id, int playerId, String name, Format format, DeckVisibility visibility) {
        validate(playerId, name, format, visibility);
        this.id = id;
        this.playerId = playerId;
        this.name = name;
        this.format = format;
        this.visibility = visibility;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public String getName() { return name; }
    public Format getFormat() { return format; }
    public DeckVisibility getVisibility() { return visibility; }

    // --- Setters ---

    public void setId(Integer id) { this.id = id; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Navn må ikke være tomt");
        this.name = name;
    }
    public void setFormat(Format format) { this.format = format; }
    public void setVisibility(DeckVisibility visibility) { this.visibility = visibility; }

    // --- Synlighed ---

    public boolean isPublic() { return this.visibility == DeckVisibility.PUBLIC; }
    public void makePublic() { this.visibility = DeckVisibility.PUBLIC; }
    public void makePrivate() { this.visibility = DeckVisibility.PRIVATE; }

    // --- Validering ---

    private void validate(int playerId, String name, Format format, DeckVisibility visibility) {
        if (playerId <= 0) throw new IllegalArgumentException("Ugyldigt spiller-id");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Navn må ikke være tomt");
        if (format == null) throw new IllegalArgumentException("Format må ikke være null");
        if (visibility == null) throw new IllegalArgumentException("Synlighed må ikke være null");
    }

    @Override
    public String toString() {
        return "Deck{id=" + id + ", playerId=" + playerId + ", name='" + name + "', format=" + format
                + ", visibility=" + visibility + "}";
    }
}