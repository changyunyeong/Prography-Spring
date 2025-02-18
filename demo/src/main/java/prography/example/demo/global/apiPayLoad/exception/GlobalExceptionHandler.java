package prography.example.demo.global.apiPayLoad.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prography.example.demo.global.apiPayLoad.ApiResponse;
import prography.example.demo.global.apiPayLoad.code.Base.ReasonDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException e) {
        ReasonDTO reason = e.getStatus().getReason();

        return ResponseEntity
                .status(reason.getHttpStatus())
                .body(ApiResponse.failure(reason.getCode(), reason.getMessage(), null));
    }
}
