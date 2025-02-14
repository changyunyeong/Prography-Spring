package prography.example.demo.domain.UserRoom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prography.example.demo.global.apiPayLoad.ApiResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class GameServiceImpl implements GameService {
    @Override
    public ApiResponse<Void> checkHealth() {
        return ApiResponse.success(null);
    }
}
