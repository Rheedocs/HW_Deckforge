package dk.zealand.hw_deckforge.presentation.config;

import dk.zealand.hw_deckforge.domain.Player;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("player") == null) {
            response.sendRedirect("/login?required=true");
            return false;
        }
        Player player = (Player) session.getAttribute("player");
        if (!player.isActive()) {
            session.invalidate();
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
