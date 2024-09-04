package com.project.ecommerce_api.controllers;

import com.project.ecommerce_api.models.UserInfo;
import com.project.ecommerce_api.models.authDto.response.CustomResponse;
import com.project.ecommerce_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile (@PathVariable UUID id) {
        CustomResponse<UserInfo> response = null;
        try {
            response = userService.getUserDetails(id);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
