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

## Date: 2024-09-06 AND 2024-09-07
### Achievements
1. Role Refactor:
   - Refactored Role from an Enum to an Entity.
   - Created a new RoleTypes Enum for managing different roles in the system.
   
2. User Entity Update:
   - Implemented authority assignment based on user roles in the User entity.
   
3. Role-Based Access Control (RBAC):
   - Applied RBAC to secure specific endpoints based on user roles.
   - Restricted category management (add, update, delete) to ADMIN and SUPER_ADMIN.

4. Category Management Endpoints:
   - Created the endpoint for adding categories (POST /api/v1/admin/categories) for ADMIN and SUPER_ADMIN.
   - Added endpoints for updating and deleting categories.

### Notes
- The implementation of Role Based Access Control(RBAC) has greatly improved the security of the API.

### Next Steps
- Implement additional endpoints as per the project’s requirements.
- Implement additional services as per the project’s requirements.

## Date: 2024-09-08
### Achievements
1. Cloudinary:
   - Added cloudinary API for media management
   - Test and implemented services

2. Product Management Endpoint:
   - Created the endpoint for adding product (POST /api/v1/admin/products) for ADMIN and SUPER_ADMIN.

### Notes
   - The implementation of Role Based Access Control(RBAC) has greatly improved the security of the API.

### Next Steps
   - Properly structure the add product endpoint for better security 
   - Implement additional endpoints as per the project’s requirements.
   - Implement additional services as per the project’s requirements.