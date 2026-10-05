# Backend Architecture

This document describes the current backend architecture for the Ultimate Fitness project.

The backend is built with Kotlin and Ktor.
Its purpose is to provide a controlled API layer between the client application and the PostgreSQL/Supabase database.

The backend should not only pass data through. It should also handle validation, security, business rules, error handling and clean API responses.

## Current Backend Status

The Ktor backend project exists inside the `backend/` directory.

The server can be started locally with:

```bash
./gradlew run
```

On Windows:

```powershell
.\gradlew.bat run
```

The health endpoint is available at:

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
- Core fitness tables created
- Row Level Security enabled for relevant tables
- Authenticated-user policies configured for user-owned data
- Read-only RLS access configured for global reference tables
- Exposed table mappings implemented
- Repository layer implemented
- Coroutine-friendly repository functions implemented with `suspend`
- Exposed database work uses `suspendTransaction`
- Write repositories implemented for user-owned training data
- Read-only repositories implemented for global reference data
- Local Supabase seed data configured
- Supabase Auth test helper implemented
- Repository integration tests implemented
- Complete backend test suite currently passes with 108 tests
- HikariCP connection pool reuse implemented for repeated test initialization

The next planned backend milestone is the service layer.

## Layer Structure

The backend follows this structure:

```text
Route
→ Service
→ Repository
→ Database
```

The layers have separate responsibilities and should remain independent where possible.

### Route

Routes receive HTTP requests and return HTTP responses.

Examples:

- `GET /health`
- `GET /exercises`
- `POST /training-days`

Routes should stay thin.

They should be responsible for tasks such as:

- reading request parameters
- reading authenticated user information
- deserializing request DTOs
- calling the appropriate service
- translating service results into HTTP responses

Routes should not contain SQL queries or complex business logic.

### Service

Services contain application and business logic.

The service layer is not implemented yet and is the next planned development phase.

Services will be responsible for:

- validating business rules
- coordinating one or more repositories
- deciding which repository operations are required
- handling domain-level conflicts
- composing richer application/domain objects
- making authorization-related decisions
- returning domain-level results to routes

Example:

```text
POST /training-days
        ↓
TrainingDayRoute
        ↓
TrainingDayService
        ↓
TrainingDayRepository
        ↓
PostgreSQL
```

A service should not return HTTP-specific values such as `HttpStatusCode`.

Instead, expected domain outcomes can later be represented through domain result types, for example:

```text
Success
AlreadyExists
ValidationError
```

The route is responsible for translating those outcomes into HTTP responses such as `201 Created`, `400 Bad Request` or `409 Conflict`.

### Repository

Repositories are responsible only for database access.

Repository responsibilities:

- execute Exposed queries
- map database rows to Kotlin data classes
- expose database operations to the service layer
- keep SQL/database access out of routes and services

Repositories should not contain:

- HTTP-specific behavior
- request/response handling
- business workflows
- UI logic

Repository functions are coroutine-friendly:

```text
suspend fun ...
```

Database work uses:

```text
suspendTransaction
```

### DTO

DTOs define the data that enters or leaves the API.

The API model should not blindly expose internal database tables.

DTOs will be added as the HTTP/API layer grows.

## Repository Layer

### Write Repositories

Repositories that manage user-owned training data currently include:

- Profiles
- Training Days
- Strength Entries
- Strength Rep Sets
- Strength Time Sets
- Endurance Entries
- Boulder Entries
- Boulder Routes

These repositories support the database operations required by the current model, including create, read, update and delete operations where applicable.

### Read-Only Reference Repositories

Global reference data is intentionally exposed through read-only repositories.

Current read-only repositories:

- Difficulty Grades
- Exercise Categories
- Exercises
- Muscle Groups
- Exercise Muscle Groups

These repositories support frontend use cases such as:

- loading all difficulty grades
- loading available difficulty grading systems
- filtering difficulty grades by system
- loading exercises
- filtering exercises by category
- finding exercises by name
- loading muscle groups
- finding muscle groups by name
- loading exercise-to-muscle-group relationships by exercise
- loading exercise-to-muscle-group relationships by muscle group

The application repository layer does not expose create, update or delete methods for these reference tables.

