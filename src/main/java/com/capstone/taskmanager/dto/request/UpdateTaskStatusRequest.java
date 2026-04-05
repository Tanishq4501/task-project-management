package com.capstone.taskmanager.dto.request;

import com.capstone.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}
