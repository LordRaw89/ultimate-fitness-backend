CREATE TABLE "public"."boulder_entries" (
  "boulder_entry_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "training_day_id"  bigint                   NOT NULL,
  "notes"            text,
  "created_at"       timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"       timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "boulder_entries_pkey" PRIMARY KEY (boulder_entry_id)
);

ALTER TABLE "public"."boulder_entries"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."boulder_routes" (
  "boulder_route_id"    bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "boulder_entry_id"    bigint                   NOT NULL,
  "difficulty_grade_id" bigint,
  "route_name"          text,
  "attempts"            smallint                 NOT NULL,
  "result"              text                     NOT NULL,
  "notes"               text,
  "created_at"          timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"          timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "boulder_routes_attempts_check" CHECK ((attempts > 0)),
  CONSTRAINT "boulder_routes_pkey" PRIMARY KEY (boulder_route_id),
  CONSTRAINT "boulder_routes_result_check" CHECK ((result = ANY (ARRAY['attempted'::text, 'zone'::text, 'top'::text, 'flash'::text])))
);

ALTER TABLE "public"."boulder_routes"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."difficulty_grades" (
  "difficulty_grade_id"     bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "difficulty_grade"        text                     NOT NULL,
  "difficulty_grade_system" text                     NOT NULL,
  "created_at"              timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"              timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "difficulty_grades_difficulty_grade_difficulty_grade_system_key" UNIQUE (difficulty_grade, difficulty_grade_system),
  CONSTRAINT "difficulty_grades_pkey" PRIMARY KEY (difficulty_grade_id)
);

ALTER TABLE "public"."difficulty_grades"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."endurance_entries" (
  "endurance_entry_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "training_day_id"    bigint                   NOT NULL,
  "exercise_id"        bigint                   NOT NULL,
  "duration_sec"       integer                  NOT NULL,
  "notes"              text,
  "created_at"         timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"         timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "endurance_entries_duration_sec_check" CHECK ((duration_sec > 0)),
  CONSTRAINT "endurance_entries_pkey" PRIMARY KEY (endurance_entry_id)
);

ALTER TABLE "public"."endurance_entries"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."exercise_categories" (
  "exercise_category_id"   bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "exercise_category_name" text                     NOT NULL,
  "created_at"             timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"             timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "exercise_categories_exercise_category_name_key" UNIQUE (exercise_category_name),
  CONSTRAINT "exercise_categories_pkey" PRIMARY KEY (exercise_category_id)
);

ALTER TABLE "public"."exercise_categories"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."exercise_muscle_groups" (
  "exercise_muscle_group_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "exercise_id"              bigint                   NOT NULL,
  "muscle_group_id"          bigint                   NOT NULL,
  "role"                     text                     NOT NULL,
  "created_at"               timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"               timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "exercise_muscle_groups_exercise_id_muscle_group_id_key" UNIQUE (exercise_id, muscle_group_id),
  CONSTRAINT "exercise_muscle_groups_pkey" PRIMARY KEY (exercise_muscle_group_id),
  CONSTRAINT "exercise_muscle_groups_role_check" CHECK ((role = ANY (ARRAY['primary'::text, 'secondary'::text, 'stabilizing'::text])))
);

ALTER TABLE "public"."exercise_muscle_groups"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."exercises" (
  "exercise_id"          bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "exercise_name"        text                     NOT NULL,
  "exercise_category_id" bigint                   NOT NULL,
  "notes"                text,
  "created_at"           timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"           timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "exercises_pkey" PRIMARY KEY (exercise_id)
);

ALTER TABLE "public"."exercises"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."muscle_groups" (
  "muscle_group_id"   bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "muscle_group_name" text                     NOT NULL,
  "created_at"        timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"        timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "muscle_groups_muscle_group_name_key" UNIQUE (muscle_group_name),
  CONSTRAINT "muscle_groups_pkey" PRIMARY KEY (muscle_group_id)
);

