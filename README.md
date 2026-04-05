# Task and Project Management Tool

Capstone Project — 40-Hours Java Backend Development using Spring Boot PICT

---

## Problem Statement

Create a project management tool where users can create projects, add tasks, assign them to team members, and track progress. Tasks have attributes like priority, status, and deadlines. The system supports filtering tasks and tracking completion status per project.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Database | H2 (default) / MySQL |
| ORM | Spring Data JPA + Hibernate |
| Validation | Spring Boot Validation (Jakarta) |
| Build Tool | Maven |
| Utility | Lombok |

---

## Project Structure

```
src/main/java/com/capstone/taskmanager/
├── config/          DataInitializer (sample data on startup)
├── controller/      UserController, ProjectController, TaskController
├── dto/
│   ├── request/     CreateUser/Project/Task, UpdateUser/Project/Task
│   └── response/    UserResponse, ProjectResponse, TaskResponse, etc.
├── entity/          User, Project, Task
├── enums/           TaskStatus, TaskPriority, ProjectStatus
├── exception/       GlobalExceptionHandler, ResourceNotFoundException, BadRequestException
├── mapper/          UserMapper, ProjectMapper, TaskMapper
├── repository/      UserRepository, ProjectRepository, TaskRepository
└── service/         UserService, ProjectService, TaskService
```

---

## Getting Started

### Prerequisites

- Java JDK 21 or higher
- Maven 3.6+
- IntelliJ IDEA (recommended) or any IDE

### Run with H2 (default)

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080` with an in-memory H2 database. Sample data (3 users, 2 projects, 7 tasks) is loaded automatically on startup.

### Run with MySQL

1. Create the database:
   ```sql
   CREATE DATABASE taskmanagerdb;
   ```

2. Update credentials in `src/main/resources/application-mysql.properties`:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Run with the MySQL profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## Database

**H2 Console** (available when running with H2 profile):
URL: `http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:taskmanagerdb`
Username: `sa`
Password: *(leave blank)*

### Entity Relationships

```
User ──< Project    (one user owns many projects)
Project ──< Task    (one project has many tasks, cascade delete)
User ──< Task       (one user can be assigned many tasks, optional)
```

---

## API Endpoints

### Users — `/api/users`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create a new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/role/{role}` | Get users by role |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/{id}/tasks` | Get tasks assigned to user |
| GET | `/api/users/{id}/tasks/summary` | Get task count summary for user |

### Projects — `/api/projects`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/projects` | Create a new project |
| GET | `/api/projects` | Get all projects |
| GET | `/api/projects/{id}` | Get project by ID (includes tasks) |
| PUT | `/api/projects/{id}` | Update project |
| DELETE | `/api/projects/{id}` | Delete project (cascades to tasks) |
| GET | `/api/projects/owner/{ownerId}` | Get projects by owner |
| GET | `/api/projects/status/{status}` | Get projects by status |
| GET | `/api/projects/{id}/completion` | Get completion statistics |
| PATCH | `/api/projects/{id}/archive` | Archive a project |
| GET | `/api/projects/{id}/tasks` | Get all tasks in a project |
| GET | `/api/projects/{id}/tasks/overdue` | Get overdue tasks in a project |

### Tasks — `/api/tasks`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks/{id}` | Get task by ID |
| GET | `/api/tasks` | Filter tasks (see query params below) |
| PUT | `/api/tasks/{id}` | Update task |
| PATCH | `/api/tasks/{id}/status` | Update task status only |
| PATCH | `/api/tasks/{id}/assign/{userId}` | Assign task to a user |
| PATCH | `/api/tasks/{id}/unassign` | Remove task assignment |
| DELETE | `/api/tasks/{id}` | Delete task |
| GET | `/api/tasks/overdue` | Get all overdue tasks |

**Filter query parameters for `GET /api/tasks`:**

```
?projectId=1          filter by project (optional)
&status=IN_PROGRESS   filter by status: TODO, IN_PROGRESS, DONE (optional)
&priority=HIGH        filter by priority: LOW, MEDIUM, HIGH (optional)
&assigneeId=2         filter by assigned user (optional)
```

All parameters are optional and can be combined freely.

---

## Sample Requests

**Create a user:**
```json
POST /api/users
{
  "username": "alice",
  "fullName": "Alice Johnson",
  "email": "alice@example.com",
  "role": "MANAGER"
}
```

**Create a project:**
```json
POST /api/projects
{
  "name": "E-Commerce Platform",
  "description": "Build the backend for an e-commerce app",
  "startDate": "2025-04-01",
  "endDate": "2025-06-30",
  "ownerId": 1
}
```

**Create a task:**
```json
POST /api/tasks
{
  "title": "Design Database Schema",
  "description": "Create all required tables and relationships",
  "priority": "HIGH",
  "status": "TODO",
  "deadline": "2025-04-15",
  "projectId": 1,
  "assigneeId": 2
}
```

**Update task status:**
```json
PATCH /api/tasks/1/status
{
  "status": "DONE"
}
```

---

## Business Rules

- A project automatically moves to `COMPLETED` status when all its tasks are marked `DONE`.
- A task is flagged as `overdue` if its deadline has passed and it is not yet `DONE`.
- Deleting a user with open (non-DONE) tasks is rejected until tasks are reassigned or completed.
- Deleting a project cascades and removes all associated tasks.
- The `completedAt` timestamp on a task is set automatically when status changes to `DONE` and cleared if status is reverted.

---

## Enums

**TaskStatus:** `TODO`, `IN_PROGRESS`, `DONE`

**TaskPriority:** `LOW`, `MEDIUM`, `HIGH`

**ProjectStatus:** `ACTIVE`, `ON_HOLD`, `COMPLETED`, `ARCHIVED`

---

## Error Responses

All errors return a structured JSON response:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id: 99",
  "timestamp": "2025-04-05T10:30:00"
}
```

Validation errors return field-level details:

```json
{
  "status": 400,
  "error": "Validation Failed",
  "fieldErrors": {
    "title": "Task title is required",
    "projectId": "Project ID is required"
  }
}
```

---

## API Testing

A Postman collection is included at the root of the project:

```
TaskManager_Postman_Collection.json
```

Import it into Postman. The base URL is pre-configured as `http://localhost:8080`. The collection covers all endpoints including validation error cases.
