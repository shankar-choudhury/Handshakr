package com.SWE2025.project.prototype.user;

import com.SWE2025.project.prototype.user.userdtos.CreateUserRequest;
import com.SWE2025.project.prototype.user.userdtos.GetUserDTO;
import com.SWE2025.project.prototype.user.userdtos.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.SWE2025.project.prototype.user.UserException.Cause.*;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        var name = createUserRequest.username();
        var email = createUserRequest.email();

        if (userRepository.existsByUsername(name))
            throw new UserException(USERNAME_ALREADY_EXISTS, name);

        if (userRepository.existsByEmail(email))
            throw new UserException(EMAIL_ALREADY_EXISTS, email);

        var user = User.builder()
                .username(name)
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(email)
                .build();

        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(ID_NOT_FOUND, id));
    }

    @Override
    public GetUserDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(GetUserDTO::new)
                .orElseThrow(() -> new UserException(USERNAME_NOT_FOUND, username));
    }

    @Override
    public List<String> findAllUsers() {
        return ((List<User>) userRepository.findAll()).stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        var currentName = userUpdateRequest.currentUserName();
        var userToModify = findByUsername(currentName);
        // Endpoints for users to change their username or password, will implement with Spring Security
        // userRepository.save(userToModify);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
