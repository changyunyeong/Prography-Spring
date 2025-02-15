package prography.example.demo.domain.User.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import prography.example.demo.domain.UserRoom.entity.UserRoom;
import prography.example.demo.global.common.BaseEntity;
import prography.example.demo.global.common.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer fakerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<UserRoom> userRoomList = new ArrayList<>();
}
