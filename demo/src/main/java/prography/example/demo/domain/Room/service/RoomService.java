package prography.example.demo.domain.Room.service;

import org.springframework.data.domain.Page;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.dto.response.RoomResponseDTO;
import prography.example.demo.domain.Room.entity.Room;

public interface RoomService {

    Room createRoom(RoomRequestDTO.CreateRoomRequestDTO request);
    Page<Room> getRoomList(int size, int page);
    RoomResponseDTO.RoomDetailDTO getRoomDetail(Integer roomId);
    void attentionRoom(Integer roomId, Integer userId);
}
