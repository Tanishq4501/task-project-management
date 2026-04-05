package com.capstone.taskmanager.service;

import com.capstone.taskmanager.dto.request.CreateTaskRequest;
import com.capstone.taskmanager.dto.request.UpdateTaskRequest;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.entity.Project;
import com.capstone.taskmanager.entity.Task;
import com.capstone.taskmanager.entity.User;
import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import com.capstone.taskmanager.exception.ResourceNotFoundException;
import com.capstone.taskmanager.mapper.TaskMapper;
import com.capstone.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public TaskResponse createTask(CreateTaskRequest req) {
        Project project = projectService.findProjectById(req.getProjectId());
        User assignee = req.getAssigneeId() != null
                ? userService.findUserById(req.getAssigneeId())
                : null;
        Task task = taskMapper.toEntity(req, project, assignee);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        return taskMapper.toResponse(findTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByProject(Long projectId) {
        projectService.findProjectById(projectId);
        return taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> filterTasks(Long projectId, TaskStatus status, TaskPriority priority, Long assigneeId) {
        return taskRepository.findByFilters(projectId, status, priority, assigneeId).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest req) {
        Task task = findTaskById(id);

        if (req.getTitle() != null) task.setTitle(req.getTitle());
        if (req.getDescription() != null) task.setDescription(req.getDescription());
        if (req.getPriority() != null) task.setPriority(req.getPriority());
        if (req.getDeadline() != null) task.setDeadline(req.getDeadline());

        if (req.isUnassign()) {
            task.setAssignee(null);
        } else if (req.getAssigneeId() != null) {
            User assignee = userService.findUserById(req.getAssigneeId());
            task.setAssignee(assignee);
        }

        if (req.getStatus() != null && req.getStatus() != task.getStatus()) {
            applyStatusTransition(task, req.getStatus());
        }

        Task saved = taskRepository.save(task);
        projectService.autoCompleteProjectIfAllDone(task.getProject().getId());
        return taskMapper.toResponse(saved);
    }

    public TaskResponse updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = findTaskById(id);
        if (newStatus != task.getStatus()) {
            applyStatusTransition(task, newStatus);
            task = taskRepository.save(task);
            projectService.autoCompleteProjectIfAllDone(task.getProject().getId());
        }
        return taskMapper.toResponse(task);
    }

    public TaskResponse assignTask(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        User user = userService.findUserById(userId);
        task.setAssignee(user);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    public TaskResponse unassignTask(Long taskId) {
        Task task = findTaskById(taskId);
        task.setAssignee(null);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        Long projectId = task.getProject().getId();
        taskRepository.delete(task);
        projectService.autoCompleteProjectIfAllDone(projectId);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now()).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getOverdueTasksByProject(Long projectId) {
        projectService.findProjectById(projectId);
        return taskRepository.findOverdueTasksByProject(projectId, LocalDate.now()).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    private void applyStatusTransition(Task task, TaskStatus newStatus) {
        TaskStatus old = task.getStatus();
        task.setStatus(newStatus);
        if (newStatus == TaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
        } else if (old == TaskStatus.DONE) {
            task.setCompletedAt(null);
        }
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }
}
