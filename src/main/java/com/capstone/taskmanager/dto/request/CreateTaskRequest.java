package com.capstone.taskmanager.dto.request;

import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 150, message = "Task title must not exceed 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    private TaskStatus status;

    private LocalDate deadline;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long assigneeId;
}
