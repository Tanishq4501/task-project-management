package com.capstone.taskmanager.repository;

import com.capstone.taskmanager.entity.Task;
import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByAssigneeIdOrderByDeadlineAsc(Long assigneeId);

    long countByProjectId(Long projectId);

    long countByProjectIdAndStatus(Long projectId, TaskStatus status);

    @Query("""
            SELECT t FROM Task t
            WHERE (:projectId IS NULL OR t.project.id = :projectId)
              AND (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
              AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)
            ORDER BY
              CASE t.priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 ELSE 3 END,
              t.deadline ASC NULLS LAST
            """)
    List<Task> findByFilters(@Param("projectId") Long projectId,
                             @Param("status") TaskStatus status,
                             @Param("priority") TaskPriority priority,
                             @Param("assigneeId") Long assigneeId);

    @Query("SELECT t FROM Task t WHERE t.deadline < :today AND t.status <> com.capstone.taskmanager.enums.TaskStatus.DONE ORDER BY t.deadline ASC")
    List<Task> findOverdueTasks(@Param("today") LocalDate today);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.deadline < :today AND t.status <> com.capstone.taskmanager.enums.TaskStatus.DONE ORDER BY t.deadline ASC")
    List<Task> findOverdueTasksByProject(@Param("projectId") Long projectId,
                                         @Param("today") LocalDate today);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee.id = :userId AND t.deadline < :today AND t.status <> com.capstone.taskmanager.enums.TaskStatus.DONE")
    long countOverdueTasksByUser(@Param("userId") Long userId, @Param("today") LocalDate today);

    long countByProjectIdAndStatusNot(Long projectId, TaskStatus status);
}
