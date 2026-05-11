-- ============================================================
-- Row Level Security Policies
-- Supabase / PostgreSQL
--
-- Purpose:
-- Protect user-owned training data while allowing public read
-- access to global reference data such as exercises, categories,
-- muscle groups and difficulty grades.
-- ============================================================

-- ============================================================
-- Enable RLS
-- ============================================================

alter table public.profiles enable row level security;
alter table public.training_days enable row level security;
alter table public.strength_entries enable row level security;
alter table public.strength_rep_sets enable row level security;
alter table public.strength_time_sets enable row level security;
alter table public.endurance_entries enable row level security;
alter table public.boulder_entries enable row level security;
alter table public.boulder_routes enable row level security;

-- Reference data can stay publicly readable.
-- RLS is not required for:
-- exercise_categories
-- muscle_groups
-- difficulty_grades
-- exercises
-- exercise_muscle_groups

-- ============================================================
-- Profiles
-- ============================================================

create policy "Users can view their own profile"
on public.profiles
for select
using (auth.uid() = profile_id);

create policy "Users can insert their own profile"
on public.profiles
for insert
with check (auth.uid() = profile_id);

create policy "Users can update their own profile"
on public.profiles
for update
using (auth.uid() = profile_id)
with check (auth.uid() = profile_id);

-- ============================================================
-- Training Days
-- ============================================================

create policy "Users can view their own training days"
on public.training_days
for select
using (
  profile_id = auth.uid()
);

create policy "Users can insert their own training days"
on public.training_days
for insert
with check (
  profile_id = auth.uid()
);

create policy "Users can update their own training days"
on public.training_days
for update
using (
  profile_id = auth.uid()
)
with check (
  profile_id = auth.uid()
);

create policy "Users can delete their own training days"
on public.training_days
for delete
using (
  profile_id = auth.uid()
);

-- ============================================================
-- Strength Entries
-- ============================================================

create policy "Users can view their own strength entries"
on public.strength_entries
for select
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = strength_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can insert their own strength entries"
on public.strength_entries
for insert
with check (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = strength_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can update their own strength entries"
on public.strength_entries
for update
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = strength_entries.training_day_id
      and td.profile_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = strength_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can delete their own strength entries"
on public.strength_entries
for delete
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = strength_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

-- ============================================================
-- Strength Rep Sets
-- ============================================================

create policy "Users can view their own strength rep sets"
on public.strength_rep_sets
for select
using (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_rep_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can insert their own strength rep sets"
on public.strength_rep_sets
for insert
with check (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_rep_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can update their own strength rep sets"
on public.strength_rep_sets
for update
using (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_rep_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_rep_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can delete their own strength rep sets"
on public.strength_rep_sets
for delete
using (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_rep_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

-- ============================================================
-- Strength Time Sets
-- ============================================================

create policy "Users can view their own strength time sets"
on public.strength_time_sets
for select
using (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_time_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can insert their own strength time sets"
on public.strength_time_sets
for insert
with check (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_time_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can update their own strength time sets"
on public.strength_time_sets
for update
using (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_time_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_time_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can delete their own strength time sets"
on public.strength_time_sets
for delete
using (
  exists (
    select 1
    from public.strength_entries se
    join public.training_days td
      on td.training_day_id = se.training_day_id
    where se.strength_entry_id = strength_time_sets.strength_entry_id
      and td.profile_id = auth.uid()
  )
);

-- ============================================================
-- Endurance Entries
-- ============================================================

create policy "Users can view their own endurance entries"
on public.endurance_entries
for select
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = endurance_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can insert their own endurance entries"
on public.endurance_entries
for insert
with check (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = endurance_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can update their own endurance entries"
on public.endurance_entries
for update
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = endurance_entries.training_day_id
      and td.profile_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = endurance_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can delete their own endurance entries"
on public.endurance_entries
for delete
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = endurance_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

-- ============================================================
-- Boulder Entries
-- ============================================================

create policy "Users can view their own boulder entries"
on public.boulder_entries
for select
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = boulder_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can insert their own boulder entries"
on public.boulder_entries
for insert
with check (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = boulder_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can update their own boulder entries"
on public.boulder_entries
for update
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = boulder_entries.training_day_id
      and td.profile_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = boulder_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can delete their own boulder entries"
on public.boulder_entries
for delete
using (
  exists (
    select 1
    from public.training_days td
    where td.training_day_id = boulder_entries.training_day_id
      and td.profile_id = auth.uid()
  )
);

-- ============================================================
-- Boulder Routes
-- ============================================================

create policy "Users can view their own boulder routes"
on public.boulder_routes
for select
using (
  exists (
    select 1
    from public.boulder_entries be
    join public.training_days td
      on td.training_day_id = be.training_day_id
    where be.boulder_entry_id = boulder_routes.boulder_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can insert their own boulder routes"
on public.boulder_routes
for insert
with check (
  exists (
    select 1
    from public.boulder_entries be
    join public.training_days td
      on td.training_day_id = be.training_day_id
    where be.boulder_entry_id = boulder_routes.boulder_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can update their own boulder routes"
on public.boulder_routes
for update
using (
  exists (
    select 1
    from public.boulder_entries be
    join public.training_days td
      on td.training_day_id = be.training_day_id
    where be.boulder_entry_id = boulder_routes.boulder_entry_id
      and td.profile_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.boulder_entries be
    join public.training_days td
      on td.training_day_id = be.training_day_id
    where be.boulder_entry_id = boulder_routes.boulder_entry_id
      and td.profile_id = auth.uid()
  )
);

create policy "Users can delete their own boulder routes"
on public.boulder_routes
for delete
using (
  exists (
    select 1
    from public.boulder_entries be
    join public.training_days td
      on td.training_day_id = be.training_day_id
    where be.boulder_entry_id = boulder_routes.boulder_entry_id
      and td.profile_id = auth.uid()
  )
);
