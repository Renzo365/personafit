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
            $table->string('anatomy_view', 20)->default('dual')->after('injury_prevention_asset');
            $table->text('primary_muscle_regions')->nullable()->after('anatomy_view');
            $table->text('secondary_muscle_regions')->nullable()->after('primary_muscle_regions');
            $table->string('muscle_mapping_confidence', 20)->default('high')->after('secondary_muscle_regions');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('exercises', function (Blueprint $table) {
            $table->dropColumn([
                'anatomy_view',
                'primary_muscle_regions',
                'secondary_muscle_regions',
                'muscle_mapping_confidence',
            ]);
        });
    }
};
