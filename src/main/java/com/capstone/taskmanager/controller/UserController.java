package com.capstone.taskmanager.controller;

import com.capstone.taskmanager.dto.request.CreateUserRequest;
import com.capstone.taskmanager.dto.request.UpdateUserRequest;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.dto.response.UserResponse;
import com.capstone.taskmanager.dto.response.UserTaskSummaryResponse;
import com.capstone.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(req));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.updateUser(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTasksByUser(id));
    }

    @GetMapping("/{id}/tasks/summary")
    public ResponseEntity<UserTaskSummaryResponse> getUserTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserTaskSummary(id));
    }
}
