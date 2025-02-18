package prography.example.demo.global.runner;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import prography.example.demo.domain.UserRoom.service.GameService;

@Component
public class HealthCheckRunner {

    private final GameService gameService;

    public HealthCheckRunner(GameService gameService) {
        this.gameService = gameService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        gameService.checkHealth();
    }
}
