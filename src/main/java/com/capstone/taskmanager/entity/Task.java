package com.capstone.taskmanager.entity;

import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TaskStatus status;

    private LocalDate deadline;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User assignee;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = TaskStatus.TODO;
        }
        if (priority == null) {
            priority = TaskPriority.MEDIUM;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
