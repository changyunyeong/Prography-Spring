package prography.example.demo.domain.Room.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import prography.example.demo.domain.Room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
