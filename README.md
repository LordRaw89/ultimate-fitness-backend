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

## Current Status

- Database schema created
- Core seed data created
- Exercise catalog started
- Muscle group mappings with primary, secondary and stabilizing roles
- Row Level Security policies implemented
- Index scripts implemented
- Initial Ktor backend created
- Health endpoint implemented
- Central Ktor application module configured
- Local database credentials loaded through environment variables
- PostgreSQL connection to Supabase configured through the Session Pooler
- HikariCP connection pool integrated
- Exposed connected to PostgreSQL
- Startup database connection check implemented with SELECT 1
- Database connection integration test implemented
- Profiles table mapped with Exposed
- Training days table mapped with Exposed
- Exercises table mapped with Exposed
- Exercise categories table mapped with Exposed
- Strength entries table mapped with Exposed
- Strength repetition sets table mapped with Exposed
- Strength time sets table mapped with Exposed
- Strength entry relationships to training days and exercises mapped
- Endurance entries table mapped with Exposed
- Foreign key relationships from endurance entries to training days and exercises mapped with Exposed
- Boulder entries table mapped with Exposed
- Difficulty grades table mapped with Exposed
- Boulder routes table mapped with Exposed
- Foreign key relationships for boulder entries, routes and difficulty grades mapped with Exposed
- Muscle groups table mapped with Exposed
- Exercise-to-muscle-group mapping table mapped with Exposed
- Foreign key relationships between exercises and muscle groups mapped with Exposed

## Database Structure

SQL files are stored in:

```text
database/sql/
```

Execution order:

```text
01_schema.sql
02_seed_core_data.sql
03_seed_exercises.sql
04_seed_exercise_muscle_groups.sql
05_rls_policies.sql
06_indexes.sql
```

## Security Notes

Local backups and environment files are ignored via `.gitignore`.

Do not commit:

- `.env`
- Supabase keys
- local database backups
- real user data
