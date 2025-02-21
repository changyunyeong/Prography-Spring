package prography.example.demo.domain.UserRoom.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.UserRoom.entity.UserRoom;
import prography.example.demo.domain.UserRoom.repository.UserRoomRepository;
import prography.example.demo.global.apiPayLoad.code.status.ErrorStatus;
import prography.example.demo.global.apiPayLoad.exception.GeneralException;
import prography.example.demo.global.common.enums.RoomStatus;
import prography.example.demo.global.common.enums.RoomType;
import prography.example.demo.global.common.enums.Team;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRoomRepository userRoomRepository;


    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(roomRepository, userRoomRepository);
    }

    @Test
    void 게임_시작_성공() {

        int roomId = 1;
        int userId = 10;

        User host = User.builder().id(userId).build();
        Room room = Room.builder().id(roomId).host(host).roomType(RoomType.SINGLE).status(RoomStatus.WAIT).build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRoomRepository.countUserRoomsByRoomId(roomId)).thenReturn(2);

        gameService.startGame(roomId, userId);

        assertEquals(RoomStatus.PROGRESS, room.getStatus());
        verify(roomRepository, times(1)).findById(roomId);
        verify(userRoomRepository, times(1)).countUserRoomsByRoomId(roomId);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void 게임_시작_실패_인원부족() {

        int roomId = 1;
        int userId = 10;

        User host = User.builder().id(userId).build();
        Room room = Room.builder().id(roomId).host(host).roomType(RoomType.SINGLE).status(RoomStatus.WAIT).build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRoomRepository.countUserRoomsByRoomId(roomId)).thenReturn(1); // Only 1 user in the room

        GeneralException exception = assertThrows(GeneralException.class, () -> {
            gameService.startGame(roomId, userId);
        });

        assertEquals(ErrorStatus.INVALID_REQUEST, exception.getStatus());
        verify(roomRepository, times(1)).findById(roomId);
        verify(userRoomRepository, times(1)).countUserRoomsByRoomId(roomId);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void 팀_변경_성공() {

        int roomId = 1;
        int userId = 10;

        User user = User.builder().id(userId).build();
        Room room = Room.builder()
                .id(roomId)
                .roomType(RoomType.SINGLE)
                .status(RoomStatus.WAIT)
                .build();
        UserRoom userRoom = UserRoom.builder()
                .user(user)
                .room(room)
                .team(Team.RED)
                .build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRoomRepository.findByUserIdAndRoomId(userId, roomId)).thenReturn(Optional.of(userRoom));
        when(userRoomRepository.existsByUserIdAndRoomId(userId, roomId)).thenReturn(true);
        when(userRoomRepository.countByRoomIdAndTeam(roomId, Team.RED)).thenReturn(1);
        when(userRoomRepository.countByRoomIdAndTeam(roomId, Team.BLUE)).thenReturn(0);

        gameService.changeTeam(roomId, userId);

        assertEquals(Team.BLUE, userRoom.getTeam());
        verify(roomRepository, times(1)).findById(roomId);
        verify(userRoomRepository, times(1)).findByUserIdAndRoomId(userId, roomId);
        verify(userRoomRepository, times(1)).existsByUserIdAndRoomId(userId, roomId);
        verify(userRoomRepository, times(1)).countByRoomIdAndTeam(roomId, Team.RED);
        verify(userRoomRepository, times(1)).countByRoomIdAndTeam(roomId, Team.BLUE);
        verify(userRoomRepository, times(1)).save(userRoom);
    }

}