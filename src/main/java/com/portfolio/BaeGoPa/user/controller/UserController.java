package com.portfolio.BaeGoPa.user.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserType;
import com.portfolio.BaeGoPa.user.model.UserRequest;
import com.portfolio.BaeGoPa.user.model.UserResponse;
import com.portfolio.BaeGoPa.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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

        ExceptionApi<UserEntity> response = ExceptionApi.<UserEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(userEntity)
                .build();

        return response;
    }
}
