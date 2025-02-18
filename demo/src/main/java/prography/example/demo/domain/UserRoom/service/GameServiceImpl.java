package prography.example.demo.domain.UserRoom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.repository.UserRepository;
import prography.example.demo.domain.UserRoom.repository.UserRoomRepository;
import prography.example.demo.global.apiPayLoad.ApiResponse;
import prography.example.demo.global.apiPayLoad.code.status.ErrorStatus;
import prography.example.demo.global.apiPayLoad.exception.GeneralException;
import prography.example.demo.global.common.enums.RoomStatus;
import prography.example.demo.global.common.enums.RoomType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@EnableScheduling
public class GameServiceImpl implements GameService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    @Override
    public ApiResponse<Void> checkHealth() {
        try {
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.badHealth();
        }
    }

    @Override
    public void startGame(Integer roomId, Integer userId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_REQUEST)); // 존재하지 않는 id에 대한 요청시 201 반환

        RoomType roomType = room.getRoomType();
        int maxCapacity = roomType == RoomType.DOUBLE ? 4 : 2; // 방 최대 정원 설정
        Integer countByRoom = userRoomRepository.countUserRoomsByRoomId(roomId); // 방에 참여한 인원 수

        if (!room.getHost().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 호스트가 아니라면 201 응답을 반환
        }
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 대기(WAIT) 상태의 방이 아니면 201 응답을 반환
        }
        if (countByRoom < maxCapacity) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 인원이 가득 차지 않으면 201 응답을 반환
        }

        room.setStatus(RoomStatus.PROGRESS);
        roomRepository.save(room);

        endGameAfterDelay();
    }

    @Scheduled(fixedDelay = 2000)
    public void endGameAfterDelay() {

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
        roomRepository.updateRoomStatusAfterTime(RoomStatus.FINISH, now, oneMinuteAgo, RoomStatus.PROGRESS);

        Room finishRoom = roomRepository.findRoomByStatus(RoomStatus.FINISH);
        if (finishRoom == null) {
            log.info("room is not finished");
            return;
        }

        log.info("room {} is finished", finishRoom.getId());
        userRoomRepository.deleteAllByRoomId(finishRoom.getId());
    }
}
