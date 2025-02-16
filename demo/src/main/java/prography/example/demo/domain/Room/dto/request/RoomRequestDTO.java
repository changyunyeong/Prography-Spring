package prography.example.demo.domain.Room.dto.request;

import lombok.Getter;
import prography.example.demo.global.common.enums.RoomType;

public class RoomRequestDTO {

    @Getter
    public static class CreateRoomRequestDTO {
        int userId;
        RoomType roomType;
        String title;
    }
}
