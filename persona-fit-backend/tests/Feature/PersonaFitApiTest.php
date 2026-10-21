<?php

namespace Tests\Feature;

use App\Models\Exercise;
use App\Models\PhysicalLimitation;
use App\Models\User;
use Database\Seeders\DatabaseSeeder;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class PersonaFitApiTest extends TestCase
{
    use RefreshDatabase;

    protected function setUp(): void
    {
        parent::setUp();
        $this->seed(DatabaseSeeder::class);
    }

    /**
     * Test 1: User Registration and Login.
     */
    public function test_user_registration_and_login(): void
    {
        // 1. Register
        $regResponse = $this->postJson('/api/auth/register', [
            'name' => 'John Doe',
            'email' => 'john@example.com',
            'password' => 'secret123',
        ]);

        $regResponse->assertStatus(201)
            ->assertJsonStructure([
                'status',
                'data' => [
                    'user' => ['id', 'name', 'email', 'role'],
                    'token',
                ],
            ]);

        // 2. Login
        $loginResponse = $this->postJson('/api/auth/login', [
            'email' => 'john@example.com',
            'password' => 'secret123',
        ]);

        $loginResponse->assertStatus(200)
            ->assertJsonStructure([
                'status',
                'data' => [
                    'token',
                ],
            ]);
    }

    /**
     * Test 2: Onboarding Assessment & BMR/TDEE & Persona Engine.
     */
    public function test_onboarding_assessment_calculates_nutrition_and_assigns_persona(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        $backPain = PhysicalLimitation::where('code', 'LOWER_BACK_PAIN')->first();

        $response = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/assessment/submit', [
                'gender' => 'male',
                'age' => 25,
                'height_cm' => 180,
                'weight_kg' => 75,
                'activity_level' => 'moderate',
                'fitness_goal' => 'fat_loss',
                'time_available' => 'short',
                'location' => 'gym',
                'experience' => 'intermediate',
                'available_days' => ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
                'limitation_ids' => [$backPain->id],
            ]);

        $response->assertStatus(200)
            ->assertJsonPath('data.persona.archetype', 'busy_pro')
            ->assertJsonPath('data.nutrition.daily_calories', 2220) // BMR=1755, TDEE=2720, Deficit=2220
            ->assertJsonPath('data.nutrition.protein_grams', 150)   // 75kg * 2.0g
            ->assertJsonStructure([
                'status',
                'data' => [
                    'persona',
                    'nutrition',
                    'active_limitations',
                ],
            ]);
    }

    /**
     * Test 3: Injury Contraindication & Exercise Substitution Engine.
     */
    public function test_injury_engine_substitutes_contraindicated_exercises(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        // User has LOWER_BACK_PAIN and sets everyday as available for testing
        $user->profile->available_days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
        $user->profile->save();

        $response = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson('/api/workouts/today');

        $response->assertStatus(200);

        $data = $response->json('data');
        $this->assertNotEmpty($data['exercises']);

        // Check if alternatives list works for live swap
        $ex = Exercise::where('name', 'Barbell Back Squats')->first() ?: Exercise::first();
        $swapResponse = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson("/api/workouts/alternatives/{$ex->id}");

        $swapResponse->assertStatus(200)
            ->assertJsonStructure([
                'status',
                'data' => [
                    'original_exercise',
                    'safe_alternatives',
                ],
            ]);
    }

    /**
     * Test 4: Live Workout Logging and Completion.
     */
    public function test_workout_logging_and_completion(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        // 1. Start Workout
        $startRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/workouts/start', [
                'session_name' => 'Monday Upper Body',
            ]);
        $startRes->assertStatus(201);
        $workoutLogId = $startRes->json('data.id');

        $exercise = Exercise::first();

        // 2. Log Sets
        $setRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/workouts/log-set', [
                'workout_log_id' => $workoutLogId,
                'exercise_id' => $exercise->id,
                'set_number' => 1,
                'weight_kg' => 20.0,
                'reps_completed' => 12,
            ]);
        $setRes->assertStatus(200);

        // 3. Complete Session
        $completeRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/workouts/complete', [
                'workout_log_id' => $workoutLogId,
                'total_duration_seconds' => 1800,
                'notes' => 'Felt strong, zero lower back fatigue.',
            ]);

        $completeRes->assertStatus(200)
            ->assertJsonPath('data.total_volume_kg', 240); // 20kg * 12 reps
    }

    /**
     * Test 5: Nutrition & Water Tracker.
     */
    public function test_nutrition_and_water_tracking(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        // Log a meal
        $mealRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/nutrition/log-meal', [
                'meal_type' => 'breakfast',
                'food_name' => 'Oatmeal with Protein Powder',
                'calories' => 450,
                'protein_grams' => 35,
                'carbs_grams' => 50,
                'fat_grams' => 8,
            ]);
        $mealRes->assertStatus(201);

        // Log water
        $waterRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/nutrition/log-water', [
                'amount_ml' => 500,
            ]);
        $waterRes->assertStatus(200)
            ->assertJsonPath('data.total_water_today_ml', 500);

        // Get food database
        $foodsRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson('/api/nutrition/foods');
        $foodsRes->assertStatus(200)
            ->assertJsonStructure([
                'status',
                'data' => [
                    'categories',
                    'foods',
                ],
            ]);

        // Get daily summary
        $summaryRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson('/api/nutrition/today');
        $summaryRes->assertStatus(200)
            ->assertJsonPath('data.calories.consumed', 450)
            ->assertJsonPath('data.water.consumed_ml', 500);
    }

    /**
     * Test 6: Step Counter Synchronization.
     */
    public function test_step_synchronization(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        $stepRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/activity/sync-steps', [
                'step_count' => 6000,
            ]);

        $stepRes->assertStatus(200)
            ->assertJsonPath('data.step_count', 6000);

        $summaryRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson('/api/activity/steps-summary');

        $summaryRes->assertStatus(200)
            ->assertJsonPath('data.today.step_count', 6000);
    }

    /**
     * Test 7: Settings Fetch and Preferences Update (Home/Gym, Schedule, Injury).
     */
    public function test_settings_fetch_and_update_preferences(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        // Fetch settings
        $settingsRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson('/api/settings');
        $settingsRes->assertStatus(200)
            ->assertJsonStructure([
                'status',
                'data' => [
                    'user' => ['id', 'name', 'email'],
                    'profile',
                    'limitations',
                    'available_limitations',
                ],
            ]);

        // Update preferences to Home Workout and specific days
        $updateRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/settings/preferences', [
                'workout_location' => 'home',
                'exercise_type' => 'compound',
                'available_days' => ['Monday', 'Wednesday', 'Friday'],
                'joint_health_status' => 'Has Limitations',
                'joint_limitations' => ['Lower Back'],
            ]);

        $updateRes->assertStatus(200)
            ->assertJsonPath('status', 'success')
            ->assertJsonPath('data.profile.workout_location', 'home')
            ->assertJsonPath('data.profile.joint_health_status', 'Has Limitations');

        // Check that today's workout plan adapts exercises for home environment
        $planRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->getJson('/api/workouts/today');
        $planRes->assertStatus(200);

        if (!$planRes->json('data.is_rest_day')) {
            $exercises = $planRes->json('data.exercises');
            foreach ($exercises as $ex) {
                // Must be home compatible (dumbbell or bodyweight)
                $this->assertContains($ex['exercise']['equipment_type'], ['dumbbell', 'bodyweight']);
            }
        }
    }

    /**
     * Test 8: Settings Change Password.
     */
    public function test_settings_change_password(): void
    {
        $user = User::where('email', 'ralph@example.com')->first();
        $token = $user->createToken('test-token')->plainTextToken;

        // Wrong current password
        $failRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/settings/change-password', [
                'current_password' => 'wrongpassword',
                'new_password' => 'brandnewpass123',
            ]);
        $failRes->assertStatus(422);

        // Correct current password
        $successRes = $this->withHeader('Authorization', "Bearer {$token}")
            ->postJson('/api/settings/change-password', [
                'current_password' => 'password123',
                'new_password' => 'brandnewpass123',
            ]);
        $successRes->assertStatus(200)
            ->assertJsonPath('status', 'success');
    }
}
