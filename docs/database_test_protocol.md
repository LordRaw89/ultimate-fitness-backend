# Database Test Protocol

This document describes the current database and repository test strategy for the Ultimate Fitness backend.

It preserves the original manual database verification and documents the automated repository integration tests that now protect the implemented backend data-access layer.

## Purpose

The goal of the database tests is to verify that:

- the database model works for realistic fitness tracking scenarios
- foreign keys and database constraints reject invalid data
- repository code correctly reads and writes the database
- seed-based reference data can be queried correctly
- Supabase Auth test users can be created and cleaned up safely
- the complete repository integration-test suite can run against the local Supabase stack

## Test Environment

Current environment:

- Database: local Supabase / PostgreSQL
- Backend: Kotlin / Ktor
- Database access: Exposed DSL
- Connection pool: HikariCP
- Authentication test support: local Supabase Auth
- Test runner: Gradle / Kotlin test

Required environment variables:

```text
TEST_DATABASE_URL
TEST_DATABASE_USER
TEST_DATABASE_PASSWORD
TEST_SUPABASE_URL
TEST_SUPABASE_SECRET_KEY
```

The local test environment is loaded with `direnv`.

`.env.test` contains local credentials and must not be committed.

`.envrc` loads `.env.test` and can be version controlled because it contains no credentials itself.

## Local Database Setup

The local Supabase database can be recreated with:

```bash
supabase db reset
```

The database schema is recreated and seed files are applied in dependency order.

Current seed files:

```text
supabase/seeds/02_seed_core_data.sql
supabase/seeds/03_seed_exercises.sql
supabase/seeds/04_seed_exercises_muscle_groups.sql
```

Seed data provides reference data for:

- exercise categories
- muscle groups
- difficulty grades
- exercises
- exercise-to-muscle-group mappings

Repository tests use seeded records instead of relying on fixed identity IDs.

## Automated Repository Integration Tests

Repository integration tests now cover the current repository layer.

The complete backend test suite currently contains **108 tests** and passes successfully.

Run the complete suite with:

```bash
./gradlew test
```

Run a single repository test class with:

```bash
./gradlew test --tests "de.exercise.ExerciseRepositoryTest"
```

These are integration tests, not isolated unit tests.

They perform real local Supabase/PostgreSQL I/O and therefore use `runBlocking`.

## Database Connection Pool During Tests

Each test class may call `DatabaseConfig.init(...)`.

`DatabaseConfig` reuses the existing HikariCP pool when the requested database configuration is unchanged.

This is important because creating a new HikariCP pool for every individual test can exhaust PostgreSQL's available connections during the complete test suite.

Current behavior:

```text
first initialization
→ create HikariCP pool

same configuration again
→ reuse existing pool

different configuration
→ close old pool
→ create new pool
```

The pool currently has a maximum size of 5 connections.

## Supabase Auth Test Users

Tests that require a profile use `SupabaseAuthTestHelper`.

Typical lifecycle:

```text
Create temporary Supabase Auth user
→ Create profile using the same UUID
→ Create required training data
→ Execute assertions
→ Delete child records
→ Delete profile
→ Delete Supabase Auth user
```

Cleanup is performed with `try/finally`.

`profiles.profile_id` and `auth.users.id` use the same UUID but represent separate database records.

Deleting a profile is therefore separate from deleting the Supabase Auth user.

## Current Automated Coverage

### Profiles

Covered behavior includes:

- create profile
- read profile
- reject duplicate username
- update username
- verify `updated_at` changes
- delete profile
- unknown ID returns `null`
- updating unknown ID returns `0`

### Training Days

Covered behavior includes:

- create training day
- read training day
- update notes
- clear notes
- verify `updated_at` changes
- delete training day
- unknown ID returns `null`
- updating unknown ID returns `0`
- reject duplicate `(profile_id, training_date)`
- allow same date for different profiles
- reject missing profile foreign key

### Strength Entries

Covered behavior includes:

- create strength entry
- read strength entry
- update notes
- clear notes
- verify `updated_at` changes
- delete strength entry
- unknown ID returns `null`
- updating unknown ID returns `0`
- reject duplicate `(training_day_id, exercise_id)`
- allow same exercise on different training days
- reject missing training day foreign key
- reject missing exercise foreign key

