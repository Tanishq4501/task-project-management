package com.capstone.taskmanager.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCompletionResponse {
    private Long projectId;
    private String projectName;
    private int totalTasks;
    private int todoCount;
    private int inProgressCount;
    private int doneCount;
    private double completionPercentage;
    private long overdueTaskCount;
}
