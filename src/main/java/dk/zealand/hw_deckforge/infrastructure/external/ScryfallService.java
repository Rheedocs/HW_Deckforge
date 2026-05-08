package dk.zealand.hw_deckforge.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Henter kortbilleder fra Scryfall API.
 * Placeret i infrastructure-laget da det er ekstern datahåndtering.
 */
@Component
public class ScryfallService {

    private static final String API_URL = "https://api.scryfall.com/cards/named?fuzzy=";
    private static final String IMAGE_URL = "https://cards.scryfall.io/normal/front/";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String fetchImageUrl(String cardName) {
        String json = fetchCardJson(cardName);
        if (json == null) return null;
        String id = extractScryfallId(json);
        if (id == null) return null;
        return buildImageUrl(id);
    }

    private String fetchCardJson(String cardName) {
        try {
            return restTemplate.getForObject(API_URL + cardName.replace(" ", "+"), String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractScryfallId(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return root.path("id").asText(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Scryfall billed-URL bygges fra kortets ID efter mønsteret:
     * /normal/front/{1. tegn}/{2. tegn}/{fuldt id}.jpg
     */
    private String buildImageUrl(String id) {
        return IMAGE_URL + id.charAt(0) + "/" + id.charAt(1) + "/" + id + ".jpg";
    }
}