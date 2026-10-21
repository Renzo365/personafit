<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class UserLimitation extends Model
{
    use HasFactory;

    protected $table = 'user_limitations';

    protected $fillable = [
        'user_id',
        'limitation_id',
        'severity',
        'notes',
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    public function limitation(): BelongsTo
    {
        return $this->belongsTo(PhysicalLimitation::class, 'limitation_id');
    }
}
