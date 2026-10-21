<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class ExerciseContraindication extends Model
{
    use HasFactory;

    protected $fillable = [
        'exercise_id',
        'limitation_id',
        'alternative_exercise_id',
        'risk_mechanism',
    ];

    public function exercise(): BelongsTo
    {
        return $this->belongsTo(Exercise::class, 'exercise_id');
    }

    public function limitation(): BelongsTo
    {
        return $this->belongsTo(PhysicalLimitation::class, 'limitation_id');
    }

    public function alternativeExercise(): BelongsTo
    {
        return $this->belongsTo(Exercise::class, 'alternative_exercise_id');
    }
}
