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
        Schema::create('exercise_contraindications', function (Blueprint $table) {
            $table->id();
            $table->foreignId('exercise_id')->constrained('exercises')->cascadeOnDelete();
            $table->foreignId('limitation_id')->constrained('physical_limitations')->cascadeOnDelete();
            $table->foreignId('alternative_exercise_id')->nullable()->constrained('exercises')->nullOnDelete();
            $table->string('risk_mechanism', 255)->nullable(); // e.g. "Heavy axial spinal compression", "Acute patellofemoral shear"
            $table->timestamps();

            $table->unique(['exercise_id', 'limitation_id'], 'ex_contra_unique');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('exercise_contraindications');
    }
};
