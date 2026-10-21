<?php

$dataset = json_decode(file_get_contents(__DIR__ . '/exercises_dataset.json'), true);
$datasetById = [];
foreach ($dataset as $item) {
    $datasetById[sprintf('%04d', (int)$item['id'])] = $item;
}

$assetsDir = __DIR__ . '/assets';
$files = scandir($assetsDir);
$gifFiles = array_values(array_filter($files, fn($f) => preg_match('/^\d+\.gif$/', $f)));

echo "Starting asset rename and catalog mapping for " . count($gifFiles) . " GIF files...\n";

// Target muscle mapping
function mapTargetMuscle($target, $category) {
    $t = strtolower(trim($target));
    $c = strtolower(trim($category));
    
    switch ($t) {
        case 'pectorals': return 'Chest';
        case 'lats': return 'Back (Lats)';
        case 'upper back': return 'Back (Upper Back)';
        case 'traps': return 'Back (Traps)';
        case 'spine': return 'Lower Back';
        case 'levator scapulae': return 'Neck';
        case 'delts': return 'Shoulders';
        case 'biceps': return 'Biceps';
        case 'triceps': return 'Triceps';
        case 'forearms': return 'Forearms';
        case 'abs': return 'Abs';
        case 'serratus anterior': return 'Core';
        case 'quads': return 'Quads';
        case 'hamstrings': return 'Hamstrings';
        case 'glutes': return 'Glutes';
        case 'calves': return 'Calves';
        case 'adductors': return 'Adductors';
        case 'abductors': return 'Abductors';
        case 'cardiovascular system': return 'Cardio';
        default: return ucfirst($t ?: $c);
    }
}

// Exercise Type: Compound vs Isolation
function classifyExerciseType($name, $target, $equipment, $category) {
    $n = strtolower($name);
    
    // Explicit single-joint exclusions even if they match compound words
    if (str_contains($n, 'straight arm') || str_contains($n, 'straight-arm') || str_contains($n, 'pullover')) {
        return 'Isolation';
    }
    if (str_contains($n, 'pushdown') || str_contains($n, 'push-down') || str_contains($n, 'triceps extension') || str_contains($n, 'tricep extension')) {
        return 'Isolation';
    }
    if (str_contains($n, 'leg extension') || str_contains($n, 'leg curl') || str_contains($n, 'hamstring curl')) {
        return 'Isolation';
    }
    if (str_contains($n, 'calf raise') || str_contains($n, 'heel raise') || str_contains($n, 'toe raise')) {
        return 'Isolation';
    }
    if (str_contains($n, 'lateral raise') || str_contains($n, 'front raise') || str_contains($n, 'rear delt fly') || str_contains($n, 'reverse fly')) {
        return 'Isolation';
    }
    if (str_contains($n, 'fly') || str_contains($n, 'flye') || str_contains($n, 'crossover') || str_contains($n, 'pec deck')) {
        return 'Isolation';
    }
    if (str_contains($n, 'curl') && !str_contains($n, 'clean')) {
        return 'Isolation';
    }

    // Explicit Compound movements
    if (preg_match('/\b(squat|bench press|chest press|overhead press|military press|shoulder press|push-up|pushup|pull-up|pullup|chin-up|chinup|deadlift|lunge|split squat|step-up|step up|leg press|hack squat|clean|snatch|thruster|dip|dips|upright row|face pull|burpee|mountain climber|bear crawl|battle rope|jump rope|rowing|ergometer|skierg|bike|elliptical|stepmill)\b/', $n)) {
        return 'Compound';
    }
    if (preg_match('/\brow\b|\brows\b/', $n)) {
        return 'Compound';
    }
    if (str_contains($n, 'pulldown') || str_contains($n, 'pull-down') || str_contains($n, 'lat pull')) {
        return 'Compound';
    }
    if (str_contains($n, 'hip thrust') || str_contains($n, 'glute bridge')) {
        return 'Compound';
    }

    return 'Isolation';
}

