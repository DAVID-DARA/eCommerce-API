package com.project.ecommerce_api.services;

//Project Classes
import com.project.ecommerce_api.entities.Token;
import com.project.ecommerce_api.entities.User;
import com.project.ecommerce_api.exceptions.CustomException;
import com.project.ecommerce_api.helpers.TokenGenerator;
import com.project.ecommerce_api.models.authDto.RegisterUserDto;
import com.project.ecommerce_api.models.authDto.SignupResponse;
import com.project.ecommerce_api.models.authDto.UserResponseDto;
import com.project.ecommerce_api.repositories.TokenRepository;
import com.project.ecommerce_api.repositories.UserRepository;
import com.project.ecommerce_api.utilities.Role;
import com.project.ecommerce_api.utilities.TokenType;

//Spring Classes
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Java Classes
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailSender;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public SignupResponse signup(RegisterUserDto userInput) {
        //Creation of required objects
        SignupResponse requestResponse = new SignupResponse();
        UserResponseDto userDetails = new UserResponseDto();
        User user = new User();

        //Check for existing user
        Optional<User> existingUser = userRepository.findByEmail(userInput.getEmail());
        if (existingUser.isPresent()) {
            requestResponse.setMessage("User Already Exists");
            requestResponse.setUser(null);
            return requestResponse;
        }

        user.setFirst_name(userInput.getFirst_name());
        user.setLast_name(userInput.getLast_name());
        user.setPhone_number(userInput.getPhone_number());
        user.setEmail(userInput.getEmail());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setIsVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        Token token = new Token();
        token.setUser(user);
        token.setToken(TokenGenerator.generateToken());
        token.setType(TokenType.EMAIL_VERIFICATION);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        token.setIsUsed(false);


        try {
            User savedUser = userRepository.save(user);
            logger.info("User successfully created: {}", user.getEmail());

            tokenRepository.save(token);
            emailSender.sendWelcomeEmail(user.getEmail(), user.getFirst_name(), token.getToken());

            logger.info("Email Sent Successfully. Proceed for verification");

            userDetails.setEmail(user.getEmail());
            userDetails.setFirst_name(user.getFirst_name());
            userDetails.setLast_name(user.getLast_name());
            userDetails.setIsVerified(user.getIsVerified());

            requestResponse.setMessage("User successfully created");
            requestResponse.setUser(userDetails);
        } catch (Exception e) {
            logger.error("Error creating new user", e);
            throw new CustomException("Error creating new user", "500");
        }

        return requestResponse;
    }
}
