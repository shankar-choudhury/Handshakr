# Handshakr

# README

## Project Overview
This project is a Spring Boot application that implements user authentication and transaction management using Spring Security and JWT. It consists of several packages, each with a specific role in the system.

---

## Package Breakdown

### `com.SWE2025.project.prototype.user`
This package contains the user-related logic, including entity definitions, services, and exceptions.
- **User**: Represents a user entity with security details.
- **UserRepository**: Repository interface for accessing user data.
- **UserService**: Interface defining user-related operations.
- **UserServiceImpl**: Implements the `UserService` interface.
- **UserException**: Custom exception class for handling user-related errors.
- **CustomUserDetailsService**: Loads user details for authentication.
- **DTOs (`userdtos`)**: Data Transfer Objects for user requests and responses.

### `com.SWE2025.project.prototype.security`
Handles authentication and authorization using Spring Security and JWT.
- **JwtUtils**: Utility class for generating and validating JWTs.
- **AuthTokenFilter**: Filters incoming requests to verify JWT tokens.
- **WebSecurityConfig**: Configures security settings and authentication.
- **AuthEntryPointJwt**: Handles unauthorized access attempts.
- **AuthController**: Manages authentication-related endpoints.
- **DTOs (`security.dto`)**: Represents authentication request payloads.

---

## Class Descriptions and Interactions

### **User Class** (`User`)
Defines a user entity with fields such as `username`, `password`, `email`, and transaction history. Implements `UserDetails` for Spring Security authentication.
- **Related Classes**: `Transaction` (as customer/vendor), `UserDetails` (Spring Security)
- **Used By**: `UserRepository`, `UserService`, `CustomUserDetailsService`

### **UserRepository**
A Spring Data `CrudRepository` that provides methods to access user data in the database.
- **Methods**: `findByUserName`, `existsByUsername`, `existsByEmail`
- **Used By**: `UserServiceImpl`, `CustomUserDetailsService`

### **UserService & UserServiceImpl**
Defines and implements user operations such as user creation, update, and retrieval.
- **Methods**: `createUser`, `updateUser`, `findById`, `findByUsername`, `findAllUsers`, `deleteUserById`
- **Uses**: `UserRepository`, `PasswordEncoder`
- **Used By**: `AuthController`

### **UserException**
Custom exception class to handle errors related to user operations.
- **Used By**: `UserServiceImpl`, `CustomUserDetailsService`

### **CustomUserDetailsService**
Loads user details required for authentication.
- **Implements**: `UserDetailsService`
- **Uses**: `UserRepository`
- **Used By**: `AuthTokenFilter`, `AuthenticationManager`

### **JwtUtils & JwtUtilsImpl**
Manages JWT token generation, validation, and extraction of user details.
- **Methods**: `generateToken`, `getUserNameFromToken`, `validateToken`
- **Used By**: `AuthTokenFilter`, `AuthController`

### **AuthTokenFilter**
Intercepts requests, extracts and validates JWT tokens, and sets user authentication.
- **Uses**: `JwtUtils`, `CustomUserDetailsService`
- **Used By**: `WebSecurityConfig`

### **WebSecurityConfig**
Configures security settings such as authentication and request filtering.
- **Uses**: `AuthTokenFilter`, `AuthenticationManager`, `PasswordEncoder`

### **AuthEntryPointJwt**
Handles unauthorized access attempts and returns HTTP 401 responses.
- **Used By**: `WebSecurityConfig`

### **AuthController**
Manages authentication endpoints (`/register`, `/login`).
- **Uses**: `UserService`, `JwtUtils`, `AuthenticationManager`

### **LoginRequest DTO**
Defines the structure of login requests containing `username` and `password`.
- **Used By**: `AuthController`

### **CreateUserRequest DTO**
Defines the structure for user registration requests.
- **Used By**: `UserServiceImpl`, `AuthController`

### **GetUserDTO & UserUpdateRequest DTOs**
- **GetUserDTO**: Encapsulates user data for responses.
- **UserUpdateRequest**: Defines user update request structure.
- **Used By**: `UserServiceImpl`

---

## Interaction Summary
1. **User Registration**
   - `AuthController` calls `UserServiceImpl.createUser`.
   - `UserServiceImpl` checks uniqueness, encodes password, and saves the user.
   - `UserRepository` stores the user in the database.

2. **User Authentication**
   - `AuthController` receives login request and calls `AuthenticationManager`.
   - `CustomUserDetailsService` loads user details.
   - `JwtUtils` generates and returns a JWT token.

3. **JWT Authentication Filter**
   - `AuthTokenFilter` extracts JWT from request headers.
   - `JwtUtils` validates the token and retrieves username.
   - `CustomUserDetailsService` loads user and sets authentication.

---

This structure ensures a secure and modular implementation of user management and authentication in the Spring Boot application.

