package com.SWE2025.project.prototype.user;

import com.SWE2025.project.prototype.user.userdtos.CreateUserRequest;
import com.SWE2025.project.prototype.user.userdtos.GetUserDTO;
import com.SWE2025.project.prototype.user.userdtos.UserUpdateRequest;

import java.util.List;

public interface UserService {
    void createUser(CreateUserRequest createUserRequest);
    void updateUser(UserUpdateRequest userUpdateRequest);
    User findById(Long id);
    GetUserDTO findByUsername(String username);
    List<String> findAllUsers();
    void deleteUserById(Long id);
}
