<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Illuminate\Database\Eloquent\Relations\HasMany;

class PhysicalLimitation extends Model
{
    use HasFactory;

    protected $fillable = [
        'code',
        'name',
        'description',
        'affected_body_part',
    ];

    public function users(): BelongsToMany
    {
        return $this->belongsToMany(User::class, 'user_limitations', 'limitation_id', 'user_id')
            ->withPivot('severity', 'notes')
            ->withTimestamps();
    }

    public function contraindications(): HasMany
    {
        return $this->hasMany(ExerciseContraindication::class, 'limitation_id');
    }
}
