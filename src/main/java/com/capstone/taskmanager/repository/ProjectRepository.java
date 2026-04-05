package com.capstone.taskmanager.repository;

import com.capstone.taskmanager.entity.Project;
import com.capstone.taskmanager.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long ownerId);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByOwnerIdAndStatus(Long ownerId, ProjectStatus status);
}
