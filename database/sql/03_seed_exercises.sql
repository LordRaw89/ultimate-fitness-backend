-- ============================================================
-- Seed Exercises
-- Ring / Egym / Future Exercise Blocks
-- ============================================================

-- ============================================================
-- 01. Ring Exercises
-- ============================================================

insert into public.exercises (
  exercise_name,
  exercise_category_id,
  notes
)
values
  (
    'Ring Push Ups',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Horizontal pushing exercise on gymnastic rings with high stabilization demand.'
  ),
  (
    'Ring Pull Ups',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Vertical pulling exercise on gymnastic rings with increased stabilization demand.'
  ),
  (
    'Ring Rows',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Horizontal pulling exercise on gymnastic rings with bodyweight resistance and core stabilization.'
  ),
  (
    'Ring Dips Assisted',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Assisted ring dip variation with reduced bodyweight load and high stabilization demand.'
  ),
  (
    'Ring Dips',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Full bodyweight ring dip with high chest, triceps and shoulder demand plus strong stabilization requirements.'
  ),
  (
    'Ring Hanging Knee Raises',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Hanging knee raise variation on gymnastic rings with strong core and shoulder stabilization demand.'
  ),
  (
    'Ring Bizeps Curls',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Bodyweight biceps curl variation on gymnastic rings with high arm and core stabilization demand.'
  ),
  (
    'Ring Flies',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Chest fly variation on gymnastic rings with high instability and strong chest isolation demand.'
  ),
  (
    'Ring L-Sit',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Static L-sit hold on gymnastic rings with high core tension and shoulder stabilization demand.'
  ),
  (
    'Ring Pike Push Ups',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Overhead pushing variation on gymnastic rings emphasizing shoulders and core stability.'
  ),
  (
    'Ring Trizeps Extensions',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Bodyweight triceps extension on gymnastic rings with strong stabilization demand.'
  ),
  (
    'Ring Face Pulls',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Face pull variation on gymnastic rings focusing on rear delts, upper back and shoulder health.'
  );

-- ============================================================
-- 02. Egym Exercises
-- ============================================================

insert into public.exercises (
  exercise_name,
  exercise_category_id,
  notes
)
values
  (
    'Egym Chest Press',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided horizontal chest press focusing on pectoralis major with reduced stabilization demand.'
  ),
  (
    'Egym Butterfly',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided chest fly focusing on pectoral isolation with low stabilization demand.'
  ),
  (
    'Egym Reverse Butterfly',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided reverse fly focusing on rear delts, upper back and posture support.'
  ),
  (
    'Egym Leg Curl',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided knee flexion exercise focusing primarily on hamstrings.'
  ),
  (
    'Egym Leg Extension',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided knee extension exercise focusing primarily on quadriceps isolation.'
  ),
  (
    'Egym Abdominal Crunch',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided abdominal crunch focusing on trunk flexion and core strength.'
  ),
  (
    'Egym Back Extension',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided trunk extension focusing on lower back, glutes and posterior chain.'
  ),
  (
    'Egym Rowing',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided horizontal rowing movement focusing on upper back and pulling strength.'
  ),
  (
    'Egym Lat Pulldown',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided vertical pulling movement focusing primarily on latissimus dorsi.'
  ),
  (
    'Egym Shoulder Press',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided vertical pressing movement focusing on shoulders and triceps.'
  ),
  (
    'Egym Adductor',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided hip adduction exercise focusing on inner thigh musculature.'
  ),
  (
    'Egym Abductor',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided hip abduction exercise focusing on outer hip and glute stabilization.'
  ),
  (
    'Egym Torso Rotator',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided rotational core exercise focusing on trunk rotation and oblique strength.'
  ),
  (
    'Egym Bizeps Curl',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided elbow flexion exercise focusing on biceps and arm isolation.'
  ),
  (
    'Egym Trizeps Press',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided triceps isolation exercise focusing on elbow extension.'
  ),
  (
    'Egym Glutaeus-Trainer',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided glute-focused exercise emphasizing hip extension and pelvic stability.'
  ),
  (
    'Egym Leg Press',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Strength'),
    'Machine-guided compound lower body exercise focusing on quadriceps and glutes with controlled movement path.'
  );
  
-- ============================================================
-- 03. Endurance Exercises
-- ============================================================

insert into public.exercises (
  exercise_name,
  exercise_category_id,
  notes
)
values
  (
    'Running',
    (select exercise_category_id from public.exercise_categories where exercise_category_name = 'Endurance'),
    'General running endurance activity.'
  );
  