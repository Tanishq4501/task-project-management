package com.capstone.taskmanager.service;

import com.capstone.taskmanager.dto.request.CreateProjectRequest;
import com.capstone.taskmanager.dto.request.UpdateProjectRequest;
import com.capstone.taskmanager.dto.response.ProjectCompletionResponse;
import com.capstone.taskmanager.dto.response.ProjectResponse;
import com.capstone.taskmanager.dto.response.ProjectSummaryResponse;
import com.capstone.taskmanager.entity.Project;
import com.capstone.taskmanager.entity.User;
import com.capstone.taskmanager.enums.ProjectStatus;
import com.capstone.taskmanager.enums.TaskStatus;
import com.capstone.taskmanager.exception.ResourceNotFoundException;
import com.capstone.taskmanager.mapper.ProjectMapper;
import com.capstone.taskmanager.repository.ProjectRepository;
import com.capstone.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectMapper projectMapper;

    public ProjectResponse createProject(CreateProjectRequest req) {
        User owner = userService.findUserById(req.getOwnerId());
        Project project = projectMapper.toEntity(req, owner);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        return projectMapper.toResponse(findProjectById(id));
    }

    @Transactional(readOnly = true)
    public List<ProjectSummaryResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectSummaryResponse> getProjectsByOwner(Long ownerId) {
        userService.findUserById(ownerId);
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(projectMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectSummaryResponse> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status).stream()
                .map(projectMapper::toSummary)
                .toList();
    }

    public ProjectResponse updateProject(Long id, UpdateProjectRequest req) {
        Project project = findProjectById(id);

        if (req.getName() != null) project.setName(req.getName());
        if (req.getDescription() != null) project.setDescription(req.getDescription());
        if (req.getStartDate() != null) project.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) project.setEndDate(req.getEndDate());
        if (req.getStatus() != null) project.setStatus(req.getStatus());
        if (req.getOwnerId() != null) {
            User newOwner = userService.findUserById(req.getOwnerId());
            project.setOwner(newOwner);
        }
        return projectMapper.toResponse(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        Project project = findProjectById(id);
        projectRepository.delete(project);
    }

    public ProjectResponse archiveProject(Long id) {
        Project project = findProjectById(id);
        project.setStatus(ProjectStatus.ARCHIVED);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectCompletionResponse getProjectCompletion(Long id) {
        Project project = findProjectById(id);
        int todo = (int) taskRepository.countByProjectIdAndStatus(id, TaskStatus.TODO);
        int inProgress = (int) taskRepository.countByProjectIdAndStatus(id, TaskStatus.IN_PROGRESS);
        int done = (int) taskRepository.countByProjectIdAndStatus(id, TaskStatus.DONE);
        long overdue = taskRepository.findOverdueTasksByProject(id, LocalDate.now()).size();
        return projectMapper.toCompletionResponse(project, todo, inProgress, done, overdue);
    }

    public void autoCompleteProjectIfAllDone(Long projectId) {
        long total = taskRepository.countByProjectId(projectId);
        if (total == 0) return;
        long notDone = taskRepository.countByProjectIdAndStatusNot(projectId, TaskStatus.DONE);
        if (notDone == 0) {
            Project project = findProjectById(projectId);
            if (project.getStatus() == ProjectStatus.ACTIVE || project.getStatus() == ProjectStatus.ON_HOLD) {
                project.setStatus(ProjectStatus.COMPLETED);
                projectRepository.save(project);
            }
        }
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }
}
