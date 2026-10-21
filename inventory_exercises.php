<?php
require __DIR__ . '/persona-fit-backend/vendor/autoload.php';
$app = require_once __DIR__ . '/persona-fit-backend/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use App\Models\Exercise;

$exercises = Exercise::orderBy('id')->get();
echo "Total active exercises in database: " . $exercises->count() . "\n";

$inventory = [];
$assetsDir = 'c:/xampp/htdocs/persona-fit/assets';

foreach ($exercises as $ex) {
    $assetPath = null;
    $found = false;
    
    // Check proper_form_asset, image_url, or name matching
    $candidates = [
        $ex->proper_form_asset,
        $ex->image_url,
        str_replace('exercises/', '', $ex->proper_form_asset ?? ''),
        str_replace('assets/', '', $ex->image_url ?? ''),
        strtolower(preg_replace('/[^a-zA-Z0-9]+/', '_', trim($ex->name))) . '.gif'
    ];
    
    foreach ($candidates as $cand) {
        if (!$cand) continue;
        $full = "$assetsDir/" . basename($cand);
        if (file_exists($full)) {
            $assetPath = $full;
            $found = true;
            break;
        }
    }
    
    $inventory[] = [
        'id' => $ex->id,
        'name' => $ex->name,
        'target_muscle' => $ex->target_muscle,
        'secondary_muscles' => $ex->secondary_muscles,
        'has_asset' => $found,
        'asset' => $assetPath ? basename($assetPath) : 'NONE'
    ];
}

echo "Found assets for " . count(array_filter($inventory, fn($i) => $i['has_asset'])) . " / " . count($inventory) . " exercises.\n";

// Show first 15 and the 5 test exercises
echo "\nSample Inventory:\n";
foreach (array_slice($inventory, 0, 15) as $item) {
    echo "  [#{$item['id']}] {$item['name']} -> {$item['target_muscle']} ({$item['secondary_muscles']}) | Asset: {$item['asset']}\n";
}
