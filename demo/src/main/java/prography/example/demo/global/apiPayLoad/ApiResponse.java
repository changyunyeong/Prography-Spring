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

    // 서버 상태를 체크 실패
    public static <T> ApiResponse<T> badHealth() {
        return new ApiResponse<>(500, "에러가 발생했습니다.", null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> failure(Integer code, String message, T result) {
        return new ApiResponse<>(code, message, result);
    }
}
