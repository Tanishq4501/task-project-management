package com.capstone.taskmanager.dto.request;

import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskRequest {

    @Size(max = 150, message = "Task title must not exceed 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private LocalDate deadline;

    private Long assigneeId;

    private boolean unassign;
}
