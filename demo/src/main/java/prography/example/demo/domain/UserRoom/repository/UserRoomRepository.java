package prography.example.demo.domain.UserRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.UserRoom.entity.UserRoom;
import prography.example.demo.global.common.enums.Team;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {

    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRoom ur WHERE ur.user = :user")
    boolean findUserRoomsByUserId(@Param("user") User user); // 유저가 어떤 방에라도 있는지 확인
    Integer countUserRoomsByRoomId(Integer roomId);
    Integer countByRoomIdAndTeam(Integer roomId, Team team);
    boolean existsByUserIdAndRoomId(Integer userId, Integer roomId); // 유저가 특정 방에 존재하는지 확인
    void deleteByUserIdAndRoomId(Integer userId, Integer roomId); // 특정 유저의 방 나가기
    void deleteAllByRoomId(Integer roomId); // 호스트가 방을 나가는 경우
    Optional<UserRoom> findByUserIdAndRoomId(Integer userId, Integer roomId);
}
