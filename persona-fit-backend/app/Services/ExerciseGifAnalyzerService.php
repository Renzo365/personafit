<?php

namespace App\Services;

class ExerciseGifAnalyzerService
{
    protected array $catalog = [];
    protected string $assetsBaseDir;

    public function __construct()
    {
        $this->assetsBaseDir = realpath(base_path('../assets')) ?: base_path('../assets');
        $catalogPath = $this->assetsBaseDir . '/exercise_assets_mapping.json';
        if (file_exists($catalogPath)) {
            $raw = json_decode(file_get_contents($catalogPath), true);
            if (is_array($raw)) {
                foreach ($raw as $item) {
                    $fn = strtolower($item['new_filename'] ?? '');
                    if ($fn) {
                        $this->catalog[$fn] = $item;
                    }
                    $ofn = strtolower($item['original_filename'] ?? '');
                    if ($ofn) {
                        $this->catalog[$ofn] = $item;
                    }
                }
            }
        }
    }

    /**
     * Analyze a GIF asset by filename or path.
     */
    public function analyzeGif(string $assetFilenameOrPath): array
    {
        $resolvedPath = $this->resolveAssetPath($assetFilenameOrPath);
        if (!$resolvedPath || !file_exists($resolvedPath)) {
            return [
                'success' => false,
                'error' => "Asset file not found: {$assetFilenameOrPath}",
                'confidence' => 'low',
                'anatomy_view' => 'dual',
                'primary_muscle_regions' => [],
                'secondary_muscle_regions' => [],
            ];
        }

        $baseFilename = strtolower(basename($resolvedPath));
        $fileSize = filesize($resolvedPath);

        // 1. Inspect GIF frame and pixel highlights
        $pixelAnalysis = $this->inspectGifHighlightPixels($resolvedPath);

        // 2. Catalog Lookup
        $catalogEntry = $this->findCatalogEntry($baseFilename);

        // 3. Derive Primary and Secondary Regions
        $targetMuscle = $catalogEntry['target_muscle'] ?? $this->inferMuscleFromVisualCentroid($pixelAnalysis, $baseFilename);
        $secondaryString = $catalogEntry['secondary_muscles'] ?? $this->inferSecondaryMuscles($targetMuscle, $baseFilename);

        $primaryRegions = $this->mapMuscleToRegionKeys($targetMuscle, true, $pixelAnalysis);
        $secondaryRegions = $this->mapMuscleToRegionKeys($secondaryString, false, $pixelAnalysis);

        // Filter out primary from secondary
        $secondaryRegions = array_values(array_diff($secondaryRegions, $primaryRegions));

        // 4. Determine Anatomy View
        $anatomyView = $this->determineAnatomyView($primaryRegions, $secondaryRegions);

        // 5. Calculate Confidence Score
        $confidence = 'high';
        if ($pixelAnalysis['highlight_pixels'] === 0 && !$catalogEntry) {
            $confidence = 'low';
        } elseif ($pixelAnalysis['highlight_pixels'] < 50 || !$catalogEntry) {
            $confidence = 'medium';
        }

        return [
            'success' => true,
            'filename' => $baseFilename,
            'file_size' => $fileSize,
            'suggested_exercise_name' => $catalogEntry['exercise_name'] ?? $this->formatExerciseNameFromFilename($baseFilename),
            'target_muscle' => $targetMuscle,
            'secondary_muscles' => $secondaryString,
            'primary_muscle_regions' => $primaryRegions,
            'secondary_muscle_regions' => $secondaryRegions,
            'anatomy_view' => $anatomyView,
            'muscle_mapping_confidence' => $confidence,
            'pixel_analysis' => $pixelAnalysis,
        ];
    }

