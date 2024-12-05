package com.nailservices.dto.auth;

import com.nailservices.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private UserRole role;
    private String email;
}
