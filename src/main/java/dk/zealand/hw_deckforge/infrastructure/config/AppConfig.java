package dk.zealand.hw_deckforge.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Konfigurerer BCryptPasswordEncoder som Spring Bean til brug i PlayerService. */
@Configuration
public class AppConfig {

    /** Spring Bean til HTTP-kald. Bruges af ScryfallService til at hente kortdata fra Scryfall API. */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /** Spring Bean til JSON-parsing af Scryfall API-svar. */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}