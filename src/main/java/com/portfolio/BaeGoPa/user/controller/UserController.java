package com.portfolio.BaeGoPa.user.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.user.JwtUtil;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.model.UserRequest;
import com.portfolio.BaeGoPa.user.model.UserResponse;
import com.portfolio.BaeGoPa.user.model.UserUpdateRequest;
import com.portfolio.BaeGoPa.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "User API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Operation(summary = "모든 사용자 목록 조회", description = "시스템에 등록된 모든 사용자의 목록을 반환")
    @GetMapping("/list")
    public ExceptionApi<List<UserResponse>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(user -> new UserResponse(user.getUserId(), user.getUsername(), user.getDisplayName(), user.getType(), user.getCreatedAt()))
                .collect(Collectors.toList());

        ExceptionApi<List<UserResponse>> response = ExceptionApi.<List<UserResponse>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(userResponses)
                .build();

        return response;
    }

    @Operation(summary = "로그인한 사용자 정보 조회", description = "로그인한 사용자의 상세 정보를 반환")
    @GetMapping("/detail")
    public ExceptionApi<UserResponse> getUserDetail() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserDetail(username);
        UserResponse userResponse = new UserResponse(user.getUserId(), user.getUsername(), user.getDisplayName(), user.getType(), user.getCreatedAt());

        ExceptionApi<UserResponse> response = ExceptionApi.<UserResponse>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(userResponse)
                .build();

        return response;
    }

    @Operation(summary = "사용자 등록", description = "새 사용자를 등록")
    @PostMapping("/register")
    public ExceptionApi<UserEntity> registerUser(
            @RequestBody UserRequest userRequest
    ){
        UserEntity userEntity = userService.registerUser(
                userRequest.getUsername(),
                userRequest.getDisplayName(),
                userRequest.getPassword(),
                userRequest.getType()
        );

        ExceptionApi<UserEntity> registerUser = ExceptionApi.<UserEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(userEntity)
                .build();

        return registerUser;
    }

    @Operation(summary = "JWT 로그인", description = "사용자를 로그인하고 JWT를 반환")
    @PostMapping("/login/jwt")
    public ExceptionApi<String> loginJwt(
            @RequestBody Map<String, String> data,
            HttpServletResponse response
    ) {
        String jwt = userService.loginJwt(data.get("username"), data.get("password"));

        var cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(60 * 10); // 10분 동안 유효
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        ExceptionApi<String> loginJwt = ExceptionApi.<String>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(jwt)
                .build();

        return loginJwt;
    }

    // cookie저장 확인용 테스트 코드
//    @GetMapping("/")
//    public String index() {
//        return "index";  // src/main/resources/static/index.html 파일을 반환
//    }

    @Operation(summary = "사용자 정보 수정", description = "지정된 사용자의 정보를 수정")
    @PutMapping("/edit/{userId}")
    public ExceptionApi<UserEntity> updateUser(
            @Parameter(description = "수정할 사용자 ID") @PathVariable Long userId,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        UserEntity updatedUser = userService.updateUser(
                userId,
                userUpdateRequest.getDisplayName(),
                userUpdateRequest.getPassword()
        );

        ExceptionApi<UserEntity> response = ExceptionApi.<UserEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedUser)
                .build();

        return response;
    }

    @Operation(summary = "사용자 삭제", description = "지정된 사용자를 삭제")
    @DeleteMapping("/delete/{userId}")
    public ExceptionApi<Void> deleteUser(
            @Parameter(description = "삭제할 사용자 ID") @PathVariable Long userId) {
        userService.deleteUser(userId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }
}
