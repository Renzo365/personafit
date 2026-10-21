<?php
require __DIR__ . '/persona-fit-backend/vendor/autoload.php';
$app = require_once __DIR__ . '/persona-fit-backend/bootstrap/app.php';
$kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

$testGifs = [
    'Standard Push-ups' => 'c:/xampp/htdocs/persona-fit/assets/pushup_proper.gif',
    'Squat' => 'c:/xampp/htdocs/persona-fit/assets/barbell_back_squat.gif',
    'Bicep Curls' => 'c:/xampp/htdocs/persona-fit/assets/dumbbell_biceps_curl.gif',
    'Lat Pulldowns' => 'c:/xampp/htdocs/persona-fit/assets/cable_lat_pulldown_full_range_of_motion.gif',
    'Glute Bridges' => 'c:/xampp/htdocs/persona-fit/assets/barbell_glute_bridge.gif'
];

foreach ($testGifs as $label => $path) {
    echo "====================================================\n";
    echo "EXERCISE: $label\n";
    echo "File: " . basename($path) . "\n";
    if (!file_exists($path)) {
        echo "FILE NOT FOUND!\n";
        continue;
    }
    
    $info = getimagesize($path);
    $w = $info[0];
    $h = $info[1];
    echo "Dimensions: {$w}x{$h}, Filesize: " . filesize($path) . " bytes\n";
    
    $im = imagecreatefromgif($path);
    $totalColors = imagecolorstotal($im);
    
    // Find highlight color indices (red/salmon)
    $highlightIndices = [];
    for ($i=0; $i<$totalColors; $i++) {
        $c = imagecolorsforindex($im, $i);
        $r = $c['red']; $g = $c['green']; $b = $c['blue'];
        if ($r > 130 && $r > $g * 1.35 && $r > $b * 1.35) {
            $highlightIndices[$i] = true;
        }
    }
    
    $pixelCount = 0;
    $minX = $w; $maxX = 0;
    $minY = $h; $maxY = 0;
    $sumX = 0; $sumY = 0;
    
    for ($y=0; $y<$h; $y++) {
        for ($x=0; $x<$w; $x++) {
            $idx = imagecolorat($im, $x, $y);
            if (isset($highlightIndices[$idx])) {
                $pixelCount++;
                if ($x < $minX) $minX = $x;
                if ($x > $maxX) $maxX = $x;
                if ($y < $minY) $minY = $y;
                if ($y > $maxY) $maxY = $y;
                $sumX += $x;
                $sumY += $y;
            }
        }
    }
    
    echo "Highlight Pixels: $pixelCount\n";
    if ($pixelCount > 0) {
        $avgX = round($sumX / $pixelCount);
        $avgY = round($sumY / $pixelCount);
        $relX = round($avgX / $w, 2);
        $relY = round($avgY / $h, 2);
        echo "Highlight Bounding Box: X=[$minX, $maxX], Y=[$minY, $maxY]\n";
        echo "Centroid: ($avgX, $avgY) -> Normalized: ($relX, $relY)\n";
        
        // Infer body region from vertical position (relY):
        // relY < 0.25 -> Head/Shoulders/Traps
        // relY 0.25..0.45 -> Upper Body (Chest/Biceps/Upper Back)
        // relY 0.45..0.60 -> Mid-Torso / Waist / Glutes / Hip Hinge
        // relY > 0.60 -> Lower Body (Quads/Hamstrings/Calves)
        if ($relY < 0.25) $regionGuess = "Shoulders / Neck / Traps";
        elseif ($relY < 0.45) $regionGuess = "Upper Body (Chest / Biceps / Lats)";
        elseif ($relY < 0.60) $regionGuess = "Mid-Body / Core / Glutes";
        else $regionGuess = "Lower Body / Quads / Hamstrings / Calves";
        echo "Anatomical Vertical Region: $regionGuess\n";
    } else {
        echo "No distinct red highlight found in initial frame.\n";
    }
    
    imagedestroy($im);
}
