package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.CollectionVisibility;
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

    public void setName(String name) {
        if (name == null){
            throw new IllegalArgumentException("Navn må ikke være tom");
        }
        this.name = name;
    }
    public void setFormat(Format format) { this.format = format; }
    public void setVisibility(DeckVisibility visibility) { this.visibility = visibility; }

    public void setPlayerId(int playerId){
        if (playerId <= 0){
            throw new IllegalArgumentException("playerId må ikke være null");
        }
        this.playerId = playerId;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id må ikke være null");
        }
        this.id = id;
    }

    public boolean isPublic(){
        return this.visibility == DeckVisibility.PUBLIC;
    }
    public void makePublic(){
        this.visibility = DeckVisibility.PUBLIC;
    }
    public void makePrivate(){
        this.visibility = DeckVisibility.PRIVATE;
    }

    public void changeVisibility(DeckVisibility visibility) {
        if (visibility == null) throw new IllegalArgumentException("Synlighed må ikke være null");
        this.visibility = visibility;
    }


    @Override
    public String toString(){
        return "id=" + id + ", Player{id='" + playerId + "', name='" + name + "', Format" + format
                + "Visibility" + visibility;
    }




}