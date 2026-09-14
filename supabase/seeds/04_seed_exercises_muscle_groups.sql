-- ============================================================
-- Seed Exercise Muscle Groups
-- Ring / Egym / Future Exercise Blocks
-- ============================================================

-- ============================================================
-- 01. Ring Exercise Muscle Groups
-- ============================================================

insert into public.exercise_muscle_groups (
  exercise_id,
  muscle_group_id,
  role
)
values
  -- Ring Push Ups
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Ring Pull Ups
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Latissimus Dorsi'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Biceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rhomboids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachioradialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pull Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),

  -- Ring Rows
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Latissimus Dorsi'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rhomboids'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Biceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachioradialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Rows'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),

  -- Ring Dips Assisted
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips Assisted'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Ring Dips
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Dips'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

    -- Ring Hanging Knee Raises
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hip Flexors'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Hanging Knee Raises'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Latissimus Dorsi'), 'stabilizing'),

  -- Ring Bizeps Curls
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Biceps Brachii'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachialis'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachioradialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Bizeps Curls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),

  -- Ring Flies
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Flies'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),

  -- Ring L-Sit
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hip Flexors'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring L-Sit'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'stabilizing'),

  -- Ring Pike Push Ups
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Pike Push Ups'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),

  -- Ring Trizeps Extensions
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Trizeps Extensions'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing'),

  -- Ring Face Pulls
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rhomboids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Infraspinatus'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Teres Minor'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Ring Face Pulls'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing');

-- ============================================================
-- 02. Egym Exercise Muscle Groups
-- ============================================================

insert into public.exercise_muscle_groups (
  exercise_id,
  muscle_group_id,
  role
)
values
  -- Egym Chest Press
  ((select exercise_id from public.exercises where exercise_name = 'Egym Chest Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Chest Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Chest Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Chest Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Chest Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Chest Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Egym Butterfly
  ((select exercise_id from public.exercises where exercise_name = 'Egym Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Major'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Pectoralis Minor'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),

  -- Egym Reverse Butterfly
  ((select exercise_id from public.exercises where exercise_name = 'Egym Reverse Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Reverse Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rhomboids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Reverse Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Reverse Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Infraspinatus'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Reverse Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Teres Minor'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Reverse Butterfly'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),

  -- Egym Leg Curl
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hamstrings'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gastrocnemius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Maximus'), 'stabilizing'),

  -- Egym Leg Extension
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Quadriceps'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),

  -- Egym Abdominal Crunch
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abdominal Crunch'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abdominal Crunch'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abdominal Crunch'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abdominal Crunch'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hip Flexors'), 'stabilizing'),

  -- Egym Back Extension
  ((select exercise_id from public.exercises where exercise_name = 'Egym Back Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Erector Spinae'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Back Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Maximus'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Back Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hamstrings'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Back Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Back Extension'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'stabilizing');

  insert into public.exercise_muscle_groups (
  exercise_id,
  muscle_group_id,
  role
)
values
  -- Egym Rowing
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Latissimus Dorsi'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rhomboids'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Biceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachioradialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Rowing'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Egym Lat Pulldown
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Latissimus Dorsi'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Biceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachioradialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rhomboids'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Lat Pulldown'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Egym Shoulder Press
  ((select exercise_id from public.exercises where exercise_name = 'Egym Shoulder Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Deltoids'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Shoulder Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Shoulder Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Trapezius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Shoulder Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Serratus Anterior'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Shoulder Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Shoulder Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Egym Adductor
  ((select exercise_id from public.exercises where exercise_name = 'Egym Adductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Adductors'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Adductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Adductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),

  -- Egym Abductor
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Medius'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Minimus'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Maximus'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Abductor'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),

  -- Egym Torso Rotator
  ((select exercise_id from public.exercises where exercise_name = 'Egym Torso Rotator'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Torso Rotator'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Torso Rotator'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Transverse Abdominis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Torso Rotator'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Erector Spinae'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Torso Rotator'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hip Flexors'), 'stabilizing'),

  -- Egym Bizeps Curl
  ((select exercise_id from public.exercises where exercise_name = 'Egym Bizeps Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Biceps Brachii'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Bizeps Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachialis'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Bizeps Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Brachioradialis'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Bizeps Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Bizeps Curl'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Egym Trizeps Press
  ((select exercise_id from public.exercises where exercise_name = 'Egym Trizeps Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Triceps Brachii'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Trizeps Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Flexors'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Trizeps Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Forearm Extensors'), 'stabilizing'),

  -- Egym Glutaeus-Trainer
  ((select exercise_id from public.exercises where exercise_name = 'Egym Glutaeus-Trainer'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Maximus'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Glutaeus-Trainer'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Medius'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Glutaeus-Trainer'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hamstrings'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Glutaeus-Trainer'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Minimus'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Glutaeus-Trainer'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Glutaeus-Trainer'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing'),

  -- Egym Leg Press
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Quadriceps'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Gluteus Maximus'), 'primary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Hamstrings'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Adductors'), 'secondary'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Rectus Abdominis'), 'stabilizing'),
  ((select exercise_id from public.exercises where exercise_name = 'Egym Leg Press'), (select muscle_group_id from public.muscle_groups where muscle_group_name = 'Obliques'), 'stabilizing');
  
