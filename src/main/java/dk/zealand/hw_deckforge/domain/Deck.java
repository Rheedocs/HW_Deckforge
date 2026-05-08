package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.DeckVisibility;
import dk.zealand.hw_deckforge.domain.enums.Format;

public class Deck {
    private int id;
    private int playerId;
    private String name;
    private Format format;
    private DeckVisibility visibility;

    public Deck(int id, int playerId, String name, Format format, DeckVisibility visibility) {
        this.id = id;
        this.playerId = playerId;
        this.name = name;
        this.format = format;
        this.visibility = visibility;
    }

    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public String getName() { return name; }
    public Format getFormat() { return format; }
    public DeckVisibility getVisibility() { return visibility; }

    public void setName(String name) { this.name = name; }
    public void setFormat(Format format) { this.format = format; }
    public void setVisibility(DeckVisibility visibility) { this.visibility = visibility; }
}