package com.capstone.taskmanager.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTaskSummaryResponse {
    private Long userId;
    private String username;
    private long todoCount;
    private long inProgressCount;
    private long doneCount;
    private long totalAssigned;
    private long overdueCount;
}
