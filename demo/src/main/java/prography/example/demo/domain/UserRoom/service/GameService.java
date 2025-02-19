package prography.example.demo.domain.UserRoom.service;

import prography.example.demo.global.apiPayLoad.ApiResponse;

public interface GameService {

    ApiResponse<Void> checkHealth();
    void startGame(Integer roomId, Integer userId);
    void changeTeam(Integer roomId, Integer userId);
}
