package dk.zealand.hw_deckforge.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScryfallServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ScryfallService scryfallService;

    private static final String NORMAL_IMAGE_JSON = """
            {"image_uris": {"normal": "https://cards.scryfall.io/normal/front/a/b/abc.jpg"}}
            """;

    private static final String DOUBLE_FACED_JSON = """
            {"card_faces": [{"image_uris": {"normal": "https://cards.scryfall.io/normal/front/a/b/abc.jpg"}}]}
            """;

    @BeforeEach
    void setUp() {
        scryfallService = new ScryfallService(restTemplate, new ObjectMapper());
    }

    // --- fetchImageUrlByScryfallLink ---

    @Test
    void fetchImageUrlByScryfallLink_validLink_returnsImageUrl() throws Exception {
        URI uri = new URI("https", "api.scryfall.com", "/cards/tla/4", null);
        when(restTemplate.getForObject(uri, String.class)).thenReturn(NORMAL_IMAGE_JSON);
        String result = scryfallService.fetchImageUrlByScryfallLink("https://scryfall.com/card/tla/4/aang-the-last-airbender");
        assertEquals("https://cards.scryfall.io/normal/front/a/b/abc.jpg", result);
    }

    @Test
    void fetchImageUrlByScryfallLink_invalidPrefix_returnsNull() {
        String result = scryfallService.fetchImageUrlByScryfallLink("https://evil.com/card/tla/4/aang");
        assertNull(result);
    }

    @Test
    void fetchImageUrlByScryfallLink_invalidSetChars_returnsNull() {
        String result = scryfallService.fetchImageUrlByScryfallLink("https://scryfall.com/card/tl!/4/aang");
        assertNull(result);
    }

    @Test
    void fetchImageUrlByScryfallLink_invalidNumberChars_returnsNull() {
        String result = scryfallService.fetchImageUrlByScryfallLink("https://scryfall.com/card/tla/4a/aang");
        assertNull(result);
    }

    @Test
    void fetchImageUrlByScryfallLink_tooShortLink_returnsNull() {
        String result = scryfallService.fetchImageUrlByScryfallLink("https://scryfall.com/card/tla");
        assertNull(result);
    }

    // --- fetchImageUrlBySetAndNumber ---

    @Test
    void fetchImageUrlBySetAndNumber_normalCard_returnsImageUrl() throws Exception {
        URI uri = new URI("https", "api.scryfall.com", "/cards/tla/4", null);
        when(restTemplate.getForObject(uri, String.class)).thenReturn(NORMAL_IMAGE_JSON);
        String result = scryfallService.fetchImageUrlBySetAndNumber("tla", "4");
        assertEquals("https://cards.scryfall.io/normal/front/a/b/abc.jpg", result);
    }

    @Test
    void fetchImageUrlBySetAndNumber_doubleFacedCard_returnsFirstFaceImageUrl() throws Exception {
        URI uri = new URI("https", "api.scryfall.com", "/cards/tla/4", null);
        when(restTemplate.getForObject(uri, String.class)).thenReturn(DOUBLE_FACED_JSON);
        String result = scryfallService.fetchImageUrlBySetAndNumber("tla", "4");
        assertEquals("https://cards.scryfall.io/normal/front/a/b/abc.jpg", result);
    }

    @Test
    void fetchImageUrlBySetAndNumber_apiReturnsNull_returnsNull() throws Exception {
        URI uri = new URI("https", "api.scryfall.com", "/cards/tla/4", null);
        when(restTemplate.getForObject(uri, String.class)).thenReturn(null);
        String result = scryfallService.fetchImageUrlBySetAndNumber("tla", "4");
        assertNull(result);
    }

    @Test
    void fetchImageUrlBySetAndNumber_invalidSet_returnsNull() {
        String result = scryfallService.fetchImageUrlBySetAndNumber("tl!", "4");
        assertNull(result);
    }

    @Test
    void fetchImageUrlBySetAndNumber_invalidNumber_returnsNull() {
        String result = scryfallService.fetchImageUrlBySetAndNumber("tla", "4a");
        assertNull(result);
    }

    @Test
    void fetchImageUrlBySetAndNumber_apiThrowsException_returnsNull() throws Exception {
        URI uri = new URI("https", "api.scryfall.com", "/cards/tla/4", null);
        when(restTemplate.getForObject(uri, String.class)).thenThrow(new RuntimeException("timeout"));
        String result = scryfallService.fetchImageUrlBySetAndNumber("tla", "4");
        assertNull(result);
    }
}