package com.portfolio.BaeGoPa.user.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.user.JwtUtil;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.model.UserRequest;
import com.portfolio.BaeGoPa.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

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

    @PostMapping("/login/jwt")
    public ExceptionApi<String> loginJwt(
            @RequestBody Map<String, String> data,
            HttpServletResponse response
            ){
        var authToken = new UsernamePasswordAuthenticationToken(
                data.get("username"), data.get("password")
        );
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var jwt = JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(jwt);

        var cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(10);
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
}
