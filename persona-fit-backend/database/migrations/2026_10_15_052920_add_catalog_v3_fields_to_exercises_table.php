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
            $table->boolean('is_active')->default(true)->after('id')->index();
            $table->string('required_equipment', 150)->nullable()->after('equipment_type');
            $table->text('common_injuries')->nullable()->after('injury_prevention_cues');
            $table->text('safer_alternatives_text')->nullable()->after('common_injuries');
            $table->text('recommendation_rules')->nullable()->after('safer_alternatives_text');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('exercises', function (Blueprint $table) {
            $table->dropColumn([
                'is_active',
                'required_equipment',
                'common_injuries',
                'safer_alternatives_text',
                'recommendation_rules',
            ]);
        });
    }
};
