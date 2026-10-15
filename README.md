# Ultimate Fitness Backend

A Kotlin/Ktor backend for a modular fitness tracking application built around **strength training, endurance training and bouldering**.

The project is currently focused on building a clean, testable backend architecture on top of PostgreSQL and Supabase.

## Highlights

- Kotlin + Ktor backend
- PostgreSQL / Supabase database
- Exposed SQL DSL
- Coroutine-friendly repositories with `suspend` and `suspendTransaction`
- HikariCP connection pooling
- Row Level Security for user-owned data
- Reproducible local Supabase migrations and seed data
- Separate read-only repositories for global reference data
- Automated repository integration tests against local Supabase/PostgreSQL
- **108 automated tests currently passing**

## Training Domains

The data model deliberately keeps the main training domains separate instead of forcing every activity into one generic workout structure.

```text
training_days
├── strength_entries
│   ├── strength_rep_sets
│   └── strength_time_sets
├── endurance_entries
└── boulder_entries
    └── boulder_routes
```

Additional reference data includes:

```text
exercises
├── exercise_categories
└── exercise_muscle_groups
    └── muscle_groups

boulder_routes
└── difficulty_grades
```

## Tech Stack

- Kotlin
- Ktor
- PostgreSQL
- Supabase
- Exposed DSL
- PostgreSQL JDBC Driver
- HikariCP
- Gradle

## Architecture

The backend follows a layered architecture:

```text
Route
→ Service
→ Repository
→ Database
```

### Repository Layer

The repository layer is implemented and tested.

Repositories are responsible for database access only. They:

- use Exposed SQL DSL
- expose coroutine-friendly `suspend` functions
- use `suspendTransaction`
- map database rows to Kotlin data classes
- stay independent from HTTP-specific logic
- do not contain business workflows

### Service Layer

The service layer is currently under development.

Implemented:
- Exercise Category service
- Response DTO for API responses
- GET `/exercise-categories` to retrieve all exercise categories
- GET `/exercise-categories/{id}` to retrieve a category by ID
- Basic request handling for invalid IDs (`400 Bad Request`)
- Not-found handling for missing categories (`404 Not Found`)

- Muscle Group service
- Response DTO for API responses
- GET `/muscle-groups` to retrieve all muscle groups
- GET `/muscle-groups/{id}` to retrieve a muscle group by ID
- Basic request handling for invalid IDs (`400 Bad Request`)
- Not-found handling for missing muscle groups (`404 Not Found`)

- Difficulty Grade service
- Response DTO for API responses
- GET `/difficulty-grades` to retrieve all difficulty grades
- Difficulty grades include their ID, grade, and grading system
- Manual verification using `curl`

Services are responsible for:
- business rules
- validation
- coordinating multiple repositories
- translating database conflicts into domain-level results
- authorization-related decisions
- composing richer objects for the API

Routes will remain thin and translate service results into HTTP responses.

## Implemented Repositories

### User-Owned Training Data

Write-capable repositories are implemented for:

- Profiles
- Training Days
- Strength Entries
- Strength Rep Sets
- Strength Time Sets
- Endurance Entries
- Boulder Entries
- Boulder Routes

### Reference Data

Read-only repositories are implemented for:

- Difficulty Grades
- Exercise Categories
- Exercises
- Muscle Groups
- Exercise Muscle Groups

These repositories support frontend use cases such as:

- loading available grading systems
- filtering difficulty grades by system
- filtering exercises by category
- finding exercises by name
- loading muscle groups
- loading exercise-to-muscle-group mappings

## Testing

Repository integration tests run against a real local Supabase/PostgreSQL environment.

The current test suite covers:

- CRUD operations where applicable
- foreign key constraints
- unique constraints
- check constraints
- nullable fields
- unknown IDs
- timestamp updates
- seeded reference data
- temporary Supabase Auth test users
- read-only reference repositories
- database connection handling

The complete test suite currently contains **108 tests** and passes successfully.

From the `backend/` directory:

```bash
./gradlew test
```

To force all tests to execute again:

```bash
./gradlew test --rerun-tasks
```

Example for a single repository test class:

```bash
./gradlew test --tests "de.exercise.ExerciseRepositoryTest"
```

## Local Development

### Prerequisites

You will need:

- JDK 17
- Docker
- Supabase CLI
- Git

`direnv` is optional but recommended for local environment-variable loading.

### 1. Clone the Repository

```bash
git clone <repository-url>
cd ultimate-fitness-backend
```

### 2. Start Local Supabase

```bash
supabase start
```

Recreate the local database and apply the current migration and seed data with:

```bash
supabase db reset
```

The database source of truth lives under:

```text
supabase/
├── migrations/
└── seeds/
```

### 3. Configure Environment Variables

An example file is provided at:

```text
backend/.env.example
```

Create your own local test environment file:

```bash
cd backend
cp .env.example .env.test
```

Fill in the required local values.

The backend currently uses:

```text
DATABASE_URL
DATABASE_USER
DATABASE_PASSWORD

TEST_DATABASE_URL
TEST_DATABASE_USER
TEST_DATABASE_PASSWORD
TEST_SUPABASE_URL
TEST_SUPABASE_SECRET_KEY
```

Do **not** commit real credentials.

If `direnv` is installed, the existing `.envrc` can load the local `.env.test` values after:

```bash
direnv allow
```

### 4. Run the Tests

```bash
./gradlew test
```

### 5. Run the Backend

```bash
./gradlew run
```

The current health endpoint is:

```text
GET /health
```

Expected response:

```json
{
  "status": "ok"
}
```

## Database and Seed Data

Local Supabase migrations define the database schema, constraints, Row Level Security policies and indexes.

Seed files provide reference data for:

- exercise categories
- muscle groups
- difficulty grades
- exercises
- exercise-to-muscle-group mappings

Tests look up seeded reference records instead of relying on fixed identity IDs.

## Security

The project uses multiple layers of protection:

- database constraints protect data integrity
- Row Level Security restricts access to user-owned data
- reference tables are read-only for normal authenticated clients
- secrets are provided through environment variables
- local environment files and backups are ignored by Git

Never commit:

- `.env`
- `.env.test`
- database passwords
- Supabase secret keys
- JWTs
- local database backups
- real user data

## Documentation

Additional technical documentation is available in `docs/`, including:

- backend architecture
- database model
- database test protocol

## Current Status

Completed:

- database schema and relationships
- Supabase migrations
- reproducible seed data
- Row Level Security policies
- Exposed table mappings
- repository layer
- repository integration tests
- database connection-pool reuse
- project documentation

Next milestone:

```text
Service Layer
→ Implement remaining vertical slices
→ Service Unit Tests
→ API Integration Tests
→ Security / RLS Tests
→ Logging and Operational Improvements
→ CI/CD
```

## Project Status

This project is actively under development.

The repository layer and its integration-test foundation are complete.
The service layer is now under development, starting with the Exercise
Categories vertical slice. This slice includes the service, response DTO,
GET endpoints, and manually verified HTTP responses.

## License

This repository is published publicly for portfolio and review purposes.

No license is granted for the use, modification, or redistribution
of this code beyond the permissions provided by GitHub's Terms of Service.
All rights reserved.
