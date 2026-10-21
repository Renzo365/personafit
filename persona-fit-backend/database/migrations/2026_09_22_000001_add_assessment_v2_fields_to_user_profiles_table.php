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
        Schema::table('user_profiles', function (Blueprint $table) {
            $table->json('available_days')->nullable()->after('fitness_goal');
            $table->string('training_experience', 50)->nullable()->default('intermediate')->after('available_days');
            $table->string('exercise_type', 50)->nullable()->default('compound')->after('training_experience');
            $table->string('workout_location', 50)->nullable()->default('gym')->after('exercise_type');
            $table->string('joint_health_status', 50)->nullable()->default('No Limitations')->after('workout_location');
            $table->json('joint_limitations')->nullable()->after('joint_health_status');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('user_profiles', function (Blueprint $table) {
            $table->dropColumn([
                'available_days',
                'training_experience',
                'exercise_type',
                'workout_location',
                'joint_health_status',
                'joint_limitations',
            ]);
        });
    }
};
