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
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.Room.repository.RoomRepository;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.User.repository.UserRepository;
import prography.example.demo.domain.UserRoom.repository.UserRoomRepository;
import prography.example.demo.global.apiPayLoad.code.status.ErrorStatus;
import prography.example.demo.global.apiPayLoad.exception.GeneralException;
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
        return roomRepository.save(room);
    }
    
    @Override
    public Page<Room> getRoomList(int size, int page) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Room> roomList = roomRepository.findAll(pageRequest);

        return roomList;
    }
}
