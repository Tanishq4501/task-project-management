package com.capstone.taskmanager.controller;

import com.capstone.taskmanager.dto.request.CreateProjectRequest;
import com.capstone.taskmanager.dto.request.UpdateProjectRequest;
import com.capstone.taskmanager.dto.response.ProjectCompletionResponse;
import com.capstone.taskmanager.dto.response.ProjectResponse;
import com.capstone.taskmanager.dto.response.ProjectSummaryResponse;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.enums.ProjectStatus;
import com.capstone.taskmanager.service.ProjectService;
import com.capstone.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(req));
    }

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateProjectRequest req) {
        return ResponseEntity.ok(projectService.updateProject(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectSummaryResponse>> getProjectsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(projectService.getProjectsByOwner(ownerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectSummaryResponse>> getProjectsByStatus(@PathVariable ProjectStatus status) {
        return ResponseEntity.ok(projectService.getProjectsByStatus(status));
    }

    @GetMapping("/{id}/completion")
    public ResponseEntity<ProjectCompletionResponse> getProjectCompletion(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectCompletion(id));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ProjectResponse> archiveProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.archiveProject(id));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getProjectTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksByProject(id));
    }

    @GetMapping("/{id}/tasks/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasksForProject(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getOverdueTasksByProject(id));
    }
}