ALTER TABLE "public"."muscle_groups"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."profiles" (
  "profile_id"   uuid                     NOT NULL,
  "username"     text                     NOT NULL,
  "display_name" text,
  "birthdate"    date,
  "weight_kg"    numeric(5,2),
  "height_cm"    smallint,
  "gender"       text,
  "created_at"   timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"   timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "profiles_gender_check" CHECK ((gender = ANY (ARRAY['male'::text, 'female'::text, 'other'::text]))),
  CONSTRAINT "profiles_pkey" PRIMARY KEY (profile_id),
  CONSTRAINT "profiles_username_key" UNIQUE (username)
);

ALTER TABLE "public"."profiles"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."strength_entries" (
  "strength_entry_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "training_day_id"   bigint                   NOT NULL,
  "exercise_id"       bigint                   NOT NULL,
  "notes"             text,
  "created_at"        timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"        timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "strength_entries_pkey" PRIMARY KEY (strength_entry_id),
  CONSTRAINT "strength_entries_training_day_id_exercise_id_key" UNIQUE (training_day_id, exercise_id)
);

ALTER TABLE "public"."strength_entries"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."strength_rep_sets" (
  "strength_rep_set_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "strength_entry_id"   bigint                   NOT NULL,
  "set_number"          smallint                 NOT NULL,
  "reps"                smallint                 NOT NULL,
  "weight_kg"           numeric(6,2)             NOT NULL,
  "notes"               text,
  "created_at"          timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"          timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "strength_rep_sets_pkey" PRIMARY KEY (strength_rep_set_id),
  CONSTRAINT "strength_rep_sets_reps_check" CHECK ((reps > 0)),
  CONSTRAINT "strength_rep_sets_set_number_check" CHECK ((set_number > 0)),
  CONSTRAINT "strength_rep_sets_strength_entry_id_set_number_key" UNIQUE (strength_entry_id, set_number),
  CONSTRAINT "strength_rep_sets_weight_kg_check" CHECK ((weight_kg >= (0)::numeric))
);

ALTER TABLE "public"."strength_rep_sets"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."strength_time_sets" (
  "strength_time_set_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "strength_entry_id"    bigint                   NOT NULL,
  "set_number"           smallint                 NOT NULL,
  "duration_sec"         integer                  NOT NULL,
  "notes"                text,
  "created_at"           timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"           timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "strength_time_sets_duration_sec_check" CHECK ((duration_sec > 0)),
  CONSTRAINT "strength_time_sets_pkey" PRIMARY KEY (strength_time_set_id),
  CONSTRAINT "strength_time_sets_set_number_check" CHECK ((set_number > 0)),
  CONSTRAINT "strength_time_sets_strength_entry_id_set_number_key" UNIQUE (strength_entry_id, set_number)
);

ALTER TABLE "public"."strength_time_sets"
  ENABLE ROW LEVEL SECURITY;

CREATE TABLE "public"."training_days" (
  "training_day_id" bigint                   GENERATED ALWAYS AS IDENTITY NOT NULL,
  "profile_id"      uuid                     NOT NULL,
  "training_date"   date                     NOT NULL,
  "notes"           text,
  "created_at"      timestamp with time zone NOT NULL DEFAULT now(),
  "updated_at"      timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT "training_days_pkey" PRIMARY KEY (training_day_id),
  CONSTRAINT "training_days_profile_id_training_date_key" UNIQUE (profile_id, training_date)
);

ALTER TABLE "public"."training_days"
  ENABLE ROW LEVEL SECURITY;

ALTER TABLE "public"."boulder_routes"
  ADD CONSTRAINT "boulder_routes_boulder_entry_id_fkey" FOREIGN KEY (boulder_entry_id) REFERENCES public.boulder_entries(boulder_entry_id) ON DELETE CASCADE;

