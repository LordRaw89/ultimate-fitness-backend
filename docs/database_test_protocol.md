# Database Test Protocol

This document describes the manual database tests performed for the Ultimate Fitness backend database.

## Purpose

The goal of these tests is to verify that the database model works for realistic fitness tracking scenarios and that important constraints prevent invalid data.

## Test Environment

- Database: Supabase / PostgreSQL
- Test user: testuser
- Test date: 2026-05-11

## Tested Areas

The following areas were tested manually:

- User profile creation
- Training day creation
- Strength training entries
- Strength repetition-based sets
- Strength time-based sets
- Endurance entries
- Bouldering entries
- Bouldering routes
- Difficulty grade mapping
- Database constraints
- Basic full training day query behavior

## Positive Tests

### Profile

A test user was created in Supabase Auth and linked to a profile record.

Verified:

- `profiles.profile_id` correctly references the Supabase Auth user ID
- `username`, `display_name`, `birthdate`, `weight_kg`, `height_cm` and `gender` were stored correctly
- `created_at` and `updated_at` were set automatically

### Training Day

A training day was created for the test user.

Verified:

- `training_days.profile_id` correctly references the profile
- `training_date` was stored correctly
- Notes were stored correctly
- The training day can be used as the central anchor for strength, endurance and bouldering entries

### Strength Entries

A strength entry was created for `Ring Push Ups`.

Verified:

- `strength_entries.training_day_id` correctly references the training day
- `strength_entries.exercise_id` correctly references the exercise
- A strength entry represents one specific exercise on one specific training day

### Strength Rep Sets

Three repetition-based sets were inserted for `Ring Push Ups`.

Verified:

- Set numbers were stored correctly
- Repetitions were stored correctly
- `weight_kg = 0` works for bodyweight exercises
- Notes were stored correctly

### Strength Time Sets

A strength entry was created for `Ring L-Sit`.

Two time-based sets were inserted.

Verified:

- Time-based strength exercises can be tracked separately from repetition-based strength exercises
- `duration_sec` was stored correctly
- Set numbers were stored correctly
- Notes were stored correctly

### Endurance Entries

An endurance entry was created for `Running`.

Verified:

- `endurance_entries.training_day_id` correctly references the training day
- `endurance_entries.exercise_id` correctly references the endurance exercise
- `duration_sec` was stored correctly
- The endurance module works independently from the strength module

### Bouldering Entries

A bouldering entry was created for the test training day.

Verified:

- `boulder_entries.training_day_id` correctly references the training day
- A bouldering session can exist independently from the exercise catalog
- The bouldering module is separate from the strength and endurance modules

### Boulder Routes

A boulder route was inserted for the bouldering entry.

Test route:

- Route name: `Blue Overhang Test Route`
- Difficulty grade: `6A`
- Difficulty system: `Fontainebleau`
- Attempts: `3`
- Result: `top`

Verified:

- `boulder_routes.boulder_entry_id` correctly references the bouldering entry
- `difficulty_grade_id` correctly references `difficulty_grades`
- `difficulty_grade_id` may remain `NULL` for outdoor or unknown grades
- Attempts were stored correctly
- Result was stored correctly

## Negative Tests

The following tests were expected to fail.  
They confirm that database constraints correctly reject invalid data.

### Duplicate Training Day

Attempted to create a second `training_days` record for the same user and date.

Expected result:

- Insert is rejected
- Unique constraint on `(profile_id, training_date)` works correctly

### Duplicate Strength Entry

Attempted to create a second `strength_entries` record for the same training day and exercise.

Expected result:

- Insert is rejected
- Unique constraint on `(training_day_id, exercise_id)` works correctly

### Duplicate Rep Set Number

Attempted to insert a second repetition-based set with the same `strength_entry_id` and `set_number`.

Expected result:

- Insert is rejected
- Unique constraint on `(strength_entry_id, set_number)` works correctly

### Invalid Strength Rep Set Values

Tested invalid values for repetition-based strength sets.

Expected results:

- `set_number = 0` is rejected
- `reps = 0` is rejected
- negative `weight_kg` is rejected

Verified constraints:

- `set_number > 0`
- `reps > 0`
- `weight_kg >= 0`

### Invalid Strength Time Set Values

Tested invalid values for time-based strength sets.

Expected result:

- `duration_sec = 0` is rejected

Verified constraint:

- `duration_sec > 0`

### Invalid Endurance Values

Tested invalid values for endurance entries.

Expected result:

- `duration_sec = 0` is rejected

Verified constraint:

- `duration_sec > 0`

### Invalid Boulder Route Values

Tested invalid values for boulder routes.

Expected results:

- `attempts = 0` is rejected
- invalid `result` value is rejected

Allowed `result` values:

- `attempted`
- `zone`
- `top`
- `flash`

Verified constraints:

- `attempts > 0`
- `result in ('attempted', 'zone', 'top', 'flash')`

## Query Behavior

A combined full training day query was tested with strength, endurance and bouldering data.

Result:

- The query successfully joined all modules through `training_days`
- Strength entries, endurance entries and bouldering routes were all reachable from one training day
- Difficulty grades could be joined correctly for boulder routes

Important observation:

A single large flat SQL query over several one-to-many relationships can create duplicated rows because the joined modules multiply each other.

Example:

- 2 strength entries
- 1 endurance entry
- 1 boulder route

Result:

- 2 rows in a flat join result

Conclusion:

For the future Ktor backend, the training day should not be loaded as one large flat join query.  
Instead, the backend should use smaller focused queries and assemble a structured response.

Preferred backend structure:

```json
{
  "trainingDate": "2026-05-11",
  "strength": [],
  "endurance": [],
  "bouldering": []
}

## Open / Future Tests

The following tests were not completed yet and should be added later:

### Row Level Security Test

A second test user should be created to verify that users cannot access each other's training data.

Planned checks:

- User A cannot read User B's `training_days`
- User A cannot insert entries for User B's training day
- User A cannot update or delete User B's data

### Backend Integration Tests

Once the Ktor backend exists, integration tests should verify that API endpoints correctly create and read:

- training days
- strength entries
- repetition-based sets
- time-based sets
- endurance entries
- bouldering entries
- bouldering routes

### Automated Database Tests

In a later CI/CD setup, database tests should verify:

- schema creation
- seed data insertion
- constraints
- indexes
- RLS policies
- basic insert and read workflows
