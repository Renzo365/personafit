<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class WorkoutLogItem extends Model
{
    use HasFactory;

    protected $fillable = [
        'workout_log_id',
        'exercise_id',
        'set_number',
        'weight_kg',
        'reps_completed',
        'is_completed',
    ];

    protected function casts(): array
    {
        return [
            'set_number' => 'integer',
            'weight_kg' => 'float',
            'reps_completed' => 'integer',
            'is_completed' => 'boolean',
        ];
    }

    public function workoutLog(): BelongsTo
    {
        return $this->belongsTo(WorkoutLog::class, 'workout_log_id');
    }

    public function exercise(): BelongsTo
    {
        return $this->belongsTo(Exercise::class, 'exercise_id');
    }
}
