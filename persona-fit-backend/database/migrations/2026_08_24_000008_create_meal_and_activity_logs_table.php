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
        Schema::create('meal_logs', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();
            $table->date('log_date');
            $table->enum('meal_type', ['breakfast', 'lunch', 'dinner', 'snack'])->default('lunch');
            $table->string('food_name', 150);
            $table->integer('calories')->default(0);
            $table->decimal('protein_grams', 5, 2)->default(0);
            $table->decimal('carbs_grams', 5, 2)->default(0);
            $table->decimal('fat_grams', 5, 2)->default(0);
            $table->timestamps();
        });

        Schema::create('water_logs', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();
            $table->date('log_date');
            $table->integer('amount_ml')->default(250);
            $table->timestamps();
        });

        Schema::create('step_logs', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();
            $table->date('log_date');
            $table->integer('step_count')->default(0);
            $table->decimal('distance_km', 6, 2)->default(0);
            $table->decimal('calories_burned', 6, 2)->default(0);
            $table->timestamps();

            $table->unique(['user_id', 'log_date']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('step_logs');
        Schema::dropIfExists('water_logs');
        Schema::dropIfExists('meal_logs');
    }
};
