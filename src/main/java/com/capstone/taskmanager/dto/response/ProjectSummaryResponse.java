package com.capstone.taskmanager.dto.response;

import com.capstone.taskmanager.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ProjectSummaryResponse {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ownerName;
    private int totalTasks;
    private int completedTasks;
    private double completionPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
