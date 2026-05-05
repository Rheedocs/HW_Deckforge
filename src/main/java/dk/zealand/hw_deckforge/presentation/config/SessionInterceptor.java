package dk.zealand.hw_deckforge.presentation.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor der kører før hver request og sikrer at brugeren er logget ind.
 * Hvis sessionen ikke indeholder et player-objekt, sendes brugeren til /access-denied.
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("player") == null) {
            response.sendRedirect("/access-denied");
            return false;
        }
        return true;
    }
}