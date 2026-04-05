package com.capstone.taskmanager.config;

import com.capstone.taskmanager.entity.Project;
import com.capstone.taskmanager.entity.Task;
import com.capstone.taskmanager.entity.User;
import com.capstone.taskmanager.enums.ProjectStatus;
import com.capstone.taskmanager.enums.TaskPriority;
import com.capstone.taskmanager.enums.TaskStatus;
import com.capstone.taskmanager.repository.ProjectRepository;
import com.capstone.taskmanager.repository.TaskRepository;
import com.capstone.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // Create users
        User alice = userRepository.save(User.builder()
                .username("alice")
                .fullName("Alice Johnson")
                .email("alice@example.com")
                .role("MANAGER")
                .build());

        User bob = userRepository.save(User.builder()
                .username("bob")
                .fullName("Bob Smith")
                .email("bob@example.com")
                .role("DEVELOPER")
                .build());

        User carol = userRepository.save(User.builder()
                .username("carol")
                .fullName("Carol Williams")
                .email("carol@example.com")
                .role("DEVELOPER")
                .build());

        // Create Project 1
        Project ecommerce = projectRepository.save(Project.builder()
                .name("E-Commerce Platform")
                .description("Build a full-featured e-commerce backend")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusMonths(2))
                .status(ProjectStatus.ACTIVE)
                .owner(alice)
                .build());

        // Create Project 2
        Project inventory = projectRepository.save(Project.builder()
                .name("Inventory Management System")
                .description("Internal inventory tracking tool")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .status(ProjectStatus.ACTIVE)
                .owner(alice)
                .build());

        // Tasks for Project 1
        taskRepository.save(Task.builder()
                .title("Setup Database Schema")
                .description("Design and create all required database tables")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.DONE)
                .deadline(LocalDate.now().minusDays(5))
                .project(ecommerce)
                .assignee(bob)
                .build());

        taskRepository.save(Task.builder()
                .title("Implement User Authentication")
                .description("JWT-based auth with login and registration endpoints")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.IN_PROGRESS)
                .deadline(LocalDate.now().plusDays(5))
                .project(ecommerce)
                .assignee(bob)
                .build());

        taskRepository.save(Task.builder()
                .title("Product Catalog API")
                .description("CRUD endpoints for product management")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.TODO)
                .deadline(LocalDate.now().plusDays(14))
                .project(ecommerce)
                .assignee(carol)
                .build());

        taskRepository.save(Task.builder()
                .title("Shopping Cart Service")
                .description("Add to cart, remove, checkout flow")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.TODO)
                .deadline(LocalDate.now().plusDays(20))
                .project(ecommerce)
                .assignee(carol)
                .build());

        taskRepository.save(Task.builder()
                .title("Write Unit Tests")
                .description("Cover all service layer methods")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.TODO)
                .deadline(LocalDate.now().minusDays(2))
                .project(ecommerce)
                .assignee(bob)
                .build());

        // Tasks for Project 2
        taskRepository.save(Task.builder()
                .title("Stock Level Alerts")
                .description("Email alerts when stock drops below threshold")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.TODO)
                .deadline(LocalDate.now().plusDays(7))
                .project(inventory)
                .assignee(carol)
                .build());

        taskRepository.save(Task.builder()
                .title("Inventory Report Generation")
                .description("Generate monthly PDF reports")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.TODO)
                .deadline(LocalDate.now().plusDays(30))
                .project(inventory)
                .build());

        log.info("Sample data initialized: 3 users, 2 projects, 7 tasks.");
    }
}
