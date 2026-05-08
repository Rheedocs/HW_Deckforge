package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.CardType;
import dk.zealand.hw_deckforge.domain.enums.Color;
import dk.zealand.hw_deckforge.domain.enums.Rarity;

public class Card {
    private int id;
    private String name;
    private CardType cardType;
    private String setName;
    private Color color;
    private Rarity rarity;
    private String ruleText;
    private String imageUrl;

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

    public int getId() { return id; }
    public String getName() { return name; }
    public CardType getCardType() { return cardType; }
    public String getSetName() { return setName; }
    public Color getColor() { return color; }
    public Rarity getRarity() { return rarity; }
    public String getRuleText() { return ruleText; }
    public String getImageUrl() { return imageUrl; }

    public void setName(String name) { this.name = name; }
    public void setCardType(CardType cardType) { this.cardType = cardType; }
    public void setSetName(String setName) { this.setName = setName; }
    public void setColor(Color color) { this.color = color; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public void setRuleText(String ruleText) { this.ruleText = ruleText; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Card{id=" + id + ", name='" + name + "', cardType=" + cardType + ", color=" + color
                + ", rarity=" + rarity + ", setName='" + setName + "'}";
    }
}