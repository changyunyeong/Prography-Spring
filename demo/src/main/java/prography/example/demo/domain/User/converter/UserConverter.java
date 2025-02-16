package prography.example.demo.domain.User.converter;

import prography.example.demo.domain.User.dto.response.UserResponseDTO;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.global.common.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static User toUser(UserResponseDTO.UserListResponse.UserDTO user) {

        return User.builder()
                .fakerId(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .status(determineUserStatus(user.getId()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static List<User> toUser(List<UserResponseDTO.UserListResponse.UserDTO> userList) {

        return userList.stream()
                .map(UserConverter::toUser)
                .collect(Collectors.toList());
    }

    private static UserStatus determineUserStatus(Integer fakerId) {

        if (fakerId <= 30) {
            return UserStatus.ACTIVE;
        } else if (fakerId <= 60) {
            return UserStatus.WAIT;
        } else {
            return UserStatus.NON_ACTIVE;
        }
    }
}
