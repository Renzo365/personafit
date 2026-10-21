<?php

namespace Database\Seeders;

use App\Models\Exercise;
use App\Models\ExerciseContraindication;
use App\Models\PhysicalLimitation;
use Illuminate\Database\Seeder;

class ContraindicationSeeder extends Seeder
{
    /**
     * Run the database seeds.
     * Seeds comprehensive biomechanical contraindication rules and safe alternatives for the official catalog.
     */
    public function run(): void
    {
        $mappings = [
            // ==================== 1. LOWER BACK PAIN RULES ====================
            [
                'exercise' => 'Conventional / Sumo Barbell Deadlifts',
                'limitation' => 'LOWER_BACK_PAIN',
                'alternative' => 'Seated Cable Rows',
                'risk_mechanism' => 'Heavy spinal shear and unsupported lumbar flexion torque under maximal axial load.',
            ],
            [
                'exercise' => 'Barbell Back Squats',
                'limitation' => 'LOWER_BACK_PAIN',
                'alternative' => 'Leg Press Machine',
                'risk_mechanism' => 'Compressive axial loading on lumbar vertebrae and L4-L5 disc compression risk.',
            ],
            [
                'exercise' => 'Romanian Deadlift (Barbell/Dumbbell)',
                'limitation' => 'LOWER_BACK_PAIN',
                'alternative' => 'Lying / Seated Leg Curls',
                'risk_mechanism' => 'High eccentric torque on lumbar erectors during forward hip hinge.',
            ],
            [
                'exercise' => 'Standing Overhead Shoulder Press',
                'limitation' => 'LOWER_BACK_PAIN',
                'alternative' => 'Seated Dumbbell Lateral Raises',
                'risk_mechanism' => 'Lumbar hyperlordosis and facet joint compression under vertical load.',
            ],
            [
                'exercise' => 'Strict Standing Overhead Barbell Press',
                'limitation' => 'LOWER_BACK_PAIN',
                'alternative' => 'Seated Dumbbell Lateral Raises',
                'risk_mechanism' => 'Strict standing axial spinal compression without leg damping.',
            ],
            [
                'exercise' => 'Dragon Flags',
                'limitation' => 'LOWER_BACK_PAIN',
                'alternative' => 'Glute Bridges',
                'risk_mechanism' => 'Extreme anterior pelvic tilt and lumbar hyperextension torque.',
            ],

            // ==================== 2. KNEE PAIN RULES ====================
            [
                'exercise' => 'Barbell Back Squats',
                'limitation' => 'KNEE_PAIN',
                'alternative' => 'Chair / Box Squats',
                'risk_mechanism' => 'Acute deep knee flexion past 90 degrees induces high patellofemoral compressive force.',
            ],
            [
                'exercise' => 'Pistol Squats',
                'limitation' => 'KNEE_PAIN',
                'alternative' => 'Chair / Box Squats',
                'risk_mechanism' => 'Extreme unilateral patellofemoral shear and acute meniscus compression.',
            ],
            [
                'exercise' => 'Walking / Reverse Lunges',
                'limitation' => 'KNEE_PAIN',
                'alternative' => 'Glute Bridges',
                'risk_mechanism' => 'High deceleration shear on anterior cruciate ligament and patellar tendon.',
            ],
            [
                'exercise' => 'Heavy Bulgarian Split Squats',
                'limitation' => 'KNEE_PAIN',
                'alternative' => 'Leg Press Machine',
                'risk_mechanism' => 'Severe unilateral knee shear under heavy loading.',
            ],
            [
                'exercise' => 'Machine Leg Extensions',
                'limitation' => 'KNEE_PAIN',
                'alternative' => 'Leg Press Machine',
                'risk_mechanism' => 'High patellofemoral joint compressive force at terminal 30 degrees of extension.',
            ],

            // ==================== 3. SHOULDER IMPINGEMENT RULES ====================
            [
                'exercise' => 'Barbell Bench Press',
                'limitation' => 'SHOULDER_IMPINGEMENT',
                'alternative' => 'Chest Press Machine',
                'risk_mechanism' => 'Extreme external rotation under barbell load compresses anterior shoulder capsule.',
            ],
            [
                'exercise' => 'Standard Floor Push-ups',
                'limitation' => 'SHOULDER_IMPINGEMENT',
                'alternative' => 'Incline / Knee Push-ups',
                'risk_mechanism' => 'Anterior capsule shear and rotator cuff pinching under full floor bodyweight.',
            ],
            [
                'exercise' => 'Standing Overhead Shoulder Press',
                'limitation' => 'SHOULDER_IMPINGEMENT',
                'alternative' => 'Seated Dumbbell Lateral Raises',
                'risk_mechanism' => 'Pinching of supraspinatus tendon in subacromial space during vertical press.',
            ],
            [
                'exercise' => 'Strict Standing Overhead Barbell Press',
                'limitation' => 'SHOULDER_IMPINGEMENT',
                'alternative' => 'Cable Lateral Raises',
                'risk_mechanism' => 'Severe subacromial impingement during strict overhead barbell pressing.',
            ],
            [
                'exercise' => 'Parallel Bar Dips',
                'limitation' => 'SHOULDER_IMPINGEMENT',
                'alternative' => 'Floor Tricep Dips',
                'risk_mechanism' => 'Shoulder hyperextension past 90 degrees strains anterior capsule and rotator cuff.',
            ],
            [
                'exercise' => 'Wall Handstand / Pike Push-ups',
                'limitation' => 'SHOULDER_IMPINGEMENT',
                'alternative' => 'Seated Dumbbell Lateral Raises',
                'risk_mechanism' => 'Heavy compressive overhead loading with restricted scapular movement.',
            ],

            // ==================== 4. WRIST PAIN RULES ====================
            [
                'exercise' => 'Barbell Bench Press',
                'limitation' => 'WRIST_TENDINITIS',
                'alternative' => 'Chest Press Machine',
                'risk_mechanism' => 'Heavy backward wrist extension torque under rigid straight barbell.',
            ],
            [
                'exercise' => 'Standard Floor Push-ups',
                'limitation' => 'WRIST_TENDINITIS',
                'alternative' => 'Incline / Knee Push-ups',
                'risk_mechanism' => '90-degree wrist extension under bodyweight compresses carpal tunnel canal.',
            ],
            [
                'exercise' => 'Plyometric / Clapping Push-ups',
                'limitation' => 'WRIST_TENDINITIS',
                'alternative' => 'Incline / Knee Push-ups',
                'risk_mechanism' => 'High-velocity impact shock and violent hyperextension upon landing.',
            ],
            [
                'exercise' => 'Preacher Curls (Barbell/EZ Bar)',
                'limitation' => 'WRIST_TENDINITIS',
                'alternative' => 'Dumbbell Bicep Curls',
                'risk_mechanism' => 'Fixed supination under barbell load strains radial and ulnar collateral ligaments.',
            ],
            [
                'exercise' => 'Wall Handstand / Pike Push-ups',
                'limitation' => 'WRIST_TENDINITIS',
                'alternative' => 'Seated Dumbbell Lateral Raises',
                'risk_mechanism' => 'Entire bodyweight compressing hyperextended wrist joints against the floor.',
            ],

            // ==================== 5. CERVICAL / NECK STRAIN RULES ====================
            [
                'exercise' => 'Barbell Back Squats',
                'limitation' => 'CERVICAL_NECK_STRAIN',
                'alternative' => 'Goblet Squat (Dumbbell/Kettlebell)',
                'risk_mechanism' => 'Direct barbell bar compression on C7 vertebra and upper cervical trapezius.',
            ],
            [
                'exercise' => 'Strict Standing Overhead Barbell Press',
                'limitation' => 'CERVICAL_NECK_STRAIN',
                'alternative' => 'Seated Dumbbell Lateral Raises',
                'risk_mechanism' => 'Heavy levator scapulae and upper trapezius tension triggering cervical spasm.',
            ],

            // ==================== 6. ANKLE INSTABILITY RULES ====================
            [
                'exercise' => 'Pistol Squats',
                'limitation' => 'ANKLE_INSTABILITY',
                'alternative' => 'Chair / Box Squats',
                'risk_mechanism' => 'High unilateral balance demand and acute dorsiflexion torque causing ankle collapse.',
            ],
            [
                'exercise' => 'Walking / Reverse Lunges',
                'limitation' => 'ANKLE_INSTABILITY',
                'alternative' => 'Chair / Box Squats',
                'risk_mechanism' => 'Ankle wobble and lateral inversion sprain risk during dynamic deceleration.',
            ],
            [
                'exercise' => 'Standing Calf Raises (Smith Machine with Step)',
                'limitation' => 'ANKLE_INSTABILITY',
                'alternative' => 'Standing Calf Raises (Flat Ground)',
                'risk_mechanism' => 'Deep deficit stretch on edge of step risks acute Achilles sprain.',
            ],
        ];

        // Clean out existing and re-seed
        ExerciseContraindication::truncate();

        foreach ($mappings as $map) {
            $ex = Exercise::where('name', $map['exercise'])->first();
            $lim = PhysicalLimitation::where('code', $map['limitation'])->first();
            $alt = Exercise::where('name', $map['alternative'])->first();

            if ($ex && $lim) {
                ExerciseContraindication::updateOrCreate(
                    [
                        'exercise_id' => $ex->id,
                        'limitation_id' => $lim->id,
                    ],
                    [
                        'alternative_exercise_id' => $alt?->id,
                        'risk_mechanism' => $map['risk_mechanism'],
                    ]
                );
            }
        }
    }
}