## Database Model

The central training structure uses `training_days` as the anchor.

```text
training_days
├── strength_entries
│   ├── strength_rep_sets
│   └── strength_time_sets
├── endurance_entries
└── boulder_entries
    └── boulder_routes
```

Additional reference relationships include:

```text
strength_entries
└── exercises

endurance_entries
└── exercises

exercises
├── exercise_categories
└── exercise_muscle_groups
    └── muscle_groups

boulder_routes
└── difficulty_grades
```

This keeps strength, endurance and bouldering separate while allowing them to belong to the same training day.

## Database Connection Management

Database access uses HikariCP and Exposed.

`DatabaseConfig` maintains a reusable HikariCP data source.

Repeated calls using the same database configuration reuse the existing pool instead of creating a new pool for every test or initialization call.

This prevents PostgreSQL connection exhaustion when the complete integration-test suite is executed.

If a different database configuration is requested, the previous pool is closed before a new one is created.

The configured pool currently uses a maximum size of 5 connections.

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
- backend services will add an additional authorization and validation layer

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

These tables currently allow normal authenticated client access for reading only:

```text
SELECT  -> authenticated users
INSERT  -> not allowed through normal RLS policies
UPDATE  -> not allowed through normal RLS policies
DELETE  -> not allowed through normal RLS policies
```

Changes to reference data should be performed in a controlled way through backend administration, database administration or versioned database migrations/seeds.

Sensitive configuration such as database URLs, passwords and API keys must be provided through environment variables.

## Testing Strategy

Testing is split into different levels.

### Repository Integration Tests

Implemented now.

These tests use:

- local Supabase
- real PostgreSQL I/O
- real Exposed queries
- temporary Supabase Auth users where required
- seeded reference data

Repository integration tests intentionally use `runBlocking`.

They perform real external I/O against the local Supabase/Auth/PostgreSQL stack, so Kotlin coroutine test virtual-time behavior is not required here.

The complete current suite contains 108 tests and passes successfully.

### Service Unit Tests

Planned for the service phase.

Service tests should be fast and should normally avoid real database access by using fakes or mocks.

`runTest` will be appropriate for coroutine-based service unit tests because those tests will not depend on real PostgreSQL or Supabase network/database I/O.

### API Integration Tests

Planned after routes and services are implemented.

These tests should verify complete flows:

```text
HTTP Request
→ Route
→ Service
→ Repository
→ Database
→ HTTP Response
```

### Security / RLS Tests

Still planned.

These tests should verify that one authenticated user cannot access or modify another user's data.

### CI/CD

Automated execution through GitHub Actions is planned for a later phase.

## Local Test Environment

Repository tests use dedicated test environment variables:

```text
TEST_DATABASE_URL
TEST_DATABASE_USER
TEST_DATABASE_PASSWORD
TEST_SUPABASE_URL
TEST_SUPABASE_SECRET_KEY
```

The local test environment is loaded with `direnv`.

`.env.test` contains local test credentials and must not be committed.

`.envrc` loads `.env.test` and can be version controlled because it contains no secret values itself.

## Local Supabase Seeds

The local Supabase database can be recreated with:

```bash
supabase db reset
```

Seed files are applied in dependency order.

Current seed files:

```text
supabase/seeds/02_seed_core_data.sql
supabase/seeds/03_seed_exercises.sql
supabase/seeds/04_seed_exercises_muscle_groups.sql
```

The seed data provides:

- exercise categories
- muscle groups
- difficulty grades
- exercises
- exercise-to-muscle-group mappings

Tests use seeded reference data rather than relying on fixed identity IDs.

## API Plan

Implemented:

- `GET /health`

Planned:

- exercise/reference-data endpoints
- training-day endpoints
- strength endpoints
- endurance endpoints
- bouldering endpoints

The API should be implemented after the service layer so that routes remain thin and business rules stay out of HTTP handlers.

## Next Milestone

The repository and repository-integration-test phase is complete.

The next step is a new service-layer development phase.

The intended progression is:

```text
Repository layer complete
→ Service layer
→ Service unit tests
→ DTOs and routes
→ API integration tests
→ Security/RLS tests
→ Logging and broader operational concerns
→ CI/CD
```
