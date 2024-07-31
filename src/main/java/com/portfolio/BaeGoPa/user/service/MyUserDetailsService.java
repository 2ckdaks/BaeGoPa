package com.portfolio.BaeGoPa.user.service;

import com.portfolio.BaeGoPa.user.db.CustomUser;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Cacheable(value = "userDetails", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var result = userRepository.findByUsername(username);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("유효하지 않은 사용자입니다.");
        }

        var user = result.get();

        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority("nomal")); //유저유형 메모 "뭐 유형이 관리자면 관리자 if문 사용하면 됨"

        var customUser = new CustomUser(user.getUsername(), user.getPassword(), authority);
        customUser.setUserId(user.getUserId());  // ID 설정
        customUser.setDisplayName(user.getDisplayName());
        customUser.setType(user.getType());

        return customUser;
    }
}
