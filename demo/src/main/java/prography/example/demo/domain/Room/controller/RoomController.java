package prography.example.demo.domain.Room.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prography.example.demo.domain.Room.converter.RoomConverter;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.dto.response.RoomResponseDTO;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.Room.service.RoomService;
import prography.example.demo.global.apiPayLoad.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    public ApiResponse<Void> createRoom (
            @RequestBody @Valid RoomRequestDTO.CreateRoomRequestDTO request) {
        Room room = roomService.createRoom(request);
        RoomConverter.toRoomResponseDTO(room);
        return ApiResponse.success(null);
    }
}
