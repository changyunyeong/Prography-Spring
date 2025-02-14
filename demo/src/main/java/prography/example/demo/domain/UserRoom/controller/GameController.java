package prography.example.demo.domain.UserRoom.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import prography.example.demo.domain.UserRoom.service.GameService;
import prography.example.demo.global.apiPayLoad.ApiResponse;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/health")
    @Operation(summary = "서버의 상태 체크하는 API", description = "모든 시나리오에 대해 최초 1회 호출")
    public ApiResponse<Void> checkHealth() {
        gameService.checkHealth();
        return ApiResponse.success(null);
    }
}
