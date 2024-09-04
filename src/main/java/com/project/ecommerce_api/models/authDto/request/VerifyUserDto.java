package com.project.ecommerce_api.models.authDto.request;

import lombok.Data;

@Data
public class VerifyUserDto {
    private String email;
    private String token;
}
