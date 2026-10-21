<?php
function generateAnatomyComposite($primaryRegions, $secondaryRegions, $outputPath) {
    $basePath = 'C:/xampp/htdocs/persona-fit/assets/anatomy/anatomy_template_clean.png';
    $base = imagecreatefrompng($basePath);
    $w = imagesx($base);
    $h = imagesy($base);
    
    // Enable alpha blending
    imagealphablending($base, true);
    imagesavealpha($base, true);
    
    // Primary highlight color: Neon Mint (#00FF88) or Vivid Red (#FF3333) with transparency
    // Let's use Vivid Red #FF1111 (alpha 35) or Neon Mint to match user's template style
    // The user's template uses bright red #FF0000! Let's provide authentic template red for primary and cyan/orange for secondary
    $maskDir = 'C:/xampp/htdocs/persona-fit/assets/anatomy/masks';
    
    // Render secondary highlights first (e.g. Neon Cyan #00E5FF)
    foreach ($secondaryRegions as $region) {
        $mPath = "$maskDir/mask_{$region}.png";
        if (!file_exists($mPath)) continue;
        $mask = imagecreatefrompng($mPath);
        // Tint mask with Cyan #00E5FF (alpha 45)
        for ($y=0; $y<$h; $y++) {
            for ($x=0; $x<$w; $x++) {
                $c = imagecolorat($mask, $x, $y);
                $a = ($c >> 24) & 0x7F;
                if ($a < 100) { // masked pixel
                    $col = imagecolorallocatealpha($base, 0, 229, 255, 45);
                    imagesetpixel($base, $x, $y, $col);
                }
            }
        }
        imagedestroy($mask);
    }
    
    // Render primary highlights (Vivid Red #FF0000 or Electric Mint #00FF88)
    foreach ($primaryRegions as $region) {
        // If it's chest, we can even use the exact user Image 1 chest mask!
        $mPath = ($region === 'chest' && file_exists('C:/xampp/htdocs/persona-fit/assets/anatomy/mask_chest.png'))
            ? 'C:/xampp/htdocs/persona-fit/assets/anatomy/mask_chest.png'
            : "$maskDir/mask_{$region}.png";
            
        if (!file_exists($mPath)) continue;
        $mask = imagecreatefrompng($mPath);
        for ($y=0; $y<$h; $y++) {
            for ($x=0; $x<$w; $x++) {
                $c = imagecolorat($mask, $x, $y);
                $a = ($c >> 24) & 0x7F;
                if ($a < 100) { // masked pixel
                    // User template vivid red #FF0000
                    $col = imagecolorallocatealpha($base, 255, 20, 20, 20);
                    imagesetpixel($base, $x, $y, $col);
                }
            }
        }
        imagedestroy($mask);
    }
    
    imagepng($base, $outputPath);
    imagedestroy($base);
    echo "Saved composite to $outputPath\n";
}

$outDir = 'C:/Users/ralph/.gemini/antigravity/brain/360a6b31-e128-460e-bcc8-ccf40f186f9b';
generateAnatomyComposite(['chest'], ['deltoids_front', 'triceps'], "$outDir/preview_pushup.png");
generateAnatomyComposite(['quads'], ['glutes', 'hamstrings'], "$outDir/preview_squat.png");
generateAnatomyComposite(['biceps'], ['forearms_front'], "$outDir/preview_bicep_curl.png");
generateAnatomyComposite(['lats'], ['biceps', 'deltoids_rear'], "$outDir/preview_lat_pulldown.png");
generateAnatomyComposite(['glutes'], ['hamstrings', 'lower_back'], "$outDir/preview_glute_bridge.png");
