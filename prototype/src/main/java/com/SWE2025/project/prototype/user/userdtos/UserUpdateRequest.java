package com.SWE2025.project.prototype.user.userdtos;

public record UserUpdateRequest(String currentUserName, String newUserName, String newPassword) {
}
