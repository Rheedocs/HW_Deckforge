package dk.zealand.hw_deckforge.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Pattern;

/**
 * Henter kortbilleder fra Scryfall API.
 * Placeret i infrastructure-laget da det er ekstern datahåndtering.
 */
@Component
public class ScryfallService {

    private static final String SCRYFALL_API_HOST = "api.scryfall.com";
    private static final String SET_PATTERN = "[a-zA-Z0-9]+";
    private static final String NUMBER_PATTERN = "[0-9]+";
    private static final Pattern SCRYFALL_CARD_URL_PATTERN = Pattern.compile(
            "^https://scryfall\\.com/card/[a-zA-Z0-9]+/[0-9]+(?:/.*)?$");

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ScryfallService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // --- Billedopslag ---

    public String fetchImageUrlByScryfallLink(String scryfallLink) {
        if (scryfallLink == null) return null;
        if (!SCRYFALL_CARD_URL_PATTERN.matcher(scryfallLink).matches()) return null;
        try {
            URI uri = URI.create(scryfallLink);
            String[] parts = uri.getPath().split("/");
            if (parts.length < 4) return null;
            String set = parts[2];
            String number = parts[3];
            if (!isValidSet(set) || !isValidNumber(number)) return null;
            return fetchImageUrlBySetAndNumber(set, number);
        } catch (Exception e) {
            return null;
        }
    }

    public String fetchImageUrlBySetAndNumber(String set, String number) {
        if (!isValidSet(set) || !isValidNumber(number)) return null;
        try {
            URI uri = new URI("https", SCRYFALL_API_HOST, "/cards/" + set + "/" + number, null);
            String json = restTemplate.getForObject(uri, String.class);
            if (json == null) return null;
            return extractImageUrl(json);
        } catch (Exception e) {
            return null;
        }
    }

    // --- Validering ---

    private boolean isValidSet(String set) {
        return set != null && set.matches(SET_PATTERN);
    }

    private boolean isValidNumber(String number) {
        return number != null && number.matches(NUMBER_PATTERN);
    }

    // --- Intern behandling ---

    /**
     * Double-faced kort har image_uris under card_faces[0] i stedet for roden.
     */
    private String extractImageUrl(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode imageUris = root.path("image_uris");
            if (!imageUris.isMissingNode()) {
                return imageUris.path("normal").asText(null);
            }
            JsonNode cardFaces = root.path("card_faces");
            if (!cardFaces.isMissingNode() && cardFaces.isArray()) {
                JsonNode firstFace = cardFaces.get(0);
                JsonNode faceImageUris = firstFace.path("image_uris");
                return faceImageUris.path("normal").asText(null);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
