package com.project.ecommerce_api.controllers;

import com.project.ecommerce_api.models.authDto.request.LoginUserDto;
import com.project.ecommerce_api.models.authDto.request.RegisterUserDto;
import com.project.ecommerce_api.models.authDto.request.VerifyUserDto;
import com.project.ecommerce_api.models.authDto.response.CustomResponse;
import com.project.ecommerce_api.models.authDto.response.LoginResponse;
import com.project.ecommerce_api.models.authDto.response.VerificationResponse;
import com.project.ecommerce_api.models.authDto.response.SignupResponse;
import com.project.ecommerce_api.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup (@RequestBody RegisterUserDto signupRequest) {
        CustomResponse<SignupResponse> response = null;
        try {
            response = authenticationService.signup(signupRequest);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify (@RequestBody VerifyUserDto verificationRequest) {
        CustomResponse<VerificationResponse> response = null;
        try {
            response = authenticationService.verify(verificationRequest);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginUserDto loginRequest) {
        CustomResponse<LoginResponse> response = null;
        try {
            response =  authenticationService.login(loginRequest);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
