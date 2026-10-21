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
            $table->string('proper_form_asset', 255)->nullable()->after('image_url');
            $table->string('common_mistakes_asset', 255)->nullable()->after('proper_form_asset');
            $table->string('injury_prevention_asset', 255)->nullable()->after('common_mistakes_asset');
            $table->integer('default_sets')->default(3)->after('difficulty');
            $table->string('default_reps', 50)->default('8-12 reps')->after('default_sets');
            $table->string('safe_starting_load', 100)->nullable()->after('default_reps');
            $table->integer('default_rest_seconds')->default(60)->after('safe_starting_load');
            $table->text('breathing_tips')->nullable()->after('injury_prevention_cues');
            $table->text('benefits')->nullable()->after('breathing_tips');
            $table->text('muscle_explanation')->nullable()->after('benefits');
        });

        // Initialize proper_form_asset from existing image_url
        \DB::table('exercises')->whereNotNull('image_url')->update([
            'proper_form_asset' => \DB::raw('image_url')
        ]);
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('exercises', function (Blueprint $table) {
            $table->dropColumn([
                'proper_form_asset',
                'common_mistakes_asset',
                'injury_prevention_asset',
                'default_sets',
                'default_reps',
                'safe_starting_load',
                'default_rest_seconds',
                'breathing_tips',
                'benefits',
                'muscle_explanation',
            ]);
        });
    }
};
