package prography.example.demo.domain.Room.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.User.repository.UserRepository;
import prography.example.demo.domain.UserRoom.entity.UserRoom;
import prography.example.demo.domain.UserRoom.repository.UserRoomRepository;
import prography.example.demo.global.apiPayLoad.code.status.ErrorStatus;
import prography.example.demo.global.apiPayLoad.exception.GeneralException;
import prography.example.demo.global.common.enums.RoomStatus;
import prography.example.demo.global.common.enums.RoomType;
import prography.example.demo.global.common.enums.Team;
import prography.example.demo.global.common.enums.UserStatus;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoomRepository userRoomRepository;

    private User user;
    private User host;
    private Room room;

    @BeforeEach
    void setUp() {
        roomService = new RoomServiceImpl(roomRepository, userRepository, userRoomRepository);
    }

    @Test
    void 방_생성_성공() {

        user = User.builder().id(1).name("Test User").status(UserStatus.ACTIVE).build();

        RoomRequestDTO.CreateRoomRequestDTO request = mock(RoomRequestDTO.CreateRoomRequestDTO.class);
        when(request.getUserId()).thenReturn(1);
        when(request.getRoomType()).thenReturn(RoomType.SINGLE);
        when(request.getTitle()).thenReturn("Test Room");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRoomRepository.findUserRoomsByUserId(user)).thenReturn(false);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(userRoomRepository.save(any(UserRoom.class))).thenReturn(mock(UserRoom.class));

        Room response = roomService.createRoom(request);

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(user);
        verify(roomRepository, times(1)).save(any(Room.class));
        verify(userRoomRepository, times(1)).save(any(UserRoom.class));

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Room");
        assertThat(response.getHost().getId()).isEqualTo(user.getId());
    }

    @Test
    void 방_생성_실패_비활성유저() {

        user = User.builder().id(1).name("Test User").status(UserStatus.NON_ACTIVE).build();

        RoomRequestDTO.CreateRoomRequestDTO request = mock(RoomRequestDTO.CreateRoomRequestDTO.class);
        when(request.getUserId()).thenReturn(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        GeneralException exception = assertThrows(GeneralException.class, () -> roomService.createRoom(request));
        assertThat(exception.getStatus()).isEqualTo(ErrorStatus.INVALID_REQUEST);

        verify(userRepository, times(1)).findById(1);
        verify(roomRepository, never()).save(any(Room.class));
        verify(userRoomRepository, never()).save(any(UserRoom.class));

    }

    @Test
    void 방_생성_실패_이미참가한유저() {

        user = User.builder().id(1).name("Test User").status(UserStatus.ACTIVE).build();

        RoomRequestDTO.CreateRoomRequestDTO request = mock(RoomRequestDTO.CreateRoomRequestDTO.class);
        when(request.getUserId()).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRoomRepository.findUserRoomsByUserId(user)).thenReturn(true);

        GeneralException exception = assertThrows(GeneralException.class,
                () -> roomService.createRoom(request));

        assertThat(exception.getStatus()).isEqualTo(ErrorStatus.INVALID_REQUEST);

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(user);
        verify(roomRepository, never()).save(any(Room.class));
        verify(userRoomRepository, never()).save(any(UserRoom.class));
    }

    @Test
    void 방_참여_성공() {
        host = User.builder().id(1).name("Host User").status(UserStatus.ACTIVE).build();
        user = User.builder().id(2).name("Test User").status(UserStatus.ACTIVE).build();
        room = Room.builder().id(1).title("Test Room").host(host).roomType(RoomType.SINGLE).status(RoomStatus.WAIT).build();

        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(userRoomRepository.findUserRoomsByUserId(user)).thenReturn(false);
        when(userRoomRepository.save(any(UserRoom.class))).thenReturn(mock(UserRoom.class));
        when(userRoomRepository.countUserRoomsByRoomId(1)).thenReturn(0);
        lenient().when(userRoomRepository.countByRoomIdAndTeam(1, Team.BLUE)).thenReturn(0);
        when(userRoomRepository.countByRoomIdAndTeam(1, Team.RED)).thenReturn(0);

        roomService.attentionRoom(1, 2);

        verify(userRepository, times(1)).findById(2);
        verify(roomRepository, times(1)).findById(1);
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(user);
        verify(userRoomRepository, times(1)).countUserRoomsByRoomId(1);
        verify(userRoomRepository, times(1)).countByRoomIdAndTeam(1, Team.RED);
        verify(userRoomRepository, times(1)).save(any(UserRoom.class));
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(user);
        verify(userRoomRepository, times(0)).countByRoomIdAndTeam(1, Team.BLUE);
    }


    @Test
    void 방_참여_실패_대기중이아닌방() {

        host = User.builder().id(1).name("Host User").status(UserStatus.ACTIVE).build();
        user = User.builder().id(2).name("Test User").status(UserStatus.ACTIVE).build();
        room = Room.builder().id(1).title("Test Room").host(host).roomType(RoomType.SINGLE).status(RoomStatus.FINISH).build();

        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(userRoomRepository.findUserRoomsByUserId(user)).thenReturn(false);
        when(userRoomRepository.countUserRoomsByRoomId(1)).thenReturn(1);

        GeneralException exception = assertThrows(GeneralException.class, () -> {
            roomService.attentionRoom(1, 2);
        });

        assertEquals(ErrorStatus.INVALID_REQUEST, exception.getStatus());

        verify(userRepository, times(1)).findById(2);
        verify(roomRepository, times(1)).findById(1);
        verify(userRoomRepository, times(1)).findUserRoomsByUserId(user);
        verify(userRoomRepository, never()).save(any(UserRoom.class));

    }

    @Test
    void 방_참가_실패_이미참여한유저() {

            host = User.builder().id(1).name("Host User").status(UserStatus.ACTIVE).build();
            user = User.builder().id(2).name("Test User").status(UserStatus.ACTIVE).build();
            room = Room.builder().id(1).title("Test Room").host(host).roomType(RoomType.SINGLE).status(RoomStatus.WAIT).build();

            when(userRepository.findById(2)).thenReturn(Optional.of(user));
            when(roomRepository.findById(1)).thenReturn(Optional.of(room));
            when(userRoomRepository.findUserRoomsByUserId(user)).thenReturn(true);

            GeneralException exception = assertThrows(GeneralException.class, () -> {
                roomService.attentionRoom(1, 2);
            });

            assertEquals(ErrorStatus.INVALID_REQUEST, exception.getStatus());

            verify(userRepository, times(1)).findById(2);
            verify(roomRepository, times(1)).findById(1);
    }

    @Test
    void 방_나가기_성공() {

        host = User.builder().id(1).name("Host User").status(UserStatus.ACTIVE).build();
        user = User.builder().id(2).name("Test User").status(UserStatus.ACTIVE).build();
        room = Room.builder().id(1).title("Test Room").host(host).roomType(RoomType.SINGLE).status(RoomStatus.WAIT).build();

        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(userRoomRepository.existsByUserIdAndRoomId(2, 1)).thenReturn(true);

        roomService.outRoom(1, 2);

        verify(roomRepository, times(1)).findById(1);
        verify(userRoomRepository, times(1)).existsByUserIdAndRoomId(2, 1);
        verify(userRoomRepository, times(1)).deleteByUserIdAndRoomId(2, 1);
        verify(roomRepository, never()).save(any(Room.class));

    }

    @Test
    void 방_나가기_실패_참가상태아님() {

        host = User.builder().id(1).name("Host User").status(UserStatus.ACTIVE).build();
        user = User.builder().id(2).name("Test User").status(UserStatus.ACTIVE).build();
        room = Room.builder()
                .id(1)
                .title("Test Room")
                .host(host)
                .roomType(RoomType.SINGLE)
                .status(RoomStatus.WAIT)
                .build();

        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(userRoomRepository.existsByUserIdAndRoomId(2, 1)).thenReturn(false); // 유저가 방에 참가하지 않은 상태

        GeneralException exception = assertThrows(GeneralException.class, () -> {
            roomService.outRoom(1, 2);
        });

        assertEquals(ErrorStatus.INVALID_REQUEST, exception.getStatus());

        verify(roomRepository, times(1)).findById(1);
        verify(userRoomRepository, times(1)).existsByUserIdAndRoomId(2, 1);
        verify(userRoomRepository, never()).deleteByUserIdAndRoomId(anyInt(), anyInt());
        verify(roomRepository, never()).save(any(Room.class));
    }


    @Test
    void 방_나가기_실패_대기중이아님() {

        host = User.builder().id(1).name("Host User").status(UserStatus.ACTIVE).build();
        user = User.builder().id(2).name("Test User").status(UserStatus.ACTIVE).build();
        room = Room.builder()
                .id(1)
                .title("Test Room")
                .host(host)
                .roomType(RoomType.SINGLE)
                .status(RoomStatus.PROGRESS)
                .build();

        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(userRoomRepository.existsByUserIdAndRoomId(2, 1)).thenReturn(true);

        GeneralException exception = assertThrows(GeneralException.class, () -> {
            roomService.outRoom(1, 2);
        });

        assertEquals(ErrorStatus.INVALID_REQUEST, exception.getStatus());

        verify(roomRepository, times(1)).findById(1);
        verify(userRoomRepository, times(1)).existsByUserIdAndRoomId(2, 1);
        verify(userRoomRepository, never()).deleteByUserIdAndRoomId(anyInt(), anyInt());
        verify(roomRepository, never()).save(any(Room.class));
    }


}