package com.portfolio.BaeGoPa.user.db;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    private Long userId;
    public String displayName;
    public UserType type;
    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = userId;
    }
}
