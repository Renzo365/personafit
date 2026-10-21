<?php

namespace Tests\Feature;

use App\Models\Exercise;
use App\Models\ExerciseContraindication;
use App\Models\PhysicalLimitation;
use App\Models\User;
use App\Models\WorkoutPlan;
use App\Models\WorkoutPlanExercise;
use App\Services\InjuryPrescriptionEngine;
use Database\Seeders\DatabaseSeeder;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class ExerciseCmsTest extends TestCase
{
    use RefreshDatabase;

    protected function setUp(): void
    {
        parent::setUp();
        $this->seed(DatabaseSeeder::class);
    }

    /**
     * 1. Test Exercise CMS Index page loads with table and badges.
     */
    public function test_exercise_cms_index_loads_with_colored_badges(): void
    {
        $response = $this->get(route('admin.exercises.index'));

        $response->assertStatus(200);
        $response->assertSee('Exercise Repository (CMS)');
        $response->assertSee('Barbell Bench Press');
        $response->assertSee('Compound');
        $response->assertSee('Beginner');
        $response->assertSee('Intermediate');
        $response->assertSee('Advanced');
    }

    /**
     * 2. Test Search by name and target muscle.
     */
    public function test_exercise_search_filters_records(): void
    {
        // Search by exact name
        $response = $this->get(route('admin.exercises.index', ['search' => 'Bench Press']));
        $response->assertStatus(200);
        $response->assertSee('Barbell Bench Press');
        $response->assertDontSee('Pistol Squats');

        // Search by keyword in description
        $responseDesc = $this->get(route('admin.exercises.index', ['search' => 'pectoral']));
        $responseDesc->assertStatus(200);
        $responseDesc->assertSee('Barbell Bench Press');
    }

    /**
     * 3. Test Filtering by Difficulty Level.
     */
    public function test_filter_by_difficulty(): void
    {
        $response = $this->get(route('admin.exercises.index', ['difficulty' => 'advanced']));
        $response->assertStatus(200);
        $response->assertSee('Conventional / Sumo Barbell Deadlifts');
        $response->assertSee('Pistol Squats');
        // Beginner exercise should not appear
        $response->assertDontSee('Standard Floor Push-ups');
    }

    /**
     * 4. Test Filtering by Workout Environment.
     */
    public function test_filter_by_workout_environment(): void
    {
        $benchPress = Exercise::where('name', 'Barbell Bench Press')->first();
        $boxSquats = Exercise::where('name', 'Chair / Box Squats')->first();

        // Test Gym only
        $responseGym = $this->get(route('admin.exercises.index', ['workout_environment' => 'gym']));
        $responseGym->assertStatus(200);
        $responseGym->assertSee(route('admin.exercises.show', $benchPress) . '"', false);
        $responseGym->assertDontSee(route('admin.exercises.show', $boxSquats) . '"', false);

        // Test Home only
        $responseHome = $this->get(route('admin.exercises.index', ['workout_environment' => 'home']));
        $responseHome->assertStatus(200);
        $responseHome->assertSee(route('admin.exercises.show', $boxSquats) . '"', false);
        $responseHome->assertDontSee(route('admin.exercises.show', $benchPress) . '"', false);
    }

    /**
     * 5. Test Filtering by Exercise Type (Compound vs Isolation).
     */
    public function test_filter_by_exercise_type(): void
    {
        // Compound
        $responseCompound = $this->get(route('admin.exercises.index', ['exercise_type' => 'compound']));
        $responseCompound->assertStatus(200);
        $responseCompound->assertSee('Barbell Bench Press');
        $responseCompound->assertDontSee('Pec Deck / Chest Fly Machine');

        // Isolation
        $responseIso = $this->get(route('admin.exercises.index', ['exercise_type' => 'isolation']));
        $responseIso->assertStatus(200);
        $responseIso->assertSee('Pec Deck / Chest Fly Machine');
        $responseIso->assertDontSee('Barbell Bench Press');
    }

    /**
     * 6. Test Filtering by Target Muscle.
     */
    public function test_filter_by_target_muscle(): void
    {
        $response = $this->get(route('admin.exercises.index', ['muscle' => 'Core']));
        $response->assertStatus(200);
        $response->assertSee('Dragon Flags');
        $response->assertSee('Hanging Leg Raises');
        $response->assertDontSee('Barbell Bench Press');
    }

    /**
     * 7. Test Combined Working Filters.
     */
    public function test_combined_working_filters(): void
    {
        $response = $this->get(route('admin.exercises.index', [
            'search' => 'Rows',
            'difficulty' => 'intermediate',
            'workout_environment' => 'gym',
            'exercise_type' => 'compound',
            'muscle' => 'Back',
        ]));

        $response->assertStatus(200);
        $response->assertSee('Seated Cable Rows');
        $response->assertDontSee('Standard Floor Push-ups');
    }

    /**
     * 8. Test Creating a New Exercise with all CMS fields and contraindications.
     */
    public function test_create_new_exercise_persists_to_database(): void
    {
        $limitation = PhysicalLimitation::where('code', 'LOWER_BACK_PAIN')->first();
        $altExercise = Exercise::where('name', 'Doorframe / Towel Rows')->first();

        $postData = [
            'name' => 'Single-Arm Dumbbell Row Variant',
            'description' => 'Unilateral back movement supporting one knee on bench to eliminate spinal flexion load.',
            'target_muscle' => 'Back',
            'secondary_muscles' => 'Biceps, Rear Delts',
            'exercise_type' => 'compound',
            'workout_environment' => 'both',
            'equipment_type' => 'dumbbell',
            'required_equipment' => 'Dumbbell, Bench',
            'difficulty' => 'beginner',
            'instructions' => 'Place one knee and same-side hand on flat bench. Pull dumbbell up towards ribcage keeping back parallel to floor.',
            'common_mistakes' => 'Jerking torso upwards during contraction.',
            'injury_prevention_cues' => 'Bench support protects lumbar spine.',
            'common_injuries' => 'Lower back rounding if bench is too low.',
            'safer_alternatives_text' => 'Chest-supported machine rows.',
            'recommendation_rules' => 'Safe for lower back recovery.',
            'is_active' => true,
            'contraindication_limitations' => [$limitation->id],
            'risk_mechanisms' => [
                $limitation->id => 'Excessive torso rotation can stress facet joints.',
            ],
            'alternative_exercise_ids' => [
                $limitation->id => $altExercise->id,
            ],
        ];

        $response = $this->post(route('admin.exercises.store'), $postData);

        $response->assertRedirect(route('admin.exercises.index'));
        $this->assertDatabaseHas('exercises', [
            'name' => 'Single-Arm Dumbbell Row Variant',
            'exercise_type' => 'compound',
            'workout_environment' => 'both',
            'difficulty' => 'beginner',
        ]);

        $created = Exercise::where('name', 'Single-Arm Dumbbell Row Variant')->first();
        $this->assertDatabaseHas('exercise_contraindications', [
            'exercise_id' => $created->id,
            'limitation_id' => $limitation->id,
            'alternative_exercise_id' => $altExercise->id,
            'risk_mechanism' => 'Excessive torso rotation can stress facet joints.',
        ]);
    }

    /**
     * 9. Test Updating an Existing Exercise.
     */
    public function test_update_existing_exercise_updates_database_and_contraindications(): void
    {
        $exercise = Exercise::where('name', 'Standard Floor Push-ups')->first();
        $wristLimitation = PhysicalLimitation::where('code', 'WRIST_TENDINITIS')->first();

        $putData = [
            'name' => 'Standard Floor Push-ups (Modified)',
            'description' => 'Updated clinical description for neutral push-up variant.',
            'target_muscle' => 'Chest',
            'secondary_muscles' => 'Core, Triceps',
            'exercise_type' => 'compound',
            'workout_environment' => 'home',
            'equipment_type' => 'bodyweight',
            'required_equipment' => 'Floor Mat',
            'difficulty' => 'intermediate',
            'instructions' => 'Place hands under shoulders. Lower chest to floor and press up.',
            'common_mistakes' => 'Flared elbows.',
            'injury_prevention_cues' => 'Neutral wrist keeps carpal tunnel open.',
            'is_active' => true,
            'contraindication_limitations' => [$wristLimitation->id],
            'risk_mechanisms' => [
                $wristLimitation->id => 'Minor pressure on carpal canal if grip slips.',
            ],
        ];

        $response = $this->put(route('admin.exercises.update', $exercise), $putData);

        $response->assertRedirect(route('admin.exercises.index'));
        $this->assertDatabaseHas('exercises', [
            'id' => $exercise->id,
            'name' => 'Standard Floor Push-ups (Modified)',
            'difficulty' => 'intermediate',
        ]);

        $this->assertDatabaseHas('exercise_contraindications', [
            'exercise_id' => $exercise->id,
            'limitation_id' => $wristLimitation->id,
        ]);
    }

    /**
     * 10. Test Delete Safety: Deleting referenced exercise is blocked.
     */
    public function test_delete_safety_blocks_deletion_of_referenced_exercise(): void
    {
        $benchPress = Exercise::where('name', 'Chest Press Machine')->first();
        $this->assertGreaterThan(0, $benchPress->planExercises()->count());

        $response = $this->delete(route('admin.exercises.destroy', $benchPress));

        $response->assertRedirect(route('admin.exercises.index'));
        $response->assertSessionHas('error');

        // Verify exercise was NOT deleted
        $this->assertDatabaseHas('exercises', [
            'id' => $benchPress->id,
            'name' => 'Chest Press Machine',
        ]);
    }

    /**
     * 11. Test Delete Safety: Deleting unreferenced exercise succeeds.
     */
    public function test_delete_unreferenced_exercise_succeeds(): void
    {
        // Create a standalone exercise with no routine references
        $standalone = Exercise::create([
            'name' => 'Temporary Test Movement',
            'description' => 'Test exercise for deletion.',
            'target_muscle' => 'Arms',
            'exercise_type' => 'isolation',
            'workout_environment' => 'home',
            'equipment_type' => 'bands',
            'difficulty' => 'beginner',
            'instructions' => 'Pull resistance bands.',
        ]);

        $response = $this->delete(route('admin.exercises.destroy', $standalone));

        $response->assertRedirect(route('admin.exercises.index'));
        $response->assertSessionHas('success');
        $this->assertDatabaseMissing('exercises', [
            'id' => $standalone->id,
        ]);
    }

    /**
     * 12. Test Recommendation Engine Integration with new environment and exercise type fields.
     */
    public function test_recommendation_engine_adapts_to_environment_and_type(): void
    {
        $engine = new InjuryPrescriptionEngine();
        $user = User::where('email', 'ralph@example.com')->first();
        $profile = $user->profile;

        // 1. Test Home Workout Environment Adaptation
        $profile->workout_location = 'home';
        $profile->save();

        $plan = WorkoutPlan::where('is_template', true)->first();
        $prescribed = $engine->filterAndPrescribePlan($plan, $user);

        // Every prescribed exercise for a home user must be suitable for home
        foreach ($prescribed['exercises'] as $item) {
            $ex = $item['exercise'];
            $this->assertContains($ex->workout_environment, ['home', 'both']);
            $this->assertContains($ex->equipment_type, ['dumbbell', 'bodyweight', 'bands']);
        }

        // 2. Test Safe Alternatives live swap returns CMS fields
        $squat = Exercise::where('name', 'Barbell Back Squats')->first();
        $alternatives = $engine->getSafeAlternatives($squat->id, $user);

        $this->assertNotEmpty($alternatives['safe_alternatives']);
        $firstAlt = $alternatives['safe_alternatives'][0];
        $this->assertArrayHasKey('exercise_type', $firstAlt);
        $this->assertArrayHasKey('workout_environment', $firstAlt);
        $this->assertArrayHasKey('description', $firstAlt);
    }
}
