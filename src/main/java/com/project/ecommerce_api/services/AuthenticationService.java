package com.project.ecommerce_api.services;

//Project Classes
import com.project.ecommerce_api.entities.Role;
import com.project.ecommerce_api.entities.Token;
import com.project.ecommerce_api.entities.User;
import com.project.ecommerce_api.exceptions.CustomException;
import com.project.ecommerce_api.helpers.TokenGenerator;
import com.project.ecommerce_api.models.JwtAuthenticationResponse;
import com.project.ecommerce_api.models.UserInfo;
import com.project.ecommerce_api.models.authDto.request.LoginUserDto;
import com.project.ecommerce_api.models.authDto.request.RegisterUserDto;
import com.project.ecommerce_api.models.authDto.request.VerifyUserDto;
import com.project.ecommerce_api.models.authDto.response.CustomResponse;
import com.project.ecommerce_api.models.authDto.response.LoginResponse;
import com.project.ecommerce_api.models.authDto.response.VerificationResponse;
import com.project.ecommerce_api.models.authDto.response.SignupResponse;
import com.project.ecommerce_api.repositories.RoleRepository;
import com.project.ecommerce_api.repositories.TokenRepository;
import com.project.ecommerce_api.repositories.UserRepository;
import com.project.ecommerce_api.utilities.RoleEnum;
import com.project.ecommerce_api.utilities.TokenType;

