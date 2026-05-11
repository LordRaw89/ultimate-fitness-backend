-- ============================================================
-- Fitness App Database Schema
-- Supabase / PostgreSQL
-- Version: 001
-- Purpose: Core schema for profiles, training days, exercises,
--          strength, endurance and bouldering modules.
-- ============================================================

-- NOTE:
-- This file contains the database structure only.
-- Seed data belongs into separate files.

-- ============================================================
-- 01. Profiles
-- ============================================================

create table public.profiles (
  profile_id uuid primary key references auth.users(id) on delete cascade,
  username text unique not null,
  display_name text,
  birthdate date,
  weight_kg numeric(5,2),
  height_cm smallint,
  gender text check (gender in ('male', 'female', 'other')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- ============================================================
-- 02. Exercise Categories
-- ============================================================

create table public.exercise_categories (
  exercise_category_id bigint generated always as identity primary key,
  exercise_category_name text not null unique,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- ============================================================
-- 03. Muscle Groups
-- ============================================================

create table public.muscle_groups (
  muscle_group_id bigint generated always as identity primary key,
  muscle_group_name text not null unique,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- ============================================================
-- 04. Difficulty Grades
-- ============================================================

create table public.difficulty_grades (
  difficulty_grade_id bigint generated always as identity primary key,
  difficulty_grade text not null,
  difficulty_grade_system text not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (difficulty_grade, difficulty_grade_system)
);

-- ============================================================
-- 05. Training Days
-- ============================================================

create table public.training_days (
  training_day_id bigint generated always as identity primary key,
  profile_id uuid not null references public.profiles(profile_id) on delete cascade,
  training_date date not null,
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (profile_id, training_date)
);

-- ============================================================
-- 06. Exercises
-- ============================================================

create table public.exercises (
  exercise_id bigint generated always as identity primary key,
  exercise_name text not null,
  exercise_category_id bigint not null references public.exercise_categories(exercise_category_id),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- ============================================================
-- 07. Exercise Muscle Groups
-- ============================================================

create table public.exercise_muscle_groups (
  exercise_muscle_group_id bigint generated always as identity primary key,
  exercise_id bigint not null references public.exercises(exercise_id) on delete cascade,
  muscle_group_id bigint not null references public.muscle_groups(muscle_group_id) on delete cascade,
  role text not null check (role in ('primary', 'secondary', 'stabilizing')),
  created_at timestamptz not null default now(),
  unique (exercise_id, muscle_group_id)
);

-- ============================================================
-- 08. Strength Entries
-- ============================================================

create table public.strength_entries (
  strength_entry_id bigint generated always as identity primary key,
  training_day_id bigint not null references public.training_days(training_day_id) on delete cascade,
  exercise_id bigint not null references public.exercises(exercise_id),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (training_day_id, exercise_id)
);

-- ============================================================
-- 09. Strength Rep Sets
-- ============================================================

create table public.strength_rep_sets (
  strength_rep_set_id bigint generated always as identity primary key,
  strength_entry_id bigint not null references public.strength_entries(strength_entry_id) on delete cascade,
  set_number smallint not null check (set_number > 0),
  reps smallint not null check (reps > 0),
  weight_kg numeric(6,2) not null check (weight_kg >= 0),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (strength_entry_id, set_number)
);

-- ============================================================
-- 10. Strength Time Sets
-- ============================================================

create table public.strength_time_sets (
  strength_time_set_id bigint generated always as identity primary key,
  strength_entry_id bigint not null references public.strength_entries(strength_entry_id) on delete cascade,
  set_number smallint not null check (set_number > 0),
  duration_sec integer not null check (duration_sec > 0),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (strength_entry_id, set_number)
);

-- ============================================================
-- 11. Endurance Entries
-- ============================================================

create table public.endurance_entries (
  endurance_entry_id bigint generated always as identity primary key,
  training_day_id bigint not null references public.training_days(training_day_id) on delete cascade,
  exercise_id bigint not null references public.exercises(exercise_id),
  duration_sec integer not null check (duration_sec > 0),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- ============================================================
-- 12. Boulder Entries
-- ============================================================

create table public.boulder_entries (
  boulder_entry_id bigint generated always as identity primary key,
  training_day_id bigint not null references public.training_days(training_day_id) on delete cascade,
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- ============================================================
-- 13. Boulder Routes
-- ============================================================

create table public.boulder_routes (
  boulder_route_id bigint generated always as identity primary key,
  boulder_entry_id bigint not null references public.boulder_entries(boulder_entry_id) on delete cascade,
  difficulty_grade_id bigint references public.difficulty_grades(difficulty_grade_id),
  route_name text,
  attempts smallint not null check (attempts > 0),
  result text not null check (result in ('attempted', 'zone', 'top', 'flash')),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
