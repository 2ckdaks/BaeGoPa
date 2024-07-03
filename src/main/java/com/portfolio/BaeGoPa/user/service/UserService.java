package com.portfolio.BaeGoPa.user.service;

import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import com.portfolio.BaeGoPa.user.db.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



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
}