Seed exercises are looked up by name rather than hard-coded identity IDs.

### Strength Rep Sets

Covered behavior includes:

- create repetition set
- read repetition set
- update set values
- verify `updated_at` changes
- delete repetition set
- reject duplicate `(strength_entry_id, set_number)`
- reject `set_number = 0`
- reject `reps = 0`
- reject negative `weight_kg`
- allow `weight_kg = 0`
- reject missing strength entry foreign key
- unknown ID returns `null`
- updating unknown ID returns `0`

Verified database constraints include:

```text
set_number > 0
reps > 0
weight_kg >= 0
```

### Strength Time Sets

Covered behavior includes:

- create time set
- read time set
- update time set
- clear notes
- verify `updated_at` changes
- delete time set
- reject duplicate `(strength_entry_id, set_number)`
- reject `set_number = 0`
- reject `duration_sec = 0`
- reject missing strength entry foreign key
- unknown ID returns `null`
- updating unknown ID returns `0`

Verified constraint:

```text
duration_sec > 0
```

### Endurance Entries

Covered behavior includes:

- create endurance entry
- read endurance entry
- update duration and notes
- clear notes
- verify `updated_at` changes
- delete endurance entry
- reject `duration_sec = 0`
- reject missing training day foreign key
- reject missing exercise foreign key
- allow the same exercise multiple times on the same training day
- unknown ID returns `null`
- updating unknown ID returns `0`

Verified constraint:

```text
duration_sec > 0
```

The database currently has no unique constraint on `(training_day_id, exercise_id)` for endurance entries.

### Boulder Entries

Covered behavior includes:

- create boulder entry
- read boulder entry
- update notes
- clear notes
- verify `updated_at` changes
- delete boulder entry
- reject missing training day foreign key
- allow multiple boulder entries on the same training day
- unknown ID returns `null`
- updating unknown ID returns `0`

The database currently has no unique constraint on `training_day_id` for boulder entries.

### Boulder Routes

Covered behavior includes:

- create route
- read route
- update route
- verify `updated_at` changes
- clear nullable fields
- delete route
- allow nullable `difficulty_grade_id`
- allow nullable `route_name`
- allow nullable `notes`
- reject `attempts = 0`
- reject invalid `result`
- accept all valid `result` values
- reject missing boulder entry foreign key
- reject missing difficulty grade foreign key
- unknown ID returns `null`
- updating unknown ID returns `0`

Allowed `result` values:

```text
attempted
zone
top
flash
```

Verified constraints:

```text
attempts > 0
result in ('attempted', 'zone', 'top', 'flash')
```

### Difficulty Grades

This repository is intentionally read-only.

Covered behavior includes:

- `findAll()`
- `findById(...)`
- `findBySystem(...)`
- `findAllSystems()`
- unknown ID returns `null`
- unknown system returns an empty list
- returned grades match the selected grading system
- grading systems are returned without duplicates

### Exercise Categories

This repository is intentionally read-only.

Covered behavior includes:

- `findAll()`
- `findById(...)`
- `findByName(...)`
- unknown ID returns `null`
- unknown name returns `null`

### Exercises

This repository is intentionally read-only.

Covered behavior includes:

- `findAll()`
- `findById(...)`
- `findByCategoryId(...)`
- `findByName(...)`
- unknown ID returns `null`
- unknown category returns an empty list
- unknown name returns an empty list
- category filtering only returns exercises from the selected category
- name filtering only returns exercises with the selected name

`exercise_name` is not currently unique, so `findByName(...)` returns a list.

### Muscle Groups

This repository is intentionally read-only.

Covered behavior includes:

- `findAll()`
- `findById(...)`
- `findByName(...)`
- unknown ID returns `null`
- unknown name returns `null`

### Exercise Muscle Groups

This repository is intentionally read-only.

Covered behavior includes:

- `findAll()`
- `findById(...)`
- `findByExerciseId(...)`
- `findByMuscleGroupId(...)`
- unknown mapping ID returns `null`
- unknown exercise returns an empty list
- unknown muscle group returns an empty list
- exercise filtering only returns mappings for the selected exercise
- muscle-group filtering only returns mappings for the selected muscle group

Current database role values are:

```text
primary
secondary
stabilizing
```

