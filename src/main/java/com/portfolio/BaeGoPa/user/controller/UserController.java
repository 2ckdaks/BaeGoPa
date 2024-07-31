package com.portfolio.BaeGoPa.user.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.user.JwtUtil;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.model.UserRequest;
import com.portfolio.BaeGoPa.user.model.UserResponse;
import com.portfolio.BaeGoPa.user.model.UserUpdateRequest;
import com.portfolio.BaeGoPa.user.service.UserService;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

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

    // 회원가입
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

    // 로그인시 jwt 발급
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
    @GetMapping("/")
    public String index() {
        return "index";  // src/main/resources/static/index.html 파일을 반환
    }

    @PutMapping("/edit/{userId}")
    public ExceptionApi<UserEntity> updateUser(
            @PathVariable Long userId,
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

    @DeleteMapping("/delete/{userId}")
    public ExceptionApi<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }
}