ALTER TABLE "public"."boulder_routes"
  ADD CONSTRAINT "boulder_routes_difficulty_grade_id_fkey" FOREIGN KEY (difficulty_grade_id) REFERENCES public.difficulty_grades(difficulty_grade_id);

ALTER TABLE "public"."exercises"
  ADD CONSTRAINT "exercises_exercise_category_id_fkey" FOREIGN KEY (exercise_category_id) REFERENCES public.exercise_categories(exercise_category_id);

ALTER TABLE "public"."endurance_entries"
  ADD CONSTRAINT "endurance_entries_exercise_id_fkey" FOREIGN KEY (exercise_id) REFERENCES public.exercises(exercise_id);

ALTER TABLE "public"."exercise_muscle_groups"
  ADD CONSTRAINT "exercise_muscle_groups_exercise_id_fkey" FOREIGN KEY (exercise_id) REFERENCES public.exercises(exercise_id) ON DELETE CASCADE;

ALTER TABLE "public"."exercise_muscle_groups"
  ADD CONSTRAINT "exercise_muscle_groups_muscle_group_id_fkey" FOREIGN KEY (muscle_group_id) REFERENCES public.muscle_groups(muscle_group_id) ON DELETE CASCADE;

ALTER TABLE "public"."profiles"
  ADD CONSTRAINT "profiles_profile_id_fkey" FOREIGN KEY (profile_id) REFERENCES auth.users(id) ON DELETE CASCADE;

ALTER TABLE "public"."strength_entries"
  ADD CONSTRAINT "strength_entries_exercise_id_fkey" FOREIGN KEY (exercise_id) REFERENCES public.exercises(exercise_id);

ALTER TABLE "public"."strength_rep_sets"
  ADD CONSTRAINT "strength_rep_sets_strength_entry_id_fkey" FOREIGN KEY (strength_entry_id) REFERENCES public.strength_entries(strength_entry_id) ON DELETE CASCADE;

ALTER TABLE "public"."strength_time_sets"
  ADD CONSTRAINT "strength_time_sets_strength_entry_id_fkey" FOREIGN KEY (strength_entry_id) REFERENCES public.strength_entries(strength_entry_id) ON DELETE CASCADE;

ALTER TABLE "public"."boulder_entries"
  ADD CONSTRAINT "boulder_entries_training_day_id_fkey" FOREIGN KEY (training_day_id) REFERENCES public.training_days(training_day_id) ON DELETE CASCADE;

ALTER TABLE "public"."endurance_entries"
  ADD CONSTRAINT "endurance_entries_training_day_id_fkey" FOREIGN KEY (training_day_id) REFERENCES public.training_days(training_day_id) ON DELETE CASCADE;

ALTER TABLE "public"."strength_entries"
  ADD CONSTRAINT "strength_entries_training_day_id_fkey" FOREIGN KEY (training_day_id) REFERENCES public.training_days(training_day_id) ON DELETE CASCADE;

ALTER TABLE "public"."training_days"
  ADD CONSTRAINT "training_days_profile_id_fkey" FOREIGN KEY (profile_id) REFERENCES public.profiles(profile_id) ON DELETE CASCADE;

CREATE INDEX idx_boulder_entries_training_day_id ON public.boulder_entries USING btree (training_day_id);

CREATE INDEX idx_boulder_routes_boulder_entry_id ON public.boulder_routes USING btree (boulder_entry_id);

CREATE INDEX idx_boulder_routes_difficulty_grade_id ON public.boulder_routes USING btree (difficulty_grade_id);

CREATE INDEX idx_endurance_entries_exercise_id ON public.endurance_entries USING btree (exercise_id);

CREATE INDEX idx_endurance_entries_training_day_id ON public.endurance_entries USING btree (training_day_id);

CREATE INDEX idx_exercise_muscle_groups_exercise_id ON public.exercise_muscle_groups USING btree (exercise_id);

CREATE INDEX idx_exercise_muscle_groups_muscle_group_id ON public.exercise_muscle_groups USING btree (muscle_group_id);

