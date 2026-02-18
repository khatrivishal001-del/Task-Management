# Task Management Application

A modern, full-stack Task Management application built with **Spring Boot** (backend) and **React with TypeScript** (frontend). This application enables users to create, read, update, and delete tasks with a clean, intuitive interface.

## Features

### Backend (Java Spring Boot)
- ✅ RESTful API endpoints with proper HTTP status codes
- ✅ Data persistence using JPA/Hibernate with H2 database
- ✅ Input validation and error handling
- ✅ Business logic for task management
- ✅ CORS support for frontend integration
- ✅ Unit tests with JUnit and Mockito
- ✅ Pagination and filtering support

### Frontend (React + TypeScript)
- ✅ Single-Page Application (SPA) architecture
- ✅ Task list view with completion status
- ✅ Add, edit, and delete tasks
- ✅ Toggle task completion status
- ✅ Filter tasks (all, completed, pending)
- ✅ Responsive UI design
- ✅ Error handling and loading states
- ✅ E2E tests with Playwright

## Tech Stack

| Component | Technology |
|-----------|------------|
| Backend | Java 17, Spring Boot 3.x, Spring Data JPA |
| Frontend | React 18, TypeScript, CSS3 |
| Database | H2 (In-Memory) / PostgreSQL / MySQL |
| Testing | JUnit, Mockito, Jest, Playwright |
| Build Tools | Maven (Backend), npm (Frontend) |

## Project Structure

```
Task-Management/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/taskmanagement/
│   │   │   │   ├── model/Task.java
│   │   │   │   ├── repository/TaskRepository.java
│   │   │   │   ├── service/TaskService.java
│   │   │   │   ├── controller/TaskController.java
│   │   │   │   └── TaskManagementApplication.java
│   │   │   └── resources/application.yml
│   │   └── test/
│   │       └── java/com/taskmanagement/service/TaskServiceTest.java
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── TaskList.tsx
│   │   │   ├── TaskForm.tsx
│   │   │   └── TaskItem.tsx
│   │   ├── services/
│   │   │   └── taskService.ts
│   │   ├── types/
│   │   │   └── Task.ts
│   │   ├── App.tsx
│   │   └── App.css
│   ├── package.json
│   └── tsconfig.json
└── README.md
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16+ and npm
- Maven 3.8+
- Git

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   The backend will start at `http://localhost:8080`

4. **Access H2 Console (optional):**
   - Navigate to `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm start
   ```
   The frontend will open at `http://localhost:3000`

## API Endpoints

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks with pagination |
| GET | `/api/tasks?isCompleted=true` | Get completed tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create a new task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |
| PATCH | `/api/tasks/{id}/toggle` | Toggle task completion status |

### Request/Response Examples

**Create Task (POST /api/tasks)**
```json
{
  "title": "Complete project",
  "description": "Finish the task management application",
  "dueDate": "2024-12-31T23:59:59"
}
```

**Response (201 Created)**
```json
{
  "id": 1,
  "title": "Complete project",
  "description": "Finish the task management application",
  "isCompleted": false,
  "dueDate": "2024-12-31T23:59:59",
  "createdAt": "2024-02-18T11:00:00",
  "updatedAt": "2024-02-18T11:00:00"
}
```

## Testing

### Backend Unit Tests
```bash
cd backend
mvn test
```

### Frontend Unit Tests
```bash
cd frontend
npm test
```

### E2E Tests
```bash
cd frontend
npm run test:e2e
```

## Architecture & Design Decisions

### Backend Architecture
1. **Layered Architecture:** Clear separation of concerns with controller, service, and repository layers
2. **Dependency Injection:** Using Spring's DI for loose coupling and testability
3. **Entity Validation:** Bean validation annotations (@Valid, @NotBlank) for input validation
4. **Error Handling:** Custom exception handling with meaningful HTTP status codes
5. **Database:** H2 in-memory database for development; easily switchable to PostgreSQL/MySQL for production

### Frontend Architecture
1. **Component-Based:** Modular, reusable React components
2. **Service Layer:** Abstracted API communication in separate service file
3. **Type Safety:** Full TypeScript support for better developer experience
4. **State Management:** useState for local state, with potential for Redux upgrade
5. **Responsive Design:** CSS Flexbox for responsive layouts

## Scalability & Performance Considerations

### Current Implementation
- Pagination support on API endpoints
- Indexed database queries for faster retrieval
- Client-side filtering for reduced server load

### Future Improvements
- Implement caching (Redis) for frequently accessed tasks
- Add database query optimization and indexing
- Implement API rate limiting
- Add comprehensive logging and monitoring
- Consider implementing GraphQL for more flexible queries
- Add authentication and authorization (JWT)

## Security Considerations

### Implemented
- Input validation at controller and entity level
- CORS configuration to allow only trusted origins
- SQL injection prevention through parameterized queries (JPA)

### Recommended for Production
- Implement JWT-based authentication
- Add role-based access control (RBAC)
- Use HTTPS for all communications
- Implement rate limiting
- Add comprehensive logging and audit trails
- Regular security updates and dependency scanning

## Accessibility

- Semantic HTML elements
- ARIA labels for form inputs
- Keyboard navigation support
- High contrast UI design
- Responsive design for various screen sizes

## Development Notes

### Running Both Services
Open two terminal windows:

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm start
```

### Troubleshooting

**Port 8080 already in use:**
```bash
# Find process using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
```

**CORS Error:**
Ensure backend is running on `http://localhost:8080` and frontend makes requests to this URL.

**Database Connection Issues:**
Check `application.yml` in backend/src/main/resources for correct database configuration.

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Open a Pull Request

## License

This project is open source and available under the MIT License.

## Author

Vishal Khatri - Java Backend Developer

## Support

For issues or questions, please create an issue in the GitHub repository.
