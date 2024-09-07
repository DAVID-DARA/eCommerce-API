package com.project.ecommerce_api.models.authDto.request;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}
