package prography.example.demo.global.apiPayLoad.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import prography.example.demo.global.apiPayLoad.code.Base.BaseStatus;
import prography.example.demo.global.apiPayLoad.code.Base.ReasonDTO;

@Getter
public class GeneralException extends RuntimeException {

    private BaseStatus status;

    public GeneralException(BaseStatus status) {

        super(status.getReason().getMessage());
        this.status = status;
    }

    public ReasonDTO getErrorReason() {
        return this.status.getReason();
    }
}
