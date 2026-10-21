<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class MealLog extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'log_date',
        'meal_type',
        'food_name',
        'calories',
        'protein_grams',
        'carbs_grams',
        'fat_grams',
    ];

    protected function casts(): array
    {
        return [
            'log_date' => 'date',
            'calories' => 'integer',
            'protein_grams' => 'float',
            'carbs_grams' => 'float',
            'fat_grams' => 'float',
        ];
    }

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }
}
