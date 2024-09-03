package com.project.ecommerce_api.controllers;

import com.project.ecommerce_api.entities.User;
import com.project.ecommerce_api.models.authDto.RegisterUserDto;
import com.project.ecommerce_api.models.authDto.SignupResponse;
import com.project.ecommerce_api.services.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup (@RequestBody RegisterUserDto userInput) {
        SignupResponse response = null;
        try {
            response = authenticationService.signup(userInput);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            logger.error("Error creating user: ", e);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
