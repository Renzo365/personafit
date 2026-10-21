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
        Schema::table('exercises', function (Blueprint $table) {
            $table->text('description')->nullable()->after('name');
            $table->enum('exercise_type', ['compound', 'isolation'])->default('compound')->after('target_muscle');
            $table->enum('workout_environment', ['home', 'gym', 'both'])->default('both')->after('exercise_type');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('exercises', function (Blueprint $table) {
            $table->dropColumn(['description', 'exercise_type', 'workout_environment']);
        });
    }
};
