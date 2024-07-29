package com.portfolio.BaeGoPa.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String displayName;
    private String password;
}
