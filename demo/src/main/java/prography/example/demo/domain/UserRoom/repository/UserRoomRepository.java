package prography.example.demo.domain.UserRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.UserRoom.entity.UserRoom;
import prography.example.demo.global.common.enums.Team;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {

    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRoom ur WHERE ur.user = :user")
    boolean findUserRoomsByUserId(@Param("user") User user);
    Integer countUserRoomsByRoomId(Integer roomId);
    Integer countByRoomIdAndTeam(Integer roomId, Team team);
}
