<?php
require __DIR__ . '/persona-fit-backend/vendor/autoload.php';
$app = require_once __DIR__ . '/persona-fit-backend/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB;
use App\Models\Exercise;

$columns = Schema::getColumnListing('exercises');
echo "Columns in 'exercises' table:\n";
print_r($columns);

$sample = Exercise::first();
echo "\nSample Exercise (#{$sample->id} - {$sample->name}):\n";
print_r($sample->toArray());
