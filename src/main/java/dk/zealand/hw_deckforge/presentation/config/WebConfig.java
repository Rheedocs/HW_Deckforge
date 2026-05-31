package dk.zealand.hw_deckforge.presentation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Registrerer SessionInterceptor på alle ruter undtagen login, registrering og statiske ressourcer. */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SessionInterceptor sessionInterceptor;

    public WebConfig(SessionInterceptor sessionInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/login",
                        "/players/register",
                        "/events",
                        "/privacy",
                        "/about",
                        "/contact",
                        "/css/**",
                        "/images/**",
                        "/access-denied"
                );
    }
}