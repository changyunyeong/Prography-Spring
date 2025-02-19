package prography.example.demo.domain.User.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prography.example.demo.global.common.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserListResponse {
        private String status;
        private Integer code;
        private String locale;
        private String seed;
        private Integer total;
        private List<UserDTO> data;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UserDTO {
            private Integer id;
            private String username;
            private String email;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPreViewDTO {
        private Integer id;
        private Integer fakerId;
        private String name;
        private String email;
        private UserStatus userStatus;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPreViewListDTO {
        List<UserPreViewDTO> userList;
        long totalElements;
        Integer totalPage;
    }
}
