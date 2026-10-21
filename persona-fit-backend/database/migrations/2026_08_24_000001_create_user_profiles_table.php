<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('user_profiles', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();
            $table->enum('gender', ['male', 'female']);
            $table->integer('age');
            $table->decimal('height_cm', 5, 2);
            $table->decimal('weight_kg', 5, 2);
            $table->decimal('target_weight_kg', 5, 2)->nullable();
            $table->enum('activity_level', ['sedentary', 'light', 'moderate', 'very_active'])->default('sedentary');
            $table->enum('fitness_goal', ['fat_loss', 'maintenance', 'muscle_gain'])->default('maintenance');
            $table->enum('persona_archetype', ['busy_pro', 'gentle_starter', 'home_warrior', 'athlete'])->default('gentle_starter');
            $table->integer('daily_calories')->default(2000);
            $table->integer('protein_grams')->default(150);
            $table->integer('carbs_grams')->default(200);
            $table->integer('fat_grams')->default(60);
            $table->integer('water_ml_target')->default(2500);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('user_profiles');
    }
};
