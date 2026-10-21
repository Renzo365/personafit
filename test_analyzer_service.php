<?php
require __DIR__ . '/persona-fit-backend/vendor/autoload.php';
$app = require_once __DIR__ . '/persona-fit-backend/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

use App\Services\ExerciseGifAnalyzerService;

$analyzer = new ExerciseGifAnalyzerService();
$testAssets = [
    'pushup_proper.gif',
    'barbell_back_squat.gif',
    'dumbbell_biceps_curl.gif',
    'cable_lat_pulldown_full_range_of_motion.gif',
    'barbell_glute_bridge.gif'
];

foreach ($testAssets as $asset) {
    $result = $analyzer->analyzeGif($asset);
    echo "========================================\n";
    echo "Asset: $asset\n";
    echo "Name: " . ($result['suggested_exercise_name'] ?? 'N/A') . "\n";
    echo "Target: " . ($result['target_muscle'] ?? 'N/A') . "\n";
    echo "Primary Regions: " . json_encode($result['primary_muscle_regions'] ?? []) . "\n";
    echo "Secondary Regions: " . json_encode($result['secondary_muscle_regions'] ?? []) . "\n";
    echo "Anatomy View: " . ($result['anatomy_view'] ?? 'N/A') . "\n";
    echo "Confidence: " . ($result['muscle_mapping_confidence'] ?? 'N/A') . "\n";
    if (!empty($result['pixel_analysis']['centroid'])) {
        echo "Visual Centroid: (" . $result['pixel_analysis']['centroid']['rel_x'] . ", " . $result['pixel_analysis']['centroid']['rel_y'] . ") pixels=" . $result['pixel_analysis']['highlight_pixels'] . "\n";
    }
}
