package prography.example.demo.domain.User.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.dto.response.UserResponseDTO;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.User.repository.UserRepository;
import prography.example.demo.global.apiPayLoad.code.status.ErrorStatus;
import prography.example.demo.global.apiPayLoad.exception.GeneralException;
import prography.example.demo.global.common.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RestTemplate restTemplate;

    private static final String FAKER_API_URL = "https://fakerapi.it/api/v1/users?_seed=%d&_quantity=%d&_locale=ko_KR";

    @Override
    public void initUser(int seed, int quantity) {

        // 기존에 있던 모든 회원 정보 및 방 정보를 삭제
        roomRepository.deleteAll();
        userRepository.deleteAll();

        String url = String.format(FAKER_API_URL, seed, quantity);
        ResponseEntity<UserResponseDTO.UserListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() == null || response.getBody().getData() == null) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }

        List<UserResponseDTO.UserListResponse.UserDTO> userList = response.getBody().getData()
                .stream()
                .sorted(Comparator.comparing(UserResponseDTO.UserListResponse.UserDTO::getId))
                .collect(Collectors.toList());

        List<User> users = userList.stream()
                .map(this::convertToUserEntity)
                .collect(Collectors.toList());

        userRepository.saveAll(users);
    }

    private User convertToUserEntity(UserResponseDTO.UserListResponse.UserDTO user) {

        return User.builder()
                .fakerId(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .status(determineUserStatus(user.getId()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private UserStatus determineUserStatus(Integer fakerId) {

        if (fakerId <= 30) {
            return UserStatus.ACTIVE;
        } else if (fakerId <= 60) {
            return UserStatus.WAIT;
        } else {
            return UserStatus.NON_ACTIVE;
        }
    }
}