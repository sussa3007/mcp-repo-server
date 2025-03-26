# MCP Repository Server

A centralized platform for managing and discovering Model Context Protocol (MCP) server and client repositories.

## Project Overview

The MCP Repository Server is a web application that provides a centralized hub for MCP-related repositories. It allows developers to discover, explore, and contribute to the MCP ecosystem by offering comprehensive information about various MCP server and client implementations.

## Key Features

- **Repository Management**

  - Server repository listing with filtering and sorting
  - Client repository listing with filtering and sorting
  - Detailed repository information view
  - GitHub integration for metadata and README synchronization

- **User Management**

  - User registration and authentication
  - JWT-based authorization
  - GitHub OAuth integration
  - User profile management

- **Content Management**

  - Blog system for MCP-related news and updates
  - Use case showcase
  - Markdown editor support
  - Tag and category management

- **Submission System**
  - New repository submission process
  - Admin review workflow
  - Automated verification using GitHub API
  - Status tracking

## Technology Stack

### Backend

- Java 17+
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- QueryDSL
- MySQL
- Redis (caching)

### Frontend (Planned Integration)

- Next.js 15.2.0 (App Router)
- TypeScript
- React 18.3.1
- Shadcn UI + Radix UI
- TailwindCSS
- React Hook Form + Zod

### Infrastructure

- Docker
- Kubernetes
- Nginx
- GitHub Actions (CI/CD)

## Project Structure

```
src/main/java/com/miraclestudio/mcpreposerver
├── config/           # Configuration classes
│   ├── security/     # Security configurations
│   ├── redis/        # Redis configurations
│   ├── web/          # Web configurations (CORS, etc.)
│   └── jpa/          # JPA configurations
├── controller/       # API controllers
├── domain/           # Entity classes
├── dto/              # Data Transfer Objects
├── exception/        # Custom exceptions
├── repository/       # Data access layer
├── security/         # Security components
├── service/          # Business logic
└── util/             # Utility classes
```

## API Endpoints

The API follows a RESTful design with consistent response structures:

### GitHub Integration API

- `GET /api/v1/github/:owner/:repo/readme` - Get repository README
- `GET /api/v1/github/:owner/:repo/contents/:path?` - Get repository contents

### Repository APIs

- `GET /api/v1/servers` - Get server repository list
- `GET /api/v1/servers/:id` - Get server repository details
- `GET /api/v1/clients` - Get client repository list
- `GET /api/v1/clients/:id` - Get client repository details

### Content APIs

- `GET /api/v1/posts` - Get blog post list
- `GET /api/v1/posts/:id` - Get blog post details
- `GET /api/v1/use-cases` - Get use case list
- `GET /api/v1/use-cases/:id` - Get use case details

### Submission API

- `POST /api/v1/submissions` - Submit a new repository
- `GET /api/v1/submissions/:id` - Check submission status

### User APIs

- `POST /api/v1/user/register` - Register new user
- `POST /api/v1/user/login` - User login
- `GET /api/v1/user` - Get authenticated user info

## Development Phases

### Phase 1 (MVP)

- Basic repository management
- GitHub integration
- User authentication

### Phase 2

- Blog system
- Use case management
- Advanced search features

### Phase 3

- Analytics dashboard
- API usage monitoring
- Community features

## Getting Started

### Prerequisites

- JDK 17+
- Maven or Gradle
- MySQL
- Redis

### Setup

1. Clone the repository
2. Configure application.properties with your database and GitHub API credentials
3. Run the application using Maven or your IDE

## Contributing

We welcome contributions to the MCP Repository Server. Please check our contribution guidelines for more details.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