CREATE INDEX idx_exercise_muscle_groups_role ON public.exercise_muscle_groups USING btree (ROLE);

CREATE INDEX idx_exercises_exercise_category_id ON public.exercises USING btree (exercise_category_id);

CREATE INDEX idx_exercises_exercise_name ON public.exercises USING btree (exercise_name);

CREATE INDEX idx_strength_entries_exercise_id ON public.strength_entries USING btree (exercise_id);

CREATE INDEX idx_strength_entries_training_day_id ON public.strength_entries USING btree (training_day_id);

CREATE INDEX idx_strength_rep_sets_strength_entry_id ON public.strength_rep_sets USING btree (strength_entry_id);

CREATE INDEX idx_strength_time_sets_strength_entry_id ON public.strength_time_sets USING btree (strength_entry_id);

CREATE INDEX idx_training_days_profile_id ON public.training_days USING btree (profile_id);

CREATE POLICY "Users can delete their own boulder entries" ON "public"."boulder_entries"
  FOR DELETE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = boulder_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can insert their own boulder entries" ON "public"."boulder_entries"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = boulder_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can update their own boulder entries" ON "public"."boulder_entries"
  FOR UPDATE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = boulder_entries.training_day_id) AND (td.profile_id = auth.uid())))))
  WITH CHECK ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = boulder_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can view their own boulder entries" ON "public"."boulder_entries"
  FOR SELECT
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = boulder_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can delete their own boulder routes" ON "public"."boulder_routes"
  FOR DELETE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.boulder_entries be
     JOIN public.training_days td ON ((td.training_day_id = be.training_day_id)))
  WHERE ((be.boulder_entry_id = boulder_routes.boulder_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can insert their own boulder routes" ON "public"."boulder_routes"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((EXISTS ( SELECT 1
   FROM (public.boulder_entries be
     JOIN public.training_days td ON ((td.training_day_id = be.training_day_id)))
  WHERE ((be.boulder_entry_id = boulder_routes.boulder_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can update their own boulder routes" ON "public"."boulder_routes"
  FOR UPDATE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.boulder_entries be
     JOIN public.training_days td ON ((td.training_day_id = be.training_day_id)))
  WHERE ((be.boulder_entry_id = boulder_routes.boulder_entry_id) AND (td.profile_id = auth.uid())))))
  WITH CHECK ((EXISTS ( SELECT 1
   FROM (public.boulder_entries be
     JOIN public.training_days td ON ((td.training_day_id = be.training_day_id)))
  WHERE ((be.boulder_entry_id = boulder_routes.boulder_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can view their own boulder routes" ON "public"."boulder_routes"
  FOR SELECT
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.boulder_entries be
     JOIN public.training_days td ON ((td.training_day_id = be.training_day_id)))
  WHERE ((be.boulder_entry_id = boulder_routes.boulder_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Authenticated users can view difficulty grades" ON "public"."difficulty_grades"
  FOR SELECT
  TO "authenticated"
  USING (true);

CREATE POLICY "Users can delete their own endurance entries" ON "public"."endurance_entries"
  FOR DELETE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = endurance_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can insert their own endurance entries" ON "public"."endurance_entries"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = endurance_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can update their own endurance entries" ON "public"."endurance_entries"
  FOR UPDATE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = endurance_entries.training_day_id) AND (td.profile_id = auth.uid())))))
  WITH CHECK ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = endurance_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can view their own endurance entries" ON "public"."endurance_entries"
  FOR SELECT
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = endurance_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Authenticated users can view exercise categories" ON "public"."exercise_categories"
  FOR SELECT
  TO "authenticated"
  USING (true);

CREATE POLICY "Authenticated users can view exercise muscle groups" ON "public"."exercise_muscle_groups"
  FOR SELECT
  TO "authenticated"
  USING (true);

CREATE POLICY "Authenticated users can view exercises" ON "public"."exercises"
  FOR SELECT
  TO "authenticated"
  USING (true);

CREATE POLICY "Authenticated users can view muscle groups" ON "public"."muscle_groups"
  FOR SELECT
  TO "authenticated"
  USING (true);

CREATE POLICY "Users can insert their own profile" ON "public"."profiles"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((auth.uid() = profile_id));

CREATE POLICY "Users can update their own profile" ON "public"."profiles"
  FOR UPDATE
  TO "authenticated"
  USING ((auth.uid() = profile_id))
  WITH CHECK ((auth.uid() = profile_id));

CREATE POLICY "Users can view their own profile" ON "public"."profiles"
  FOR SELECT
  TO "authenticated"
  USING ((auth.uid() = profile_id));

CREATE POLICY "Users can delete their own strength entries" ON "public"."strength_entries"
  FOR DELETE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = strength_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can insert their own strength entries" ON "public"."strength_entries"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = strength_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can update their own strength entries" ON "public"."strength_entries"
  FOR UPDATE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = strength_entries.training_day_id) AND (td.profile_id = auth.uid())))))
  WITH CHECK ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = strength_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can view their own strength entries" ON "public"."strength_entries"
  FOR SELECT
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM public.training_days td
  WHERE ((td.training_day_id = strength_entries.training_day_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can delete their own strength rep sets" ON "public"."strength_rep_sets"
  FOR DELETE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_rep_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can insert their own strength rep sets" ON "public"."strength_rep_sets"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_rep_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can update their own strength rep sets" ON "public"."strength_rep_sets"
  FOR UPDATE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_rep_sets.strength_entry_id) AND (td.profile_id = auth.uid())))))
  WITH CHECK ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_rep_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can view their own strength rep sets" ON "public"."strength_rep_sets"
  FOR SELECT
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_rep_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can delete their own strength time sets" ON "public"."strength_time_sets"
  FOR DELETE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_time_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can insert their own strength time sets" ON "public"."strength_time_sets"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_time_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can update their own strength time sets" ON "public"."strength_time_sets"
  FOR UPDATE
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_time_sets.strength_entry_id) AND (td.profile_id = auth.uid())))))
  WITH CHECK ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_time_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can view their own strength time sets" ON "public"."strength_time_sets"
  FOR SELECT
  TO "authenticated"
  USING ((EXISTS ( SELECT 1
   FROM (public.strength_entries se
     JOIN public.training_days td ON ((td.training_day_id = se.training_day_id)))
  WHERE ((se.strength_entry_id = strength_time_sets.strength_entry_id) AND (td.profile_id = auth.uid())))));

