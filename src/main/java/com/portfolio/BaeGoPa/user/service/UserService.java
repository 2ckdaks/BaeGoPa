package com.portfolio.BaeGoPa.user.service;

import com.portfolio.BaeGoPa.user.JwtUtil;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import com.portfolio.BaeGoPa.user.db.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public List<UserEntity> getAllUsers() {
        // 로그인한 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 관리자 권한 검사
        if (!user.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("You do not have permission to view this list");
        }

        return userRepository.findAll();
    }

    public UserEntity registerUser(
            String username,
            String displayName,
            String password,
            UserType type
    ){
        if(userRepository.findByUsername(username).isPresent() || userRepository.findByDisplayName(displayName).isPresent()){
            throw new IllegalArgumentException("아이디 또는 닉네임이 이미 사용중입니다.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setDisplayName(displayName);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setType(type);

        return userRepository.save(userEntity);
    }

    public String loginJwt(String username, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(username, password);
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
    }
}
