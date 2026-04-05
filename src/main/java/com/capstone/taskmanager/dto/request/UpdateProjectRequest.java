package com.capstone.taskmanager.dto.request;

import com.capstone.taskmanager.enums.ProjectStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProjectRequest {

    @Size(max = 100, message = "Project name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus status;

    private Long ownerId;
}
