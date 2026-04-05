package com.capstone.taskmanager.dto.response;

import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate deadline;
    private boolean overdue;
    private Long projectId;
    private String projectName;
    private UserResponse assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}
