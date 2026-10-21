<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class UserProfile extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'gender',
        'age',
        'height_cm',
        'weight_kg',
        'target_weight_kg',
        'activity_level',
        'fitness_goal',
        'available_days',
        'training_experience',
        'exercise_type',
        'workout_location',
        'joint_health_status',
        'joint_limitations',
        'persona_archetype',
        'daily_calories',
        'protein_grams',
        'carbs_grams',
        'fat_grams',
        'water_ml_target',
    ];

    protected function casts(): array
    {
        return [
            'height_cm' => 'float',
            'weight_kg' => 'float',
            'target_weight_kg' => 'float',
            'available_days' => 'array',
            'joint_limitations' => 'array',
            'daily_calories' => 'integer',
            'protein_grams' => 'integer',
            'carbs_grams' => 'integer',
            'fat_grams' => 'integer',
            'water_ml_target' => 'integer',
        ];
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }
}
