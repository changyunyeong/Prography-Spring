package prography.example.demo.domain.Room.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prography.example.demo.domain.Room.entity.Room;
import prography.example.demo.global.common.enums.RoomStatus;

import java.time.LocalDateTime;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    Room findRoomByStatus(RoomStatus status);
    @Modifying
    @Query("UPDATE Room r SET r.status = :status, r.updatedAt = :time WHERE r.updatedAt <= :ago AND r.status = :waitStatus")
    void updateRoomStatusAfterTime(
            @Param("status") RoomStatus status,
            @Param("time") LocalDateTime time,
            @Param("ago") LocalDateTime ago,
            @Param("waitStatus") RoomStatus waitStatus
    );
}
