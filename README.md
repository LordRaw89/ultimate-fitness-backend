# Ultimate Fitness Backend

Backend and database layer for a modular fitness tracking app.

## Goal

This project contains the backend and database structure for a fitness tracking application.

The app is designed around separate training domains instead of forcing every activity into a single generic workout model.

## Main Domains

- Strength training
- Endurance training
- Bouldering

## Tech Stack

- PostgreSQL
- Supabase
- Kotlin
- Ktor
- Exposed DSL
- PostgreSQL JDBC Driver
- HikariCP Connection Pool
- Gradle

## Architecture

The backend follows a layered structure:

```text
Route
→ Service
→ Repository
→ Database
```

### Repository Layer

The repository layer is responsible for database access.

Repositories:

- use Exposed SQL DSL
- expose coroutine-friendly `suspend` functions
- use `suspendTransaction`
- map database rows to Kotlin data classes
- do not contain HTTP-specific logic
- do not contain business logic

### Service Layer

The service layer is the next backend milestone.

Services will be responsible for:

- business rules
- validation
- coordinating multiple repositories
- translating database-level conflicts into domain-level results
- authorization-related decisions
- composing richer domain objects for the API

## Current Status

### Backend Foundation

- Initial Ktor backend created
- Health endpoint implemented
- Central Ktor application module configured
- PostgreSQL connection to Supabase configured
- HikariCP connection pool integrated
- Exposed connected to PostgreSQL
- Startup database connection check implemented with `SELECT 1`
- Database connection integration test implemented
- Repeated database initialization reuses the existing HikariCP pool
- Database pool can be shut down explicitly through `DatabaseConfig.shutdown()`

### Database

- Database schema created
- Core seed data created
- Exercise catalog created
- Muscle group mappings with primary, secondary and stabilizing roles
- Row Level Security policies implemented
- Indexes implemented
- Foreign key relationships mapped across the training domains

### Exposed Table Mappings

The following tables are mapped with Exposed:

- `profiles`
- `training_days`
- `exercise_categories`
- `exercises`
- `muscle_groups`
- `exercise_muscle_groups`
- `difficulty_grades`
- `strength_entries`
- `strength_rep_sets`
- `strength_time_sets`
- `endurance_entries`
- `boulder_entries`
- `boulder_routes`

### Repository Layer

Write repositories are implemented for user-owned training data:

- Profiles
- Training Days
- Strength Entries
- Strength Rep Sets
- Strength Time Sets
- Endurance Entries
- Boulder Entries
- Boulder Routes

Read-only repositories are implemented for reference data:

- Difficulty Grades
- Exercise Categories
- Exercises
- Muscle Groups
- Exercise Muscle Groups

Read-only reference repositories expose the queries needed by the frontend, including:

- difficulty grades by grading system
- available grading systems
- exercises by category
- exercises by name
- muscle groups by name
- exercise-to-muscle-group mappings by exercise or muscle group

## Integration Tests

Repository integration tests run against the local Supabase PostgreSQL database.

Covered areas include:

- create, read, update and delete operations where applicable
- foreign key constraints
- unique constraints
- check constraints
- nullable values
- unknown IDs
- timestamp updates
- seed-based reference data
- Supabase Auth test users
- read-only reference repositories

The complete backend test suite currently contains **108 tests** and passes successfully.

Run all tests with:

```bash
./gradlew test
```

Run a single repository test class with:

```bash
./gradlew test --tests "de.exercise.ExerciseRepositoryTest"
```

### Test Environment

The integration tests use these environment variables:

```text
TEST_DATABASE_URL
TEST_DATABASE_USER
TEST_DATABASE_PASSWORD
TEST_SUPABASE_URL
TEST_SUPABASE_SECRET_KEY
```

The local test environment is loaded through `direnv`.

`.env.test` contains the local test credentials and must not be committed.

`.envrc` loads `.env.test` and may be version controlled because it contains no credentials itself.

### Test Data Lifecycle

Tests that require profiles create temporary Supabase Auth users through `SupabaseAuthTestHelper`.

Typical lifecycle:

```text
Create Supabase Auth test user
→ Create profile
→ Create required training data
→ Execute test
→ Delete child records
→ Delete profile
→ Delete Supabase Auth user
```

Cleanup is performed with `try/finally`.

## Local Supabase Database

The local database can be recreated with:

```bash
supabase db reset
```

Supabase migrations create the schema and the configured seed files are applied in dependency order.

Current seed files:

```text
supabase/seeds/02_seed_core_data.sql
supabase/seeds/03_seed_exercises.sql
supabase/seeds/04_seed_exercises_muscle_groups.sql
```

The seed data provides reference data for:

- exercise categories
- muscle groups
- difficulty grades
- exercises
- exercise-to-muscle-group mappings

Repository tests use seeded reference data instead of relying on fixed database IDs.

## Security Notes

Local backups and environment files are ignored via `.gitignore`.

Do not commit:

- `.env`
- `.env.test`
- Supabase keys
- database passwords
- local database backups
- real user data

Secrets, passwords and JWTs must never be written to logs.

## Next Milestone

The repository and integration-test phase is complete.

The next planned step is the service layer, where business logic will be added on top of the tested repository foundation.
