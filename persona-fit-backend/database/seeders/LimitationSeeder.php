<?php

namespace Database\Seeders;

use App\Models\PhysicalLimitation;
use Illuminate\Database\Seeder;

class LimitationSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $limitations = [
            [
                'code' => 'LOWER_BACK_PAIN',
                'name' => 'Lower Back Pain / Lumbar Disc Vulnerability',
                'description' => 'Discomfort or history of herniated disc in lumbar spine. Requires avoidance of heavy axial spinal loading and unsupported spinal flexion.',
                'affected_body_part' => 'Lower Back',
            ],
            [
                'code' => 'KNEE_PAIN',
                'name' => 'Knee Pain / Meniscus / Patellofemoral',
                'description' => 'Chondromalacia, meniscus tear, or patellar tendinitis. Requires avoiding deep acute knee angles and high-impact jumping.',
                'affected_body_part' => 'Knees',
            ],
            [
                'code' => 'SHOULDER_IMPINGEMENT',
                'name' => 'Shoulder Impingement / Rotator Cuff',
                'description' => 'Subacromial impingement or supraspinatus strain. Requires avoiding extreme overhead pressing with internal rotation.',
                'affected_body_part' => 'Shoulders',
            ],
            [
                'code' => 'WRIST_TENDINITIS',
                'name' => 'Wrist Pain / Carpal Tunnel / Tendinitis',
                'description' => 'Wrist extension strain under heavy barbell loads. Requires neutral-grip or EZ-bar substitutions.',
                'affected_body_part' => 'Wrists',
            ],
            [
                'code' => 'CERVICAL_NECK_STRAIN',
                'name' => 'Neck / Cervical Spine Tension',
                'description' => 'Upper trap stiffness or forward head posture strain. Requires avoiding heavy shrugs and behind-the-neck movements.',
                'affected_body_part' => 'Neck',
            ],
            [
                'code' => 'ANKLE_INSTABILITY',
                'name' => 'Ankle Mobility Limitation / Instability',
                'description' => 'Limited dorsiflexion or chronic sprains. Requires elevated heel or stable machine variants.',
                'affected_body_part' => 'Ankles',
            ],
        ];

        foreach ($limitations as $lim) {
            PhysicalLimitation::updateOrCreate(['code' => $lim['code']], $lim);
        }
    }
}
