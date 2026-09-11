# Backend Architecture

This document describes the planned backend architecture for the Ultimate Fitness project.

The backend is built with Kotlin and Ktor.  
Its purpose is to provide a controlled API layer between the client application and the PostgreSQL/Supabase database.

The backend should not only pass data through. It should also handle validation, security, error handling and clean API responses.

## Current Backend Status

The initial Ktor backend project has been created inside the `backend/` directory.

The server can be started locally with:

```bash
./gradlew run
```

On Windows:

```powershell
.\gradlew.bat run
```

The first health endpoint is available at:

```text
GET /health
```

Expected response:

```json
{
  "status": "ok"
}
```

Currently implemented:

- Ktor project created
- Local server start works
- JSON response works
- `GET /health` is implemented
- PostgreSQL/Supabase database structure created
- core fitness tables created
- Row Level Security enabled for relevant tables
- authenticated user policies configured for user-owned data
- read-only RLS access configured for global reference tables

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

```text
Route → Service → Repository → Database
```

### Route

Routes receive HTTP requests and return HTTP responses.

Examples:

- `GET /health`
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

Security is handled in multiple layers.

The backend validates incoming requests and controls which operations are allowed.  
The database additionally protects important rules through constraints and Row Level Security (RLS).

The basic principles are:

- the client should not access the database directly
- secrets must never be committed to GitHub
- all user-owned data must only be accessible by the owning authenticated user
- Row Level Security is enabled for all relevant public tables
- RLS policies are restricted to the `authenticated` role where appropriate
- database constraints should reject invalid data even if backend validation fails

### Row Level Security

User-owned tables use RLS policies for:

- `SELECT`
- `INSERT`
- `UPDATE`
- `DELETE`

Access is restricted using `auth.uid()`.

For tables that contain a direct user reference, ownership can be checked directly against the authenticated user's ID.

For dependent tables such as `strength_entries`, ownership is checked through the related `training_days` row.

Example ownership check:

```sql
EXISTS (
    SELECT 1
    FROM training_days td
    WHERE td.training_day_id = strength_entries.training_day_id
      AND td.profile_id = auth.uid()
)
```

This ensures that users can only access strength entries that belong to one of their own training days.

Update policies use both `USING` and `WITH CHECK` where necessary.  
This prevents users from changing an existing row in a way that would move it into another user's data scope.

### Reference Tables

Global reference tables are readable by authenticated users but are not directly writable through normal client access.

Examples include:

- `difficulty_grades`
- `exercise_categories`
- `exercise_muscle_groups`
- `exercises`
- `muscle_groups`

These tables currently allow:

```text
SELECT  -> authenticated users
INSERT  -> not allowed through RLS policies
UPDATE  -> not allowed through RLS policies
DELETE  -> not allowed through RLS policies
```

Changes to reference data should be performed in a controlled way through backend logic, database administration or versioned database migrations.

Sensitive configuration such as database URLs, passwords and API keys must be provided through environment variables.

A `.env.example` file can be added to document required configuration values without exposing real secrets.

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

Planned / implemented first endpoints:

- `GET /health` - implemented
- `GET /exercises` - planned
- `GET /training-days/{date}` - planned

The first goal is not to implement every feature at once.  
The first goal is to verify that:

- the Ktor server starts correctly
- routing works
- JSON responses work
- the backend can read from the database
- the project structure is clean

Writing data should be added later after the basic read structure works reliably.
