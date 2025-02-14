package prography.example.demo.global.apiPayLoad.code.Base;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ReasonDTO {

    private HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
