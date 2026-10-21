<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class WorkoutLog extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'workout_plan_id',
        'session_name',
        'started_at',
        'completed_at',
        'total_duration_seconds',
        'status',
        'total_exercises_completed',
        'total_volume_kg',
        'notes',
    ];

    protected function casts(): array
    {
        return [
            'started_at' => 'datetime',
            'completed_at' => 'datetime',
            'total_duration_seconds' => 'integer',
            'total_exercises_completed' => 'integer',
            'total_volume_kg' => 'float',
        ];
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    public function plan(): BelongsTo
    {
        return $this->belongsTo(WorkoutPlan::class, 'workout_plan_id');
    }

    public function items(): HasMany
    {
        return $this->hasMany(WorkoutLogItem::class, 'workout_log_id');
    }
}
