-- ============================================================
-- Indexes
-- Supabase / PostgreSQL
--
-- Purpose:
-- Improve query performance for foreign keys, lookups and
-- common filtering paths.
-- ============================================================

-- ============================================================
-- Profiles
-- ============================================================

create index idx_profiles_username
on public.profiles (username);


-- ============================================================
-- Training Days
-- ============================================================

create index idx_training_days_profile_id
on public.training_days (profile_id);

create index idx_training_days_profile_id_training_date
on public.training_days (profile_id, training_date);


-- ============================================================
-- Exercises
-- ============================================================

create index idx_exercises_exercise_category_id
on public.exercises (exercise_category_id);

create index idx_exercises_exercise_name
on public.exercises (exercise_name);

-- ============================================================
-- Exercise Muscle Groups
-- ============================================================

create index idx_exercise_muscle_groups_exercise_id
on public.exercise_muscle_groups (exercise_id);

create index idx_exercise_muscle_groups_muscle_group_id
on public.exercise_muscle_groups (muscle_group_id);

create index idx_exercise_muscle_groups_role
on public.exercise_muscle_groups (role);


-- ============================================================
-- Strength Entries
-- ============================================================

create index idx_strength_entries_training_day_id
on public.strength_entries (training_day_id);

create index idx_strength_entries_exercise_id
on public.strength_entries (exercise_id);

create index idx_strength_entries_training_day_id_exercise_id
on public.strength_entries (training_day_id, exercise_id);


-- ============================================================
-- Strength Rep Sets
-- ============================================================

create index idx_strength_rep_sets_strength_entry_id
on public.strength_rep_sets (strength_entry_id);

create index idx_strength_rep_sets_strength_entry_id_set_number
on public.strength_rep_sets (strength_entry_id, set_number);


-- ============================================================
-- Strength Time Sets
-- ============================================================

create index idx_strength_time_sets_strength_entry_id
on public.strength_time_sets (strength_entry_id);

create index idx_strength_time_sets_strength_entry_id_set_number
on public.strength_time_sets (strength_entry_id, set_number);

-- ============================================================
-- Endurance Entries
-- ============================================================

create index idx_endurance_entries_training_day_id
on public.endurance_entries (training_day_id);

create index idx_endurance_entries_exercise_id
on public.endurance_entries (exercise_id);


-- ============================================================
-- Boulder Entries
-- ============================================================

create index idx_boulder_entries_training_day_id
on public.boulder_entries (training_day_id);


-- ============================================================
-- Boulder Routes
-- ============================================================

create index idx_boulder_routes_boulder_entry_id
on public.boulder_routes (boulder_entry_id);

create index idx_boulder_routes_difficulty_grade_id
on public.boulder_routes (difficulty_grade_id);
