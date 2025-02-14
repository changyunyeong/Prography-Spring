package prography.example.demo.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {

    WAIT("대기"),
    ACTIVE("활성"),
    NON_ACTIVE("비활성");

    private final String status;
}
