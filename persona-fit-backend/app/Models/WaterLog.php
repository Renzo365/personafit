<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class WaterLog extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'log_date',
        'amount_ml',
    ];

    protected function casts(): array
    {
        return [
            'log_date' => 'date',
            'amount_ml' => 'integer',
        ];
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }
}
