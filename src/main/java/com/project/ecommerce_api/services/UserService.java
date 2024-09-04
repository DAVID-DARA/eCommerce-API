package com.project.ecommerce_api.services;

import com.project.ecommerce_api.entities.User;
import com.project.ecommerce_api.models.UserInfo;
import com.project.ecommerce_api.models.authDto.response.CustomResponse;
import com.project.ecommerce_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CustomResponse<UserInfo> getUserDetails (UUID id) {
        CustomResponse<UserInfo> userResponse = new CustomResponse<>();

        Optional<User> userOptional = userRepository.findByUserId(id);
        if (userOptional.isEmpty()) {
            return createErrorResponse(userResponse, HttpStatus.NOT_FOUND, "user_not_found");
        }
        User user = userOptional.get();

        UserInfo userInfo = new UserInfo(
                user.getUserId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getEmail(),
                user.getIsVerified()
        );
        userResponse.setSuccess(true);
        userResponse.setStatusCode(HttpStatus.OK);
        userResponse.setMessage("User Details");
        userResponse.setData(userInfo);

        return userResponse;
    }

    private <T> CustomResponse<T> createErrorResponse(
            CustomResponse<T> response,
            HttpStatus status,
            String message
    ) {
        response.setSuccess(false);
        response.setStatusCode(status);
        response.setMessage(message);
        response.setData(null);

        return response;
    }
}
