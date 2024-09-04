package com.project.ecommerce_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserInfo {
    private UUID id;
    private String first_name;
    private String last_name;
    private String email;
    private Boolean isVerified;
}
