package prography.example.demo.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prography.example.demo.domain.User.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
