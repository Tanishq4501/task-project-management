package com.capstone.taskmanager.mapper;

import com.capstone.taskmanager.dto.request.CreateProjectRequest;
import com.capstone.taskmanager.dto.response.ProjectCompletionResponse;
import com.capstone.taskmanager.dto.response.ProjectResponse;
import com.capstone.taskmanager.dto.response.ProjectSummaryResponse;
import com.capstone.taskmanager.dto.response.TaskResponse;
import com.capstone.taskmanager.entity.Project;
import com.capstone.taskmanager.entity.User;
import com.capstone.taskmanager.enums.ProjectStatus;
import com.capstone.taskmanager.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProjectMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    public ProjectResponse toResponse(Project project) {
        if (project == null) return null;
        List<TaskResponse> taskResponses = project.getTasks().stream()
                .map(taskMapper::toResponse)
                .toList();

        int total = taskResponses.size();
        int completed = (int) taskResponses.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();
        double pct = total == 0 ? 0.0 : Math.round((completed * 100.0 / total) * 10.0) / 10.0;

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .owner(userMapper.toResponse(project.getOwner()))
                .tasks(taskResponses)
                .totalTasks(total)
                .completedTasks(completed)
                .completionPercentage(pct)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public ProjectSummaryResponse toSummary(Project project) {
        if (project == null) return null;
        int total = project.getTasks().size();
        int completed = (int) project.getTasks().stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();
        double pct = total == 0 ? 0.0 : Math.round((completed * 100.0 / total) * 10.0) / 10.0;

        return ProjectSummaryResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .ownerName(project.getOwner() != null ? project.getOwner().getFullName() : null)
                .totalTasks(total)
                .completedTasks(completed)
                .completionPercentage(pct)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public Project toEntity(CreateProjectRequest req, User owner) {
        return Project.builder()
                .name(req.getName())
                .description(req.getDescription())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .status(ProjectStatus.ACTIVE)
                .owner(owner)
                .build();
    }

    public ProjectCompletionResponse toCompletionResponse(Project project,
                                                           int todo,
                                                           int inProgress,
                                                           int done,
                                                           long overdue) {
        int total = todo + inProgress + done;
        double pct = total == 0 ? 0.0 : Math.round((done * 100.0 / total) * 10.0) / 10.0;

        return ProjectCompletionResponse.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .totalTasks(total)
                .todoCount(todo)
                .inProgressCount(inProgress)
                .doneCount(done)
                .completionPercentage(pct)
                .overdueTaskCount(overdue)
                .build();
    }
}
