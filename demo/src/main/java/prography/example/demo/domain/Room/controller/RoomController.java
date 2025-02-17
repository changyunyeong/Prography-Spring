package prography.example.demo.domain.Room.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prography.example.demo.domain.Room.converter.RoomConverter;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.Room.service.RoomService;
import prography.example.demo.global.apiPayLoad.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    @Operation(summary = "방 생성 API",
            description = "방을 생성하려고 하는 user(userId)의 상태가 활성(ACTIVE)상태일 때만, 방을 생성할 수 있음\n"
                + " 방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방을 생성할 수 없음")
    public ApiResponse<Void> createRoom (
            @RequestBody @Valid RoomRequestDTO.CreateRoomRequestDTO request) {
        Room room = roomService.createRoom(request);
        RoomConverter.toRoomResponseDTO(room);
        return ApiResponse.success(null);
    }

}
