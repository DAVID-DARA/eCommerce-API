package com.project.ecommerce_api.models.authDto;

import lombok.Data;

@Data
public class SignupResponse {
    private String message;
    private UserResponseDto user;
}
