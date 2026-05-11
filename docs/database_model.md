# Database Model

The database is built around a modular fitness tracking structure.

The central table is `training_days`.  
A training day belongs to one user profile and acts as the anchor for all training data of one date.

From there, the data is separated into different training modules:

```text
training_days
├── strength_entries
├── endurance_entries
└── boulder_entries
```

I chose this structure because strength training, endurance training and bouldering work differently and should not be forced into one generic table.

## Strength

Strength training uses:

```text
strength_entries
strength_rep_sets
strength_time_sets
```

A `strength_entry` represents one exercise on one training day.

Example:

```text
2026-05-11 + Ring Push Ups = one strength entry
```

Repetition-based sets and time-based sets are separated because they store different values.

Rep sets store reps and weight.  
Time sets store duration.

## Endurance

Endurance training uses:

```text
endurance_entries
```

At the moment, every endurance entry stores at least a duration.  
The model can later be extended with distance, pace, heart rate or interval data.

Endurance activities still use the general `exercises` table because activities like running, cycling or swimming fit well into the exercise catalog.

## Bouldering

Bouldering is separated from the general exercise catalog.

It uses:

```text
boulder_entries
boulder_routes
difficulty_grades
```

A bouldering session can have multiple routes/problems.

The difficulty grade is optional because especially outdoor problems may not always have a known grade.

## Exercises and Muscle Groups

The `exercises` table is used for strength, endurance, mobility and balance exercises.

Muscle involvement is stored through `exercise_muscle_groups` with three roles:

```text
primary
secondary
stabilizing
```

This allows later analysis of which muscles are trained directly, indirectly or mainly for stabilization.

## Constraints

Important rules are enforced directly in the database.

Examples:

- one training day per user and date
- one strength entry per exercise and training day
- set numbers must be greater than zero
- reps must be greater than zero
- weight cannot be negative
- duration must be greater than zero
- boulder attempts must be greater than zero
- boulder results must use allowed values

## Security

User-owned tables use Row Level Security.

The goal is that users can only access training data that belongs to their own profile.

## Query Strategy

A full training day should not be loaded with one huge flat join.

Instead, the backend should load the modules separately and assemble a structured response, for example:

```json
{
  "trainingDate": "2026-05-11",
  "strength": [],
  "endurance": [],
  "bouldering": []
}
```

This avoids duplicated rows from large joins and keeps the backend response clean.

## Summary

The main idea is:

```text
one shared training day
+ separate modules for different training types
```

This keeps the model understandable, flexible and easier to extend later.
