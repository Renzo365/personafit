<?php
/**
 * Exact anatomical coordinate mapping on the 883x655 template.
 * Anterior figure on Left (X: 50..400)
 * Posterior figure on Right (X: 480..830)
 */
$muscleRegions = [
    // ANTERIOR (FRONT)
    'chest' => [
        'view' => 'anterior',
        'label' => 'Pectoralis Major (Chest)',
        'polygon' => [
            // Left pec (figure's right side)
            [152, 134], [195, 128], [218, 136], [220, 185], [195, 192], [165, 182], [152, 155],
            // Right pec (figure's left side)
            [224, 185], [226, 136], [249, 128], [290, 134], [290, 155], [277, 182], [249, 192]
        ],
        // Precise bounding box for overlay
        'bounds' => [152, 128, 290, 192]
    ],
    'deltoids_front' => [
        'view' => 'anterior',
        'label' => 'Anterior Deltoids (Front Shoulders)',
        'bounds' => [118, 125, 324, 185],
        'polygons' => [
            // Left delt
            [[118, 160], [128, 132], [155, 126], [168, 150], [150, 182], [130, 178]],
            // Right delt
            [[274, 150], [287, 126], [314, 132], [324, 160], [312, 178], [292, 182]]
        ]
    ],
    'biceps' => [
        'view' => 'anterior',
        'label' => 'Biceps Brachii',
        'bounds' => [98, 180, 344, 255],
        'polygons' => [
            // Left bicep
            [[112, 185], [136, 182], [144, 225], [130, 250], [105, 235], [102, 200]],
            // Right bicep
            [[306, 182], [330, 185], [340, 200], [337, 235], [312, 250], [298, 225]]
        ]
    ],
    'forearms_front' => [
        'view' => 'anterior',
        'label' => 'Forearms (Flexors)',
        'bounds' => [65, 250, 377, 335],
        'polygons' => [
            [[95, 255], [122, 252], [112, 315], [80, 325], [70, 290]],
            [[320, 252], [347, 255], [372, 290], [362, 325], [330, 315]]
        ]
    ],
    'abs' => [
        'view' => 'anterior',
        'label' => 'Rectus Abdominis (Abs / Core)',
        'bounds' => [192, 192, 250, 305],
        'polygon' => [
            [195, 194], [247, 194], [248, 255], [238, 300], [221, 305], [204, 300], [194, 255]
        ]
    ],
    'obliques' => [
        'view' => 'anterior',
        'label' => 'External Obliques',
        'bounds' => [165, 205, 277, 295],
        'polygons' => [
            [[170, 208], [192, 202], [192, 280], [182, 292], [168, 260]],
            [[250, 202], [272, 208], [274, 260], [260, 292], [250, 280]]
        ]
    ],
    'quads' => [
        'view' => 'anterior',
        'label' => 'Quadriceps (Thighs)',
        'bounds' => [146, 325, 296, 485],
        'polygons' => [
            // Left quad
            [[160, 330], [212, 330], [210, 430], [195, 475], [178, 475], [152, 420]],
            // Right quad
            [[230, 330], [282, 330], [290, 420], [264, 475], [247, 475], [232, 430]]
        ]
    ],
    'calves_front' => [
        'view' => 'anterior',
        'label' => 'Tibialis Anterior & Calves',
        'bounds' => [150, 485, 292, 615],
        'polygons' => [
            [[170, 490], [195, 490], [192, 580], [182, 610], [160, 580], [155, 530]],
            [[247, 490], [272, 490], [287, 530], [282, 580], [260, 610], [250, 580]]
        ]
    ],
    
    // POSTERIOR (BACK)
    'traps' => [
        'view' => 'posterior',
        'label' => 'Trapezius (Upper / Mid Back)',
        'bounds' => [605, 115, 705, 220],
        'polygon' => [
            [655, 115], [685, 135], [702, 175], [678, 218], [655, 225], [632, 218], [608, 175], [625, 135]
        ]
    ],
    'deltoids_rear' => [
        'view' => 'posterior',
        'label' => 'Posterior / Lateral Deltoids',
        'bounds' => [552, 125, 758, 185],
        'polygons' => [
            [[552, 160], [562, 132], [589, 126], [602, 150], [584, 182], [564, 178]],
            [[708, 150], [721, 126], [748, 132], [758, 160], [746, 178], [726, 182]]
        ]
    ],
    'triceps' => [
        'view' => 'posterior',
        'label' => 'Triceps Brachii',
        'bounds' => [532, 165, 778, 255],
        'polygons' => [
            // Left tricep (figure's right side)
            [[546, 170], [570, 168], [578, 215], [564, 250], [539, 235], [536, 195]],
            // Right tricep (figure's left side)
            [[740, 168], [764, 170], [774, 195], [771, 235], [746, 250], [732, 215]]
        ]
    ],
    'lats' => [
        'view' => 'posterior',
        'label' => 'Latissimus Dorsi (Lats)',
        'bounds' => [585, 185, 725, 285],
        'polygons' => [
            // Left lat
            [[590, 190], [632, 218], [635, 275], [605, 280], [585, 225]],
            // Right lat
            [[678, 218], [720, 190], [725, 225], [705, 280], [675, 275]]
        ]
    ],
    'lower_back' => [
        'view' => 'posterior',
        'label' => 'Erector Spinae (Lower Back)',
        'bounds' => [630, 225, 680, 295],
        'polygon' => [
            [635, 225], [675, 225], [675, 290], [655, 295], [635, 290]
        ]
    ],
    'glutes' => [
        'view' => 'posterior',
        'label' => 'Gluteus Maximus',
        'bounds' => [588, 288, 722, 365],
        'polygons' => [
            // Left glute
            [[595, 292], [654, 292], [653, 360], [625, 362], [590, 335]],
            // Right glute
            [[656, 292], [715, 292], [720, 335], [685, 362], [657, 360]]
        ]
    ],
    'hamstrings' => [
        'view' => 'posterior',
        'label' => 'Hamstrings',
        'bounds' => [580, 360, 730, 485],
        'polygons' => [
            // Left hamstring
            [[595, 365], [650, 365], [648, 475], [620, 475], [585, 420]],
            // Right hamstring
            [[660, 365], [715, 365], [725, 420], [690, 475], [662, 475]]
        ]
    ],
    'calves_rear' => [
        'view' => 'posterior',
        'label' => 'Gastrocnemius & Soleus (Calves)',
        'bounds' => [584, 485, 726, 615],
        'polygons' => [
            [[604, 490], [639, 490], [636, 580], [626, 610], [594, 580], [589, 530]],
            [[671, 490], [706, 490], [721, 530], [716, 580], [684, 610], [674, 580]]
        ]
    ]
];

