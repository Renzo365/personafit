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
        Schema::create('workout_plans', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->nullable()->constrained()->cascadeOnDelete();
            $table->string('name', 150);
            $table->enum('persona_archetype', ['busy_pro', 'gentle_starter', 'home_warrior', 'athlete']);
            $table->integer('day_of_week')->default(1); // 1 = Monday, 7 = Sunday
            $table->string('focus_area', 100); // e.g. "Upper Body & Posture", "Lower Body & Core"
            $table->integer('estimated_duration_minutes')->default(35);
            $table->boolean('is_template')->default(false);
            $table->timestamps();
        });

        Schema::create('workout_plan_exercises', function (Blueprint $table) {
            $table->id();
            $table->foreignId('workout_plan_id')->constrained('workout_plans')->cascadeOnDelete();
            $table->foreignId('exercise_id')->constrained('exercises')->cascadeOnDelete();
            $table->integer('order_in_routine')->default(1);
            $table->integer('target_sets')->default(3);
            $table->integer('target_reps_min')->default(8);
            $table->integer('target_reps_max')->default(12);
            $table->integer('rest_seconds')->default(60);
            $table->text('notes')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('workout_plan_exercises');
        Schema::dropIfExists('workout_plans');
    }
};
