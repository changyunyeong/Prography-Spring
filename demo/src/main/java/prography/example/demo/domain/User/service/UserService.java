package prography.example.demo.domain.User.service;

import org.springframework.data.domain.Page;
import prography.example.demo.domain.User.entity.User;

public interface UserService {

    void initUser(int seed, int quantity);

    Page<User> getUserList(int page, int size);
}
