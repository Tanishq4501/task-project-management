package com.capstone.taskmanager.dto.response;

import com.capstone.taskmanager.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private UserResponse owner;
    private List<TaskResponse> tasks;
    private int totalTasks;
    private int completedTasks;
    private double completionPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
