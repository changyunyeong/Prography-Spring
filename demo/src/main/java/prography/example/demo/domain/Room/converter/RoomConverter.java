package prography.example.demo.domain.Room.converter;

import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.dto.response.RoomResponseDTO;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.global.common.enums.RoomStatus;

import java.time.LocalDateTime;

public class RoomConverter {

    public static Room toRoom(RoomRequestDTO.CreateRoomRequestDTO room, User user) {

        return Room.builder()
                .host(user)
                .roomType(room.getRoomType())
                .status(RoomStatus.WAIT)
                .title(room.getTitle())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static RoomResponseDTO.CreateRoomResponseDTO toRoomResponseDTO(Room room) {

        return RoomResponseDTO.CreateRoomResponseDTO.builder()
                .id(room.getId())
                .hostId(room.getHost().getId())
                .status(room.getStatus())
                .build();
    }
}