package prography.example.demo.domain.User.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
