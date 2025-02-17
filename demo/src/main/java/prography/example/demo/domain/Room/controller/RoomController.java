package prography.example.demo.domain.Room.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
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
    @Operation(summary = "방 생성 API",
            description = "방을 생성하려고 하는 user(userId)의 상태가 활성(ACTIVE)상태일 때만, 방을 생성할 수 있음\n"
                + " 방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방을 생성할 수 없음")
    public ApiResponse<Void> createRoom (
            @RequestBody @Valid RoomRequestDTO.CreateRoomRequestDTO request) {
        Room room = roomService.createRoom(request);
        RoomConverter.toRoomResponseDTO(room);
        return ApiResponse.success(null);
    }

    @GetMapping("/room")
    @Operation(summary = "방 전체 조회 API", description = "모든 방에 대한 데이터를 반환")
    public ApiResponse<RoomResponseDTO.RoomPreViewListDTO> getRoomList (
            @RequestParam("size") int size, @RequestParam("page") int page) {
        Page<Room> roomList = roomService.getRoomList(size, page);
        return ApiResponse.success(RoomConverter.roomPreViewListDTO(roomList));
    }
}