// Exercise Level: beginner, intermediate, advance
function classifyExerciseLevel($name, $equipment, $target, $category) {
    $n = strtolower($name);
    $eq = strtolower($equipment);

    // 1. Advance
    if (preg_match('/\b(snatch|clean and jerk|clean and press|power clean|hang clean|split jerk|clean-grip|jerk|muscle-up|muscle up|handstand|dragon flag|pistol squat|pistol|planche|front lever|back lever|human flag|clapping|plyometric|plyo|one arm pull|one arm push|one-arm pull|one-arm push|overhead squat|deficit deadlift|tire flip|ring dip|l-sit|windshield wiper|flag|salute|jump squat|box jump|depth jump)\b/', $n)) {
        return 'advance';
    }
    if ($eq === 'olympic barbell') {
        return 'advance';
    }

    // 2. Beginner
    if (str_contains($n, 'stretch') || str_contains($n, 'roller') || $eq === 'roller') {
        return 'beginner';
    }
    if (str_starts_with($n, 'assisted') || $eq === 'assisted' || str_contains($n, 'band') || $eq === 'band' || $eq === 'resistance band') {
        return 'beginner';
    }
    if (str_contains($eq, 'machine') || $eq === 'smith machine' || $eq === 'leverage machine' || $eq === 'stationary bike' || $eq === 'elliptical machine' || $eq === 'stepmill machine') {
        return 'beginner';
    }
    if ($eq === 'body weight' || $eq === 'bodyweight') {
        if (preg_match('/\b(pull-up|pullup|chin-up|chinup|dip|dips|hanging leg raise|hanging knee raise|inverted row)\b/', $n)) {
            return 'intermediate';
        }
        return 'beginner';
    }
    if ($eq === 'stability ball' || $eq === 'bosu ball' || $eq === 'medicine ball') {
        return 'beginner';
    }

    // 3. Intermediate (Free weights: barbells, dumbbells, cables, kettlebells)
    return 'intermediate';
}

// Special canonical mappings & snake_case sanitizer
function generateFilename($id, $name, $equipment, $target) {
    // Explicit canonical overrides based on prompt instructions
    if ($id === '0662') {
        return 'pushup_proper.gif';
    }
    if ($id === '0043') {
        return 'barbell_back_squat.gif';
    }
    if ($id === '1760') {
        return 'goblet_squat.gif';
    }
    if ($id === '0534') {
        return 'goblet_squat_kettlebell.gif';
    }
    if ($id === '0659') {
        return 'pushup_wall_prevention.gif';
    }
    if ($id === '3211') {
        return 'pushup_knee_prevention.gif';
    }
    if ($id === '0493') {
        return 'pushup_incline_prevention.gif';
    }

    // Duplicate pairs resolution
    $duplicateOverrides = [
        '0088' => 'barbell_seated_calf_raise_proper.gif',
        '1371' => 'barbell_seated_calf_raise_var2.gif',
        '0296' => 'dumbbell_close_grip_press_proper.gif',
        '1731' => 'dumbbell_close_grip_press_var2.gif',
        '0422' => 'dumbbell_standing_one_arm_curl_over_incline_bench_proper.gif',
        '1680' => 'dumbbell_standing_one_arm_curl_over_incline_bench_var2.gif',
        '0454' => 'ez_barbell_spider_curl_proper.gif',
        '1628' => 'ez_barbell_spider_curl_var2.gif',
        '0576' => 'lever_chest_press_proper.gif',
        '0577' => 'lever_chest_press_var2.gif',
        '0655' => 'push_up_on_stability_ball_proper.gif',
        '0656' => 'push_up_on_stability_ball_var2.gif',
        '0697' => 'self_assisted_inverse_leg_curl_proper.gif',
        '1766' => 'self_assisted_inverse_leg_curl_var2.gif',
        '0763' => 'smith_reverse_calf_raises_proper.gif',
        '1394' => 'smith_reverse_calf_raises_var2.gif',
    ];

    if (isset($duplicateOverrides[$id])) {
        return $duplicateOverrides[$id];
    }

    // Standard snake_case generation
    $str = $name;
    // Replace degree signs
    $str = preg_replace('/[°в°]+/', '_degree_', $str);
    // Replace 3/4
    $str = str_replace('3/4', '3_4', $str);
    // Replace &
    $str = str_replace('&', 'and', $str);
    // Replace v. 2, v. 3
    $str = preg_replace('/\bv\.?\s*(\d+)\b/i', 'v$1', $str);
    // Remove apostrophes, quotes
    $str = str_replace(["'", '"', '`'], '', $str);
    // Replace non-alphanumeric with underscore
    $str = preg_replace('/[^a-zA-Z0-9]+/', '_', $str);
    // Collapse multiple underscores
    $str = preg_replace('/_+/', '_', $str);
    $baseName = strtolower(trim($str, '_'));

    return $baseName . '.gif';
}

$mapping = [];
$renamedCount = 0;

