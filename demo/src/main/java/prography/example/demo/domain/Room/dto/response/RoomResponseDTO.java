package prography.example.demo.domain.Room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prography.example.demo.global.common.enums.RoomStatus;
import prography.example.demo.global.common.enums.RoomType;

import java.time.LocalDateTime;
import java.util.List;

public class RoomResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoomResponseDTO {
        private Integer id;
        private Integer hostId;
        private RoomStatus status;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomPreViewDTO {
        private Integer id;
        private String title;
        private Integer hostId;
        private RoomType roomType;
        private RoomStatus status;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomPreViewListDTO {
        private List<RoomPreViewDTO> roomList;
        private Integer totalElements;
        private Integer totalPages;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDetailDTO {
        private Integer id;
        private String title;
        private Integer hostId;
        private RoomType roomType;
        private RoomStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
