<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class WorkoutPlan extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'name',
        'persona_archetype',
        'day_of_week',
        'focus_area',
        'estimated_duration_minutes',
        'is_template',
    ];

    protected function casts(): array
    {
        return [
            'day_of_week' => 'integer',
            'estimated_duration_minutes' => 'integer',
            'is_template' => 'boolean',
        ];
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    public function exercises(): HasMany
    {
        return $this->hasMany(WorkoutPlanExercise::class, 'workout_plan_id')->orderBy('order_in_routine');
    }

    public function logs(): HasMany
    {
        return $this->hasMany(WorkoutLog::class);
    }
}
