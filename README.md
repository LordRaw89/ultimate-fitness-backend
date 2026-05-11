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

## Current Status

- Database schema created
- Core seed data created
- Exercise catalog started
- Muscle group mappings with primary, secondary and stabilizing roles
- Row Level Security policies prepared
- Index scripts prepared

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