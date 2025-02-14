package prography.example.demo.global.apiPayLoad.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import prography.example.demo.global.apiPayLoad.code.Base.BaseStatus;
import prography.example.demo.global.apiPayLoad.code.Base.ReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseStatus {

    // 잘못된 API 요청
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 201, "불가능한 요청입니다."),
    // 그 외 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .httpStatus(httpStatus)
                .message(message)
                .code(code)
                .build();
    }

}
