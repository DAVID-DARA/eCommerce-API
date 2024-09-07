# PROGRESS OF API

## Date: 2024-09-04
### Achievements
1. **Changed Token Data Type:**
    - Updated the token field from Integer to String data type.
    - Adjusted all associated methods to handle this change, including:
      - Signup method in the AuthenticationService class. 
      - SendWelcomeEmail method in the EmailService class.

2. Custom Response Model:
   - Created a Custom Response Model to standardize API responses.
   - Implemented this model with the signup method in the AuthenticationService class for improved response structure.
   
3. Verification Endpoint:
   - Created and tested the verify endpoint to handle user verification.
   - Ensured proper status code handling for various scenarios (e.g., token expiration, invalid token).
   
4. Security Configuration:
   - Reconfigured the SecurityConfig class to use CustomUserDetailService instead of UserDetailService.
   - This change enhanced the customization of user authentication.
   
5. Login Endpoint:
   - Created and tested the login endpoint.
   - Implemented JWT generation and response structure.
   - Handled scenarios where users are not verified and provided appropriate responses.
   
6. User Profile Endpoint:
   - Created and tested the GET /api/v1/users/{id} endpoint.
   - Enabled retrieval of user profile details based on user ID.
   - Structured the response to include user information using the UserInfo model.
   
### Notes
   - Consideration was given to using either email or id as the request parameter for the user profile endpoint. It was decided to use id to maintain consistency and security.
   - The implementation of the Custom Response Model has greatly improved the consistency and clarity of API responses.
   
### Next Steps
   - Continue refining the API’s security features, particularly around JWT handling and user authentication.
   - Implement additional endpoints as per the project’s requirements.

## Date: 2024-09-05
### Achievements
1. Created Test for Authentication Service: 

## Date: 2024-09-06
### Achievements
1. Changed Roles from Enum to Entity
2.  Added separate Enum class RoleTypes for the Role Entity
3.  Implemented authorities based on roles in User Entity
4.  Implemented Role Based Access Control on certain endpoints
5.  Created the add categories endpoint (ADMIN, SUPER_ADMIN)
6.  Created update and delete category endpoint