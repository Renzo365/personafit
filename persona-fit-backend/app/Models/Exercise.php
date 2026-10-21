<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Exercise extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'description',
        'target_muscle',
        'exercise_type',
        'workout_environment',
        'secondary_muscles',
        'equipment_type',
        'required_equipment',
        'difficulty',
        'default_sets',
        'default_reps',
        'safe_starting_load',
        'default_rest_seconds',
        'instructions',
        'common_mistakes',
        'injury_prevention_cues',
        'common_injuries',
        'safer_alternatives_text',
        'recommendation_rules',
        'breathing_tips',
        'benefits',
        'muscle_explanation',
        'is_active',
        'image_url',
        'proper_form_asset',
        'common_mistakes_asset',
        'injury_prevention_asset',
        'anatomy_view',
        'primary_muscle_regions',
        'secondary_muscle_regions',
        'muscle_mapping_confidence',
    ];

    protected $casts = [
        'is_active' => 'boolean',
        'default_sets' => 'integer',
        'default_rest_seconds' => 'integer',
        'primary_muscle_regions' => 'array',
        'secondary_muscle_regions' => 'array',
    ];

    public function getProperFormAssetAttribute($value): ?string
    {
        return $value ?: $this->image_url;
    }

    public function setProperFormAssetAttribute($value): void
    {
        $this->attributes['proper_form_asset'] = $value;
        if (!empty($value)) {
            $this->attributes['image_url'] = $value;
        }
    }

    public function scopeActive($query)
    {
        return $query->where('is_active', true);
    }

    public function contraindications(): HasMany
    {
        return $this->hasMany(ExerciseContraindication::class, 'exercise_id');
    }

    public function planExercises(): HasMany
    {
        return $this->hasMany(WorkoutPlanExercise::class, 'exercise_id');
    }

    public function logItems(): HasMany
    {
        return $this->hasMany(WorkoutLogItem::class, 'exercise_id');
    }
}
