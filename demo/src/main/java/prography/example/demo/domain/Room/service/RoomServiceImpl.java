package prography.example.demo.domain.Room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prography.example.demo.domain.Room.converter.RoomConverter;
import prography.example.demo.domain.Room.dto.request.RoomRequestDTO;
import prography.example.demo.domain.Room.dto.response.RoomResponseDTO;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Override
    public Room createRoom(RoomRequestDTO.CreateRoomRequestDTO request) {

        int userId = request.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_REQUEST)); // user가 존재하지 않을 경우에도 201 반환

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 활성상태가 아닐때는 201 응답을 반환
        }

        boolean isUserInRoom = userRoomRepository.findUserRoomsByUserId(user);

        if (isUserInRoom) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }

        Room room = RoomConverter.toRoom(request, user);
        roomRepository.save(room); // 방 생성

        UserRoom hostUserRoom = RoomConverter.toAttentionRoom(user, room, Team.RED);
        userRoomRepository.save(hostUserRoom); // 방 생성 후 호스트가 자동으로 방에 참가

        return room;
    }
    
    @Override
    public Page<Room> getRoomList(int size, int page) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Room> roomList = roomRepository.findAll(pageRequest);

        return roomList;
    }

    @Override
    public RoomResponseDTO.RoomDetailDTO getRoomDetail(Integer roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_REQUEST)); // 존재하지 않는 id에 대한 요청시 201 반환

        return RoomConverter.roomDetailViewDTO(room);
    }

    @Override
    public void attentionRoom(Integer roomId, Integer userId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_REQUEST)); // 존재하지 않는 id에 대한 요청시 201 반환

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_REQUEST)); // user가 존재하지 않을 경우에도 201 반환
        boolean isUserInRoom = userRoomRepository.findUserRoomsByUserId(user);

        Integer countByRoom = userRoomRepository.countUserRoomsByRoomId(roomId); // 방에 참여한 인원 수
        Integer readTeamCount = userRoomRepository.countByRoomIdAndTeam(roomId, Team.RED); // 레드팀 인원 수

        RoomType roomType = room.getRoomType();
        int maxCapacity = roomType == RoomType.DOUBLE ? 4 : 2; // 방 최대 정원 설정

        if (room.getStatus() != RoomStatus.WAIT) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 대기상태가 아닌 방이라면 201 응답을 반환
        }
        if (user.getStatus() != UserStatus.ACTIVE || isUserInRoom) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 활성상태가 아니고 만약 참여한 방이 있다면 201 응답을 반환
        }
        if (countByRoom >= maxCapacity) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 인원이 가득 찼다면 201 응답을 반환
        }

        Team team;
        if (readTeamCount >= maxCapacity / 2) {
            team = Team.BLUE; // 레드팀 인원이 찬 경우 블루팀으로 배정
        } else {
            team = Team.RED; // 기본은 레드팀으로 배정
        }

        UserRoom userRoom = RoomConverter.toAttentionRoom(user, room, team);
        userRoomRepository.save(userRoom);
    }

    @Override
    public void outRoom(Integer roomId, Integer userId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_REQUEST)); // 존재하지 않는 id에 대한 요청시 201 반환
        boolean isUserInRoom = userRoomRepository.existsByUserIdAndRoomId(userId, roomId);

        if (!isUserInRoom) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 참가한 상태가 아니라면 201 응답을 반환
        }
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST); // 시작(PROGRESS) 상태인 방이거나 끝난(FINISH) 상태의 방이면 201 응답을 반환
        }
        if (room.getHost().getId().equals(userId)) {
            userRoomRepository.deleteAllByRoomId(roomId); // 호스트가 방을 나가게 되면 방에 있던 모든 사람도 해당 방에서 나감
            room.setStatus(RoomStatus.FINISH);
            roomRepository.save(room);
            return;
        }

        userRoomRepository.deleteByUserIdAndRoomId(userId, roomId);
        // 호스트가 나가면 방이 종료되므로 방의 남은 인원이 0명인지 체크할 필요 없음
    }
}
