<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $this->call([
            LimitationSeeder::class,
            ExerciseSeeder::class,
            EnrichExerciseGuideDataSeeder::class,
            SeedExerciseAnatomyMappingSeeder::class,
            ContraindicationSeeder::class,
            TemplateWorkoutPlanSeeder::class,
            UserSeeder::class,
        ]);
    }
}