## Original Manual Database Verification

Before the automated repository layer existed, the database model was manually tested with realistic records.

The following areas were verified manually:

- user profile creation
- training day creation
- strength training entries
- strength repetition-based sets
- strength time-based sets
- endurance entries
- bouldering entries
- bouldering routes
- difficulty grade mapping
- database constraints
- combined training-day query behavior

### Profile

A test user was created in Supabase Auth and linked to a profile record.

Verified:

- `profiles.profile_id` correctly references the Supabase Auth user ID
- profile fields were stored correctly
- `created_at` and `updated_at` were set automatically

### Training Day

A training day was created for the test user.

Verified:

- `training_days.profile_id` correctly references the profile
- `training_date` was stored correctly
- notes were stored correctly
- the training day can act as the central anchor for strength, endurance and bouldering entries

### Strength Entries

A strength entry was created for `Ring Push Ups`.

Verified:

- `strength_entries.training_day_id` correctly references the training day
- `strength_entries.exercise_id` correctly references the exercise
- one strength entry represents one exercise on one training day

### Strength Rep Sets

Three repetition-based sets were inserted for `Ring Push Ups`.

Verified:

- set numbers were stored correctly
- repetitions were stored correctly
- `weight_kg = 0` works for bodyweight exercises
- notes were stored correctly

### Strength Time Sets

A strength entry was created for `Ring L-Sit`.

Verified:

- time-based strength exercises can be tracked separately from repetition-based strength exercises
- `duration_sec` was stored correctly
- set numbers were stored correctly
- notes were stored correctly

### Endurance Entries

An endurance entry was created for `Running`.

Verified:

- `endurance_entries.training_day_id` correctly references the training day
- `endurance_entries.exercise_id` correctly references the endurance exercise
- `duration_sec` was stored correctly
- the endurance module works independently from the strength module

### Bouldering Entries

A bouldering entry was created for the test training day.

Verified:

- `boulder_entries.training_day_id` correctly references the training day
- a bouldering session can exist independently from the exercise catalog
- the bouldering module is separate from the strength and endurance modules

### Boulder Routes

A boulder route was inserted for the bouldering entry.

The manual test verified:

- `boulder_routes.boulder_entry_id` correctly references the bouldering entry
- `difficulty_grade_id` correctly references `difficulty_grades`
- `difficulty_grade_id` may remain `NULL`
- attempts were stored correctly
- result was stored correctly

## Query Behavior

A combined full training-day query was manually tested with strength, endurance and bouldering data.

The query confirmed that all modules are reachable through `training_days`.

Important observation:

A single large flat SQL query over several one-to-many relationships can create duplicated rows because joined child collections multiply each other.

Therefore the backend should not load a full training day as one large flat join.

Preferred approach:

```text
TrainingDayService
├── load training day
├── load strength entries
├── load endurance entries
└── load bouldering entries/routes
```

The service layer can then assemble a structured response such as:

```json
{
  "trainingDate": "2026-05-11",
  "strength": [],
  "endurance": [],
  "bouldering": []
}
```

## What Repository Tests Do Not Yet Prove

The repository tests connect directly to PostgreSQL.

They verify repository behavior and database constraints, but they do not replace end-to-end API or RLS/security tests.

The following areas remain separate future work.

### Row Level Security / Authorization Tests

Planned checks include:

- User A cannot read User B's `training_days`
- User A cannot insert entries for User B's training day
- User A cannot update User B's data
- User A cannot delete User B's data
- normal authenticated users cannot modify reference tables

These tests should exercise the authentication/RLS path rather than only direct repository database access.

### Service Unit Tests

Planned for the next backend phase.

They should test:

- business validation
- conflict handling
- repository coordination
- domain result types
- authorization-related decisions

These tests should use fakes or mocks where appropriate and can use `runTest`.

### API Integration Tests

Planned after services and routes are implemented.

They should verify complete flows:

```text
HTTP Request
→ Route
→ Service
→ Repository
→ Database
→ HTTP Response
```

### CI/CD

A later GitHub Actions setup should run the appropriate automated test suite and verify that the backend remains reproducible.

## Current Conclusion

The repository and repository-integration-test phase is complete.

All current repository areas are covered and the full backend test suite passes.

The next planned development phase is the service layer.
