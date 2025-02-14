package prography.example.demo.global.apiPayLoad.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import prography.example.demo.global.apiPayLoad.code.Base.BaseStatus;
import prography.example.demo.global.apiPayLoad.code.Base.ReasonDTO;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseStatus {

    OK(HttpStatus.OK, 200, "API 요청이 성공했습니다.");

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