//Spring Classes
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Java Classes
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailSender;
    private final CustomUserDetailService userDetailService;
    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public CustomResponse<SignupResponse> signup(RegisterUserDto userInput) {
        CustomResponse<SignupResponse> signupResponse = new CustomResponse<>();
        SignupResponse userDetails = new SignupResponse();
        User user = new User();

        // Check for existing user
        Optional<User> existingUser = userRepository.findByEmail(userInput.getEmail());
        if (existingUser.isPresent()) {
            return createErrorResponse(signupResponse, HttpStatus.CONFLICT, "User Already Exists");
        }
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }
        Role userRole = optionalRole.get();


        // Set user details
        user.setFirst_name(userInput.getFirst_name());
        user.setLast_name(userInput.getLast_name());
        user.setPhone_number(userInput.getPhone_number());
        user.setEmail(userInput.getEmail());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        user.setRole(userRole);
        user.setIsVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        // Create and set token
        Token token = new Token();
        token.setUser(user);
        token.setToken(TokenGenerator.generateToken().toString());
        token.setType(TokenType.EMAIL_VERIFICATION);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        token.setIsUsed(false);

        try {
            User savedUser = userRepository.save(user);
            logger.info("User successfully created: {}", savedUser.getUserId());

            tokenRepository.save(token);
            emailSender.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirst_name(), token.getToken());

            logger.info("Email Sent Successfully. Proceed for verification");

            userDetails.setEmail(savedUser.getEmail());
            userDetails.setFirst_name(savedUser.getFirst_name());
            userDetails.setLast_name(savedUser.getLast_name());
            userDetails.setIsVerified(savedUser.getIsVerified());

            signupResponse.setSuccess(true);
            signupResponse.setStatusCode(HttpStatus.CREATED);
            signupResponse.setMessage("User successfully created (Not Verified)");
            signupResponse.setData(userDetails);
        } catch (Exception e) {
            logger.error("Error creating new user", e);
            throw new CustomException("Error creating new user", "500");
        }
        return signupResponse;
    }


    public CustomResponse<VerificationResponse> verify(VerifyUserDto userInput) {
        CustomResponse<VerificationResponse> verificationResponse = new CustomResponse<>();
        VerificationResponse verifyInfo = new VerificationResponse();

        // Check if user exists
        Optional<User> user = userRepository.findByEmail(userInput.getEmail());
        if (user.isEmpty()) {
            return createErrorResponse(verificationResponse, HttpStatus.NOT_FOUND, "User Doesn't exist");
        }
        User requiredUser = user.get();

        // Check if token exists
        Optional<Token> token = tokenRepository.findByUser(requiredUser);
        if (token.isEmpty()) {
            return createErrorResponse(verificationResponse, HttpStatus.NOT_FOUND, "Token doesn't exist");
        }
        Token requiredToken = token.get();

        //Check if token isUsed
        if (requiredToken.getIsUsed()) {
            return  createErrorResponse(verificationResponse, HttpStatus.IM_USED, "Token has been used");
        }

        //Check if token is expired
        if (requiredToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            return  createErrorResponse(verificationResponse, HttpStatus.UNAUTHORIZED, "Expired Token");
        }

        // Check if user input matches token value
        if (!Objects.equals(userInput.getToken(), requiredToken.getToken())) {
            return createErrorResponse(verificationResponse, HttpStatus.UNAUTHORIZED, "Invalid Token");
        }

        // Update user and token status
        requiredUser.setIsVerified(true);
        requiredToken.setIsUsed(true);

        try {
            User savedUser = userRepository.save(requiredUser);
            tokenRepository.save(requiredToken);

            // Set authentication context
            UserDetails userDetails = userDetailService.loadUserByUsername(requiredUser.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, requiredUser.getPassword(), userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtService.generateToken(userDetails);

            verifyInfo.setAuthInfo(new JwtAuthenticationResponse(accessToken));
            verifyInfo.setUser(
                    new UserInfo(
                            savedUser.getUserId(),
                            savedUser.getFirst_name(),
                            savedUser.getLast_name(),
                            savedUser.getEmail(),
                            savedUser.getIsVerified()
                    )
            );

            verificationResponse.setSuccess(true);
            verificationResponse.setStatusCode(HttpStatus.OK);
            verificationResponse.setMessage("Verification Successful");
            verificationResponse.setData(verifyInfo);

            logger.info("Verification Successful for user: {}", requiredUser.getUserId());
        } catch (Exception e) {
            logger.error("Error in verify (AuthenticationService.java): ", e);
            throw new CustomException("Error in user verification");
        }
        return verificationResponse;
    }

    public CustomResponse<LoginResponse> login (LoginUserDto userInput) {
        CustomResponse<LoginResponse> loginResponse = new CustomResponse<>();
        LoginResponse loginInfo = new LoginResponse();

        // Check if user exists
        Optional<User> user = userRepository.findByEmail(userInput.getEmail());
        if (user.isEmpty()) {
            return createErrorResponse(loginResponse, HttpStatus.NOT_FOUND, "User Doesn't exist");
        }
        User requiredUser = user.get();

        boolean isValidPassword = passwordEncoder.matches(userInput.getPassword(), requiredUser.getPassword());
        if (!isValidPassword) {
            return createErrorResponse(loginResponse, HttpStatus.UNAUTHORIZED, "Invalid Password");
        }

        if (!requiredUser.getIsVerified()) {
            loginInfo.setUserInfo(new UserInfo(
                    requiredUser.getUserId(),
                    requiredUser.getFirst_name(),
                    requiredUser.getLast_name(),
                    requiredUser.getEmail(),
                    false
            ));

            loginInfo.setAuthInfo(new JwtAuthenticationResponse(null));

            loginResponse.setSuccess(false);
            loginResponse.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
            loginResponse.setMessage("Credentials accepted, but user is not verified. Verification required.");
            loginResponse.setData(loginInfo);

            return loginResponse;
        }

        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(requiredUser.getEmail());
            String accessToken = jwtService.generateToken(userDetails);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, requiredUser.getPassword(), userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            loginInfo.setAuthInfo(new JwtAuthenticationResponse(accessToken));
            loginInfo.setUserInfo(new UserInfo(
                    requiredUser.getUserId(),
                    requiredUser.getFirst_name(),
                    requiredUser.getLast_name(),
                    requiredUser.getEmail(),
                    requiredUser.getIsVerified()
            ));

            loginResponse.setSuccess(true);
            loginResponse.setStatusCode(HttpStatus.ACCEPTED);
            loginResponse.setMessage("User authenticated successfully");
            loginResponse.setData(loginInfo);
        } catch (Exception e) {
            logger.error("Error authentication user, login(AuthenticationService.java): ", e);
            throw new CustomException("Error in user authentication");
        }
        return loginResponse;
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
