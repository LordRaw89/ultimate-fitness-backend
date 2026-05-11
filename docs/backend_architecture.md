# Backend Architecture

This document describes the planned backend architecture for the Ultimate Fitness project.

The backend will be built with Kotlin and Ktor.  
Its purpose is to provide a controlled API layer between the client application and the PostgreSQL/Supabase database.

The backend should not only pass data through. It should also handle validation, security, error handling and clean API responses.

## Main Responsibilities

The backend is responsible for:

- exposing HTTP endpoints for the client
- validating incoming data before writing it to the database
- reading data from the database through repositories
- assembling structured API responses
- handling authentication and authorization
- returning consistent error responses
- keeping database access out of the client application

The database still protects important rules through constraints and Row Level Security.  
The backend adds an additional controlled application layer on top.

## Planned Layer Structure

The backend should be separated into clear layers.

Route → Service → Repository → Database

### Route

Routes receive HTTP requests and return HTTP responses.

Example:

- `GET /exercises`
- `POST /training-days`

Routes should stay thin.  
They should not contain SQL queries or complex business logic.

### Service

Services contain application and business logic.

They decide:

- whether input data is valid
- which repository methods are needed
- how errors should be handled
- how the final response should be assembled

### Repository

Repositories are responsible for database access.

They contain SQL queries and return data to the service layer.

### DTO

DTOs define the data that enters or leaves the API.

The API model should not blindly expose the internal database tables.

## Security Approach

Security should be handled in more than one layer.

The backend will validate incoming requests and decide which operations are allowed.  
The database also protects important rules through constraints and Row Level Security.

The basic idea is:

- the client should not access the database directly
- secrets must never be committed to GitHub
- user-owned data must only be accessible by the owning user
- database constraints should reject invalid data even if backend validation fails

Environment variables will be used for sensitive configuration such as database URLs, passwords and API keys.

A `.env.example` file can be added later to show required configuration values without exposing real secrets.

## Testing Strategy

Testing should be part of the backend from the beginning.

Planned test areas:

- unit tests for validation and service logic
- repository tests for database access
- integration tests for API endpoints
- security tests for authentication and user data access
- later CI/CD tests through GitHub Actions

Important cases to test:

- invalid input should be rejected
- database constraints should still protect the data
- users should not access other users' training data
- API responses should have the expected structure
- common workflows should work from request to database and back

## Initial API Plan

The backend should start small and grow step by step.

Planned first endpoints:

- `GET /health`
- `GET /exercises`
- `GET /training-days/{date}`

The first goal is not to implement every feature at once.  
The first goal is to verify that:

- the Ktor server starts correctly
- routing works
- JSON responses work
- the backend can read from the database
- the project structure is clean

Writing data should be added later after the basic read structure works reliably.
