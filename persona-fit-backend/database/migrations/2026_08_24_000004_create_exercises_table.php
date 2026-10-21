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
        Schema::create('exercises', function (Blueprint $table) {
            $table->id();
            $table->string('name', 150);
            $table->string('target_muscle', 50); // e.g., Chest, Back, Quads, Hamstrings, Shoulders, Arms, Core
            $table->string('secondary_muscles', 100)->nullable();
            $table->enum('equipment_type', ['bodyweight', 'dumbbell', 'barbell', 'machine', 'cable', 'bands'])->default('bodyweight');
            $table->enum('difficulty', ['beginner', 'intermediate', 'advanced'])->default('beginner');
            $table->text('instructions');
            $table->text('common_mistakes')->nullable();
            $table->text('injury_prevention_cues')->nullable();
            $table->string('image_url', 255)->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('exercises');
    }
};