CREATE POLICY "Users can delete their own training days" ON "public"."training_days"
  FOR DELETE
  TO "authenticated"
  USING ((profile_id = auth.uid()));

CREATE POLICY "Users can insert their own training days" ON "public"."training_days"
  FOR INSERT
  TO "authenticated"
  WITH CHECK ((profile_id = auth.uid()));

CREATE POLICY "Users can update their own training days" ON "public"."training_days"
  FOR UPDATE
  TO "authenticated"
  USING ((profile_id = auth.uid()))
  WITH CHECK ((profile_id = auth.uid()));

CREATE POLICY "Users can view their own training days" ON "public"."training_days"
  FOR SELECT
  TO "authenticated"
  USING ((profile_id = auth.uid()));

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."boulder_entries" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."boulder_routes" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."difficulty_grades" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."endurance_entries" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."exercise_categories" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."exercise_muscle_groups" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."exercises" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."muscle_groups" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."profiles" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."strength_entries" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."strength_rep_sets" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."strength_time_sets" TO "anon", "authenticated", "postgres", "service_role";

GRANT DELETE, INSERT, MAINTAIN, REFERENCES, SELECT, TRIGGER, TRUNCATE, UPDATE ON TABLE "public"."training_days" TO "anon", "authenticated", "postgres", "service_role";