foreach ($gifFiles as $gif) {
    $id = pathinfo($gif, PATHINFO_FILENAME);
    $item = $datasetById[sprintf('%04d', (int)$id)] ?? null;
    if (!$item) {
        die("Missing metadata for {$gif}\n");
    }

    $newFile = generateFilename($id, $item['name'], $item['equipment'], $item['target']);
    $exerciseName = ucwords(str_replace(['_', '-'], ' ', $item['name']));
    $targetMuscle = mapTargetMuscle($item['target'], $item['category']);
    $exerciseType = classifyExerciseType($item['name'], $item['target'], $item['equipment'], $item['category']);
    $exerciseLevel = classifyExerciseLevel($item['name'], $item['equipment'], $item['target'], $item['category']);

    $oldPath = $assetsDir . '/' . $gif;
    $newPath = $assetsDir . '/' . $newFile;

    // Perform rename
    if (!rename($oldPath, $newPath)) {
        die("Error renaming {$oldPath} to {$newPath}!\n");
    }
    $renamedCount++;

    $mapping[] = [
        'original_filename' => $gif,
        'new_filename' => $newFile,
        'exercise_name' => $exerciseName,
        'target_muscle' => $targetMuscle,
        'exercise_type' => $exerciseType,
        'exercise_level' => $exerciseLevel,
        'equipment' => $item['equipment'],
        'body_part' => $item['body_part'],
    ];
}

echo "Successfully renamed {$renamedCount} files!\n";

// Create alias copies for prompt canonical examples so both forms are immediately accessible:
// standard_pushup.gif <=> pushup_proper.gif
if (file_exists($assetsDir . '/pushup_proper.gif') && !file_exists($assetsDir . '/standard_pushup.gif')) {
    copy($assetsDir . '/pushup_proper.gif', $assetsDir . '/standard_pushup.gif');
    echo "Created alias copy: standard_pushup.gif -> pushup_proper.gif\n";
}
// barbell_back_squat.gif <=> barbell_back_squat_proper.gif
if (file_exists($assetsDir . '/barbell_back_squat.gif') && !file_exists($assetsDir . '/barbell_back_squat_proper.gif')) {
    copy($assetsDir . '/barbell_back_squat.gif', $assetsDir . '/barbell_back_squat_proper.gif');
    echo "Created alias copy: barbell_back_squat_proper.gif -> barbell_back_squat.gif\n";
}
// goblet_squat.gif <=> dumbbell_goblet_squat.gif
if (file_exists($assetsDir . '/goblet_squat.gif') && !file_exists($assetsDir . '/dumbbell_goblet_squat.gif')) {
    copy($assetsDir . '/goblet_squat.gif', $assetsDir . '/dumbbell_goblet_squat.gif');
    echo "Created alias copy: dumbbell_goblet_squat.gif -> goblet_squat.gif\n";
}

// 1. Save JSON mapping
$jsonPath = $assetsDir . '/exercise_assets_mapping.json';
file_put_contents($jsonPath, json_encode($mapping, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES));
echo "Saved JSON mapping to: {$jsonPath}\n";

// 2. Save CSV mapping
$csvPath = $assetsDir . '/exercise_assets_mapping.csv';
$fp = fopen($csvPath, 'w');
fputcsv($fp, ['original_filename', 'new_filename', 'exercise_name', 'target_muscle', 'exercise_type', 'exercise_level', 'equipment', 'body_part']);
foreach ($mapping as $row) {
    fputcsv($fp, [
        $row['original_filename'],
        $row['new_filename'],
        $row['exercise_name'],
        $row['target_muscle'],
        $row['exercise_type'],
        $row['exercise_level'],
        $row['equipment'],
        $row['body_part'],
    ]);
}
fclose($fp);
echo "Saved CSV mapping to: {$csvPath}\n";

// Also copy mapping to backend storage/app and public so Admin CMS can read it
$backendStorageDir = __DIR__ . '/persona-fit-backend/storage/app';
if (is_dir($backendStorageDir)) {
    copy($jsonPath, $backendStorageDir . '/exercise_assets_mapping.json');
    copy($csvPath, $backendStorageDir . '/exercise_assets_mapping.csv');
    echo "Synced mapping files to backend storage directory: {$backendStorageDir}\n";
}

$backendPublicDir = __DIR__ . '/persona-fit-backend/public';
if (is_dir($backendPublicDir)) {
    if (!is_dir($backendPublicDir . '/assets')) {
        mkdir($backendPublicDir . '/assets', 0755, true);
    }
    copy($jsonPath, $backendPublicDir . '/assets/exercise_assets_mapping.json');
    copy($csvPath, $backendPublicDir . '/assets/exercise_assets_mapping.csv');
    echo "Synced mapping files to backend public directory: {$backendPublicDir}/assets\n";
}

echo "All tasks completed successfully!\n";
