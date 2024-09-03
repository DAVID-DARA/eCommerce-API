package com.project.ecommerce_api.models.authDto;

import lombok.Data;

@Data
public class UserResponseDto {
    private String first_name;
    private String last_name;
    private String email;
    private Boolean isVerified;
}
