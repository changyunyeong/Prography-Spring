package prography.example.demo.domain.Room.converter;

import org.springframework.data.domain.Page;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.dto.response.RoomResponseDTO;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.global.common.enums.RoomStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static RoomResponseDTO.RoomPreViewDTO roomPreViewDTO(Room room) {

        return RoomResponseDTO.RoomPreViewDTO.builder()
                .id(room.getId())
                .title(room.getTitle())
                .hostId(room.getHost().getId())
                .roomType(room.getRoomType())
                .status(room.getStatus())
                .build();
    }

    public static RoomResponseDTO.RoomPreViewListDTO roomPreViewListDTO(Page<Room> roomList) {

        List<RoomResponseDTO.RoomPreViewDTO> roomPreViewDTOList = roomList.stream()
                .map(RoomConverter::roomPreViewDTO)
                .collect(Collectors.toList());

        return RoomResponseDTO.RoomPreViewListDTO.builder()
                .roomList(roomPreViewDTOList)
                .totalElements(roomList.getTotalPages())
                .totalPages(roomList.getTotalPages())
                .build();
    }

    public static RoomResponseDTO.RoomDetailDTO roomDetailViewDTO(Room room) {

        return RoomResponseDTO.RoomDetailDTO.builder()
                .id(room.getId())
                .title(room.getTitle())
                .hostId(room.getHost().getId())
                .roomType(room.getRoomType())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}