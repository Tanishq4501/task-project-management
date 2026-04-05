package com.capstone.taskmanager.mapper;

import com.capstone.taskmanager.dto.request.CreateTaskRequest;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.entity.Project;
import com.capstone.taskmanager.entity.Task;
import com.capstone.taskmanager.entity.User;
import com.capstone.taskmanager.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskMapper {

    @Autowired
    private UserMapper userMapper;

    public TaskResponse toResponse(Task task) {
        if (task == null) return null;
        boolean overdue = task.getDeadline() != null
                && task.getDeadline().isBefore(LocalDate.now())
                && task.getStatus() != TaskStatus.DONE;

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .overdue(overdue)
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .projectName(task.getProject() != null ? task.getProject().getName() : null)
                .assignee(userMapper.toResponse(task.getAssignee()))
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }

    public Task toEntity(CreateTaskRequest req, Project project, User assignee) {
        return Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .priority(req.getPriority())
                .status(req.getStatus() != null ? req.getStatus() : TaskStatus.TODO)
                .deadline(req.getDeadline())
                .project(project)
                .assignee(assignee)
                .build();
    }
}
