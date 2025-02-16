package prography.example.demo.domain.Room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prography.example.demo.global.common.enums.RoomStatus;

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
}