    /**
     * Inspect GIF frames for target muscle highlights (red/salmon hues).
     */
    protected function inspectGifHighlightPixels(string $path): array
    {
        if (!function_exists('imagecreatefromgif')) {
            return ['highlight_pixels' => 0, 'centroid' => null, 'bounding_box' => null];
        }

        $im = @imagecreatefromgif($path);
        if (!$im) {
            return ['highlight_pixels' => 0, 'centroid' => null, 'bounding_box' => null];
        }

        $w = imagesx($im);
        $h = imagesy($im);
        $totalColors = imagecolorstotal($im);

        $highlightIndices = [];
        for ($i = 0; $i < $totalColors; $i++) {
            $c = imagecolorsforindex($im, $i);
            $r = $c['red']; $g = $c['green']; $b = $c['blue'];
            // Detect reddish / salmon / coral highlight colors typical in 3D fitness anatomy GIFs
            if ($r > 130 && $r > $g * 1.35 && $r > $b * 1.35) {
                $highlightIndices[$i] = true;
            }
        }

        $pixelCount = 0;
        $minX = $w; $maxX = 0;
        $minY = $h; $maxY = 0;
        $sumX = 0; $sumY = 0;

        for ($y = 0; $y < $h; $y += 2) {
            for ($x = 0; $x < $w; $x += 2) {
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

        imagedestroy($im);

        if ($pixelCount > 0) {
            $avgX = round($sumX / $pixelCount);
            $avgY = round($sumY / $pixelCount);
            return [
                'highlight_pixels' => $pixelCount * 4, // account for step 2
                'centroid' => [
                    'x' => $avgX,
                    'y' => $avgY,
                    'rel_x' => round($avgX / $w, 3),
                    'rel_y' => round($avgY / $h, 3),
                ],
                'bounding_box' => [
                    'min_x' => $minX, 'max_x' => $maxX,
                    'min_y' => $minY, 'max_y' => $maxY,
                    'width' => $maxX - $minX,
                    'height' => $maxY - $minY,
                ]
            ];
        }

        return ['highlight_pixels' => 0, 'centroid' => null, 'bounding_box' => null];
    }

    /**
     * Map friendly muscle names to the 16 exact template region keys.
     */
    public function mapMuscleToRegionKeys(string $muscleString, bool $isPrimary = true, array $pixelAnalysis = []): array
    {
        $s = strtolower($muscleString);
        $regions = [];

        // Check Chest
        if (str_contains($s, 'chest') || str_contains($s, 'pec')) {
            $regions[] = 'chest';
        }

        // Check Deltoids / Shoulders
        if (str_contains($s, 'shoulder') || str_contains($s, 'delt')) {
            if (str_contains($s, 'rear') || str_contains($s, 'posterior')) {
                $regions[] = 'deltoids_rear';
            } elseif (str_contains($s, 'front') || str_contains($s, 'anterior')) {
                $regions[] = 'deltoids_front';
            } else {
                $regions[] = 'deltoids_front';
                $regions[] = 'deltoids_rear';
            }
        }

        // Check Biceps
        if (str_contains($s, 'bicep')) {
            $regions[] = 'biceps';
        }

        // Check Triceps
        if (str_contains($s, 'tricep')) {
            $regions[] = 'triceps';
        }

        // Check Forearms
        if (str_contains($s, 'forearm') || str_contains($s, 'wrist') || str_contains($s, 'grip')) {
            $regions[] = 'forearms_front';
        }

        // Check Abs / Core / Waist
        if (str_contains($s, 'ab') || str_contains($s, 'core') || str_contains($s, 'waist')) {
            $regions[] = 'abs';
            if (str_contains($s, 'oblique')) {
                $regions[] = 'obliques';
            }
        }
        if (str_contains($s, 'oblique')) {
            $regions[] = 'obliques';
        }

        // Check Lats / Back
        if (str_contains($s, 'lat') || str_contains($s, 'pulldown') || str_contains($s, 'pullup')) {
            $regions[] = 'lats';
        }
        if (str_contains($s, 'trap') || str_contains($s, 'rhomboid') || str_contains($s, 'upper back')) {
            $regions[] = 'traps';
        }
        if (str_contains($s, 'lower back') || str_contains($s, 'erector') || str_contains($s, 'lumbar')) {
            $regions[] = 'lower_back';
        }
        if (str_contains($s, 'back') && !in_array('lats', $regions) && !in_array('traps', $regions) && !in_array('lower_back', $regions)) {
            $regions[] = 'lats';
            $regions[] = 'traps';
        }

        // Check Quads / Legs
        if (str_contains($s, 'quad') || str_contains($s, 'thigh')) {
            $regions[] = 'quads';
        }

        // Check Glutes
        if (str_contains($s, 'glute') || str_contains($s, 'butt') || str_contains($s, 'hip')) {
            $regions[] = 'glutes';
        }

        // Check Hamstrings
        if (str_contains($s, 'hamstring')) {
            $regions[] = 'hamstrings';
        }

        // Check Calves
        if (str_contains($s, 'calf') || str_contains($s, 'calves') || str_contains($s, 'soleus') || str_contains($s, 'gastrocnemius')) {
            $regions[] = 'calves_rear';
            $regions[] = 'calves_front';
        }

        // Fallback for general "legs"
        if (str_contains($s, 'leg') && empty(array_intersect($regions, ['quads', 'glutes', 'hamstrings', 'calves_rear']))) {
            $regions[] = 'quads';
            $regions[] = 'hamstrings';
        }

        // Fallback for general "arms"
        if (str_contains($s, 'arm') && !in_array('biceps', $regions) && !in_array('triceps', $regions)) {
            $regions[] = 'biceps';
            $regions[] = 'triceps';
        }

        return array_unique($regions);
    }

    /**
     * Determine optimal anatomy view (anterior, posterior, or dual).
     */
    protected function determineAnatomyView(array $primaryRegions, array $secondaryRegions): string
    {
        $all = array_merge($primaryRegions, $secondaryRegions);
        $anteriorKeys = ['chest', 'deltoids_front', 'biceps', 'forearms_front', 'abs', 'obliques', 'quads', 'calves_front'];
        $posteriorKeys = ['traps', 'deltoids_rear', 'triceps', 'lats', 'lower_back', 'glutes', 'hamstrings', 'calves_rear'];

        $hasAnterior = !empty(array_intersect($all, $anteriorKeys));
        $hasPosterior = !empty(array_intersect($all, $posteriorKeys));

        if ($hasAnterior && $hasPosterior) {
            return 'dual';
        }
        if ($hasPosterior) {
            return 'posterior';
        }
        return 'anterior';
    }

    protected function findCatalogEntry(string $filename): ?array
    {
        if (isset($this->catalog[$filename])) {
            return $this->catalog[$filename];
        }
        // Match base name without extensions or prefix
        $clean = preg_replace('/(_proper|_prevention|_mistake)?\.gif$/', '', $filename);
        foreach ($this->catalog as $key => $entry) {
            if (str_starts_with($key, $clean) || str_starts_with($clean, str_replace('.gif', '', $key))) {
                return $entry;
            }
        }
        return null;
    }

    protected function inferMuscleFromVisualCentroid(array $pixelAnalysis, string $filename): string
    {
        $f = strtolower($filename);
        if (str_contains($f, 'pushup') || str_contains($f, 'press')) return 'Chest';
        if (str_contains($f, 'squat')) return 'Quads';
        if (str_contains($f, 'curl')) return 'Biceps';
        if (str_contains($f, 'pulldown') || str_contains($f, 'row')) return 'Back';
        if (str_contains($f, 'bridge') || str_contains($f, 'hip')) return 'Glutes';
        if (str_contains($f, 'dip') || str_contains($f, 'extension')) return 'Triceps';

        if (!empty($pixelAnalysis['centroid'])) {
            $relY = $pixelAnalysis['centroid']['rel_y'];
            if ($relY < 0.28) return 'Shoulders';
            if ($relY < 0.45) return 'Chest';
            if ($relY < 0.60) return 'Abs';
            if ($relY < 0.80) return 'Quads';
            return 'Calves';
        }
        return 'Chest';
    }

    protected function inferSecondaryMuscles(string $primaryMuscle, string $filename): string
    {
        $p = strtolower($primaryMuscle);
        if (str_contains($p, 'chest')) return 'Front Delts, Triceps, Core';
        if (str_contains($p, 'quad')) return 'Glutes, Hamstrings, Core';
        if (str_contains($p, 'back') || str_contains($p, 'lat')) return 'Biceps, Rear Delts, Rhomboids';
        if (str_contains($p, 'bicep')) return 'Forearms, Front Delts';
        if (str_contains($p, 'tricep')) return 'Front Delts, Chest';
        if (str_contains($p, 'glute')) return 'Hamstrings, Core, Erector Spinae';
        if (str_contains($p, 'shoulder')) return 'Triceps, Upper Traps';
        if (str_contains($p, 'ab') || str_contains($p, 'core')) return 'Obliques, Hip Flexors';
        return 'Core';
    }

    protected function formatExerciseNameFromFilename(string $filename): string
    {
        $name = pathinfo($filename, PATHINFO_FILENAME);
        $name = str_replace(['_', '-'], ' ', $name);
        return ucwords($name);
    }

    protected function resolveAssetPath(string $assetFilenameOrPath): ?string
    {
        if (file_exists($assetFilenameOrPath)) {
            return $assetFilenameOrPath;
        }

        $base = basename($assetFilenameOrPath);
        $candidates = [
            $this->assetsBaseDir . '/' . $base,
            public_path('assets/' . $base),
            storage_path('app/public/exercises/' . $base),
            storage_path('app/public/' . $base),
        ];

        foreach ($candidates as $c) {
            if (file_exists($c)) return $c;
        }

        return null;
    }
}
