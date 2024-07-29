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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 관리자 권한 검사
        if (!user.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("이 목록을 볼 권한이 없습니다.");
        }

        return userRepository.findAll();
    }

    // 로그인한 사용자 정보 조회 기능
    public UserEntity getUserDetail() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
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

    @Transactional
    public UserEntity updateUser(Long userId, String displayName, String password) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 권한 확인 (본인 또는 관리자만 수정 가능)
        if (!loggedInUser.getUserId().equals(userId) && !loggedInUser.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("이 계정을 수정할 권한이 없습니다.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다. ID: " + userId));

        if (displayName != null && !displayName.isEmpty()) {
            user.setDisplayName(displayName);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 권한 확인 (본인 또는 관리자만 삭제 가능)
        if (!loggedInUser.getUserId().equals(userId) && !loggedInUser.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("이 계정을 삭제할 권한이 없습니다.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다. ID: " + userId));
        userRepository.delete(user);
    }
}