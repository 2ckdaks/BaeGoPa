package com.portfolio.BaeGoPa.user.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUser extends User implements Serializable {
    private static final long serialVersionUID = 1L; // 명시적으로 serialVersionUID 설정
    private Long userId;
    private String displayName;
    private UserType type;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
