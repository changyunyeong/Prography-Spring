package prography.example.demo.domain.User.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.dto.response.UserResponseDTO;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.User.repository.UserRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestTemplate restTemplate;
    private String FAKER_API_URL = "https://fakerapi.it/api/v1/users";

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, roomRepository, restTemplate);
        ReflectionTestUtils.setField(userService, "FAKER_API_URL", "https://fakerapi.it/api/v1/users?_seed={seed}&_quantity={quantity}&_locale=ko_KR");
    }

    @Test
    void 유저초기화_성공() {
        int seed = 123;
        int quantity = 10;

        List<UserResponseDTO.UserListResponse.UserDTO> mockUserList = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            mockUserList.add(UserResponseDTO.UserListResponse.UserDTO.builder()
                    .id(i) // ✅ fakerId 설정
                    .username("User" + i)
                    .email("user" + i + "@example.com")
                    .build());
        }

        UserResponseDTO.UserListResponse mockResponse = UserResponseDTO.UserListResponse.builder()
                .status("success")
                .code(200)
                .locale("en")
                .seed(String.valueOf(seed))
                .total(quantity)
                .data(mockUserList)
                .build();

        ResponseEntity<UserResponseDTO.UserListResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        URI expectedUri = UriComponentsBuilder.fromUriString(FAKER_API_URL)
                .queryParam("_seed", seed)
                .queryParam("_quantity", quantity)
                .queryParam("_locale", "ko_KR")
                .build()
                .toUri();

        when(restTemplate.exchange(
                eq(expectedUri.toString()),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        when(userRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        userService.initUser(seed, quantity);

        verify(roomRepository, times(1)).deleteAll();
        verify(userRepository, times(1)).deleteAll();

        ArgumentCaptor<List<User>> userCaptor = ArgumentCaptor.forClass(List.class);
        verify(userRepository, times(1)).saveAll(userCaptor.capture());

        List<User> savedUsers = userCaptor.getValue();
        assertEquals(quantity, savedUsers.size());

        for (int i = 0; i < quantity; i++) {
            assertEquals(i + 1, savedUsers.get(i).getFakerId());
            assertEquals("User" + (i + 1), savedUsers.get(i).getName());
            assertEquals("user" + (i + 1) + "@example.com", savedUsers.get(i).getEmail());
        }
    }

}