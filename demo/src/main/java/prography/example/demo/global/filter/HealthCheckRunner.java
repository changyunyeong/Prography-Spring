package prography.example.demo.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import prography.example.demo.domain.UserRoom.service.GameService;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HealthCheckFilter extends OncePerRequestFilter {

    private final GameService gameService;
    private final AtomicBoolean healthChecked = new AtomicBoolean(false);

    public HealthCheckFilter(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!healthChecked.get()) {
            synchronized (this) {
                if (!healthChecked.get()) {  // 최초 1회만 실행
                    gameService.checkHealth();
                    healthChecked.set(true);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

