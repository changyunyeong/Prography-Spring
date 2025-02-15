package prography.example.demo.domain.User.dto.request;

import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class InitUserRequestDTO {

        private int seed;
        private int quantity;
    }
}
