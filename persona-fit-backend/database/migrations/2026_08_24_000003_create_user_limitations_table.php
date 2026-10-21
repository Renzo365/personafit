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
        Schema::create('user_limitations', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->cascadeOnDelete();
            $table->foreignId('limitation_id')->constrained('physical_limitations')->cascadeOnDelete();
            $table->enum('severity', ['mild', 'moderate', 'severe'])->default('moderate');
            $table->text('notes')->nullable();
            $table->timestamps();

            $table->unique(['user_id', 'limitation_id']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('user_limitations');
    }
};
