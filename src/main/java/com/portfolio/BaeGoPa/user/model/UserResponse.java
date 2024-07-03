package com.portfolio.BaeGoPa.user.model;


import com.portfolio.BaeGoPa.user.db.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String displayName;

    private UserType type;

    private LocalDateTime createdAt;
}
