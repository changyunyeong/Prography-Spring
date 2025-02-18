package prography.example.demo.global.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import prography.example.demo.domain.UserRoom.service.GameService;

@Component
@Slf4j
public class HealthCheckRunner {

    private final GameService gameService;

    public HealthCheckRunner(GameService gameService) {
        this.gameService = gameService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        gameService.checkHealth();
        log.info("Application is ready");
    }
}
