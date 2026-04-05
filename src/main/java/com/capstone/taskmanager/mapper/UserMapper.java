package com.capstone.taskmanager.mapper;

import com.capstone.taskmanager.dto.request.CreateUserRequest;
import com.capstone.taskmanager.dto.response.UserResponse;
import com.capstone.taskmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toEntity(CreateUserRequest req) {
        return User.builder()
                .username(req.getUsername())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .role(req.getRole() != null && !req.getRole().isBlank() ? req.getRole().toUpperCase() : "DEVELOPER")
                .build();
    }
}
