package prography.example.demo.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {

    SINGLE("단식"),
    DOUBLE("복식");

    private final String type;
}
