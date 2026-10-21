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
        Schema::create('workout_logs', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();
            $table->foreignId('workout_plan_id')->nullable()->constrained('workout_plans')->nullOnDelete();
            $table->string('session_name', 150)->default('Workout Session');
            $table->timestamp('started_at')->useCurrent();
            $table->timestamp('completed_at')->nullable();
            $table->integer('total_duration_seconds')->default(0);
            $table->enum('status', ['in_progress', 'completed', 'abandoned'])->default('completed');
            $table->integer('total_exercises_completed')->default(0);
            $table->decimal('total_volume_kg', 8, 2)->default(0);
            $table->text('notes')->nullable();
            $table->timestamps();
        });

        Schema::create('workout_log_items', function (Blueprint $table) {
            $table->id();
            $table->foreignId('workout_log_id')->constrained('workout_logs')->cascadeOnDelete();
            $table->foreignId('exercise_id')->constrained('exercises')->cascadeOnDelete();
            $table->integer('set_number')->default(1);
            $table->decimal('weight_kg', 5, 2)->default(0);
            $table->integer('reps_completed')->default(0);
            $table->boolean('is_completed')->default(true);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('workout_log_items');
        Schema::dropIfExists('workout_logs');
    }
};
