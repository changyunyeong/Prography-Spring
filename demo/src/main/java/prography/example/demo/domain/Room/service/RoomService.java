package prography.example.demo.domain.Room.service;

import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.entity.Room;

public interface RoomService {

    Room createRoom(RoomRequestDTO.CreateRoomRequestDTO request);
}
