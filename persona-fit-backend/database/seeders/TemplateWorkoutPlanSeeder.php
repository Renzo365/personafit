<?php

namespace Database\Seeders;

use App\Models\Exercise;
use App\Models\WorkoutPlan;
use App\Models\WorkoutPlanExercise;
use Illuminate\Database\Seeder;

class TemplateWorkoutPlanSeeder extends Seeder
{
    /**
     * Run the database seeds.
     * Maps the official PersonaFit exercises into tailored routine templates.
     */
    public function run(): void
    {
        $templates = [
            // ==================== THE BUSY PROFESSIONAL ====================
            [
                'name' => 'Busy Pro - Day 1: Upper Body & Posture',
                'persona_archetype' => 'busy_pro',
                'day_of_week' => 1,
                'focus_area' => 'Upper Body & Posture Alignment',
                'estimated_duration_minutes' => 30,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Seated Cable Rows', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Barbell Bench Press', 'sets' => 3, 'min' => 8, 'max' => 10, 'rest' => 45],
                    ['exercise' => 'Cable Lateral Raises', 'sets' => 3, 'min' => 12, 'max' => 15, 'rest' => 30],
                    ['exercise' => 'Cable Tricep Pushdowns', 'sets' => 3, 'min' => 12, 'max' => 15, 'rest' => 30],
                ],
            ],
            [
                'name' => 'Busy Pro - Day 2: Lower Body & Core',
                'persona_archetype' => 'busy_pro',
                'day_of_week' => 3,
                'focus_area' => 'Lower Body Strength & Core',
                'estimated_duration_minutes' => 30,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Leg Press Machine', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Romanian Deadlift (Barbell/Dumbbell)', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Glute Bridges', 'sets' => 3, 'min' => 12, 'max' => 15, 'rest' => 30],
                    ['exercise' => 'Standing Calf Raises (Flat Ground)', 'sets' => 3, 'min' => 15, 'max' => 20, 'rest' => 30],
                ],
            ],
            [
                'name' => 'Busy Pro - Day 3: Full Body Conditioning',
                'persona_archetype' => 'busy_pro',
                'day_of_week' => 5,
                'focus_area' => 'Full Body Efficiency Split',
                'estimated_duration_minutes' => 30,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Goblet Squat (Dumbbell/Kettlebell)', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Lat Pulldown Machine', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Standing Overhead Shoulder Press', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Dumbbell Bicep Curls', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 30],
                ],
            ],

            // ==================== THE GENTLE STARTER ====================
            [
                'name' => 'Gentle Starter - Day 1: Low-Impact Foundation',
                'persona_archetype' => 'gentle_starter',
                'day_of_week' => 1,
                'focus_area' => 'Joint Mobility & Low Impact Movement',
                'estimated_duration_minutes' => 25,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Chest Press Machine', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Lat Pulldown Machine', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Chair / Box Squats', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Glute Bridges', 'sets' => 2, 'min' => 10, 'max' => 12, 'rest' => 45],
                ],
            ],
            [
                'name' => 'Gentle Starter - Day 2: Posture & Core Stability',
                'persona_archetype' => 'gentle_starter',
                'day_of_week' => 3,
                'focus_area' => 'Gentle Calisthenics & Core',
                'estimated_duration_minutes' => 25,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Incline / Knee Push-ups', 'sets' => 3, 'min' => 8, 'max' => 10, 'rest' => 60],
                    ['exercise' => 'Doorframe / Towel Rows', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Prone Cobra / Superman Holds', 'sets' => 3, 'min' => 15, 'max' => 20, 'rest' => 45],
                    ['exercise' => 'Standing Calf Raises (Flat Ground)', 'sets' => 2, 'min' => 12, 'max' => 15, 'rest' => 45],
                ],
            ],

            // ==================== THE HOME WARRIOR ====================
            [
                'name' => 'Home Warrior - Day 1: Calisthenics & Dumbbell Split',
                'persona_archetype' => 'home_warrior',
                'day_of_week' => 1,
                'focus_area' => 'Home Dumbbell & Bodyweight Conditioning',
                'estimated_duration_minutes' => 35,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Standard Floor Push-ups', 'sets' => 3, 'min' => 10, 'max' => 15, 'rest' => 45],
                    ['exercise' => 'Doorframe / Towel Rows', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Chair / Box Squats', 'sets' => 3, 'min' => 12, 'max' => 15, 'rest' => 45],
                    ['exercise' => 'Glute Bridges', 'sets' => 3, 'min' => 15, 'max' => 20, 'rest' => 30],
                ],
            ],
            [
                'name' => 'Home Warrior - Day 2: Upper Pull & Lower Unilateral',
                'persona_archetype' => 'home_warrior',
                'day_of_week' => 3,
                'focus_area' => 'Unilateral Balance & Calisthenic Pull',
                'estimated_duration_minutes' => 35,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Inverted Rows (Under Sturdy Table)', 'sets' => 3, 'min' => 8, 'max' => 10, 'rest' => 60],
                    ['exercise' => 'Walking / Reverse Lunges', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Chair / Bench Dips', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 45],
                    ['exercise' => 'Side Plank Hip Abductions', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 30],
                ],
            ],

            // ==================== THE DEDICATED ATHLETE ====================
            [
                'name' => 'Dedicated Athlete - Day 1: Push Hypertrophy',
                'persona_archetype' => 'athlete',
                'day_of_week' => 1,
                'focus_area' => 'Chest, Delts & Triceps Intensity',
                'estimated_duration_minutes' => 50,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Barbell Bench Press', 'sets' => 4, 'min' => 6, 'max' => 8, 'rest' => 90],
                    ['exercise' => 'Strict Standing Overhead Barbell Press', 'sets' => 3, 'min' => 8, 'max' => 10, 'rest' => 90],
                    ['exercise' => 'Pec Deck / Chest Fly Machine', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Cable Tricep Pushdowns', 'sets' => 3, 'min' => 12, 'max' => 15, 'rest' => 60],
                ],
            ],
            [
                'name' => 'Dedicated Athlete - Day 2: Pull Hypertrophy',
                'persona_archetype' => 'athlete',
                'day_of_week' => 2,
                'focus_area' => 'Back, Rear Delts & Biceps Intensity',
                'estimated_duration_minutes' => 50,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Conventional / Sumo Barbell Deadlifts', 'sets' => 3, 'min' => 5, 'max' => 5, 'rest' => 120],
                    ['exercise' => 'Pull-ups / Chin-ups', 'sets' => 4, 'min' => 6, 'max' => 8, 'rest' => 90],
                    ['exercise' => 'Seated Cable Rows', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                    ['exercise' => 'Incline Dumbbell Curls', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                ],
            ],
            [
                'name' => 'Dedicated Athlete - Day 3: Lower Body Power',
                'persona_archetype' => 'athlete',
                'day_of_week' => 4,
                'focus_area' => 'Quad, Hamstring & Glute Power',
                'estimated_duration_minutes' => 50,
                'is_template' => true,
                'exercises' => [
                    ['exercise' => 'Barbell Back Squats', 'sets' => 4, 'min' => 6, 'max' => 8, 'rest' => 120],
                    ['exercise' => 'Heavy Bulgarian Split Squats', 'sets' => 3, 'min' => 8, 'max' => 10, 'rest' => 90],
                    ['exercise' => 'Romanian Deadlift (Barbell/Dumbbell)', 'sets' => 3, 'min' => 8, 'max' => 10, 'rest' => 60],
                    ['exercise' => 'Heavy Barbell Hip Thrusts', 'sets' => 3, 'min' => 10, 'max' => 12, 'rest' => 60],
                ],
            ],
        ];

        foreach ($templates as $tmpl) {
            $plan = WorkoutPlan::updateOrCreate(
                [
                    'name' => $tmpl['name'],
                    'persona_archetype' => $tmpl['persona_archetype'],
                    'day_of_week' => $tmpl['day_of_week'],
                ],
                [
                    'focus_area' => $tmpl['focus_area'],
                    'estimated_duration_minutes' => $tmpl['estimated_duration_minutes'],
                    'is_template' => true,
                ]
            );

            // Clear previous template exercise mappings and re-insert
            $plan->exercises()->delete();

            $order = 1;
            foreach ($tmpl['exercises'] as $exData) {
                $ex = Exercise::where('name', $exData['exercise'])->first();
                if ($ex) {
                    WorkoutPlanExercise::create([
                        'workout_plan_id' => $plan->id,
                        'exercise_id' => $ex->id,
                        'order_in_routine' => $order++,
                        'target_sets' => $exData['sets'],
                        'target_reps_min' => $exData['min'],
                        'target_reps_max' => $exData['max'],
                        'rest_seconds' => $exData['rest'],
                    ]);
                }
            }
        }
    }
}