echo "Defined " . count($muscleRegions) . " anatomical muscle regions with exact pixel bounds.\n";

// Let's generate transparent PNG masks for EACH muscle region!
$maskDir = 'C:/xampp/htdocs/persona-fit/assets/anatomy/masks';
if (!is_dir($maskDir)) mkdir($maskDir, 0777, true);

$baseClean = imagecreatefrompng('C:/xampp/htdocs/persona-fit/assets/anatomy/anatomy_template_clean.png');
$w = imagesx($baseClean);
$h = imagesy($baseClean);

foreach ($muscleRegions as $key => $info) {
    $mask = imagecreatetruecolor($w, $h);
    imagealphablending($mask, false);
    imagesavealpha($mask, true);
    $trans = imagecolorallocatealpha($mask, 0, 0, 0, 127);
    imagefill($mask, 0, 0, $trans);
    
    // Electric green for primary, cyan for secondary or pure red mask
    $redFill = imagecolorallocatealpha($mask, 255, 0, 0, 30); // semi-transparent red
    
    // Draw polygon or polygons
    if (isset($info['polygon'])) {
        $flat = [];
        foreach ($info['polygon'] as $pt) { $flat[] = $pt[0]; $flat[] = $pt[1]; }
        imagefilledpolygon($mask, $flat, $redFill);
    } elseif (isset($info['polygons'])) {
        foreach ($info['polygons'] as $poly) {
            $flat = [];
            foreach ($poly as $pt) { $flat[] = $pt[0]; $flat[] = $pt[1]; }
            imagefilledpolygon($mask, $flat, $redFill);
        }
    }
    
    imagepng($mask, "$maskDir/mask_{$key}.png");
    imagedestroy($mask);
}
echo "Generated all " . count($muscleRegions) . " mask PNGs in $maskDir\n";

imagedestroy($baseClean);
