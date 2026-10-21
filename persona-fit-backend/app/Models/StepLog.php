<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class StepLog extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'log_date',
        'step_count',
        'distance_km',
        'calories_burned',
    ];

    protected function casts(): array
    {
        return [
            'log_date' => 'date',
            'step_count' => 'integer',
            'distance_km' => 'float',
            'calories_burned' => 'float',
        ];
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }
}
