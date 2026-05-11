-- ============================================================
-- Seed Core Data
-- exercise_categories
-- muscle_groups
-- difficulty_grades
-- ============================================================

-- ============================================================
-- 01. Exercise Categories
-- ============================================================

insert into public.exercise_categories (exercise_category_name)
values
  ('Strength'),
  ('Endurance'),
  ('Mobility'),
  ('Balance');

-- ============================================================
-- 02. Muscle Groups
-- ============================================================

insert into public.muscle_groups (muscle_group_name)
values
  ('Pectoralis Major'),
  ('Pectoralis Minor'),
  ('Serratus Anterior'),

  ('Latissimus Dorsi'),
  ('Trapezius'),
  ('Rhomboids'),
  ('Teres Major'),
  ('Erector Spinae'),

  ('Deltoids'),
  ('Supraspinatus'),
  ('Infraspinatus'),
  ('Teres Minor'),
  ('Subscapularis'),

  ('Biceps Brachii'),
  ('Triceps Brachii'),
  ('Brachialis'),
  ('Brachioradialis'),
  ('Forearm Flexors'),
  ('Forearm Extensors'),

  ('Rectus Abdominis'),
  ('Obliques'),
  ('Transverse Abdominis'),

  ('Quadriceps'),
  ('Hamstrings'),
  ('Adductors'),
  ('Abductors'),
  ('Gastrocnemius'),
  ('Soleus'),
  ('Tibialis Anterior'),

  ('Gluteus Maximus'),
  ('Gluteus Medius'),
  ('Gluteus Minimus'),
  ('Hip Flexors'),

  ('Sternocleidomastoid');

-- ============================================================
-- 03. Difficulty Grades
-- ============================================================

insert into public.difficulty_grades (
  difficulty_grade,
  difficulty_grade_system
)
values
  -- Fontainebleau
  ('4', 'Font'),
  ('4+', 'Font'),
  ('5', 'Font'),
  ('5+', 'Font'),
  ('6A', 'Font'),
  ('6A+', 'Font'),
  ('6B', 'Font'),
  ('6B+', 'Font'),
  ('6C', 'Font'),
  ('6C+', 'Font'),
  ('7A', 'Font'),
  ('7A+', 'Font'),
  ('7B', 'Font'),
  ('7B+', 'Font'),
  ('7C', 'Font'),
  ('7C+', 'Font'),
  ('8A', 'Font'),
  ('8A+', 'Font'),

  -- V-Scale
  ('VB', 'V-Scale'),
  ('V0', 'V-Scale'),
  ('V1', 'V-Scale'),
  ('V2', 'V-Scale'),
  ('V3', 'V-Scale'),
  ('V4', 'V-Scale'),
  ('V5', 'V-Scale'),
  ('V6', 'V-Scale'),
  ('V7', 'V-Scale'),
  ('V8', 'V-Scale'),
  ('V9', 'V-Scale'),
  ('V10', 'V-Scale'),
  ('V11', 'V-Scale'),
  ('V12', 'V-Scale'),

  -- Gym Internal
  ('Beginner', 'Gym'),
  ('Intermediate', 'Gym'),
  ('Advanced', 'Gym');
