<?php

namespace Database\Seeders;

use App\Models\PhysicalLimitation;
use App\Models\User;
use App\Models\UserProfile;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // 1. Admin User
        User::updateOrCreate(
            ['email' => 'admin@personafit.com'],
            [
                'name' => 'System Coach / Admin',
                'password' => Hash::make('password123'),
                'role' => 'admin',
            ]
        );

        // 2. Demo Mobile User
        $user = User::updateOrCreate(
            ['email' => 'ralph@example.com'],
            [
                'name' => 'Ralph Mobile User',
                'password' => Hash::make('password123'),
                'role' => 'app_user',
            ]
        );

        UserProfile::updateOrCreate(
            ['user_id' => $user->id],
            [
                'gender' => 'male',
                'age' => 25,
                'height_cm' => 178.00,
                'weight_kg' => 78.50,
                'target_weight_kg' => 74.00,
                'activity_level' => 'moderate',
                'fitness_goal' => 'fat_loss',
                'available_days' => ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                'training_experience' => 'intermediate',
                'persona_archetype' => 'busy_pro',
                'daily_calories' => 2150,
                'protein_grams' => 155,
                'carbs_grams' => 230,
                'fat_grams' => 65,
                'water_ml_target' => 2800,
            ]
        );

        // Attach Lower Back limitation to demo user
        $backPain = PhysicalLimitation::where('code', 'LOWER_BACK_PAIN')->first();
        if ($backPain) {
            $user->limitations()->syncWithoutDetaching([
                $backPain->id => [
                    'severity' => 'moderate',
                    'notes' => 'Twinge in lumbar during axial spinal loads.',
                ],
            ]);
        }
    }
}
