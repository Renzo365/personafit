<?php

namespace Database\Seeders;

use App\Models\Exercise;
use Illuminate\Database\Seeder;

class EnrichExerciseGuideDataSeeder extends Seeder
{
    public function run(): void
    {
        $data = [
            1 => [ // Standard Floor Push-ups
                'common_mistakes' => "• [Shoulders] Mistake: Flaring elbows perpendicular (90°) to torso | Fix: Tuck elbows at 45° to 60° forward in the natural scapular plane to prevent rotator cuff impingement.\n• [Lower Back] Mistake: Sagging hips and hyperextending lumbar spine | Fix: Squeeze glutes and brace abdominals to hold a rigid straight line from crown to heels.\n• [Neck] Mistake: Craning or dropping head forward toward floor | Fix: Keep chin tucked and cervical spine aligned with the thoracic vertebrae.",
                'injury_prevention_cues' => "• [High-Risk Joints] Supraspinatus tendon (Rotator Cuff) and Carpal Wrist joint.\n• [Safety Precautions] Keep shoulder blades depressed and retracted on descent; actively protract slightly at the top.\n• [Regressions & Stop Signs] Stop immediately if anterior shoulder pinching occurs. Regress to Incline Push-ups or Knee Push-ups.",
                'proper_form_asset' => 'exercises/pushup_proper.gif',
                'injury_prevention_asset' => 'exercises/pushup_incline_prevention.gif',
            ],
            2 => [ // Incline / Knee Push-ups
                'common_mistakes' => "• [Lower Back] Mistake: Sagging belly toward floor or breaking hip angle | Fix: Lock pelvis in neutral tilt and maintain core engagement.\n• [Shoulders] Mistake: Shrugging shoulders up toward ears | Fix: Pull shoulder blades down toward back pockets (scapular depression).\n• [Neck] Mistake: Bending neck downward instead of flexing elbows | Fix: Guide chest to touch the elevated surface while head remains neutral.",
                'injury_prevention_cues' => "• [High-Risk Joints] Anterior Shoulder Capsule and Wrist joint.\n• [Safety Precautions] Elevating hands shifts 35-50% of bodyweight load off upper extremities onto the feet.\n• [Regressions & Stop Signs] If wrist extension causes pain, perform on fists or neutral parallettes. Regress further to Wall Push-ups if needed.",
                'proper_form_asset' => 'exercises/pushup_incline_prevention.gif',
                'injury_prevention_asset' => 'exercises/pushup_knee_prevention.gif',
            ],
            3 => [ // Chair / Box Squats
                'common_mistakes' => "• [Knees] Mistake: Knees caving inward (valgus collapse) on rising | Fix: Actively press knees outward tracking over second and third toes.\n• [Lower Back] Mistake: Collapsing onto chair and losing abdominal tension | Fix: Lightly touch chair with glutes without unloading bodyweight or rocking backward.\n• [Feet] Mistake: Lifting heels off the ground during descent | Fix: Distribute bodyweight evenly across the tripod of the foot (heel, first MTP, fifth MTP).",
                'injury_prevention_cues' => "• [High-Risk Joints] Patellofemoral Joint and Meniscus.\n• [Safety Precautions] Box establishes a physical depth barrier at or above 90°, preventing excessive patellar tendon shear.\n• [Regressions & Stop Signs] If anterior knee pain occurs, increase box height by 2-3 inches or transition to Glute Bridges.",
                'proper_form_asset' => 'exercises/smith_chair_squat.gif',
            ],
            4 => [ // Doorframe / Towel Rows
                'common_mistakes' => "• [Shoulders] Mistake: Shrugging shoulders with upper trapezius | Fix: Depress scapulae and pull with lats by driving elbows back.\n• [Hips] Mistake: Jerking hips and using momentum to pull | Fix: Keep glutes locked in a rigid reverse plank throughout the entire repetition.\n• [Grip] Mistake: Slipping or uneven hand placement | Fix: Secure towel tightly around door handle or grip door molding with hook grip.",
                'injury_prevention_cues' => "• [High-Risk Joints] Cervical Spine and Forearm/Wrist flexors.\n• [Safety Precautions] Completely eliminates axial compressive spinal load while training horizontal retraction.\n• [Regressions & Stop Signs] To reduce load, move feet further away from the doorframe; move feet closer to increase difficulty.",
                'proper_form_asset' => 'exercises/bodyweight_squatting_row_with_towel.gif',
            ],
            5 => [ // Glute Bridges
                'common_mistakes' => "• [Lower Back] Mistake: Hyperextending lumbar spine at peak | Fix: Stop hip elevation when glutes are fully contracted and torso forms a straight line from knees to shoulders.\n• [Feet] Mistake: Pushing through toes instead of heels | Fix: Dig heels firmly into mat to maximize gluteus maximus recruitment.\n• [Knees] Mistake: Letting knees flare outward excessively or collapse | Fix: Keep knees hip-width apart parallel throughout extension.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar facet joints (L4-L5, L5-S1).\n• [Safety Precautions] Restores glute activation without any axial compressive load on spinal discs.\n• [Regressions & Stop Signs] If hamstring cramps occur, bring feet 1-2 inches closer to hips.",
                'proper_form_asset' => 'exercises/barbell_glute_bridge.gif',
            ],
            6 => [ // Squat
                'common_mistakes' => "• [Knees] Mistake: Valgus knee collapse on concentric ascent | Fix: Screw feet into the floor and drive knees outward along line of toes.\n• [Lower Back] Mistake: Posterior pelvic tilt (butt-wink) at bottom | Fix: Limit depth to active mobility range where lumbar spine remains neutral.\n• [Feet] Mistake: Weight shifting into toes with heels lifting | Fix: Anchor heels firmly into ground and sit back into hips.",
                'injury_prevention_cues' => "• [High-Risk Joints] Patellofemoral Joint, Anterior Cruciate Ligament (ACL), and Lumbar Spine.\n• [Safety Precautions] Maintain a rigid 360° intra-abdominal brace before descending.\n• [Regressions & Stop Signs] Stop immediately if sharp joint pain occurs. Regress to Box Squats or Wall Sit.",
                'proper_form_asset' => 'exercises/barbell_back_squat.gif',
            ],
            7 => [ // Standing Calf Raises (Flat Ground)
                'common_mistakes' => "• [Ankles] Mistake: Rolling weight onto outer edges of feet | Fix: Drive evenly through balls of big toes and second toes.\n• [Achilles] Mistake: Bouncing ballistically at bottom | Fix: Pause 1 full second at bottom stretch before driving upward smoothly.\n• [Knees] Mistake: Bending knees during movement | Fix: Keep knees extended (soft lockout) to isolate gastrocnemius.",
                'injury_prevention_cues' => "• [High-Risk Joints] Achilles Tendon and Plantar Fascia.\n• [Safety Precautions] Strict eccentric control (2-3 seconds) prevents ballistic micro-tears in Achilles fibers.\n• [Regressions & Stop Signs] If Achilles soreness appears, regress to Seated Calf Raises to offload gastrocnemius tension.",
                'proper_form_asset' => 'exercises/barbell_floor_calf_raise.gif',
            ],
            8 => [ // Prone Cobra / Superman Holds
                'common_mistakes' => "• [Neck] Mistake: Hyperextending cervical spine by looking at ceiling | Fix: Keep gaze on mat 6 inches forward with chin tucked.\n• [Lower Back] Mistake: Bending excessively through lumbar spine | Fix: Focus extension through thoracic spine by opening chest and pinching scapulae.\n• [Breathing] Mistake: Holding breath during static hold | Fix: Breathe continuously through diaphragm while sustaining hold.",
                'injury_prevention_cues' => "• [High-Risk Joints] Cervical facet joints and Lumbar posterior elements.\n• [Safety Precautions] Enhances scapular retractors and thoracic extension posture with zero gravitational shear.\n• [Regressions & Stop Signs] Lower lift height if lumbar pinching occurs; perform single-limb Bird Dog as regression.",
            ],
            9 => [ // Floor Tricep Dips
                'common_mistakes' => "• [Shoulders] Mistake: Anterior shoulder rounding and scapular elevation | Fix: Keep chest proud, collarbones wide, and pack shoulder blades down.\n• [Elbows] Mistake: Flaring elbows outward | Fix: Direct elbows straight backward on flexion.\n• [Range] Mistake: Dipping too deep beyond 90° elbow flexion | Fix: Stop when upper arms are parallel to floor to protect anterior shoulder capsule.",
                'injury_prevention_cues' => "• [High-Risk Joints] Anterior Glenohumeral Joint capsule and Acromioclavicular Joint.\n• [Safety Precautions] Limit depth to 90° elbow bend; do not sink into shoulder joints.\n• [Regressions & Stop Signs] If anterior shoulder pain occurs, regress to Cable Tricep Pushdowns or Floor Close-Grip Push-ups.",
                'proper_form_asset' => 'exercises/bench_dip_on_floor.gif',
            ],
            10 => [ // Chest Press Machine
                'common_mistakes' => "• [Shoulders] Mistake: Handles set too high, driving elbows into 90° flare | Fix: Adjust seat height so handles align with mid-to-lower sternum.\n• [Back] Mistake: Shoulders peeling forward off backrest at lockout | Fix: Keep scapulae pinned flat against back pad throughout entire press.\n• [Elbows] Mistake: Violent snapping elbow lockout | Fix: Stop 1 inch short of full extension to keep continuous tension on pectorals.",
                'injury_prevention_cues' => "• [High-Risk Joints] Anterior Shoulder Capsule and Sternoclavicular Joint.\n• [Safety Precautions] Machine track provides guided plane; adjust start position pin to prevent excessive horizontal abduction stretch.\n• [Regressions & Stop Signs] Regress load if shoulder anterior glide occurs.",
                'proper_form_asset' => 'exercises/lever_chest_press_proper.gif',
            ],
            11 => [ // Lat Pulldown Machine
                'common_mistakes' => "• [Cervical] Mistake: Pulling bar behind neck | Fix: Always pull bar down to upper chest/collarbone in front of head.\n• [Lower Back] Mistake: Excessive backward torso swinging to jerk weight | Fix: Keep torso stationary with slight 10-15° backward lean.\n• [Shoulders] Mistake: Shrugging up on return without lat control | Fix: Maintain control over 3-second negative, feeling lat stretch under tension.",
                'injury_prevention_cues' => "• [High-Risk Joints] Rotator cuff, Cervical spine, and Long head of bicep.\n• [Safety Precautions] Behind-the-neck pulldowns cause extreme cervical flexion and shoulder impingement; keep movement strictly anterior.\n• [Regressions & Stop Signs] If shoulder impingement exists, switch to neutral-grip (V-bar) attachment.",
                'proper_form_asset' => 'exercises/cable_lat_pulldown_full_range_of_motion.gif',
            ],
            12 => [ // Leg Press Machine
                'common_mistakes' => "• [Lower Back] Mistake: Pelvis lifting off pad (lumbar flexion) at bottom | Fix: Set sled stop limit so lower back never loses contact with backrest.\n• [Knees] Mistake: Knees collapsing inward during descent | Fix: Align feet shoulder-width, toes slightly outward, knees tracking over toes.\n• [Knees] Mistake: Snapping knees into hyperextension at top | Fix: Maintain soft bend in knees at top of press; never hyperextend.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar Intervertebral Discs (L5-S1) and Patellofemoral Joint.\n• [Safety Precautions] Pelvic tucking under heavy load causes extreme posterior disc herniation risk; keep sacrum pinned to seat.\n• [Regressions & Stop Signs] Reduce range of motion and weight; substitute with Goblet Squat if lower back tightens.",
                'proper_form_asset' => 'exercises/sled_45_degree_leg_press.gif',
            ],
            13 => [ // Goblet Squat (Dumbbell/Kettlebell)
                'common_mistakes' => "• [Upper Back] Mistake: Upper back rounding under dumbbell weight | Fix: Hold dumbbell tight against sternum with elbows tucked under like a goblet.\n• [Knees] Mistake: Knees tracking forward before hips hinge | Fix: Initiate descent by pushing hips back and spreading the floor with feet.\n• [Torso] Mistake: Folding forward at waist | Fix: Keep chest proud and eye line forward to maintain upright torso angle.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar Spine and Patellofemoral Joint.\n• [Safety Precautions] Anterior counterbalance naturally teaches upright torso, significantly reducing lumbar shear compared to barbell back squats.\n• [Regressions & Stop Signs] Regress to Box Squat if knee pain occurs at parallel depth.",
                'proper_form_asset' => 'exercises/goblet_squat.gif',
            ],
            14 => [ // Dumbbell Sumo Squat
                'common_mistakes' => "• [Knees] Mistake: Knees failing to track over flared toes | Fix: Set stance 1.5x shoulder width and actively open groin/hips.\n• [Lower Back] Mistake: Leaning too far forward | Fix: Drop hips straight down between thighs keeping torso nearly vertical.\n• [Feet] Mistake: Rolling inward onto arches | Fix: Press through outer edges of heels and balls of feet.",
                'injury_prevention_cues' => "• [High-Risk Joints] Adductor magnus tendons and Medial Collateral Ligament (MCL).\n• [Safety Precautions] Stance width must match hip mobility; avoid excessively wide stances that force knee valgus.\n• [Regressions & Stop Signs] Bring stance in slightly to shoulder-width if adductor groin strain is felt.",
                'proper_form_asset' => 'exercises/smith_sumo_squat.gif',
            ],
            15 => [ // Dumbbell Bicep Curls
                'common_mistakes' => "• [Shoulders] Mistake: Swinging elbows forward and shrugging shoulders | Fix: Pin elbows tight to ribcage; isolate movement entirely to elbow flexion.\n• [Lower Back] Mistake: Arching lumbar spine to heave dumbbells | Fix: Tighten glutes, brace abs, and choose a load controllable for full cadence.\n• [Wrists] Mistake: Cocking wrists into hyperextension | Fix: Keep wrists neutral or slightly curled forward throughout.",
                'injury_prevention_cues' => "• [High-Risk Joints] Bicep brachii distal tendon and Lumbar Spine.\n• [Safety Precautions] Strict 3-second negative prevents tendonitis and maximizes hypertrophy.\n• [Regressions & Stop Signs] Switch to Incline Curls with lighter dumbbells or Cable Curls for joint-friendly constant tension.",
                'proper_form_asset' => 'exercises/dumbbell_biceps_curl.gif',
            ],
            16 => [ // Cable Tricep Pushdowns
                'common_mistakes' => "• [Elbows] Mistake: Elbows drifting forward and backward during rep | Fix: Lock elbows firmly at sides like hinges; move only forearms.\n• [Torso] Mistake: Leaning heavy chest over bar | Fix: Maintain slight forward torso hinge from hips with rigid core.\n• [Wrists] Mistake: Snapping wrists downward at extension | Fix: Keep wrists firm and straight in line with forearm.",
                'injury_prevention_cues' => "• [High-Risk Joints] Triceps tendon insertion at olecranon process.\n• [Safety Precautions] Constant cable tension eliminates sticking point impact spikes on elbow joints.\n• [Regressions & Stop Signs] If elbow tendon ache occurs, switch from straight bar to Rope attachment with neutral wrists.",
                'proper_form_asset' => 'exercises/cable_triceps_pushdown_v_bar.gif',
            ],
            17 => [ // Machine Leg Extensions
                'common_mistakes' => "• [Knees] Mistake: Ballistic kicking with rapid rebound at bottom | Fix: Initiate with smooth quadriceps contraction and pause 1s at top.\n• [Setup] Mistake: Knee axis misaligned with machine pivot point | Fix: Align knee joint center precisely with machine rotational cam axis.\n• [Hips] Mistake: Glutes lifting off seat on heavy extension | Fix: Grip side handles firmly to hold pelvis anchored against seat pad.",
                'injury_prevention_cues' => "• [High-Risk Joints] Patellar tendon and Cruciate ligaments (ACL/PCL).\n• [Safety Precautions] High shear force near full extension; avoid hyper-heavy explosive loads.\n• [Regressions & Stop Signs] Strictly contraindicated for acute ACL rehabilitation or patellofemoral pain syndrome; substitute with Goblet Squats.",
                'proper_form_asset' => 'exercises/lever_leg_extension.gif',
            ],
            18 => [ // Seated Dumbbell Lateral Raises
                'common_mistakes' => "• [Shoulders] Mistake: Raising dumbbells directly out in coronal plane (0°) | Fix: Raise arms 20-30° forward in the scapular plane with thumbs slightly higher than pinkies.\n• [Trapezius] Mistake: Shrugging shoulders up to ears | Fix: Depress scapulae down; imagine pushing dumbbells out toward side walls rather than up.\n• [Torso] Mistake: Rocking torso backward for momentum | Fix: Seated posture eliminates hip swing; keep chest still against backrest.",
                'injury_prevention_cues' => "• [High-Risk Joints] Subacromial Space and Supraspinatus tendon.\n• [Safety Precautions] Avoid the internal rotation 'pouring water' myth which causes subacromial impingement; maintain slight external rotation.\n• [Regressions & Stop Signs] If shoulder clicks, switch to Cable Lateral Raises with arm starting across the body.",
                'proper_form_asset' => 'exercises/dumbbell_seated_lateral_raise.gif',
            ],
            19 => [ // Walking / Reverse Lunges
                'common_mistakes' => "• [Knees] Mistake: Front knee slamming forward past toes or caving inward | Fix: Step back into 90° angles for both front and back knees, tracking straight.\n• [Torso] Mistake: Tilting torso excessively sideways | Fix: Maintain proud chest with core braced and pelvis level.\n• [Back Knee] Mistake: Banging rear knee into hard floor | Fix: Decelerate smoothly until rear knee hovers 1 inch above mat.",
                'injury_prevention_cues' => "• [High-Risk Joints] Patellofemoral Joint and Meniscus.\n• [Safety Precautions] Reverse lunges generate significantly less anterior patellar shear than forward lunges.\n• [Regressions & Stop Signs] Perform Split Squats stationary with hand on wall support for balance.",
                'proper_form_asset' => 'exercises/dumbbell_single_leg_split_squat.gif',
            ],
            20 => [ // Chair / Bench Dips
                'common_mistakes' => "• [Shoulders] Mistake: Hips drifting far forward from bench edge | Fix: Keep back and glutes scraping close along the bench edge.\n• [Shoulders] Mistake: Sinking past 90° elbow flexion with shoulders rolled forward | Fix: Stop descent when upper arms reach parallel with floor.\n• [Scapula] Mistake: Allowing chest to cave inward | Fix: Retract shoulder blades and keep sternum lifted.",
                'injury_prevention_cues' => "• [High-Risk Joints] Anterior Shoulder Capsule and Rotator Cuff.\n• [Safety Precautions] Extreme shoulder extension angle under bodyweight load; regress feet closer to offload tension.\n• [Regressions & Stop Signs] Contraindicated for shoulder impingement or anterior instability; substitute with Floor Tricep Pushups.",
                'proper_form_asset' => 'exercises/bench_dip_knees_bent.gif',
            ],
            21 => [ // Inverted Rows (Under Sturdy Table)
                'common_mistakes' => "• [Lower Back] Mistake: Hips sagging toward floor | Fix: Engage glutes and hamstrings to hold a rigid straight line from heels to shoulders.\n• [Shoulders] Mistake: Shrugging traps and pulling table to mouth | Fix: Pull table edge to lower chest / sternum by driving elbows down and back.\n• [Head] Mistake: Craning neck backward or forward | Fix: Maintain neutral cervical spine looking up at table underside.",
                'injury_prevention_cues' => "• [High-Risk Joints] Cervical Spine and Scapulothoracic Joint.\n• [Safety Precautions] Ensure table is thoroughly weighted and stable; test grip before taking full bodyweight.\n• [Regressions & Stop Signs] Bend knees at 90° with feet flat on floor to decrease lever arm and reduce load.",
                'proper_form_asset' => 'exercises/bodyweight_squatting_row.gif',
            ],
            22 => [ // Single-leg Calf Raises
                'common_mistakes' => "• [Ankles] Mistake: Rolling ankle outward into supination | Fix: Drive force through first and second metatarsal heads.\n• [Cadence] Mistake: Bouncing at bottom using Achilles elastic rebound | Fix: Control 2-second stretch and pause 1s at top contraction.\n• [Knee] Mistake: Bending knee during extension | Fix: Maintain stable soft lockout at knee joint.",
                'injury_prevention_cues' => "• [High-Risk Joints] Achilles Tendon and Ankle ligaments.\n• [Safety Precautions] Doubles bodyweight load on a single Achilles tendon; warm up with bilateral calf raises first.\n• [Regressions & Stop Signs] If Achilles tenderness occurs, regress to Bilateral Flat Ground Calf Raises.",
                'proper_form_asset' => 'exercises/dumbbell_single_leg_calf_raise.gif',
            ],
            23 => [ // Side Plank Hip Abductions
                'common_mistakes' => "• [Hips] Mistake: Bottom hip sagging toward floor | Fix: Drive bottom knee and hip up into mat to form straight diagonal line.\n• [Top Leg] Mistake: Rotating top hip backward and lifting with hip flexors | Fix: Keep pelvis perpendicular to floor, leading with top heel.\n• [Shoulder] Mistake: Sinking into supporting shoulder joint | Fix: Press supporting elbow firmly into mat to engage serratus anterior.",
                'injury_prevention_cues' => "• [High-Risk Joints] Supporting Shoulder Joint and Lumbar lateral stabilizers (Quadratus Lumborum).\n• [Safety Precautions] High gluteus medius recruitment restores pelvic stability and reduces knee valgus risk.\n• [Regressions & Stop Signs] Perform Clamshells lying flat on side as regression.",
                'proper_form_asset' => 'exercises/side_plank_hip_adduction.gif',
            ],
            24 => [ // Towel Isometric Bicep Curls
                'common_mistakes' => "• [Lower Back] Mistake: Jerking backward against towel | Fix: Maintain anchored spine and pull strictly through bicep contraction.\n• [Wrists] Mistake: Bending wrists backward under intense pull | Fix: Keep wrists neutral.\n• [Breathing] Mistake: Valsalva maneuver (holding breath) during max effort | Fix: Exhale rhythmically during 6-second isometric contraction.",
                'injury_prevention_cues' => "• [High-Risk Joints] Bicep tendon and blood pressure spikes.\n• [Safety Precautions] Zero joint impact since movement is static; ensure continuous rhythmic breathing.\n• [Regressions & Stop Signs] Modulate effort to 70% if tendon discomfort appears.",
            ],
            25 => [ // Barbell Bench Press
                'common_mistakes' => "• [Shoulders] Mistake: Flaring elbows out at 90° with bar touching neck | Fix: Tuck elbows at 45-75° and touch bar to lower sternum in a curved bar path.\n• [Scapula] Mistake: Flat back with unlocked shoulder blades | Fix: Retract and depress scapulae, creating a stable upper back shelf into bench.\n• [Wrists] Mistake: Wrists bent backward under bar load | Fix: Position bar over heel of palm with straight, stacked wrists.",
                'injury_prevention_cues' => "• [High-Risk Joints] Rotator Cuff (supraspinatus), Pectoralis major tendon, and AC Joint.\n• [Safety Precautions] Never bench with false (thumbless) grip; set safety pins inside power rack.\n• [Regressions & Stop Signs] If shoulder impingement strikes, switch to Dumbbell Bench Press with neutral/semi-tucked grip.",
                'proper_form_asset' => 'exercises/barbell_bench_press.gif',
            ],
            26 => [ // Barbell Back Squats
                'common_mistakes' => "• [Lower Back] Mistake: Lumbar rounding (butt-wink) at depth or 'good morning' squatting | Fix: Maintain 360° intra-abdominal pressure, keeping chest proud and knees tracking out.\n• [Knees] Mistake: Inward knee collapse under heavy drive | Fix: Actively push knees outward against bar descent.\n• [Bar Path] Mistake: Bar drifting forward over toes | Fix: Keep bar moving in a strictly vertical line over mid-foot.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar Intervertebral Discs (L4-S1), Meniscus, and Patellar Tendon.\n• [Safety Precautions] Always squat inside safety spotter arms; master hip mobility before adding axial load.\n• [Regressions & Stop Signs] Regress to Goblet Squats or Leg Press if lumbar flexion occurs.",
                'proper_form_asset' => 'exercises/barbell_back_squat.gif',
            ],
            27 => [ // Seated Cable Rows
                'common_mistakes' => "• [Lower Back] Mistake: Excessive forward-and-backward swinging of torso | Fix: Lock torso stationary at 90-95° upright angle; move only arms and shoulder blades.\n• [Shoulders] Mistake: Shrugging shoulders up to ears | Fix: Depress shoulder blades down and drive elbows back toward hips.\n• [Spine] Mistake: Rounding lumbar spine during forward reach | Fix: Hinge from hips, keeping spine neutral on the stretch.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar spine erectors and Bicep tendon.\n• [Safety Precautions] Initiate pull with scapular retraction before bending elbows.\n• [Regressions & Stop Signs] If lower back fatigues, switch to Chest-Supported Machine Rows.",
                'proper_form_asset' => 'exercises/cable_seated_row.gif',
            ],
            28 => [ // Standing Overhead Shoulder Press
                'common_mistakes' => "• [Lower Back] Mistake: Excessive lumbar hyperextension (arching back) to push weight | Fix: Squeeze glutes and brace core tight to lock ribcage down.\n• [Bar Path] Mistake: Pushing bar outward around face | Fix: Pull chin back slightly so bar travels in straight vertical line, pressing through window at top.\n• [Wrists] Mistake: Cocked wrists transferring weight into wrist tendons | Fix: Stack bar directly over forearms with neutral wrist angle.",
                'injury_prevention_cues' => "• [High-Risk Joints] Subacromial Space, Rotator cuff, and Lumbar Spine.\n• [Safety Precautions] Requires full thoracic extension; avoid if active shoulder impingement exists.\n• [Regressions & Stop Signs] Regress to Seated Dumbbell Shoulder Press or Landmine Press.",
                'proper_form_asset' => 'exercises/barbell_standing_wide_military_press.gif',
            ],
            29 => [ // Romanian Deadlift (Barbell/Dumbbell)
                'common_mistakes' => "• [Lower Back] Mistake: Rounding spinal column to reach floor | Fix: Movement is a hip hinge, NOT bending at waist; stop when hips stop traveling backward.\n• [Bar Path] Mistake: Letting bar drift away from shins | Fix: Keep bar skimming thighs and shins throughout entire rep to minimize spinal lever arm.\n• [Knees] Mistake: Squatting knees forward | Fix: Maintain soft 15-20° bend in knees; push hips back into wall behind you.",
                'injury_prevention_cues' => "• [High-Risk Joints] L4-L5 and L5-S1 lumbar discs, Hamstring proximal tendons.\n• [Safety Precautions] Strict neutral spine is mandatory; keep lats packed tight (protects spine).\n• [Regressions & Stop Signs] Regress to Glute Bridges or Dumbbell RDL with lighter load.",
                'proper_form_asset' => 'exercises/barbell_romanian_deadlift.gif',
            ],
            30 => [ // Cable Lateral Raises
                'common_mistakes' => "• [Shoulders] Mistake: Raising above shoulder height with internal rotation | Fix: Stop raise at collarbone level with hands neutral or pinkies slightly down.\n• [Torso] Mistake: Leaning or swinging torso to lift cable | Fix: Anchor stance and let side deltoid perform pure isolated abduction.\n• [Elbows] Mistake: Bending and straightening elbows during rep | Fix: Maintain fixed soft elbow bend from start to finish.",
                'injury_prevention_cues' => "• [High-Risk Joints] Supraspinatus tendon and Subacromial bursa.\n• [Safety Precautions] Cable maintains consistent tension throughout bottom range, preventing rotator cuff shocks.\n• [Regressions & Stop Signs] Perform seated or regress load if impingement click is felt.",
                'proper_form_asset' => 'exercises/cable_lateral_raise.gif',
            ],
            31 => [ // Lying / Seated Leg Curls
                'common_mistakes' => "• [Lower Back] Mistake: Hips lifting off pad and hyperextending lower back | Fix: Press hips down into pad and brace abs.\n• [Feet] Mistake: Pointing toes down (plantarflexion) cramping calves | Fix: Keep ankles dorsiflexed (toes pulled toward shins) to focus on hamstrings.\n• [Cadence] Mistake: Dropping weight stack uncontrollably | Fix: Control 3-second negative eccentric lowering.",
                'injury_prevention_cues' => "• [High-Risk Joints] Hamstring muscle belly and Knee cruciate ligaments.\n• [Safety Precautions] Seated variations place hamstrings in optimal active stretch over hip joint.\n• [Regressions & Stop Signs] If knee joint aches, reduce range of motion and load.",
                'proper_form_asset' => 'exercises/lever_lying_leg_curl.gif',
            ],
            32 => [ // Pec Deck / Chest Fly Machine
                'common_mistakes' => "• [Shoulders] Mistake: Setting arms too far back, overextending anterior capsule | Fix: Set starting arms forward so elbows never travel behind torso plane.\n• [Elbows] Mistake: Straightening arms into rigid lockout | Fix: Keep consistent soft 15° bend in elbows.\n• [Shoulders] Mistake: Shrugging traps and slumping shoulders forward | Fix: Pin shoulder blades into backrest and lead with chest.",
                'injury_prevention_cues' => "• [High-Risk Joints] Anterior Shoulder Capsule, Labrum, and Bicep tendon.\n• [Safety Precautions] Extreme horizontal abduction under load is #1 cause of fly-related shoulder impingement; strictly limit rear range.\n• [Regressions & Stop Signs] Substitute with Floor Dumbbell Flyes or Machine Chest Press.",
                'proper_form_asset' => 'exercises/cable_standing_fly.gif',
            ],
            33 => [ // Incline Dumbbell Curls
                'common_mistakes' => "• [Shoulders] Mistake: Benching angle too low, overstretching anterior shoulder | Fix: Set incline bench at 60-70°, never completely flat.\n• [Elbows] Mistake: Swinging elbows forward during curl | Fix: Keep elbows pointed straight down at floor, isolating long head of bicep.\n• [Wrists] Mistake: Flaccid wrist hyperextension | Fix: Keep wrists neutral or supinated throughout.",
                'injury_prevention_cues' => "• [High-Risk Joints] Long head of bicep tendon and Anterior shoulder capsule.\n• [Safety Precautions] Deep long-head stretch requires strict lighter loading; do not perform ballistic heavy singles.\n• [Regressions & Stop Signs] Regress to Standing Dumbbell Curls if shoulder tendon aches.",
                'proper_form_asset' => 'exercises/dumbbell_incline_biceps_curl.gif',
            ],
            34 => [ // Overhead Cable Tricep Extensions
                'common_mistakes' => "• [Elbows] Mistake: Flaring elbows out wide away from ears | Fix: Keep elbows pointed forward and tucked in line with shoulders.\n• [Lower Back] Mistake: Arching lower back under cable pull | Fix: Stagger stance and brace core to resist spinal extension.\n• [Range] Mistake: Short-changing the bottom stretch | Fix: Allow hands to travel deep behind head with control.",
                'injury_prevention_cues' => "• [High-Risk Joints] Medial Epicondyle and Triceps distal tendon.\n• [Safety Precautions] Overhead position puts long head on stretch; warm up elbow joints thoroughly.\n• [Regressions & Stop Signs] If elbow soreness appears, switch to Cable Tricep Pushdowns (hands down).",
                'proper_form_asset' => 'exercises/cable_overhead_triceps_extension_rope_attachment.gif',
            ],
            35 => [ // Pistol Squats
                'common_mistakes' => "• [Knee] Mistake: Knee caving inward (valgus) under single-leg load | Fix: Track knee strictly over outer toes.\n• [Heel] Mistake: Lifting working heel off floor | Fix: Anchor entire foot, improving ankle dorsiflexion mobility.\n• [Lower Back] Mistake: Severe spinal flexion to balance | Fix: Keep chest lifted and extend non-working leg straight forward.",
                'injury_prevention_cues' => "• [High-Risk Joints] Patellar tendon, Meniscus, and Lower back.\n• [Safety Precautions] Advanced unilateral movement requiring elite ankle mobility and tendon capacity.\n• [Regressions & Stop Signs] Regress to Bulgarian Split Squats or Box Pistol Squats.",
                'proper_form_asset' => 'exercises/single_leg_squat_pistol_male.gif',
            ],
            36 => [ // Wall Handstand / Pike Push-ups
                'common_mistakes' => "• [Shoulders] Mistake: Flaring elbows outward into chicken-wing position | Fix: Angle elbows backward at 45° toward ribs.\n• [Head] Mistake: Dropping crown straight down between hands | Fix: Head must travel forward into tripod position with hands.\n• [Core] Mistake: Banana-backing (arching lumbar spine) | Fix: Squeeze glutes and tuck pelvis to maintain straight stacked bodyline.",
                'injury_prevention_cues' => "• [High-Risk Joints] Acromioclavicular Joint, Cervical Spine, and Wrists.\n• [Safety Precautions] Place head cushion on floor; never train to muscular failure where neck could collapse.\n• [Regressions & Stop Signs] Regress to Pike Push-ups with feet on floor or Incline Push-ups.",
                'proper_form_asset' => 'exercises/handstand_push_up.gif',
            ],
            37 => [ // Pull-ups / Chin-ups
                'common_mistakes' => "• [Shoulders] Mistake: Shrugging shoulders up at top or initiating with arm pull | Fix: Initiate with active scapular depression (pulling shoulder blades down) before arms bend.\n• [Momentum] Mistake: Kicking legs and kipping | Fix: Cross ankles, brace core, and perform strict deadstop reps.\n• [Neck] Mistake: Craning neck over bar to fake full range | Fix: Pull upper chest toward bar while cervical spine remains neutral.",
                'injury_prevention_cues' => "• [High-Risk Joints] Rotator Cuff, Long head of bicep, and Medial epicondyle.\n• [Safety Precautions] Control the dead hang stretch; do not disengage shoulder stabilizers at bottom.\n• [Regressions & Stop Signs] Regress to Lat Pulldowns or Resistance Band-Assisted Pull-ups.",
                'proper_form_asset' => 'exercises/pull_up.gif',
            ],
            38 => [ // Parallel Bar Dips
                'common_mistakes' => "• [Shoulders] Mistake: Sinking too deep past 90° with chest caved in | Fix: Stop descent when shoulders align with elbow height.\n• [Shoulders] Mistake: Shrugging shoulders into ears at bottom | Fix: Keep shoulder blades depressed and stable throughout.\n• [Torso] Mistake: Excessive swinging or kicking legs | Fix: Cross ankles, brace core, and lean forward slightly for chest focus.",
                'injury_prevention_cues' => "• [High-Risk Joints] Sternoclavicular Joint, Anterior Shoulder Capsule, and Triceps tendon.\n• [Safety Precautions] High sternal and anterior shoulder stretch; do not load until mastering bodyweight with strict form.\n• [Regressions & Stop Signs] Regress to Bench Dips or Push-ups if sternal pain or anterior impingement occurs.",
                'proper_form_asset' => 'exercises/chest_dip.gif',
            ],
            39 => [ // Plyometric / Clapping Push-ups
                'common_mistakes' => "• [Wrists] Mistake: Landing with stiff straight arms | Fix: Absorb landing immediately with soft elbows and controlled descent.\n• [Hips] Mistake: Sagging hips during explosive drive | Fix: Maintain rigid plank from head to heels throughout flight phase.\n• [Hands] Mistake: Rushing clap before achieving sufficient height | Fix: Drive explosively through floor first; only clap when clearance is clear.",
                'injury_prevention_cues' => "• [High-Risk Joints] Wrist carpal bones, Elbow joints, and Rotator cuff.\n• [Safety Precautions] High impact reactive forces; perform on shock-absorbing mat.\n• [Regressions & Stop Signs] Regress to standard Push-ups with explosive concentric drive without leaving floor.",
                'proper_form_asset' => 'exercises/clap_push_up.gif',
            ],
            40 => [ // Nordic Hamstring Curls
                'common_mistakes' => "• [Hips] Mistake: Breaking at the hips and sticking glutes backward | Fix: Maintain locked straight line from knees through hips to shoulders throughout descent.\n• [Cadence] Mistake: Collapsing immediately to floor | Fix: Fight gravity and decelerate the negative as far forward as possible.\n• [Lower Back] Mistake: Hyperextending lumbar spine | Fix: Posteriorly tilt pelvis and brace abs.",
                'injury_prevention_cues' => "• [High-Risk Joints] Hamstring distal/proximal myotendinous junctions and Knee joints.\n• [Safety Precautions] Extreme eccentric hamstring load; highest single preventative drill for ACL and hamstring tears.\n• [Regressions & Stop Signs] Use hands to push off floor on return or use resistance band support.",
                'proper_form_asset' => 'exercises/self_assisted_inverse_leg_curl_proper.gif',
            ],
            41 => [ // Bodyweight Skull Crushers
                'common_mistakes' => "• [Elbows] Mistake: Flaring elbows out wide | Fix: Keep elbows shoulder-width and pointing forward.\n• [Hips] Mistake: Sagging hips toward bar | Fix: Maintain solid plank tension throughout.\n• [Head] Mistake: Banging head into bar | Fix: Guide forehead smoothly under or toward bar edge under control.",
                'injury_prevention_cues' => "• [High-Risk Joints] Triceps tendon insertion and Elbow joint.\n• [Safety Precautions] Control cadence; adjust bar height to modulate resistance.\n• [Regressions & Stop Signs] Elevate bar higher to reduce load; switch to Cable Pushdowns if elbow pain occurs.",
                'proper_form_asset' => 'exercises/bodyweight_kneeling_triceps_extension.gif',
            ],
            42 => [ // Dragon Flags
                'common_mistakes' => "• [Lower Back] Mistake: Bending at hips instead of keeping body in rigid straight board | Fix: Only shoulders touch bench; entire body moves as one lever.\n• [Neck] Mistake: Putting compressive bodyweight on cervical spine | Fix: Weight rests strictly across upper back and shoulder blades, never neck.\n• [Cadence] Mistake: Dropping hips rapidly | Fix: Decelerate lowering phase over 3 seconds.",
                'injury_prevention_cues' => "• [High-Risk Joints] Cervical Spine and Lumbar intervertebral discs.\n• [Safety Precautions] Advanced calisthenic movement; requires master-level core stability.\n• [Regressions & Stop Signs] Regress to Hanging Leg Raises or Dead Bugs.",
                'proper_form_asset' => 'exercises/flag.gif',
            ],
            43 => [ // Hanging Leg Raises
                'common_mistakes' => "• [Momentum] Mistake: Swinging body back and forth using momentum | Fix: Stop completely between reps; initiate strictly from abdominals.\n• [Pelvis] Mistake: Lifting legs without posterior pelvic tilt | Fix: Roll pelvis upward toward ribs at top of rep to activate rectus abdominis.\n• [Shoulders] Mistake: Hanging completely passive with loose shoulders | Fix: Maintain active hang with scapulae depressed.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar spine, Shoulders, and Hip flexors.\n• [Safety Precautions] Active shoulder depression prevents impingement at top stretch.\n• [Regressions & Stop Signs] Perform Captain's Chair Knee Raises or Floor Dead Bugs.",
                'proper_form_asset' => 'exercises/hanging_leg_raise.gif',
            ],
            44 => [ // Conventional / Sumo Barbell Deadlifts
                'common_mistakes' => "• [Lower Back] Mistake: Rounding lumbar spine (spinal flexion) under load | Fix: Lock lats, set chest proud, and brace core 360° before pulling.\n• [Bar Path] Mistake: Bar drifting away from shins | Fix: Keep bar in continuous contact with legs throughout entire lift.\n• [Hips] Mistake: Hips shooting up before bar leaves floor | Fix: Push floor away with legs; shoulders and hips rise simultaneously.",
                'injury_prevention_cues' => "• [High-Risk Joints] L4-L5 / L5-S1 Lumbar Discs, Bicep Tendons, and Sacroiliac Joint.\n• [Safety Precautions] Never yank or jerk bar off floor; pull slack out of bar first. Keep arms straight (never curl with mixed grip).\n• [Regressions & Stop Signs] If lumbar rounding occurs, switch to Trap Bar Deadlift or Romanian Deadlift.",
                'proper_form_asset' => 'exercises/barbell_deadlift.gif',
            ],
            45 => [ // Strict Standing Overhead Barbell Press
                'common_mistakes' => "• [Lower Back] Mistake: Arching lower back excessively to turn lift into incline press | Fix: Lock glutes and brace abdominals firmly.\n• [Bar Path] Mistake: Pushing bar around face | Fix: Pull chin back so bar ascends vertically, pushing head through window at top.\n• [Elbows] Mistake: Elbows dropping behind bar | Fix: Keep elbows slightly in front of bar at start position.",
                'injury_prevention_cues' => "• [High-Risk Joints] Supraspinatus Tendon, Acromioclavicular Joint, and Lumbar Spine.\n• [Safety Precautions] Requires full active shoulder overhead flexion; do not perform if painful arc exists.\n• [Regressions & Stop Signs] Switch to Seated Dumbbell Press or High Incline Press.",
                'proper_form_asset' => 'exercises/barbell_standing_wide_military_press.gif',
            ],
            46 => [ // Weighted Pull-ups / Dips
                'common_mistakes' => "• [Shoulders] Mistake: Allowing extra weight to yank shoulders into passive hyperextension | Fix: Keep muscular tension throughout entire range.\n• [Momentum] Mistake: Kipping or violently jerking legs | Fix: Maintain dead-hang control with stable pelvic position.\n• [Belt Setup] Mistake: Plates swinging unpredictably | Fix: Secure weight belt tightly with chain between thighs.",
                'injury_prevention_cues' => "• [High-Risk Joints] Sternum, Rotator Cuff, and Bicep tendon.\n• [Safety Precautions] High joint stress; warm up with bodyweight sets before adding external load.\n• [Regressions & Stop Signs] Strip external weight; perform bodyweight Pull-ups or Dips.",
                'proper_form_asset' => 'exercises/weighted_pull_up.gif',
            ],
            47 => [ // Heavy Barbell Hip Thrusts
                'common_mistakes' => "• [Lower Back] Mistake: Hyperextending lumbar spine at top lock | Fix: Keep chin tucked and ribs down; movement pivots strictly at hips.\n• [Neck] Mistake: Tilting head backward over bench | Fix: Keep eyes looking forward toward knees throughout entire rep.\n• [Feet] Mistake: Feet placed too far forward or too close | Fix: Set feet so shins are perpendicular (90°) to floor at top lockout.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar Intervertebral Discs and Hip Capsule.\n• [Safety Precautions] Always use thick barbell pad to cushion anterior pelvic crest.\n• [Regressions & Stop Signs] Regress to Dumbbell Hip Thrusts or Bilateral Glute Bridges.",
                'proper_form_asset' => 'exercises/barbell_glute_bridge_two_legs_on_bench_male.gif',
            ],
            48 => [ // Heavy Bulgarian Split Squats
                'common_mistakes' => "• [Knees] Mistake: Front knee wobbling inward (valgus collapse) | Fix: Screw front foot into floor and track knee over second toe.\n• [Torso] Mistake: Severe forward torso slump or twisting hips | Fix: Keep hips squared forward with chest tall.\n• [Rear Leg] Mistake: Pushing predominantly with rear foot | Fix: Front leg does 85% of work; rear leg is strictly for balance.",
                'injury_prevention_cues' => "• [High-Risk Joints] Patellar Tendon, Hip Flexor insertion, and Lower Back.\n• [Safety Precautions] Tremendous unilateral leg power with significantly lower spinal load than back squats.\n• [Regressions & Stop Signs] Regress to Standard Split Squats (both feet on floor) or Goblet Squats.",
                'proper_form_asset' => 'exercises/dumbbell_single_leg_split_squat.gif',
            ],
            49 => [ // Preacher Curls (Barbell/EZ Bar)
                'common_mistakes' => "• [Elbows] Mistake: Hyperextending and bouncing at bottom lockout | Fix: Stop 1-2 inches before full lockout to protect bicep tendon.\n• [Shoulders] Mistake: Lifting elbows or shoulders off preacher pad | Fix: Keep triceps and chest firmly anchored against angled pad.\n• [Wrists] Mistake: Over-cocking wrists backward | Fix: Use EZ-bar with neutral angled grips to preserve wrists.",
                'injury_prevention_cues' => "• [High-Risk Joints] Distal Biceps Tendon and Medial Epicondyle.\n• [Safety Precautions] High tendon stress at bottom extension; never drop weight quickly into bottom.\n• [Regressions & Stop Signs] Regress to Standing EZ-bar Curls or Incline Dumbbell Curls.",
                'proper_form_asset' => 'exercises/barbell_preacher_curl.gif',
            ],
            50 => [ // Standing Calf Raises (Smith Machine with Step)
                'common_mistakes' => "• [Achilles] Mistake: Rapid bouncy cadence at bottom stretch | Fix: Pause 2 seconds at deep stretch and 1 second at peak contraction.\n• [Ankles] Mistake: Supinating onto outer foot edges | Fix: Drive evenly through balls of big toes.\n• [Knees] Mistake: Bending knees to push with quads | Fix: Keep knees in soft locked extension.",
                'injury_prevention_cues' => "• [High-Risk Joints] Achilles Tendon and Plantar Fascia.\n• [Safety Precautions] Step gives deep stretch below horizontal; avoid ballistic drop to prevent Achilles rupture.\n• [Regressions & Stop Signs] Regress to Flat Ground Calf Raises without step deficit.",
                'proper_form_asset' => 'exercises/smith_reverse_calf_raises_proper.gif',
            ],
            51 => [ // Incline Rear Delt Flyes
                'common_mistakes' => "• [Traps] Mistake: Squeezing shoulder blades together and using upper traps | Fix: Keep scapulae relatively still; drive pinkies out and wide.\n• [Torso] Mistake: Jerking torso up off bench | Fix: Keep chest glued flat against incline pad.\n• [Elbows] Mistake: Bending elbows excessively into a row | Fix: Maintain wide fixed arc with soft elbow bend.",
                'injury_prevention_cues' => "• [High-Risk Joints] Posterior Shoulder Capsule and Cervical Spine.\n• [Safety Precautions] Light weights are mandatory for rear deltoid isolation.\n• [Regressions & Stop Signs] Switch to Cable Face Pulls or Prone Cobra.",
                'proper_form_asset' => 'exercises/dumbbell_rear_delt_raise.gif',
            ],
            52 => [ // Cable Glute Kickbacks
                'common_mistakes' => "• [Lower Back] Mistake: Hyperextending lower back to swing leg high | Fix: Lock lumbar spine neutral; kickback movement comes strictly from hip extension.\n• [Torso] Mistake: Twisting hips open to side | Fix: Keep hips squared forward parallel to cable machine.\n• [Cadence] Mistake: Letting weight yank foot forward | Fix: Squeeze glute at peak for 1 second, control return over 2 seconds.",
                'injury_prevention_cues' => "• [High-Risk Joints] Lumbar Spine and Sacroiliac Joint.\n• [Safety Precautions] Eliminates spinal compression; isolate gluteus maximus without lumbar contribution.\n• [Regressions & Stop Signs] Regress to Bodyweight Glute Bridges or Quadruped Kickbacks.",
                'proper_form_asset' => 'exercises/cable_kickback.gif',
            ],
        ];

        foreach ($data as $id => $fields) {
            Exercise::where('id', $id)->update($fields);
        }

        $this->command->info('Enriched 52 exercises with structured common mistakes, injury prevention cues, and animation assets.');
    }
}
