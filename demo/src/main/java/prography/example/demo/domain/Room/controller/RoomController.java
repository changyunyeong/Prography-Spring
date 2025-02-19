package prography.example.demo.domain.Room.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    @Operation(summary = "방 생성 API",
            description = "방을 생성하려고 하는 user(userId)의 상태가 활성(ACTIVE)상태일 때만, 방을 생성할 수 있음\n"
                    + " 방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방을 생성할 수 없음")
    public ApiResponse<Void> createRoom(
            @RequestBody @Valid RoomRequestDTO.CreateRoomRequestDTO request) {
        Room room = roomService.createRoom(request);
        RoomConverter.toRoomResponseDTO(room);
        return ApiResponse.success(null);
    }

    @GetMapping("/room")
    @Operation(summary = "방 전체 조회 API", description = "모든 방에 대한 데이터를 반환")
    public ApiResponse<RoomResponseDTO.RoomPreViewListDTO> getRoomList(
            @RequestParam("size") int size, @RequestParam("page") int page) {
        Page<Room> roomList = roomService.getRoomList(size, page);
        return ApiResponse.success(RoomConverter.roomPreViewListDTO(roomList));
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "방 상세 조회 API", description = "roomId를 받아 방에 대한 상세 조회")
    public ApiResponse<RoomResponseDTO.RoomDetailDTO> getRoomDetail(
            @PathVariable("roomId") Integer roomId) {
        RoomResponseDTO.RoomDetailDTO roomDetail = roomService.getRoomDetail(roomId);
        return ApiResponse.success(roomDetail);
    }

    @PostMapping("/room/attention/{roomId}")
    @Operation(summary = "방 참가 API")
    public ApiResponse<Void> attentionRoom(
            @PathVariable("roomId") Integer roomId, @RequestBody @Valid RoomRequestDTO.RoomActionDTO request) {
        roomService.attentionRoom(roomId, request.getUserId());
        return ApiResponse.success(null);
    }

    @PostMapping("/room/out/{roomId}")
    @Operation(summary = "방 나가기 API", description = "호스트가 방을 나가게 되면 방에 있던 모든 사람도 해당 방에서 나가게 됨")
    public ApiResponse<Void> outRoom(
            @PathVariable("roomId") Integer roomId, @RequestBody @Valid RoomRequestDTO.RoomActionDTO request) {
        roomService.outRoom(roomId, request.getUserId());
        return ApiResponse.success(null);
    }
}
