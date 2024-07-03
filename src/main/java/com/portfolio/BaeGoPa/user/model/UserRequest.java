package com.portfolio.BaeGoPa.user.model;

import com.portfolio.BaeGoPa.user.db.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String displayName;
    private String password;
    private UserType type;
}
