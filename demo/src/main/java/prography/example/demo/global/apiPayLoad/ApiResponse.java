package prography.example.demo.global.apiPayLoad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import prography.example.demo.global.apiPayLoad.code.Base.BaseStatus;
import prography.example.demo.global.apiPayLoad.code.status.SuccessStatus;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class ApiResponse<T> {

    private final Integer code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공 응답
    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(SuccessStatus.OK.getCode() , SuccessStatus.OK.getMessage(), result);
    }

    // 공통 응답 처리
    public static <T> ApiResponse<T> of(BaseStatus status, T result) {
        return new ApiResponse<>(status.getReason().getCode(), status.getReason().getMessage(), result);
    }

    // 실패 응답
    public static <T> ApiResponse<T> failure(Integer code, String message, T result) {
        return new ApiResponse<>(code, message, result);
    }
}
