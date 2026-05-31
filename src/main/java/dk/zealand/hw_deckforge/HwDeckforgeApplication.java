package dk.zealand.hw_deckforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Applikationens indgangspunkt. @EnableScheduling aktiverer de to @Scheduled jobs i TradeService og EventService. */
@SpringBootApplication
@EnableScheduling
public class HwDeckforgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwDeckforgeApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}