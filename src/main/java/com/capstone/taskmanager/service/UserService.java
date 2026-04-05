package com.capstone.taskmanager.service;

import com.capstone.taskmanager.dto.request.CreateUserRequest;
import com.capstone.taskmanager.dto.request.UpdateUserRequest;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.dto.response.UserResponse;
import com.capstone.taskmanager.dto.response.UserTaskSummaryResponse;
import com.capstone.taskmanager.entity.User;
import com.capstone.taskmanager.enums.TaskStatus;
import com.capstone.taskmanager.exception.BadRequestException;
import com.capstone.taskmanager.exception.ResourceNotFoundException;
import com.capstone.taskmanager.mapper.TaskMapper;
import com.capstone.taskmanager.mapper.UserMapper;
import com.capstone.taskmanager.repository.TaskRepository;
import com.capstone.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public UserResponse createUser(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username '" + req.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email '" + req.getEmail() + "' is already registered.");
        }
        User user = userMapper.toEntity(req);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUserById(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.findByRole(role.toUpperCase()).stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse updateUser(Long id, UpdateUserRequest req) {
        User user = findUserById(id);

        if (req.getUsername() != null && !req.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(req.getUsername())) {
                throw new BadRequestException("Username '" + req.getUsername() + "' is already taken.");
            }
            user.setUsername(req.getUsername());
        }
        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new BadRequestException("Email '" + req.getEmail() + "' is already registered.");
            }
            user.setEmail(req.getEmail());
        }
        if (req.getFullName() != null) {
            user.setFullName(req.getFullName());
        }
        if (req.getRole() != null) {
            user.setRole(req.getRole().toUpperCase());
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);
        long openTasks = taskRepository.findByAssigneeId(id).stream()
                .filter(t -> t.getStatus() != TaskStatus.DONE)
                .count();
        if (openTasks > 0) {
            throw new BadRequestException(
                    "Cannot delete user with " + openTasks + " open task(s). Reassign or complete them first.");
        }
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUser(Long userId) {
        findUserById(userId);
        return taskRepository.findByAssigneeIdOrderByDeadlineAsc(userId).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserTaskSummaryResponse getUserTaskSummary(Long userId) {
        User user = findUserById(userId);
        List<com.capstone.taskmanager.entity.Task> tasks = taskRepository.findByAssigneeId(userId);
        long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        long overdue = taskRepository.countOverdueTasksByUser(userId, LocalDate.now());

        return UserTaskSummaryResponse.builder()
                .userId(userId)
                .username(user.getUsername())
                .todoCount(todo)
                .inProgressCount(inProgress)
                .doneCount(done)
                .totalAssigned(tasks.size())
                .overdueCount(overdue)
                .build();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
