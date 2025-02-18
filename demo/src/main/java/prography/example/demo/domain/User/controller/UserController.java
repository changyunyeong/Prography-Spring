package prography.example.demo.domain.User.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import prography.example.demo.domain.User.converter.UserConverter;
import prography.example.demo.domain.User.dto.request.UserRequestDTO;
import prography.example.demo.domain.User.dto.response.UserResponseDTO;
import prography.example.demo.domain.User.entity.User;
import prography.example.demo.domain.User.service.UserService;
import prography.example.demo.global.apiPayLoad.ApiResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/init")
    @Operation(summary = "기존 정보 삭제 및 재생성 API", description = "기존에 있던 모든 회원 정보 및 방 정보를 삭제\n"
            + " 이후 서비스에 필요한 회원 정보를 저장")
    public ApiResponse<Void> initUser(@RequestBody UserRequestDTO.InitUserRequestDTO request) {
        userService.initUser(request.getSeed(), request.getQuantity());
        return ApiResponse.success(null);
    }

    @GetMapping("/user")
    @Operation(summary = "모든 회원 정보를 응답 API", description = "id 기준 오름차순으로 정렬해서 반환")
    public ApiResponse<UserResponseDTO.UserPreViewListDTO> getUserList(
            @RequestParam("size") int size, @RequestParam("page") int page) {
        Page<User> userList = userService.getUserList(page, size);
        return ApiResponse.success(UserConverter.userPreViewListDTO(userList));
    }
}
