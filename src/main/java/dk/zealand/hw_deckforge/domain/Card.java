package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;

public class Card {
    private Integer id;
    private String name;
    private CardType cardType;
    private String setName;
    private Color color;
    private Rarity rarity;
    private String ruleText;
    private String imageUrl;

    public Card(){}

    public Card(int id, String name, CardType cardType, String setName, Color color, Rarity rarity, String ruleText, String imageUrl) {
        this.id = id;
        this.name = name;
        this.cardType = cardType;
        this.setName = setName;
        this.color = color;
        this.rarity = rarity;
        this.ruleText = ruleText;
        this.imageUrl = imageUrl;
    }

    // --- Getters ---

    public Integer getId() { return id; }
    public String getName() { return name; }
    public CardType getCardType() { return cardType; }
    public String getSetName() { return setName; }
    public Color getColor() { return color; }
    public Rarity getRarity() { return rarity; }
    public String getRuleText() { return ruleText; }
    public String getImageUrl() { return imageUrl; }

    // --- Adfærd ---

    public String getScryfallUrl() {
        if (name == null || name.isBlank()) return "";
        return "https://scryfall.com/search?q=%21%22" + name.replace(" ", "+") + "%22&unique=cards";
    }
    public boolean hasImage(){ return imageUrl != null && !imageUrl.isBlank();}

    // --- Setters ---

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Navn må ikke være tomt");
        this.name = name;
    }
    public void setId(int id){this.id = id;}
    public void setCardType(CardType cardType) { this.cardType = cardType; }
    public void setSetName(String setName) { this.setName = setName; }
    public void setColor(Color color) { this.color = color; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public void setRuleText(String ruleText) { this.ruleText = ruleText; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // --- Hjælpemetoder ---

    @Override
    public String toString() {
        return "Card{id=" + id + ", name='" + name + "', cardType=" + cardType + ", color=" + color
                + ", rarity=" + rarity + ", setName='" + setName + "'}";
    }
}