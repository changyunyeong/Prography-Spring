package prography.example.demo.domain.UserRoom.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
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

    @PutMapping("/room/start/{roomId}")
    @Operation(summary = "방 시작 API", description = "1분 뒤 종료")
    public ApiResponse<Void> outRoom(
            @PathVariable("roomId") Integer roomId, @RequestBody @Valid RoomRequestDTO.RoomActionDTO request) {
        gameService.startGame(roomId, request.getUserId());
        return ApiResponse.success(null);
    }

    @PutMapping("/team/{roomId}")
    @Operation(summary = "팀 변경 API", description = "반대 팀으로 변경")
    public ApiResponse<Void> changeRoom(
            @PathVariable("roomId") Integer roomId, @RequestBody @Valid RoomRequestDTO.RoomActionDTO request) {
        gameService.changeTeam(roomId, request.getUserId());
        return ApiResponse.success(null);
    }
}
