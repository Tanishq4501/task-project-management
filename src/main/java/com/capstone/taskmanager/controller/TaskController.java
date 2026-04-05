package com.capstone.taskmanager.controller;

import com.capstone.taskmanager.dto.request.CreateTaskRequest;
import com.capstone.taskmanager.dto.request.UpdateTaskRequest;
import com.capstone.taskmanager.dto.request.UpdateTaskStatusRequest;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import com.capstone.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> filterTasks(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long assigneeId) {
        return ResponseEntity.ok(taskService.filterTasks(projectId, status, priority, assigneeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateTaskRequest req) {
        return ResponseEntity.ok(taskService.updateTask(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateTaskStatusRequest req) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, req.getStatus()));
    }

    @PatchMapping("/{id}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.assignTask(id, userId));
    }

    @PatchMapping("/{id}/unassign")
    public ResponseEntity<TaskResponse> unassignTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.unassignTask(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }
}
