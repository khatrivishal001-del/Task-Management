# Complete Setup Guide - Task Management Application

## Quick Start

This guide provides all the code and steps to set up the complete Task Management application locally.

## Backend Setup (Java/Spring Boot)

### Step 1: Create Backend Project Structure

```bash
mkdir -p backend/src/main/java/com/taskmanagement/{model,repository,service,controller}
mkdir -p backend/src/main/resources
mkdir -p backend/src/test/java/com/taskmanagement/service
```

### Step 2: Copy Files

The backend uses:
- **pom.xml** - Maven configuration (already added)
- **application.yml** - Spring Boot configuration
- **Task.java** - JPA Entity
- **TaskRepository.java** - Data layer
- **TaskService.java** - Business logic
- **TaskController.java** - REST API
- **TaskManagementApplication.java** - Main application class
- **TaskServiceTest.java** - Unit tests

### Step 3: Add Application Configuration

Create: `backend/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: task-management-backend
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password: 
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080
  servlet:
    context-path: /
```

### Step 4: Build and Run

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## Frontend Setup (React + TypeScript)

### Step 1: Create React App

```bash
npx create-react-app frontend --template typescript
cd frontend
```

### Step 2: Install Dependencies

```bash
npm install axios
npm install --save-dev @testing-library/react @testing-library/jest-dom
npm install --save-dev @playwright/test
```

### Step 3: Project Structure

Create the following structure:

```
frontend/src/
├── components/
│   ├── TaskList.tsx
│   ├── TaskForm.tsx
│   └── TaskItem.tsx
├── services/
│   └── taskService.ts
├── types/
│   └── Task.ts
├── App.tsx
├── App.css
└── index.tsx
```

### Step 4: Start Frontend

```bash
npm start
```

## File Contents Summary

All files are organized in the GitHub repository under:
- `backend/` - Java Spring Boot project
- `frontend/` - React TypeScript project

## Running Complete Application

### Terminal 1 - Backend
```bash
cd backend
mvn spring-boot:run
# Backend runs on http://localhost:8080
```

### Terminal 2 - Frontend
```bash
cd frontend
npm start
# Frontend opens at http://localhost:3000
```

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### E2E Tests
```bash
cd frontend
npx playwright test
```

## API Documentation

Once backend is running, access:
- H2 Console: http://localhost:8080/h2-console
- API Health: http://localhost:8080/actuator/health

## Common Issues

**CORS Error:**
- Ensure backend is running on http://localhost:8080
- Frontend is running on http://localhost:3000

**Port Already in Use:**
```bash
# Kill process on port 8080
lsof -i :8080
kill -9 <PID>
```

**Dependencies not found:**
```bash
# Clear Maven cache
rm -rf ~/.m2/repository
mvn clean install
```

## Next Steps

1. Clone all Java source files from repository
2. Clone all React TypeScript files from repository
3. Run backend and frontend in separate terminals
4. Access application at http://localhost:3000

## File Generation Commands

To quickly generate the complete project structure:

### Backend Generation
```bash
cd backend
mvn archetype:generate \
  -DgroupId=com.taskmanagement \
  -DartifactId=task-management \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 \
  -DinteractiveMode=false
```

### Frontend Generation
```bash
npx create-react-app task-management-frontend --template typescript
```

## Git Push Steps

```bash
# Add all files
git add .

# Commit
git commit -m "feat: Add complete Task Management application"

# Push to GitHub
git push origin main
```

## Assessment Submission

Once complete:
1. Ensure all tests pass
2. Verify README and setup instructions
3. Push final code to GitHub
4. Share repository link: https://github.com/khatrivishal001-del/Task-Management
5. Submit assessment in Coderbyte

## Additional Resources

For detailed code implementation, refer to:
- README.md - Full project documentation
- Backend source files - Java implementation
- Frontend source files - React TypeScript implementation

## Support

For questions about implementation, refer to the comprehensive README.md file in the root directory.
