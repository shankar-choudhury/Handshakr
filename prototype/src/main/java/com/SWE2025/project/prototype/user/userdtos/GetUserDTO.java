package com.SWE2025.project.prototype.user.userdtos;

import com.SWE2025.project.prototype.user.User;

public record GetUserDTO(String userName) {
    public GetUserDTO(User user){this(user.getUsername());}
}
