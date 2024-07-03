package com.portfolio.BaeGoPa.user.service;

import com.portfolio.BaeGoPa.user.db.CustomUser;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var result = userRepository.findByUsername(username);
        if(result.isEmpty()){
            // TODO : 알맞은 에러처리
        }

        var user = result.get();

        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority("nomal")); //유저유형 메모 "뭐 유형이 관리자면 관리자 if문 사용하면 됨"

        var customUser = new CustomUser(user.getUsername(), user.getPassword(), authority);
        customUser.setId(user.getUserId());  // ID 설정
        customUser.displayName = user.getDisplayName();
        customUser.type = user.getType();

        return customUser;
    }

}
