package prography.example.demo.domain.User.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.converter.UserConverter;
import prography.example.demo.domain.User.dto.response.UserResponseDTO;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.User.repository.UserRepository;
import prography.example.demo.global.apiPayLoad.code.status.ErrorStatus;
import prography.example.demo.global.apiPayLoad.exception.GeneralException;
import prography.example.demo.global.common.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RestTemplate restTemplate;

    @Value("${faker.api.url}")
    private String FAKER_API_URL;

    @Override
    public void initUser(int seed, int quantity) {

        // 기존에 있던 모든 회원 정보 및 방 정보를 삭제
        roomRepository.deleteAll();
        userRepository.deleteAll();

        Map<String, Object> uriParams = new HashMap<>();
        uriParams.put("seed", seed);
        uriParams.put("quantity", quantity);

//        String url = String.format(FAKER_API_URL, seed, quantity);

        String url = UriComponentsBuilder.fromUriString(FAKER_API_URL)
                .buildAndExpand(uriParams)
                .toUriString();

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

        List<User> users = UserConverter.toUser(userList);

        userRepository.saveAll(users);
    }

    @Override
    public Page<User> getUserList(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<User> userList = userRepository.findAll(pageRequest);

        return userList;
    }
}