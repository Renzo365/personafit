-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 21, 2026 at 10:54 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `persona_fit_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `cache`
--

CREATE TABLE `cache` (
  `key` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  `expiration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cache_locks`
--

CREATE TABLE `cache_locks` (
  `key` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `expiration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `exercises`
--

CREATE TABLE `exercises` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `name` varchar(150) NOT NULL,
  `description` text DEFAULT NULL,
  `target_muscle` varchar(50) NOT NULL,
  `exercise_type` enum('compound','isolation') NOT NULL DEFAULT 'compound',
  `workout_environment` enum('home','gym','both') NOT NULL DEFAULT 'both',
  `secondary_muscles` varchar(100) DEFAULT NULL,
  `equipment_type` enum('bodyweight','dumbbell','barbell','machine','cable','bands') NOT NULL DEFAULT 'bodyweight',
  `required_equipment` varchar(150) DEFAULT NULL,
  `difficulty` enum('beginner','intermediate','advanced') NOT NULL DEFAULT 'beginner',
  `default_sets` int(11) NOT NULL DEFAULT 3,
  `default_reps` varchar(50) NOT NULL DEFAULT '8-12 reps',
  `safe_starting_load` varchar(100) DEFAULT NULL,
  `default_rest_seconds` int(11) NOT NULL DEFAULT 60,
  `instructions` text NOT NULL,
  `common_mistakes` text DEFAULT NULL,
  `injury_prevention_cues` text DEFAULT NULL,
  `breathing_tips` text DEFAULT NULL,
  `benefits` text DEFAULT NULL,
  `muscle_explanation` text DEFAULT NULL,
  `common_injuries` text DEFAULT NULL,
  `safer_alternatives_text` text DEFAULT NULL,
  `recommendation_rules` text DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `proper_form_asset` varchar(255) DEFAULT NULL,
  `common_mistakes_asset` varchar(255) DEFAULT NULL,
  `injury_prevention_asset` varchar(255) DEFAULT NULL,
  `anatomy_view` varchar(20) NOT NULL DEFAULT 'dual',
  `primary_muscle_regions` text DEFAULT NULL,
  `secondary_muscle_regions` text DEFAULT NULL,
  `muscle_mapping_confidence` varchar(20) NOT NULL DEFAULT 'high',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `exercises`
--

INSERT INTO `exercises` (`id`, `is_active`, `name`, `description`, `target_muscle`, `exercise_type`, `workout_environment`, `secondary_muscles`, `equipment_type`, `required_equipment`, `difficulty`, `default_sets`, `default_reps`, `safe_starting_load`, `default_rest_seconds`, `instructions`, `common_mistakes`, `injury_prevention_cues`, `breathing_tips`, `benefits`, `muscle_explanation`, `common_injuries`, `safer_alternatives_text`, `recommendation_rules`, `image_url`, `proper_form_asset`, `common_mistakes_asset`, `injury_prevention_asset`, `anatomy_view`, `primary_muscle_regions`, `secondary_muscle_regions`, `muscle_mapping_confidence`, `created_at`, `updated_at`) VALUES
(1, 1, 'Standard Floor Push-ups', 'The definitive horizontal bodyweight press building the pectorals, anterior deltoids, and triceps while maintaining rigid core stability.', 'Chest', 'compound', 'home', 'Front Delts, Triceps, Core', 'bodyweight', 'Bodyweight / Floor Mat', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Place hands slightly wider than shoulder-width, fingers pointing forward or slightly flared.\n2. Form a straight line from heels through glutes to head, engaging core and glutes.\n3. Lower chest over 2-3 seconds until hovering 1 inch above floor, elbows at 45 degrees.\n4. Press floor away firmly back to starting position without hyperextending elbows.', '• [Shoulders] Mistake: Flaring elbows perpendicular (90°) to torso | Fix: Tuck elbows at 45° to 60° forward in the natural scapular plane to prevent rotator cuff impingement.\n• [Lower Back] Mistake: Sagging hips and hyperextending lumbar spine | Fix: Squeeze glutes and brace abdominals to hold a rigid straight line from crown to heels.\n• [Neck] Mistake: Craning or dropping head forward toward floor | Fix: Keep chin tucked and cervical spine aligned with the thoracic vertebrae.', '• [High-Risk Joints] Supraspinatus tendon (Rotator Cuff) and Carpal Wrist joint.\n• [Safety Precautions] Keep shoulder blades depressed and retracted on descent; actively protract slightly at the top.\n• [Regressions & Stop Signs] Stop immediately if anterior shoulder pinching occurs. Regress to Incline Push-ups or Knee Push-ups.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This beginner-level compound movement primarily targets the Chest, while engaging Front Delts, Triceps, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Anterior shoulder impingement (rotator cuff compression) from flared elbows; wrist carpal hyperextension strain.', 'Regress to Incline / Knee Push-ups on an elevated surface to reduce compressive load on shoulders and wrists.', 'Suitable for BMI < 30. High-BMI or deconditioned trainees should start with Incline / Knee Push-ups. Primary movement for hypertrophy and calisthenic upper body foundation.', 'assets/pushup_proper.gif', 'exercises/pushup_proper.gif', NULL, 'exercises/pushup_incline_prevention.gif', 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(2, 1, 'Incline / Knee Push-ups', 'Gentle regression of the push-up utilizing an elevated surface (countertop, bench, chair) or knees to reduce mechanical load on joints.', 'Chest', 'compound', 'home', 'Front Delts, Triceps, Core', 'bodyweight', 'Elevated Surface (Bench, Table, Sturdy Chair) or Floor Mat', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Place hands on elevated surface or mat with knees on ground.\n2. Keep torso aligned from shoulders to knees/heels.\n3. Lower chest with control until touching or nearing the edge.\n4. Push back smoothly while maintaining abdominal tension.', '• [Lower Back] Mistake: Sagging belly toward floor or breaking hip angle | Fix: Lock pelvis in neutral tilt and maintain core engagement.\n• [Shoulders] Mistake: Shrugging shoulders up toward ears | Fix: Pull shoulder blades down toward back pockets (scapular depression).\n• [Neck] Mistake: Bending neck downward instead of flexing elbows | Fix: Guide chest to touch the elevated surface while head remains neutral.', '• [High-Risk Joints] Anterior Shoulder Capsule and Wrist joint.\n• [Safety Precautions] Elevating hands shifts 35-50% of bodyweight load off upper extremities onto the feet.\n• [Regressions & Stop Signs] If wrist extension causes pain, perform on fists or neutral parallettes. Regress further to Wall Push-ups if needed.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This beginner-level compound movement primarily targets the Chest, while engaging Front Delts, Triceps, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Minimal risk. Occasional mild wrist extension discomfort if surface angle is sharp.', 'Wall push-ups for ultra-low impact; Standard Floor Push-ups for progression.', 'Top choice for beginners, seniors, high BMI (>30), and individuals rehabilitating wrist or shoulder discomfort.', 'assets/pushup_incline_prevention.gif', 'exercises/pushup_incline_prevention.gif', NULL, 'exercises/pushup_knee_prevention.gif', 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(3, 1, 'Chair / Box Squats', 'Controlled lower-body squat utilizing a chair or plyometric box to enforce parallel depth and eliminate patellofemoral knee shear.', 'Quads', 'compound', 'home', 'Glutes, Hamstrings, Core', 'bodyweight', 'Sturdy Chair or Plyometric Box (16-18 inches)', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Stand 3-4 inches in front of a chair with feet shoulder-width apart.\n2. Hinge back at hips and bend knees, lowering chest proud until glutes lightly touch the seat.\n3. Pause for half a second without rocking or relaxing.\n4. Drive through mid-foot and heels to stand back up.', '• [Knees] Mistake: Knees caving inward (valgus collapse) on rising | Fix: Actively press knees outward tracking over second and third toes.\n• [Lower Back] Mistake: Collapsing onto chair and losing abdominal tension | Fix: Lightly touch chair with glutes without unloading bodyweight or rocking backward.\n• [Feet] Mistake: Lifting heels off the ground during descent | Fix: Distribute bodyweight evenly across the tripod of the foot (heel, first MTP, fifth MTP).', '• [High-Risk Joints] Patellofemoral Joint and Meniscus.\n• [Safety Precautions] Box establishes a physical depth barrier at or above 90°, preventing excessive patellar tendon shear.\n• [Regressions & Stop Signs] If anterior knee pain occurs, increase box height by 2-3 inches or transition to Glute Bridges.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Patellofemoral compression if knees slide forward past toes without hip hinge.', 'Glute Bridges (zero knee shear) or Higher Box Squat.', 'Mandatory foundational exercise for high BMI, knee vulnerability, and elderly fitness programs.', 'assets/barbell_bench_squat.gif', 'exercises/smith_chair_squat.gif', NULL, 'assets/barbell_bench_squat.gif', 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(4, 1, 'Doorframe / Towel Rows', 'Home horizontal pulling movement using a doorway frame or towel wrapped around a secure post to strengthen lats and rhomboids with zero axial spinal compression.', 'Back', 'compound', 'home', 'Biceps, Rear Delts, Rhomboids', 'bodyweight', 'Sturdy Doorframe or Towel wrapped around pillar/door handle', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Stand facing doorframe, grasp molding or looped towel firmly with both hands at chest height.\n2. Lean back until arms are straight, bracing body in a rigid reverse plank.\n3. Pull chest toward frame by driving elbows back and squeezing shoulder blades.\n4. Lower back with control over 3 seconds.', '• [Shoulders] Mistake: Shrugging shoulders with upper trapezius | Fix: Depress scapulae and pull with lats by driving elbows back.\n• [Hips] Mistake: Jerking hips and using momentum to pull | Fix: Keep glutes locked in a rigid reverse plank throughout the entire repetition.\n• [Grip] Mistake: Slipping or uneven hand placement | Fix: Secure towel tightly around door handle or grip door molding with hook grip.', '• [High-Risk Joints] Cervical Spine and Forearm/Wrist flexors.\n• [Safety Precautions] Completely eliminates axial compressive spinal load while training horizontal retraction.\n• [Regressions & Stop Signs] To reduce load, move feet further away from the doorframe; move feet closer to increase difficulty.', '• Inhale: Inhale steadily as arms reach full extension, feeling a gentle stretch in the lats.\n• Exhale: Exhale powerfully as you pull elbows down and back, actively depressing the shoulder blades.\n• Bracing: Keep ribs pulled down to avoid hyperextending the lower back.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This beginner-level compound movement primarily targets the Back, while engaging Biceps, Rear Delts, Rhomboids as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Forearm / grip strain if towel is slippery; neck tension from shrugging.', 'Prone Cobra / Superman Holds; Inverted Rows under table for progression.', 'Optimal for desk workers, rounded shoulder posture, and home training without gym equipment.', 'assets/bodyweight_squatting_row_with_towel.gif', 'exercises/bodyweight_squatting_row_with_towel.gif', NULL, 'assets/bodyweight_squatting_row.gif', 'dual', '[\"lats\",\"traps\"]', '[\"deltoids_rear\",\"biceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(5, 1, 'Glute Bridges', 'Supine hip extension movement targeting the gluteus maximus and hamstrings with zero spinal compression, restoring posterior chain activation.', 'Glutes', 'compound', 'home', 'Hamstrings, Core, Erector Spinae', 'bodyweight', 'Floor Mat', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Lie flat on back with knees bent at 90 degrees, feet flat on floor shoulder-width apart.\n2. Drive through heels to lift hips until thighs align with torso.\n3. Squeeze glutes forcefully at the top for 2 seconds.\n4. Lower hips with control until lightly touching mat.', '• [Lower Back] Mistake: Hyperextending lumbar spine at peak | Fix: Stop hip elevation when glutes are fully contracted and torso forms a straight line from knees to shoulders.\n• [Feet] Mistake: Pushing through toes instead of heels | Fix: Dig heels firmly into mat to maximize gluteus maximus recruitment.\n• [Knees] Mistake: Letting knees flare outward excessively or collapse | Fix: Keep knees hip-width apart parallel throughout extension.', '• [High-Risk Joints] Lumbar facet joints (L4-L5, L5-S1).\n• [Safety Precautions] Restores glute activation without any axial compressive load on spinal discs.\n• [Regressions & Stop Signs] If hamstring cramps occur, bring feet 1-2 inches closer to hips.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level compound movement primarily targets the Glutes, while engaging Hamstrings, Core, Erector Spinae as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar facet joint pinching if over-arching lower back at top.', 'Safe for all limitations. Single-leg Glute Bridge for progression.', 'Universal baseline recommendation across all BMI categories and lumbar injury profiles.', 'assets/barbell_glute_bridge_two_legs_on_bench_male.gif', 'exercises/barbell_glute_bridge.gif', NULL, NULL, 'posterior', '[\"glutes\"]', '[\"hamstrings\",\"lower_back\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(6, 1, 'Squat', 'Unweighted full-range bodyweight squat developing lower body strength, functional hip mobility, and quadriceps endurance.', 'Quads', 'compound', 'home', 'Glutes, Hamstrings, Calves, Core', 'bodyweight', 'Bodyweight', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Stand with feet slightly wider than shoulder-width, toes angled 15-30 degrees outward.\n2. Brace core, push hips back, and bend knees.\n3. Descend smoothly until thighs are parallel to ground while keeping chest tall.\n4. Drive through mid-foot to return to upright stance.', '• [Knees] Mistake: Valgus knee collapse on concentric ascent | Fix: Screw feet into the floor and drive knees outward along line of toes.\n• [Lower Back] Mistake: Posterior pelvic tilt (butt-wink) at bottom | Fix: Limit depth to active mobility range where lumbar spine remains neutral.\n• [Feet] Mistake: Weight shifting into toes with heels lifting | Fix: Anchor heels firmly into ground and sit back into hips.', '• [High-Risk Joints] Patellofemoral Joint, Anterior Cruciate Ligament (ACL), and Lumbar Spine.\n• [Safety Precautions] Maintain a rigid 360° intra-abdominal brace before descending.\n• [Regressions & Stop Signs] Stop immediately if sharp joint pain occurs. Regress to Box Squats or Wall Sit.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Calves, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Patellofemoral irritation if squatting with forward knee translation; lower back rounding.', 'Chair / Box Squats or Static Wall Sit.', 'Fundamental human movement pattern for general fitness and fat loss.', 'assets/bodyweight_drop_jump_squat.gif', 'exercises/barbell_back_squat.gif', NULL, 'assets/barbell_bench_squat.gif', 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(7, 1, 'Standing Calf Raises (Flat Ground)', 'Ankle plantarflexion movement isolating the gastrocnemius and soleus muscles to enhance lower leg strength and ankle stability.', 'Calves', 'isolation', 'home', 'Soleus, Tibialis Posterior', 'bodyweight', 'Flat Ground / Wall for Balance', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Stand barefoot or in flat shoes near a wall for balance.\n2. Push through balls of feet to elevate heels as high as possible.\n3. Squeeze calves at peak contraction for 1 full second.\n4. Lower heels back to floor with control over 2 seconds.', '• [Ankles] Mistake: Rolling weight onto outer edges of feet | Fix: Drive evenly through balls of big toes and second toes.\n• [Achilles] Mistake: Bouncing ballistically at bottom | Fix: Pause 1 full second at bottom stretch before driving upward smoothly.\n• [Knees] Mistake: Bending knees during movement | Fix: Keep knees extended (soft lockout) to isolate gastrocnemius.', '• [High-Risk Joints] Achilles Tendon and Plantar Fascia.\n• [Safety Precautions] Strict eccentric control (2-3 seconds) prevents ballistic micro-tears in Achilles fibers.\n• [Regressions & Stop Signs] If Achilles soreness appears, regress to Seated Calf Raises to offload gastrocnemius tension.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Ankle Stability: Bolsters gastrocnemius and soleus muscular endurance for sprinting and deceleration.\n• Achilles Tendon Health: Improves elastic energy storage and ankle plantarflexion power.\n• Balance & Agility: Fosters lower kinetic chain equilibrium.', 'This beginner-level isolation movement primarily targets the Calves, while engaging Soleus, Tibialis Posterior as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Achilles tendinopathy or calf muscle cramps if done ballistically.', 'Seated calf raises; Single-leg Calf Raises for progression.', 'Safe for all BMI categories and rehabilitation protocols.', 'assets/barbell_floor_calf_raise.gif', 'exercises/barbell_floor_calf_raise.gif', NULL, NULL, 'dual', '[\"calves_rear\",\"calves_front\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(8, 1, 'Prone Cobra / Superman Holds', 'Isometric thoracic extension hold on the floor activating the posterior chain, lower trapezius, and deep lumbar stabilizers.', 'Back', 'isolation', 'home', 'Glutes, Rear Delts, Erector Spinae', 'bodyweight', 'Floor Mat', 'beginner', 3, '30-45s hold', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Lie face down on floor with arms along sides, palms facing down.\n2. Simultaneously lift chest and thighs a few inches off mat.\n3. Rotate thumbs toward ceiling, squeezing shoulder blades together.\n4. Hold isometric contraction for 20-30 seconds while breathing steadily.', '• [Neck] Mistake: Hyperextending cervical spine by looking at ceiling | Fix: Keep gaze on mat 6 inches forward with chin tucked.\n• [Lower Back] Mistake: Bending excessively through lumbar spine | Fix: Focus extension through thoracic spine by opening chest and pinching scapulae.\n• [Breathing] Mistake: Holding breath during static hold | Fix: Breathe continuously through diaphragm while sustaining hold.', '• [High-Risk Joints] Cervical facet joints and Lumbar posterior elements.\n• [Safety Precautions] Enhances scapular retractors and thoracic extension posture with zero gravitational shear.\n• [Regressions & Stop Signs] Lower lift height if lumbar pinching occurs; perform single-limb Bird Dog as regression.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This beginner-level isolation movement primarily targets the Back, while engaging Glutes, Rear Delts, Erector Spinae as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Cervical neck strain if looking up; lower back pinch if lifting too high.', 'Bird Dog or Glute Bridges.', 'Ideal for correcting thoracic kyphosis and forward head posture from screen usage.', 'assets/superman_push_up.gif', 'assets/superman_push_up.gif', NULL, NULL, 'posterior', '[\"lats\",\"traps\"]', '[\"deltoids_rear\",\"lower_back\",\"glutes\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(9, 1, 'Floor Tricep Dips', 'Low-impact bodyweight elbow extension performed directly on the floor to isolate the triceps without excessive shoulder extension strain.', 'Arms', 'isolation', 'home', 'Triceps Brachii, Anterior Deltoids', 'bodyweight', 'Floor Mat', 'beginner', 3, '8-12 reps', 'Bodyweight (Incline / Assisted regression if needed)', 45, '1. Sit on floor with knees bent, feet flat, hands placed behind hips with fingers pointing toward feet.\n2. Lift hips 2-3 inches off floor.\n3. Bend elbows back to lower hips toward mat without touching.\n4. Extend elbows fully to press back up, flexing triceps.', '• [Shoulders] Mistake: Anterior shoulder rounding and scapular elevation | Fix: Keep chest proud, collarbones wide, and pack shoulder blades down.\n• [Elbows] Mistake: Flaring elbows outward | Fix: Direct elbows straight backward on flexion.\n• [Range] Mistake: Dipping too deep beyond 90° elbow flexion | Fix: Stop when upper arms are parallel to floor to protect anterior shoulder capsule.', '• [High-Risk Joints] Anterior Glenohumeral Joint capsule and Acromioclavicular Joint.\n• [Safety Precautions] Limit depth to 90° elbow bend; do not sink into shoulder joints.\n• [Regressions & Stop Signs] If anterior shoulder pain occurs, regress to Cable Tricep Pushdowns or Floor Close-Grip Push-ups.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This beginner-level isolation movement primarily targets the Arms, while engaging Triceps Brachii, Anterior Deltoids as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Anterior shoulder joint anterior glide if shoulders roll forward.', 'Incline / Knee Push-ups (narrow grip) or Cable Tricep Pushdowns.', 'Safe entry-level arm isolation for home routines.', 'assets/triceps_dips_floor.gif', 'exercises/bench_dip_on_floor.gif', NULL, 'assets/bench_dip_on_floor.gif', 'dual', '[\"biceps\",\"triceps\"]', '[\"deltoids_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(10, 1, 'Chest Press Machine', 'Supported selectorized chest press offering a fixed guided trajectory, loading pectorals and anterior delts with zero stabilizer burden.', 'Chest', 'compound', 'gym', 'Triceps, Front Delts', 'machine', 'Chest Press Machine', 'beginner', 3, '10-12 reps', 'Pin 2-3 (15 - 20 kg / 30 - 45 lbs)', 60, '1. Adjust seat height so horizontal handles align across mid-chest (nipple line).\n2. Sit tall with upper back and hips pressed against back pad, feet firmly planted.\n3. Press handles forward smoothly until arms are almost extended (avoid locking elbows).\n4. Return slowly over 3 seconds until feeling a deep stretch across pectorals.', '• [Shoulders] Mistake: Handles set too high, driving elbows into 90° flare | Fix: Adjust seat height so handles align with mid-to-lower sternum.\n• [Back] Mistake: Shoulders peeling forward off backrest at lockout | Fix: Keep scapulae pinned flat against back pad throughout entire press.\n• [Elbows] Mistake: Violent snapping elbow lockout | Fix: Stop 1 inch short of full extension to keep continuous tension on pectorals.', '• [High-Risk Joints] Anterior Shoulder Capsule and Sternoclavicular Joint.\n• [Safety Precautions] Machine track provides guided plane; adjust start position pin to prevent excessive horizontal abduction stretch.\n• [Regressions & Stop Signs] Regress load if shoulder anterior glide occurs.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This beginner-level compound movement primarily targets the Chest, while engaging Triceps, Front Delts as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Shoulder impingement if seat is too low and handles align above clavicle.', 'Neutral-grip Machine Chest Press or Incline Push-ups.', 'Premier pressing exercise for beginners and gym starters of all BMI levels.', 'assets/lever_chest_press_proper.gif', 'exercises/lever_chest_press_proper.gif', NULL, NULL, 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(11, 1, 'Lat Pulldown Machine', 'Vertical pulling movement utilizing selectorized cable resistance and adjustable thigh pads to safely develop back width and lat strength.', 'Back', 'compound', 'gym', 'Biceps, Rear Delts, Rhomboids', 'cable', 'Lat Pulldown Station with Wide/Neutral Grip Bar', 'beginner', 3, '12-15 reps', 'Pin 1-2 (5 - 10 kg / 10 - 20 lbs)', 45, '1. Adjust thigh pad snugly over quadriceps. Grasp bar with overhand grip just outside shoulder width.\n2. Sit tall with chest lifted and slight 10-degree backward torso lean.\n3. Pull bar down smoothly to upper collarbone, driving elbows toward hips.\n4. Control eccentric ascent until lats are fully stretched.', '• [Cervical] Mistake: Pulling bar behind neck | Fix: Always pull bar down to upper chest/collarbone in front of head.\n• [Lower Back] Mistake: Excessive backward torso swinging to jerk weight | Fix: Keep torso stationary with slight 10-15° backward lean.\n• [Shoulders] Mistake: Shrugging up on return without lat control | Fix: Maintain control over 3-second negative, feeling lat stretch under tension.', '• [High-Risk Joints] Rotator cuff, Cervical spine, and Long head of bicep.\n• [Safety Precautions] Behind-the-neck pulldowns cause extreme cervical flexion and shoulder impingement; keep movement strictly anterior.\n• [Regressions & Stop Signs] If shoulder impingement exists, switch to neutral-grip (V-bar) attachment.', '• Inhale: Inhale steadily as arms reach full extension, feeling a gentle stretch in the lats.\n• Exhale: Exhale powerfully as you pull elbows down and back, actively depressing the shoulder blades.\n• Bracing: Keep ribs pulled down to avoid hyperextending the lower back.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This beginner-level compound movement primarily targets the Back, while engaging Biceps, Rear Delts, Rhomboids as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Cervical spine strain if pulling behind head; biceps tendon irritation from excessive elbow flexion.', 'Seated Cable Rows or Doorframe / Towel Rows.', 'Optimal for vertical pulling without requiring the high strength threshold of a full pull-up.', 'assets/cable_lat_pulldown_full_range_of_motion.gif', 'exercises/cable_lat_pulldown_full_range_of_motion.gif', NULL, NULL, 'dual', '[\"lats\"]', '[\"biceps\",\"deltoids_rear\",\"traps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(12, 1, 'Leg Press Machine', '45-degree lower body sled compound press delivering intense quadriceps and glute stimulus with total lumbar spine support.', 'Quads', 'compound', 'gym', 'Glutes, Hamstrings', 'machine', '45-Degree Leg Press Machine', 'beginner', 3, '10-12 reps', 'Pin 2-3 (15 - 20 kg / 30 - 45 lbs)', 60, '1. Sit firmly back into seat pad with hips pushed all the way into corner.\n2. Place feet shoulder-width on footplate, toes angled slightly out.\n3. Release safety catches and lower sled until knees form a 90-degree angle.\n4. Drive through mid-foot and heels to push sled back up without hyperextending knees.', '• [Lower Back] Mistake: Pelvis lifting off pad (lumbar flexion) at bottom | Fix: Set sled stop limit so lower back never loses contact with backrest.\n• [Knees] Mistake: Knees collapsing inward during descent | Fix: Align feet shoulder-width, toes slightly outward, knees tracking over toes.\n• [Knees] Mistake: Snapping knees into hyperextension at top | Fix: Maintain soft bend in knees at top of press; never hyperextend.', '• [High-Risk Joints] Lumbar Intervertebral Discs (L5-S1) and Patellofemoral Joint.\n• [Safety Precautions] Pelvic tucking under heavy load causes extreme posterior disc herniation risk; keep sacrum pinned to seat.\n• [Regressions & Stop Signs] Reduce range of motion and weight; substitute with Goblet Squat if lower back tightens.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar disc compression if tailbone rounds; knee hyperextension hyperextension trauma.', 'Chair / Box Squats or Goblet Squat.', 'Safest high-load lower body exercise for individuals with lower-back pain or high BMI.', 'assets/sled_45_degree_leg_press.gif', 'exercises/sled_45_degree_leg_press.gif', NULL, NULL, 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(13, 1, 'Goblet Squat (Dumbbell/Kettlebell)', 'Front-loaded squat holding a single dumbbell or kettlebell against the chest, enforcing upright spinal posture and natural hip mechanics.', 'Quads', 'compound', 'gym', 'Glutes, Hamstrings, Core', 'dumbbell', 'Dumbbell or Kettlebell (8-24kg)', 'beginner', 3, '8-12 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 60, '1. Hold dumbbell vertically cupped in both hands against upper chest, elbows pointing down.\n2. Stand with feet shoulder-width apart, toes turned outward 15-30 degrees.\n3. Squat down between knees until elbows lightly graze inside of knees (around parallel).\n4. Push floor away through mid-foot to stand up tall.', '• [Upper Back] Mistake: Upper back rounding under dumbbell weight | Fix: Hold dumbbell tight against sternum with elbows tucked under like a goblet.\n• [Knees] Mistake: Knees tracking forward before hips hinge | Fix: Initiate descent by pushing hips back and spreading the floor with feet.\n• [Torso] Mistake: Folding forward at waist | Fix: Keep chest proud and eye line forward to maintain upright torso angle.', '• [High-Risk Joints] Lumbar Spine and Patellofemoral Joint.\n• [Safety Precautions] Anterior counterbalance naturally teaches upright torso, significantly reducing lumbar shear compared to barbell back squats.\n• [Regressions & Stop Signs] Regress to Box Squat if knee pain occurs at parallel depth.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Patellofemoral friction if knees track forward excessively without hip hinge.', 'Chair / Box Squats or Leg Press Machine.', 'Universal gold-standard squat teaching movement for gym trainees.', 'assets/goblet_squat.gif', 'exercises/goblet_squat.gif', NULL, NULL, 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(14, 1, 'Dumbbell Sumo Squat', 'Wide-stance squat holding a dumbbell between legs, emphasizing adductors, gluteus medius, and quadriceps with vertical spine alignment.', 'Quads', 'compound', 'gym', 'Adductors, Glutes, Hamstrings', 'dumbbell', 'Heavy Dumbbell', 'beginner', 3, '8-12 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 60, '1. Stand with feet roughly 1.5 to 2 times shoulder width apart, toes angled 45 degrees out.\n2. Hold one dumbbell with both hands hanging between thighs.\n3. Squat down by pushing knees out over toes while keeping torso upright.\n4. Drive through heels to return to top, contracting glutes.', '• [Knees] Mistake: Knees failing to track over flared toes | Fix: Set stance 1.5x shoulder width and actively open groin/hips.\n• [Lower Back] Mistake: Leaning too far forward | Fix: Drop hips straight down between thighs keeping torso nearly vertical.\n• [Feet] Mistake: Rolling inward onto arches | Fix: Press through outer edges of heels and balls of feet.', '• [High-Risk Joints] Adductor magnus tendons and Medial Collateral Ligament (MCL).\n• [Safety Precautions] Stance width must match hip mobility; avoid excessively wide stances that force knee valgus.\n• [Regressions & Stop Signs] Bring stance in slightly to shoulder-width if adductor groin strain is felt.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level compound movement primarily targets the Quads, while engaging Adductors, Glutes, Hamstrings as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Adductor / groin strain if stance is wider than hip anatomical capability.', 'Goblet Squat or Leg Press Machine.', 'Ideal for trainees wanting inner thigh (adductor) and glute focus with minimal spinal load.', 'assets/smith_sumo_squat.gif', 'exercises/smith_sumo_squat.gif', NULL, NULL, 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(15, 1, 'Dumbbell Bicep Curls', 'Unilateral or bilateral elbow flexion isolating the biceps brachii, brachialis, and forearm flexors with free range of motion.', 'Arms', 'isolation', 'gym', 'Biceps Brachii, Forearms', 'dumbbell', 'Dumbbells (Pair)', 'beginner', 3, '12-15 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 45, '1. Stand tall with dumbbells at sides, palms facing inward (neutral).\n2. Curl dumbbells upward while supinating wrists so palms face upward at top.\n3. Squeeze biceps for 1 second at top contraction.\n4. Lower weights with control over 3 seconds back to starting position.', '• [Shoulders] Mistake: Swinging elbows forward and shrugging shoulders | Fix: Pin elbows tight to ribcage; isolate movement entirely to elbow flexion.\n• [Lower Back] Mistake: Arching lumbar spine to heave dumbbells | Fix: Tighten glutes, brace abs, and choose a load controllable for full cadence.\n• [Wrists] Mistake: Cocking wrists into hyperextension | Fix: Keep wrists neutral or slightly curled forward throughout.', '• [High-Risk Joints] Bicep brachii distal tendon and Lumbar Spine.\n• [Safety Precautions] Strict 3-second negative prevents tendonitis and maximizes hypertrophy.\n• [Regressions & Stop Signs] Switch to Incline Curls with lighter dumbbells or Cable Curls for joint-friendly constant tension.', '• Inhale: Inhale during the controlled 2-3 second eccentric lengthening phase.\n• Exhale: Exhale smoothly as you contract and squeeze the prime mover at peak flexion or extension.\n• Bracing: Keep shoulder blades pinned and avoid torso momentum or swinging.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This beginner-level isolation movement primarily targets the Arms, while engaging Biceps Brachii, Forearms as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Distal biceps tendinitis or lower back hyperextension ache from torso swinging.', 'Preacher Curls or Seated Dumbbell Curls.', 'Fundamental arm hypertrophy exercise.', 'assets/dumbbell_biceps_curl.gif', 'exercises/dumbbell_biceps_curl.gif', NULL, NULL, 'anterior', '[\"biceps\"]', '[\"forearms_front\",\"deltoids_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(16, 1, 'Cable Tricep Pushdowns', 'Isolated elbow extension using a high cable pulley, maintaining constant accommodating resistance throughout the triceps contraction.', 'Arms', 'isolation', 'gym', 'Triceps Brachii (All Heads)', 'cable', 'High Cable Tower with Rope or Straight Bar', 'beginner', 3, '12-15 reps', 'Pin 1-2 (5 - 10 kg / 10 - 20 lbs)', 45, '1. Attach rope or straight bar to high pulley. Grasp handle with elbows pinned at ribs.\n2. Stand with slight forward hinge at hips, knees unlocked.\n3. Push cable down until elbows are fully extended, flaring rope ends apart at bottom.\n4. Control return until forearms reach parallel to floor (90 degrees).', '• [Elbows] Mistake: Elbows drifting forward and backward during rep | Fix: Lock elbows firmly at sides like hinges; move only forearms.\n• [Torso] Mistake: Leaning heavy chest over bar | Fix: Maintain slight forward torso hinge from hips with rigid core.\n• [Wrists] Mistake: Snapping wrists downward at extension | Fix: Keep wrists firm and straight in line with forearm.', '• [High-Risk Joints] Triceps tendon insertion at olecranon process.\n• [Safety Precautions] Constant cable tension eliminates sticking point impact spikes on elbow joints.\n• [Regressions & Stop Signs] If elbow tendon ache occurs, switch from straight bar to Rope attachment with neutral wrists.', '• Inhale: Inhale during the controlled 2-3 second eccentric lengthening phase.\n• Exhale: Exhale smoothly as you contract and squeeze the prime mover at peak flexion or extension.\n• Bracing: Keep shoulder blades pinned and avoid torso momentum or swinging.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This beginner-level isolation movement primarily targets the Arms, while engaging Triceps Brachii (All Heads) as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Olecranon bursitis or triceps tendinitis from snapping into hyperextension.', 'Floor Tricep Dips or Dumbbell Overhead Extension.', 'Top isolation movement for arm firming and tricep tendon health.', 'assets/cable_triceps_pushdown_v_bar.gif', 'exercises/cable_triceps_pushdown_v_bar.gif', NULL, NULL, 'dual', '[\"biceps\",\"triceps\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(17, 1, 'Machine Leg Extensions', 'Targeted quadriceps isolation movement utilizing a padded lever to train the terminal 30 degrees of knee extension.', 'Quads', 'isolation', 'gym', 'Rectus Femoris, Vastus Medialis', 'machine', 'Leg Extension Machine', 'beginner', 3, '10-12 reps', 'Pin 2-3 (15 - 20 kg / 30 - 45 lbs)', 60, '1. Adjust back pad so knee joint aligns directly with the machine pivot point.\n2. Position shin pad just above ankles.\n3. Extend legs upward smoothly until quads are fully contracted.\n4. Lower weight under strict control over 3 seconds without letting weight stack touch.', '• [Knees] Mistake: Ballistic kicking with rapid rebound at bottom | Fix: Initiate with smooth quadriceps contraction and pause 1s at top.\n• [Setup] Mistake: Knee axis misaligned with machine pivot point | Fix: Align knee joint center precisely with machine rotational cam axis.\n• [Hips] Mistake: Glutes lifting off seat on heavy extension | Fix: Grip side handles firmly to hold pelvis anchored against seat pad.', '• [High-Risk Joints] Patellar tendon and Cruciate ligaments (ACL/PCL).\n• [Safety Precautions] High shear force near full extension; avoid hyper-heavy explosive loads.\n• [Regressions & Stop Signs] Strictly contraindicated for acute ACL rehabilitation or patellofemoral pain syndrome; substitute with Goblet Squats.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This beginner-level isolation movement primarily targets the Quads, while engaging Rectus Femoris, Vastus Medialis as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Patellar tendinitis or chondromalacia pain if loaded too heavily at end range.', 'Leg Press Machine or Chair / Box Squats.', 'Use for quadriceps hypertrophy; avoid maximal heavy 1RMs to prevent patellar shear.', 'assets/lever_leg_extension.gif', 'exercises/lever_leg_extension.gif', NULL, NULL, 'anterior', '[\"quads\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(18, 1, 'Seated Dumbbell Lateral Raises', 'Strict deltoid abduction performed seated to eliminate lower-body cheating, isolating the lateral deltoid head for shoulder width.', 'Shoulders', 'isolation', 'gym', 'Lateral Deltoids, Trapezius', 'dumbbell', 'Light Dumbbells (2-10kg), Flat/Upright Bench', 'beginner', 3, '12-15 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 45, '1. Sit tall on end of bench, holding light dumbbells at sides.\n2. Raise dumbbells outward in the scapular plane (30 degrees forward of torso).\n3. Lift until elbows reach shoulder height, leading slightly with elbows.\n4. Lower slowly over 3 seconds to starting position.', '• [Shoulders] Mistake: Raising dumbbells directly out in coronal plane (0°) | Fix: Raise arms 20-30° forward in the scapular plane with thumbs slightly higher than pinkies.\n• [Trapezius] Mistake: Shrugging shoulders up to ears | Fix: Depress scapulae down; imagine pushing dumbbells out toward side walls rather than up.\n• [Torso] Mistake: Rocking torso backward for momentum | Fix: Seated posture eliminates hip swing; keep chest still against backrest.', '• [High-Risk Joints] Subacromial Space and Supraspinatus tendon.\n• [Safety Precautions] Avoid the internal rotation \'pouring water\' myth which causes subacromial impingement; maintain slight external rotation.\n• [Regressions & Stop Signs] If shoulder clicks, switch to Cable Lateral Raises with arm starting across the body.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Deltoid Cap Development: Targets lateral, anterior, and posterior deltoid heads for upper body definition.\n• Overhead Mobility: Enhances scapulohumeral rhythm and thoracic extension capacity.\n• Rotator Cuff Support: Conditions stabilizing musculature around the shoulder capsule.', 'This beginner-level isolation movement primarily targets the Shoulders, while engaging Lateral Deltoids, Trapezius as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Subacromial impingement of supraspinatus tendon if lifted behind shoulder plane.', 'Cable Lateral Raises or Face Pulls.', 'Safe for building shoulder aesthetics and posture without overhead strain.', 'assets/dumbbell_seated_lateral_raise.gif', 'exercises/dumbbell_seated_lateral_raise.gif', NULL, NULL, 'dual', '[\"deltoids_front\",\"deltoids_rear\"]', '[\"lats\",\"traps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11');
INSERT INTO `exercises` (`id`, `is_active`, `name`, `description`, `target_muscle`, `exercise_type`, `workout_environment`, `secondary_muscles`, `equipment_type`, `required_equipment`, `difficulty`, `default_sets`, `default_reps`, `safe_starting_load`, `default_rest_seconds`, `instructions`, `common_mistakes`, `injury_prevention_cues`, `breathing_tips`, `benefits`, `muscle_explanation`, `common_injuries`, `safer_alternatives_text`, `recommendation_rules`, `image_url`, `proper_form_asset`, `common_mistakes_asset`, `injury_prevention_asset`, `anatomy_view`, `primary_muscle_regions`, `secondary_muscle_regions`, `muscle_mapping_confidence`, `created_at`, `updated_at`) VALUES
(19, 1, 'Walking / Reverse Lunges', 'Dynamic unilateral lower-body compound movement training quads, glutes, hamstring deceleration, and pelvic balance.', 'Quads', 'compound', 'home', 'Glutes, Hamstrings, Calves, Core', 'bodyweight', 'Bodyweight or Dumbbells', 'intermediate', 3, '10-15 reps', 'Full Bodyweight', 60, '1. Stand tall with feet together.\n2. Step back (or forward) into a lunge, bending both knees to roughly 90 degrees.\n3. Lower back knee until hovering 1 inch above floor.\n4. Drive through front heel to step back to starting position. Alternate legs.', '• [Knees] Mistake: Front knee slamming forward past toes or caving inward | Fix: Step back into 90° angles for both front and back knees, tracking straight.\n• [Torso] Mistake: Tilting torso excessively sideways | Fix: Maintain proud chest with core braced and pelvis level.\n• [Back Knee] Mistake: Banging rear knee into hard floor | Fix: Decelerate smoothly until rear knee hovers 1 inch above mat.', '• [High-Risk Joints] Patellofemoral Joint and Meniscus.\n• [Safety Precautions] Reverse lunges generate significantly less anterior patellar shear than forward lunges.\n• [Regressions & Stop Signs] Perform Split Squats stationary with hand on wall support for balance.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This intermediate-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Calves, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Patellar tendinitis and knee ligament shear during abrupt deceleration.', 'Chair / Box Squats or Glute Bridges.', 'Core unilateral exercise for intermediate athletes; reverse variant preferred for knee-sensitive trainees.', 'assets/barbell_lunge.gif', 'exercises/dumbbell_single_leg_split_squat.gif', NULL, NULL, 'dual', '[\"quads\"]', '[\"abs\",\"glutes\",\"hamstrings\",\"calves_rear\",\"calves_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(20, 1, 'Chair / Bench Dips', 'Elevated bodyweight dip targeting the triceps, lower chest, and anterior deltoids using sturdy furniture or a bench.', 'Arms', 'compound', 'home', 'Triceps, Lower Chest, Front Delts', 'bodyweight', 'Sturdy Chair or Flat Bench', 'intermediate', 3, '10-15 reps', 'Full Bodyweight', 60, '1. Sit on edge of chair/bench, place hands next to hips with fingers forward.\n2. Slide hips off edge with legs extended forward (straight legs for harder, bent for easier).\n3. Lower torso until elbows bend to 90 degrees.\n4. Press through palms to lock out triceps back at top.', '• [Shoulders] Mistake: Hips drifting far forward from bench edge | Fix: Keep back and glutes scraping close along the bench edge.\n• [Shoulders] Mistake: Sinking past 90° elbow flexion with shoulders rolled forward | Fix: Stop descent when upper arms reach parallel with floor.\n• [Scapula] Mistake: Allowing chest to cave inward | Fix: Retract shoulder blades and keep sternum lifted.', '• [High-Risk Joints] Anterior Shoulder Capsule and Rotator Cuff.\n• [Safety Precautions] Extreme shoulder extension angle under bodyweight load; regress feet closer to offload tension.\n• [Regressions & Stop Signs] Contraindicated for shoulder impingement or anterior instability; substitute with Floor Tricep Pushups.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This intermediate-level compound movement primarily targets the Arms, while engaging Triceps, Lower Chest, Front Delts as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Anterior shoulder impingement and capsule stretching if descending too deeply.', 'Floor Tricep Dips or Standard Floor Push-ups.', 'Solid bodyweight triceps builder for intermediate home routines.', 'assets/bench_dip_knees_bent.gif', 'exercises/bench_dip_knees_bent.gif', NULL, 'assets/bench_dip_knees_bent.gif', 'dual', '[\"biceps\",\"triceps\"]', '[\"chest\",\"deltoids_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:11'),
(21, 1, 'Inverted Rows (Under Sturdy Table)', 'Horizontal bodyweight pull performed under a sturdy table, building latissimus dorsi, rhomboids, and biceps with bodyweight resistance.', 'Back', 'compound', 'home', 'Biceps, Rear Delts, Rhomboids, Core', 'bodyweight', 'Sturdy Dining Table or Bed Sheet Door Anchor', 'intermediate', 3, '10-15 reps', 'Full Bodyweight', 60, '1. Lie under a heavy sturdy table with chest directly beneath edge.\n2. Grasp edge with hands shoulder-width apart.\n3. Keep body in a rigid straight line from heels to head.\n4. Pull chest up to touch the underside of table, squeezing shoulder blades together.\n5. Lower down with control.', '• [Lower Back] Mistake: Hips sagging toward floor | Fix: Engage glutes and hamstrings to hold a rigid straight line from heels to shoulders.\n• [Shoulders] Mistake: Shrugging traps and pulling table to mouth | Fix: Pull table edge to lower chest / sternum by driving elbows down and back.\n• [Head] Mistake: Craning neck backward or forward | Fix: Maintain neutral cervical spine looking up at table underside.', '• [High-Risk Joints] Cervical Spine and Scapulothoracic Joint.\n• [Safety Precautions] Ensure table is thoroughly weighted and stable; test grip before taking full bodyweight.\n• [Regressions & Stop Signs] Bend knees at 90° with feet flat on floor to decrease lever arm and reduce load.', '• Inhale: Inhale steadily as arms reach full extension, feeling a gentle stretch in the lats.\n• Exhale: Exhale powerfully as you pull elbows down and back, actively depressing the shoulder blades.\n• Bracing: Keep ribs pulled down to avoid hyperextending the lower back.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This intermediate-level compound movement primarily targets the Back, while engaging Biceps, Rear Delts, Rhomboids, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Biceps strain or falling hazard if table is unsteady.', 'Doorframe / Towel Rows.', 'The premier calisthenic horizontal row progression before gym barbell/cable movements.', 'assets/bodyweight_squatting_row.gif', 'exercises/bodyweight_squatting_row.gif', NULL, NULL, 'dual', '[\"lats\",\"traps\"]', '[\"deltoids_rear\",\"biceps\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(22, 1, 'Single-leg Calf Raises', 'Unilateral calf isolation overloading gastrocnemius and soleus with entire bodyweight on one leg, improving ankle resilience.', 'Calves', 'isolation', 'home', 'Soleus, Ankle Stabilizers', 'bodyweight', 'Step Edge or Flat Ground, Wall for Balance', 'intermediate', 3, '10-15 reps', 'Full Bodyweight', 60, '1. Stand on ball of one foot near a wall for balance, hook other foot behind ankle.\n2. Lower heel slightly below foot level (if on step) for deep calf stretch.\n3. Push firmly through big toe to rise up onto tip of ball of foot.\n4. Hold top for 1 second, then lower over 3 seconds.', '• [Ankles] Mistake: Rolling ankle outward into supination | Fix: Drive force through first and second metatarsal heads.\n• [Cadence] Mistake: Bouncing at bottom using Achilles elastic rebound | Fix: Control 2-second stretch and pause 1s at top contraction.\n• [Knee] Mistake: Bending knee during extension | Fix: Maintain stable soft lockout at knee joint.', '• [High-Risk Joints] Achilles Tendon and Ankle ligaments.\n• [Safety Precautions] Doubles bodyweight load on a single Achilles tendon; warm up with bilateral calf raises first.\n• [Regressions & Stop Signs] If Achilles tenderness occurs, regress to Bilateral Flat Ground Calf Raises.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Ankle Stability: Bolsters gastrocnemius and soleus muscular endurance for sprinting and deceleration.\n• Achilles Tendon Health: Improves elastic energy storage and ankle plantarflexion power.\n• Balance & Agility: Fosters lower kinetic chain equilibrium.', 'This intermediate-level isolation movement primarily targets the Calves, while engaging Soleus, Ankle Stabilizers as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Achilles tendinitis from ballistic bouncing; ankle roll.', 'Standing Calf Raises (Flat Ground).', 'Excellent for runners, jumpers, and fixing unilateral calf imbalances.', 'assets/dumbbell_single_leg_calf_raise.gif', 'exercises/dumbbell_single_leg_calf_raise.gif', NULL, NULL, 'dual', '[\"calves_rear\",\"calves_front\"]', '[\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(23, 1, 'Side Plank Hip Abductions', 'Integrated core and lateral hip exercise strengthening the gluteus medius, obliques, and quadratus lumborum to prevent knee valgus.', 'Glutes', 'isolation', 'home', 'Glute Medius, Obliques, Core', 'bodyweight', 'Floor Mat', 'intermediate', 3, '30-45s hold', 'Full Bodyweight', 60, '1. Lie on side with forearm directly under shoulder, legs stacked straight.\n2. Lift hips off floor into a rigid side plank.\n3. Raise top leg smoothly toward ceiling without rotating hips forward or backward.\n4. Lower leg back to stack, maintaining side plank elevation.', '• [Hips] Mistake: Bottom hip sagging toward floor | Fix: Drive bottom knee and hip up into mat to form straight diagonal line.\n• [Top Leg] Mistake: Rotating top hip backward and lifting with hip flexors | Fix: Keep pelvis perpendicular to floor, leading with top heel.\n• [Shoulder] Mistake: Sinking into supporting shoulder joint | Fix: Press supporting elbow firmly into mat to engage serratus anterior.', '• [High-Risk Joints] Supporting Shoulder Joint and Lumbar lateral stabilizers (Quadratus Lumborum).\n• [Safety Precautions] High gluteus medius recruitment restores pelvic stability and reduces knee valgus risk.\n• [Regressions & Stop Signs] Perform Clamshells lying flat on side as regression.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This intermediate-level isolation movement primarily targets the Glutes, while engaging Glute Medius, Obliques, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Shoulder joint ache if elbow is misplaced away from center of mass.', 'Standard Side Plank or Glute Bridges.', 'High clinical value for correcting knee collapse and pelvic instability.', 'assets/side_plank_hip_adduction.gif', 'exercises/side_plank_hip_adduction.gif', NULL, NULL, 'dual', '[\"glutes\"]', '[\"abs\",\"obliques\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(24, 1, 'Towel Isometric Bicep Curls', 'Self-regulated maximal voluntary isometric contraction targeting the biceps brachii using an unyielding bath towel looped under foot.', 'Arms', 'isolation', 'home', 'Biceps Brachii, Brachialis, Grip', 'bodyweight', 'Sturdy Bath Towel', 'intermediate', 3, '10-15 reps', 'Full Bodyweight', 60, '1. Stand upright, loop a sturdy towel under one or both feet.\n2. Grasp towel ends firmly with elbows bent at 90 degrees.\n3. Pull upward with maximal voluntary bicep force against unyielding towel for 8-12 seconds.\n4. Rest 30 seconds and repeat.', '• [Lower Back] Mistake: Jerking backward against towel | Fix: Maintain anchored spine and pull strictly through bicep contraction.\n• [Wrists] Mistake: Bending wrists backward under intense pull | Fix: Keep wrists neutral.\n• [Breathing] Mistake: Valsalva maneuver (holding breath) during max effort | Fix: Exhale rhythmically during 6-second isometric contraction.', '• [High-Risk Joints] Bicep tendon and blood pressure spikes.\n• [Safety Precautions] Zero joint impact since movement is static; ensure continuous rhythmic breathing.\n• [Regressions & Stop Signs] Modulate effort to 70% if tendon discomfort appears.', '• Inhale: Inhale during the controlled 2-3 second eccentric lengthening phase.\n• Exhale: Exhale smoothly as you contract and squeeze the prime mover at peak flexion or extension.\n• Bracing: Keep shoulder blades pinned and avoid torso momentum or swinging.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This intermediate-level isolation movement primarily targets the Arms, while engaging Biceps Brachii, Brachialis, Grip as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Biceps tendon strain if pulling with sudden jerking spikes.', 'Dumbbell Bicep Curls or Doorframe / Towel Rows.', 'Proven muscle hypertrophy stimulus when dumbbells are unavailable.', 'assets/isometric_wipers.gif', 'assets/isometric_wipers.gif', NULL, NULL, 'anterior', '[\"biceps\"]', '[\"forearms_front\",\"deltoids_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(25, 1, 'Barbell Bench Press', 'Foundational upper-body horizontal compound press delivering progressive overload to pectoralis major, anterior deltoids, and triceps.', 'Chest', 'compound', 'gym', 'Triceps, Front Delts', 'barbell', 'Olympic Barbell, Flat Bench, Weight Plates, Safety Catches', 'intermediate', 3, '8-10 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Lie flat on bench with eyes under barbell. Plant feet firmly on floor.\n2. Grip bar slightly wider than shoulder width. Retract and depress shoulder blades.\n3. Unrack bar, lower with controlled cadence over 3 seconds to lower sternum.\n4. Press bar upward and slightly back toward eye line without flaring elbows.', '• [Shoulders] Mistake: Flaring elbows out at 90° with bar touching neck | Fix: Tuck elbows at 45-75° and touch bar to lower sternum in a curved bar path.\n• [Scapula] Mistake: Flat back with unlocked shoulder blades | Fix: Retract and depress scapulae, creating a stable upper back shelf into bench.\n• [Wrists] Mistake: Wrists bent backward under bar load | Fix: Position bar over heel of palm with straight, stacked wrists.', '• [High-Risk Joints] Rotator Cuff (supraspinatus), Pectoralis major tendon, and AC Joint.\n• [Safety Precautions] Never bench with false (thumbless) grip; set safety pins inside power rack.\n• [Regressions & Stop Signs] If shoulder impingement strikes, switch to Dumbbell Bench Press with neutral/semi-tucked grip.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This intermediate-level compound movement primarily targets the Chest, while engaging Triceps, Front Delts as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Rotator cuff impingement, pectoralis tendon tear, wrist strain under heavy extension.', 'Chest Press Machine or Incline Dumbbell Press (Neutral Grip).', 'Primary strength benchmark. Avoid or substitute if user has shoulder impingement or wrist tendinitis.', 'assets/barbell_bench_press.gif', 'exercises/barbell_bench_press.gif', NULL, 'assets/barbell_incline_bench_press.gif', 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(26, 1, 'Barbell Back Squats', 'The definitive lower-body compound movement loading quadriceps, glutes, and core under axial barbell placement.', 'Quads', 'compound', 'gym', 'Glutes, Hamstrings, Erector Spinae, Core', 'barbell', 'Olympic Barbell, Squat Rack, Weight Plates', 'intermediate', 3, '8-10 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Step under bar resting across upper traps (high bar) or rear delts (low bar).\n2. Unrack with 2 steps back. Set feet shoulder-width, toes turned 15-30 degrees.\n3. Inhale into belly, brace core, push hips back and bend knees.\n4. Squat to parallel depth, then drive through mid-foot to stand.', '• [Lower Back] Mistake: Lumbar rounding (butt-wink) at depth or \'good morning\' squatting | Fix: Maintain 360° intra-abdominal pressure, keeping chest proud and knees tracking out.\n• [Knees] Mistake: Inward knee collapse under heavy drive | Fix: Actively push knees outward against bar descent.\n• [Bar Path] Mistake: Bar drifting forward over toes | Fix: Keep bar moving in a strictly vertical line over mid-foot.', '• [High-Risk Joints] Lumbar Intervertebral Discs (L4-S1), Meniscus, and Patellar Tendon.\n• [Safety Precautions] Always squat inside safety spotter arms; master hip mobility before adding axial load.\n• [Regressions & Stop Signs] Regress to Goblet Squats or Leg Press if lumbar flexion occurs.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This intermediate-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Erector Spinae, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar disc herniation under axial loading; patellofemoral compressive cartilage breakdown.', 'Leg Press Machine or Goblet Squat (Dumbbell/Kettlebell).', 'Best for intermediate gym lifters without active lumbar or acute knee pathology.', 'assets/barbell_back_squat.gif', 'exercises/barbell_back_squat.gif', NULL, 'assets/barbell_bench_squat.gif', 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(27, 1, 'Seated Cable Rows', 'Horizontal pulling movement with low pulley, targeting mid-back thickness, rhomboids, latissimus dorsi, and posterior deltoids.', 'Back', 'compound', 'gym', 'Lats, Rhomboids, Biceps, Traps', 'cable', 'Low Cable Row Station with V-Grip or Wide Grip', 'intermediate', 3, '12-15 reps', 'Pin 1-2 (5 - 10 kg / 10 - 20 lbs)', 45, '1. Sit on bench with feet on footplates, knees slightly bent (not locked).\n2. Grasp handle with arms extended and torso upright with neutral spine.\n3. Pull handle toward lower abdomen while driving elbows back.\n4. Squeeze shoulder blades together for 1 second, then extend arms with control.', '• [Lower Back] Mistake: Excessive forward-and-backward swinging of torso | Fix: Lock torso stationary at 90-95° upright angle; move only arms and shoulder blades.\n• [Shoulders] Mistake: Shrugging shoulders up to ears | Fix: Depress shoulder blades down and drive elbows back toward hips.\n• [Spine] Mistake: Rounding lumbar spine during forward reach | Fix: Hinge from hips, keeping spine neutral on the stretch.', '• [High-Risk Joints] Lumbar spine erectors and Bicep tendon.\n• [Safety Precautions] Initiate pull with scapular retraction before bending elbows.\n• [Regressions & Stop Signs] If lower back fatigues, switch to Chest-Supported Machine Rows.', '• Inhale: Inhale steadily as arms reach full extension, feeling a gentle stretch in the lats.\n• Exhale: Exhale powerfully as you pull elbows down and back, actively depressing the shoulder blades.\n• Bracing: Keep ribs pulled down to avoid hyperextending the lower back.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This intermediate-level compound movement primarily targets the Back, while engaging Lats, Rhomboids, Biceps, Traps as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar erector strain from swinging torso under heavy weight stacks.', 'Chest-Supported Dumbbell Row or Doorframe / Towel Rows.', 'Outstanding back builder with lower spinal shear than bent-over barbell rows.', 'assets/cable_seated_row.gif', 'exercises/cable_seated_row.gif', NULL, NULL, 'dual', '[\"lats\",\"traps\"]', '[\"biceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(28, 1, 'Standing Overhead Shoulder Press', 'Vertical barbell/dumbbell press challenging deltoids, triceps, and abdominal core stabilization through an overhead path.', 'Shoulders', 'compound', 'gym', 'Triceps, Upper Chest, Core', 'barbell', 'Barbell or Dumbbells, Power Rack', 'intermediate', 3, '8-10 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Unrack bar at collarbone level with hands just outside shoulders.\n2. Squeeze glutes and brace core tightly to lock lumbar spine.\n3. Press bar vertically overhead, moving head slightly back to clear chin.\n4. Lock out overhead with head returning forward between arms.', '• [Lower Back] Mistake: Excessive lumbar hyperextension (arching back) to push weight | Fix: Squeeze glutes and brace core tight to lock ribcage down.\n• [Bar Path] Mistake: Pushing bar outward around face | Fix: Pull chin back slightly so bar travels in straight vertical line, pressing through window at top.\n• [Wrists] Mistake: Cocked wrists transferring weight into wrist tendons | Fix: Stack bar directly over forearms with neutral wrist angle.', '• [High-Risk Joints] Subacromial Space, Rotator cuff, and Lumbar Spine.\n• [Safety Precautions] Requires full thoracic extension; avoid if active shoulder impingement exists.\n• [Regressions & Stop Signs] Regress to Seated Dumbbell Shoulder Press or Landmine Press.', '• Inhale: Take a diaphragmatic breath at shoulder level, squeezing glutes and engaging abs.\n• Exhale: Exhale through pursed lips as the bar clears your face and locks out overhead.\n• Bracing: Prevent lumbar hyperextension by keeping pelvis neutral throughout the press.', '• Deltoid Cap Development: Targets lateral, anterior, and posterior deltoid heads for upper body definition.\n• Overhead Mobility: Enhances scapulohumeral rhythm and thoracic extension capacity.\n• Rotator Cuff Support: Conditions stabilizing musculature around the shoulder capsule.', 'This intermediate-level compound movement primarily targets the Shoulders, while engaging Triceps, Upper Chest, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Subacromial shoulder impingement; lumbar facet joint compression.', 'Seated Dumbbell Lateral Raises or Cable Lateral Raises.', 'Demands good thoracic spine mobility. Caution with active shoulder impingement.', 'assets/dumbbell_standing_overhead_press.gif', 'exercises/barbell_standing_wide_military_press.gif', NULL, NULL, 'dual', '[\"deltoids_front\",\"deltoids_rear\"]', '[\"chest\",\"triceps\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(29, 1, 'Romanian Deadlift (Barbell/Dumbbell)', 'Pure hip-hinge compound movement targeting hamstrings, gluteus maximus, and erectors through high eccentric tension.', 'Hamstrings', 'compound', 'gym', 'Glutes, Lower Back, Forearms', 'barbell', 'Barbell or Dumbbells', 'intermediate', 3, '8-10 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Hold barbell at hip level with overhand grip, feet hip-width apart.\n2. Soften knees slightly and push hips back horizontally.\n3. Slide bar down thighs while keeping back completely flat and lats engaged.\n4. Stop just below knees when hamstrings are fully stretched, then drive hips forward to lockout.', '• [Lower Back] Mistake: Rounding spinal column to reach floor | Fix: Movement is a hip hinge, NOT bending at waist; stop when hips stop traveling backward.\n• [Bar Path] Mistake: Letting bar drift away from shins | Fix: Keep bar skimming thighs and shins throughout entire rep to minimize spinal lever arm.\n• [Knees] Mistake: Squatting knees forward | Fix: Maintain soft 15-20° bend in knees; push hips back into wall behind you.', '• [High-Risk Joints] L4-L5 and L5-S1 lumbar discs, Hamstring proximal tendons.\n• [Safety Precautions] Strict neutral spine is mandatory; keep lats packed tight (protects spine).\n• [Regressions & Stop Signs] Regress to Glute Bridges or Dumbbell RDL with lighter load.', '• Inhale: Inhale deeply through the belly at the bar setup, packing the lats and locking lumbar neutral.\n• Hold: Hold the breath during the initial floor pull or hinge extension.\n• Exhale: Exhale smoothly at hip lockout; reset intra-abdominal brace before the next repetition.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This intermediate-level compound movement primarily targets the Hamstrings, while engaging Glutes, Lower Back, Forearms as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Acute lumbar erector sprain or disc bulge if spine flexes under load.', 'Lying / Seated Leg Curls or Glute Bridges.', 'Superior hamstring builder. Protects knees by strengthening the posterior chain.', 'assets/barbell_romanian_deadlift.gif', 'exercises/barbell_romanian_deadlift.gif', NULL, NULL, 'dual', '[\"hamstrings\"]', '[\"forearms_front\",\"lower_back\",\"glutes\",\"biceps\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(30, 1, 'Cable Lateral Raises', 'Unilateral shoulder abduction delivering constant smooth resistance across the entire range of motion for lateral deltoid capping.', 'Shoulders', 'isolation', 'gym', 'Lateral Deltoids, Trapezius', 'cable', 'Cable Tower with D-Handle or Cuff', 'intermediate', 3, '12-15 reps', 'Pin 1-2 (5 - 10 kg / 10 - 20 lbs)', 45, '1. Set cable pulley to knee/hip height. Grasp handle with outside hand.\n2. Stand with slight forward lean, arm crossing body.\n3. Raise arm out and up in scapular plane until elbow is level with shoulder.\n4. Lower with strict 3-second control back across body.', '• [Shoulders] Mistake: Raising above shoulder height with internal rotation | Fix: Stop raise at collarbone level with hands neutral or pinkies slightly down.\n• [Torso] Mistake: Leaning or swinging torso to lift cable | Fix: Anchor stance and let side deltoid perform pure isolated abduction.\n• [Elbows] Mistake: Bending and straightening elbows during rep | Fix: Maintain fixed soft elbow bend from start to finish.', '• [High-Risk Joints] Supraspinatus tendon and Subacromial bursa.\n• [Safety Precautions] Cable maintains consistent tension throughout bottom range, preventing rotator cuff shocks.\n• [Regressions & Stop Signs] Perform seated or regress load if impingement click is felt.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Deltoid Cap Development: Targets lateral, anterior, and posterior deltoid heads for upper body definition.\n• Overhead Mobility: Enhances scapulohumeral rhythm and thoracic extension capacity.\n• Rotator Cuff Support: Conditions stabilizing musculature around the shoulder capsule.', 'This intermediate-level isolation movement primarily targets the Shoulders, while engaging Lateral Deltoids, Trapezius as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Rotator cuff irritation if pulled behind shoulder plane.', 'Seated Dumbbell Lateral Raises.', 'Optimal isolation for shoulder width and definition.', 'assets/cable_lateral_raise.gif', 'exercises/cable_lateral_raise.gif', NULL, 'assets/dumbbell_one_arm_lateral_raise_with_support.gif', 'dual', '[\"deltoids_front\",\"deltoids_rear\"]', '[\"lats\",\"traps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(31, 1, 'Lying / Seated Leg Curls', 'Machine-guided knee flexion isolating the biceps femoris, semitendinosus, and semimembranosus with zero spinal compressive load.', 'Hamstrings', 'isolation', 'gym', 'Gastrocnemius', 'machine', 'Lying or Seated Leg Curl Machine', 'intermediate', 3, '10-12 reps', 'Pin 2-3 (15 - 20 kg / 30 - 45 lbs)', 60, '1. Align knee joint with machine pivot axis. Position pad just below calf bellies.\n2. Keep torso stabilized against back/chest pad.\n3. Curl pad toward glutes through full knee flexion, pausing for 1 second.\n4. Return under control over 3 seconds without letting weight stack touch.', '• [Lower Back] Mistake: Hips lifting off pad and hyperextending lower back | Fix: Press hips down into pad and brace abs.\n• [Feet] Mistake: Pointing toes down (plantarflexion) cramping calves | Fix: Keep ankles dorsiflexed (toes pulled toward shins) to focus on hamstrings.\n• [Cadence] Mistake: Dropping weight stack uncontrollably | Fix: Control 3-second negative eccentric lowering.', '• [High-Risk Joints] Hamstring muscle belly and Knee cruciate ligaments.\n• [Safety Precautions] Seated variations place hamstrings in optimal active stretch over hip joint.\n• [Regressions & Stop Signs] If knee joint aches, reduce range of motion and load.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This intermediate-level isolation movement primarily targets the Hamstrings, while engaging Gastrocnemius as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Hamstring tendon strain if hyper-extended at bottom.', 'Glute Bridges or Nordic Hamstring Curls.', 'Safe hamstring isolation suitable for all injury profiles and BMI levels.', 'assets/lever_lying_leg_curl.gif', 'exercises/lever_lying_leg_curl.gif', NULL, NULL, 'dual', '[\"hamstrings\"]', '[\"calves_rear\",\"calves_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(32, 1, 'Pec Deck / Chest Fly Machine', 'Supported machine horizontal chest adduction providing peak pectoral tension across full stretch and squeeze with bent elbows.', 'Chest', 'isolation', 'gym', 'Anterior Deltoids', 'machine', 'Pec Deck / Machine Fly Station', 'intermediate', 3, '10-12 reps', 'Pin 2-3 (15 - 20 kg / 30 - 45 lbs)', 60, '1. Adjust seat so handles/elbow pads align with mid-chest.\n2. Sit back against pad with feet planted firmly.\n3. Bring handles together in front of chest in a hugging motion, squeezing pectorals.\n4. Return slowly until elbows reach torso plane (do not hyperextend).', '• [Shoulders] Mistake: Setting arms too far back, overextending anterior capsule | Fix: Set starting arms forward so elbows never travel behind torso plane.\n• [Elbows] Mistake: Straightening arms into rigid lockout | Fix: Keep consistent soft 15° bend in elbows.\n• [Shoulders] Mistake: Shrugging traps and slumping shoulders forward | Fix: Pin shoulder blades into backrest and lead with chest.', '• [High-Risk Joints] Anterior Shoulder Capsule, Labrum, and Bicep tendon.\n• [Safety Precautions] Extreme horizontal abduction under load is #1 cause of fly-related shoulder impingement; strictly limit rear range.\n• [Regressions & Stop Signs] Substitute with Floor Dumbbell Flyes or Machine Chest Press.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This intermediate-level isolation movement primarily targets the Chest, while engaging Anterior Deltoids as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Anterior shoulder capsule stretch and subacromial impingement.', 'Chest Press Machine or Floor Push-ups.', 'Safe chest hypertrophy isolation with no stabilization required.', 'assets/cable_standing_fly.gif', 'exercises/cable_standing_fly.gif', NULL, NULL, 'anterior', '[\"chest\"]', '[\"deltoids_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(33, 1, 'Incline Dumbbell Curls', 'Biceps curl performed on an incline bench to position the shoulder in extension, placing the long head of the biceps on a full stretch.', 'Arms', 'isolation', 'gym', 'Biceps (Long Head), Brachialis', 'dumbbell', 'Incline Bench (45-60 degrees), Dumbbells', 'intermediate', 3, '12-15 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 45, '1. Set bench to 45-60 degrees. Sit back with head and shoulders supported.\n2. Let arms hang straight down with dumbbells in supinated or neutral grip.\n3. Curl dumbbells upward keeping elbows pointing straight toward floor.\n4. Squeeze biceps at peak and lower with a controlled 3-second negative.', '• [Shoulders] Mistake: Benching angle too low, overstretching anterior shoulder | Fix: Set incline bench at 60-70°, never completely flat.\n• [Elbows] Mistake: Swinging elbows forward during curl | Fix: Keep elbows pointed straight down at floor, isolating long head of bicep.\n• [Wrists] Mistake: Flaccid wrist hyperextension | Fix: Keep wrists neutral or supinated throughout.', '• [High-Risk Joints] Long head of bicep tendon and Anterior shoulder capsule.\n• [Safety Precautions] Deep long-head stretch requires strict lighter loading; do not perform ballistic heavy singles.\n• [Regressions & Stop Signs] Regress to Standing Dumbbell Curls if shoulder tendon aches.', '• Inhale: Inhale during the controlled 2-3 second eccentric lengthening phase.\n• Exhale: Exhale smoothly as you contract and squeeze the prime mover at peak flexion or extension.\n• Bracing: Keep shoulder blades pinned and avoid torso momentum or swinging.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This intermediate-level isolation movement primarily targets the Arms, while engaging Biceps (Long Head), Brachialis as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Anterior shoulder bicep tendon irritation if bench angle is too low.', 'Dumbbell Bicep Curls or Preacher Curls.', 'Biceps peak development for intermediate bodybuilders.', 'assets/dumbbell_incline_biceps_curl.gif', 'exercises/dumbbell_incline_biceps_curl.gif', NULL, NULL, 'dual', '[\"biceps\",\"triceps\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(34, 1, 'Overhead Cable Tricep Extensions', 'Triceps elbow extension performed overhead with a cable rope, isolating the long head of the triceps under maximum stretch.', 'Arms', 'isolation', 'gym', 'Triceps (Long Head)', 'cable', 'Cable Tower with Rope Attachment', 'intermediate', 3, '12-15 reps', 'Pin 1-2 (5 - 10 kg / 10 - 20 lbs)', 45, '1. Attach rope to cable at chest/head height. Face away from stack in staggered stance.\n2. Bring rope overhead, elbows bent next to ears.\n3. Extend forearms forward and overhead, separating rope ends at full extension.\n4. Slowly return under control to deep elbow flexion stretch.', '• [Elbows] Mistake: Flaring elbows out wide away from ears | Fix: Keep elbows pointed forward and tucked in line with shoulders.\n• [Lower Back] Mistake: Arching lower back under cable pull | Fix: Stagger stance and brace core to resist spinal extension.\n• [Range] Mistake: Short-changing the bottom stretch | Fix: Allow hands to travel deep behind head with control.', '• [High-Risk Joints] Medial Epicondyle and Triceps distal tendon.\n• [Safety Precautions] Overhead position puts long head on stretch; warm up elbow joints thoroughly.\n• [Regressions & Stop Signs] If elbow soreness appears, switch to Cable Tricep Pushdowns (hands down).', '• Inhale: Take a diaphragmatic breath at shoulder level, squeezing glutes and engaging abs.\n• Exhale: Exhale through pursed lips as the bar clears your face and locks out overhead.\n• Bracing: Prevent lumbar hyperextension by keeping pelvis neutral throughout the press.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This intermediate-level isolation movement primarily targets the Arms, while engaging Triceps (Long Head) as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Elbow joint ache if elbows flare; shoulder impingement if mobility is restricted.', 'Cable Tricep Pushdowns or Floor Tricep Dips.', 'Superior triceps long head developer for intermediate fitness trainees.', 'assets/cable_overhead_triceps_extension_rope_attachment.gif', 'exercises/cable_overhead_triceps_extension_rope_attachment.gif', NULL, NULL, 'dual', '[\"biceps\",\"triceps\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(35, 1, 'Pistol Squats', 'Advanced unilateral single-leg squat demanding exceptional ankle dorsiflexion, hip mobility, and single-leg quadriceps strength.', 'Quads', 'compound', 'home', 'Glutes, Hamstrings, Core, Ankle Stabilizers', 'bodyweight', 'Bodyweight (Optional Counter/Doorframe for Balance)', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Stand on one leg with opposite leg extended straight forward.\n2. Hinge back and squat deep on the working leg while keeping extended leg off floor.\n3. Descend with control until hamstring touches calf.\n4. Drive through mid-foot to stand back up without letting knee cave inward.', '• [Knee] Mistake: Knee caving inward (valgus) under single-leg load | Fix: Track knee strictly over outer toes.\n• [Heel] Mistake: Lifting working heel off floor | Fix: Anchor entire foot, improving ankle dorsiflexion mobility.\n• [Lower Back] Mistake: Severe spinal flexion to balance | Fix: Keep chest lifted and extend non-working leg straight forward.', '• [High-Risk Joints] Patellar tendon, Meniscus, and Lower back.\n• [Safety Precautions] Advanced unilateral movement requiring elite ankle mobility and tendon capacity.\n• [Regressions & Stop Signs] Regress to Bulgarian Split Squats or Box Pistol Squats.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This advanced-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Core, Ankle Stabilizers as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Meniscus tear or patellar tendonitis under extreme unilateral shear force.', 'Walking / Reverse Lunges or Chair / Box Squats.', 'For advanced calisthenics trainees with healthy joints and BMI < 26. Contraindicated for knee pain.', 'assets/single_leg_squat_pistol_male.gif', 'exercises/single_leg_squat_pistol_male.gif', NULL, NULL, 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(36, 1, 'Wall Handstand / Pike Push-ups', 'Inverted bodyweight pressing movement targeting the anterior deltoids, triceps, and upper pectorals with high overhead loading.', 'Shoulders', 'compound', 'home', 'Triceps, Upper Chest, Core', 'bodyweight', 'Sturdy Wall or Elevated Box/Chair', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Assume pike push-up position or kick up into a wall-supported handstand.\n2. Keep hands slightly wider than shoulder width, fingers spread wide.\n3. Lower crown of head forward in a controlled arc to form a tripod with hands.\n4. Press floor away firmly until arms are locked out overhead.', '• [Shoulders] Mistake: Flaring elbows outward into chicken-wing position | Fix: Angle elbows backward at 45° toward ribs.\n• [Head] Mistake: Dropping crown straight down between hands | Fix: Head must travel forward into tripod position with hands.\n• [Core] Mistake: Banana-backing (arching lumbar spine) | Fix: Squeeze glutes and tuck pelvis to maintain straight stacked bodyline.', '• [High-Risk Joints] Acromioclavicular Joint, Cervical Spine, and Wrists.\n• [Safety Precautions] Place head cushion on floor; never train to muscular failure where neck could collapse.\n• [Regressions & Stop Signs] Regress to Pike Push-ups with feet on floor or Incline Push-ups.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Deltoid Cap Development: Targets lateral, anterior, and posterior deltoid heads for upper body definition.\n• Overhead Mobility: Enhances scapulohumeral rhythm and thoracic extension capacity.\n• Rotator Cuff Support: Conditions stabilizing musculature around the shoulder capsule.', 'This advanced-level compound movement primarily targets the Shoulders, while engaging Triceps, Upper Chest, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Neck compression if head drops; shoulder impingement and wrist hyperextension sprain.', 'Standing Overhead Shoulder Press or Incline / Knee Push-ups.', 'Advanced bodyweight athletes. Avoid with high blood pressure, glaucoma, or shoulder injuries.', 'assets/handstand_push_up.gif', 'exercises/handstand_push_up.gif', NULL, NULL, 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(37, 1, 'Pull-ups / Chin-ups', 'Elite vertical pulling compound exercise loading the latissimus dorsi, biceps, and posterior shoulder girdle using total body weight.', 'Back', 'compound', 'home', 'Biceps, Forearms, Core, Rear Delts', 'bodyweight', 'Sturdy Pull-up Bar', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Grasp pull-up bar with overhand (pull-up) or underhand (chin-up) grip outside shoulder width.\n2. Hang with arms straight and shoulders packed (dead hang into active hang).\n3. Drive elbows down and back to pull chin smoothly over the bar.\n4. Lower with complete 3-second eccentric control to full arm extension.', '• [Shoulders] Mistake: Shrugging shoulders up at top or initiating with arm pull | Fix: Initiate with active scapular depression (pulling shoulder blades down) before arms bend.\n• [Momentum] Mistake: Kicking legs and kipping | Fix: Cross ankles, brace core, and perform strict deadstop reps.\n• [Neck] Mistake: Craning neck over bar to fake full range | Fix: Pull upper chest toward bar while cervical spine remains neutral.', '• [High-Risk Joints] Rotator Cuff, Long head of bicep, and Medial epicondyle.\n• [Safety Precautions] Control the dead hang stretch; do not disengage shoulder stabilizers at bottom.\n• [Regressions & Stop Signs] Regress to Lat Pulldowns or Resistance Band-Assisted Pull-ups.', '• Inhale: Inhale steadily as arms reach full extension, feeling a gentle stretch in the lats.\n• Exhale: Exhale powerfully as you pull elbows down and back, actively depressing the shoulder blades.\n• Bracing: Keep ribs pulled down to avoid hyperextending the lower back.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This advanced-level compound movement primarily targets the Back, while engaging Biceps, Forearms, Core, Rear Delts as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Medial epicondylitis (golfer\'s elbow); shoulder impingement from violent kipping.', 'Lat Pulldown Machine or Inverted Rows (Under Sturdy Table).', 'High strength-to-weight ratio requirement. Heavy/obese BMI users should substitute with Inverted Rows.', 'assets/pull_up.gif', 'exercises/pull_up.gif', NULL, 'assets/assisted_pull_up.gif', 'dual', '[\"lats\"]', '[\"biceps\",\"deltoids_rear\",\"traps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12');
INSERT INTO `exercises` (`id`, `is_active`, `name`, `description`, `target_muscle`, `exercise_type`, `workout_environment`, `secondary_muscles`, `equipment_type`, `required_equipment`, `difficulty`, `default_sets`, `default_reps`, `safe_starting_load`, `default_rest_seconds`, `instructions`, `common_mistakes`, `injury_prevention_cues`, `breathing_tips`, `benefits`, `muscle_explanation`, `common_injuries`, `safer_alternatives_text`, `recommendation_rules`, `image_url`, `proper_form_asset`, `common_mistakes_asset`, `injury_prevention_asset`, `anatomy_view`, `primary_muscle_regions`, `secondary_muscle_regions`, `muscle_mapping_confidence`, `created_at`, `updated_at`) VALUES
(38, 1, 'Parallel Bar Dips', 'High-intensity calisthenic compound press overloading the lower pectorals, anterior deltoids, and triceps on parallel bars.', 'Chest', 'compound', 'home', 'Triceps, Front Delts', 'bodyweight', 'Parallel Dip Bars or Sturdy Dip Station', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Mount parallel bars with straight arms, wrists locked neutral, shoulders depressed.\n2. Lean torso forward 15-20 degrees for chest focus.\n3. Lower body with control until elbows form a 90-degree angle.\n4. Press back up powerfully to full lockout, squeezing chest and triceps.', '• [Shoulders] Mistake: Sinking too deep past 90° with chest caved in | Fix: Stop descent when shoulders align with elbow height.\n• [Shoulders] Mistake: Shrugging shoulders into ears at bottom | Fix: Keep shoulder blades depressed and stable throughout.\n• [Torso] Mistake: Excessive swinging or kicking legs | Fix: Cross ankles, brace core, and lean forward slightly for chest focus.', '• [High-Risk Joints] Sternoclavicular Joint, Anterior Shoulder Capsule, and Triceps tendon.\n• [Safety Precautions] High sternal and anterior shoulder stretch; do not load until mastering bodyweight with strict form.\n• [Regressions & Stop Signs] Regress to Bench Dips or Push-ups if sternal pain or anterior impingement occurs.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This advanced-level compound movement primarily targets the Chest, while engaging Triceps, Front Delts as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Sternoclavicular joint strain (costochondritis); anterior shoulder capsule tear.', 'Chair / Bench Dips or Standard Floor Push-ups.', 'Advanced upper body builder. Contraindicated for active shoulder impingement.', 'assets/chest_dip.gif', 'exercises/chest_dip.gif', NULL, 'assets/assisted_chest_dip_kneeling.gif', 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(39, 1, 'Plyometric / Clapping Push-ups', 'High-velocity explosive push-up developing fast-twitch muscle fiber recruitment, power output, and rate of force development.', 'Chest', 'compound', 'home', 'Triceps, Front Delts, Core', 'bodyweight', 'Bodyweight / Non-Slip Mat', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Assume push-up plank position with rigid core.\n2. Lower chest rapidly to floor.\n3. Explode upward with maximal force so hands lift completely off floor.\n4. Clap hands rapidly (optional) and land with soft, bent elbows immediately absorbing descent.', '• [Wrists] Mistake: Landing with stiff straight arms | Fix: Absorb landing immediately with soft elbows and controlled descent.\n• [Hips] Mistake: Sagging hips during explosive drive | Fix: Maintain rigid plank from head to heels throughout flight phase.\n• [Hands] Mistake: Rushing clap before achieving sufficient height | Fix: Drive explosively through floor first; only clap when clearance is clear.', '• [High-Risk Joints] Wrist carpal bones, Elbow joints, and Rotator cuff.\n• [Safety Precautions] High impact reactive forces; perform on shock-absorbing mat.\n• [Regressions & Stop Signs] Regress to standard Push-ups with explosive concentric drive without leaving floor.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Hypertrophy: Maximizes mechanical tension across pectoralis major and anterior deltoids.\n• Functional Strength: Builds foundational horizontal pushing power transferred to athletic and daily tasks.\n• Joint Integrity: Enhances shoulder stability and triceps lockout strength when executed with proper scapular retraction.', 'This advanced-level compound movement primarily targets the Chest, while engaging Triceps, Front Delts, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Wrist sprain or hyperextension trauma; anterior shoulder capsule jarring.', 'Standard Floor Push-ups or Incline / Knee Push-ups.', 'Athletic power development. Strictly contraindicated for wrist tendinitis or high BMI (>28).', 'assets/clap_push_up.gif', 'exercises/clap_push_up.gif', NULL, 'assets/incline_push_up_on_box.gif', 'dual', '[\"chest\"]', '[\"deltoids_front\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(40, 1, 'Nordic Hamstring Curls', 'The ultimate bodyweight eccentric hamstring exercise, proven by sports science to dramatically reduce hamstring tears and build knee flexion power.', 'Hamstrings', 'isolation', 'home', 'Glutes, Calves', 'bodyweight', 'Sturdy Ankle Anchor (Couch, heavy weight, or partner)', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Kneel on a padded mat with ankles securely anchored under a sturdy base.\n2. Keep hips fully extended so body forms a straight line from knees to head.\n3. Lower torso forward toward floor as slowly as humanly possible using hamstrings to resist gravity.\n4. Catch with hands on floor when failure is reached, lightly push back up.', '• [Hips] Mistake: Breaking at the hips and sticking glutes backward | Fix: Maintain locked straight line from knees through hips to shoulders throughout descent.\n• [Cadence] Mistake: Collapsing immediately to floor | Fix: Fight gravity and decelerate the negative as far forward as possible.\n• [Lower Back] Mistake: Hyperextending lumbar spine | Fix: Posteriorly tilt pelvis and brace abs.', '• [High-Risk Joints] Hamstring distal/proximal myotendinous junctions and Knee joints.\n• [Safety Precautions] Extreme eccentric hamstring load; highest single preventative drill for ACL and hamstring tears.\n• [Regressions & Stop Signs] Use hands to push off floor on return or use resistance band support.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This advanced-level isolation movement primarily targets the Hamstrings, while engaging Glutes, Calves as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Acute hamstring muscle tear or avulsion if attempted without warm-up.', 'Romanian Deadlift or Lying / Seated Leg Curls.', 'Gold standard athletic exercise for hamstring injury prevention.', 'assets/self_assisted_inverse_leg_curl_proper.gif', 'exercises/self_assisted_inverse_leg_curl_proper.gif', NULL, NULL, 'dual', '[\"hamstrings\"]', '[\"glutes\",\"calves_rear\",\"calves_front\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(41, 1, 'Bodyweight Skull Crushers', 'Inverted calisthenic triceps extension performed against an elevated bar or sturdy counter, shifting bodyweight onto triceps long heads.', 'Arms', 'isolation', 'home', 'Triceps Brachii, Core', 'bodyweight', 'Elevated Bar, Sturdy Countertop, or Low Table', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Stand facing a sturdy bar or edge at waist/chest height. Grasp with overhand grip.\n2. Step feet back into a rigid plank.\n3. Bend elbows to lower forehead toward or beneath the bar, keeping elbows tucked.\n4. Press through palms to extend elbows back to straight arms.', '• [Elbows] Mistake: Flaring elbows out wide | Fix: Keep elbows shoulder-width and pointing forward.\n• [Hips] Mistake: Sagging hips toward bar | Fix: Maintain solid plank tension throughout.\n• [Head] Mistake: Banging head into bar | Fix: Guide forehead smoothly under or toward bar edge under control.', '• [High-Risk Joints] Triceps tendon insertion and Elbow joint.\n• [Safety Precautions] Control cadence; adjust bar height to modulate resistance.\n• [Regressions & Stop Signs] Elevate bar higher to reduce load; switch to Cable Pushdowns if elbow pain occurs.', '• Inhale: Inhale during the controlled 2-3 second eccentric lengthening phase.\n• Exhale: Exhale smoothly as you contract and squeeze the prime mover at peak flexion or extension.\n• Bracing: Keep shoulder blades pinned and avoid torso momentum or swinging.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This advanced-level isolation movement primarily targets the Arms, while engaging Triceps Brachii, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Triceps tendonitis (distal elbow ache) if elbows flare outward under load.', 'Floor Tricep Dips or Cable Tricep Pushdowns.', 'Advanced bodyweight arm isolation for home athletes.', 'assets/bodyweight_kneeling_triceps_extension.gif', 'exercises/bodyweight_kneeling_triceps_extension.gif', NULL, NULL, 'dual', '[\"biceps\",\"triceps\"]', '[\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(42, 1, 'Dragon Flags', 'Bruce Lee-inspired advanced abdominal movement requiring intense full-body tension to lower the entire body from an elevated pivot point.', 'Core', 'isolation', 'home', 'Rectus Abdominis, Hip Flexors, Lats', 'bodyweight', 'Sturdy Bench or Heavy Furniture Post', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Lie on bench grasping edges or secure pole behind head.\n2. Drive legs and torso straight up toward ceiling until resting solely on upper back/shoulders.\n3. Keep body in a completely straight rigid line from shoulders to toes.\n4. Lower body down with slow eccentric control, hovering 1 inch above bench before lifting.', '• [Lower Back] Mistake: Bending at hips instead of keeping body in rigid straight board | Fix: Only shoulders touch bench; entire body moves as one lever.\n• [Neck] Mistake: Putting compressive bodyweight on cervical spine | Fix: Weight rests strictly across upper back and shoulder blades, never neck.\n• [Cadence] Mistake: Dropping hips rapidly | Fix: Decelerate lowering phase over 3 seconds.', '• [High-Risk Joints] Cervical Spine and Lumbar intervertebral discs.\n• [Safety Precautions] Advanced calisthenic movement; requires master-level core stability.\n• [Regressions & Stop Signs] Regress to Hanging Leg Raises or Dead Bugs.', '• Exhale: Exhale deeply through pursed lips during peak abdominal contraction, drawing navel toward spine.\n• Inhale: Inhale softly on return without losing intra-abdominal stiffness.\n• Bracing: Avoid holding breath (Valsalva) on static holds; practice rhythmic diaphragmatic breathing.', '• Anti-Extension Stability: Resists excessive lumbar extension, safeguarding intervertebral discs.\n• Force Transmission: Bridges energy transfer seamlessly between lower body and upper body movements.\n• Postural Control: Elevates intra-abdominal pressure regulation and pelvifemoral alignment.', 'This advanced-level isolation movement primarily targets the Core, while engaging Rectus Abdominis, Hip Flexors, Lats as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Severe lumbar spine hyperextension sprain if core collapses under load.', 'Hanging Leg Raises or Glute Bridges.', 'For elite core stability. Strictly contraindicated for lumbar disc herniation or neck pain.', 'assets/flag.gif', 'exercises/flag.gif', NULL, NULL, 'dual', '[\"abs\"]', '[\"lats\",\"glutes\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(43, 1, 'Hanging Leg Raises', 'Decompressed vertical core movement lifting the legs to horizontal while hanging from a bar, developing lower abdominals and hip flexors.', 'Core', 'isolation', 'home', 'Lower Abs, Hip Flexors, Forearms', 'bodyweight', 'Sturdy Pull-up Bar', 'advanced', 3, '12-20 reps (or to RPE 8)', 'Bodyweight (Strict Form / Controlled Tempo)', 60, '1. Hang from pull-up bar with overhand grip and engaged shoulders.\n2. Posteriorly tilt pelvis and lift legs straight up until parallel to floor (or toes to bar).\n3. Pause for half a second without swinging.\n4. Lower legs slowly with controlled negative.', '• [Momentum] Mistake: Swinging body back and forth using momentum | Fix: Stop completely between reps; initiate strictly from abdominals.\n• [Pelvis] Mistake: Lifting legs without posterior pelvic tilt | Fix: Roll pelvis upward toward ribs at top of rep to activate rectus abdominis.\n• [Shoulders] Mistake: Hanging completely passive with loose shoulders | Fix: Maintain active hang with scapulae depressed.', '• [High-Risk Joints] Lumbar spine, Shoulders, and Hip flexors.\n• [Safety Precautions] Active shoulder depression prevents impingement at top stretch.\n• [Regressions & Stop Signs] Perform Captain\'s Chair Knee Raises or Floor Dead Bugs.', '• Exhale: Exhale deeply through pursed lips during peak abdominal contraction, drawing navel toward spine.\n• Inhale: Inhale softly on return without losing intra-abdominal stiffness.\n• Bracing: Avoid holding breath (Valsalva) on static holds; practice rhythmic diaphragmatic breathing.', '• Anti-Extension Stability: Resists excessive lumbar extension, safeguarding intervertebral discs.\n• Force Transmission: Bridges energy transfer seamlessly between lower body and upper body movements.\n• Postural Control: Elevates intra-abdominal pressure regulation and pelvifemoral alignment.', 'This advanced-level isolation movement primarily targets the Core, while engaging Lower Abs, Hip Flexors, Forearms as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Hip flexor tendonitis or lumbar pull if swinging violently.', 'Glute Bridges or Prone Cobra / Superman Holds.', 'Excellent for decompressing spine while building functional core strength.', 'assets/hanging_leg_raise.gif', 'exercises/hanging_leg_raise.gif', NULL, 'assets/captains_chair_straight_leg_raise.gif', 'dual', '[\"abs\"]', '[\"forearms_front\",\"glutes\",\"biceps\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(44, 1, 'Conventional / Sumo Barbell Deadlifts', 'The ultimate heavy posterior chain compound lift testing full-body CNS output, hip extension, and upper back thickness.', 'Back', 'compound', 'gym', 'Glutes, Hamstrings, Erector Spinae, Traps, Forearms', 'barbell', 'Olympic Barbell, Olympic Plates, Deadlift Platform', 'advanced', 4, '6-8 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Set stance: feet hip-width (Conventional) or wide stance with toes flared (Sumo).\n2. Grip bar just outside legs (Conventional) or inside legs (Sumo).\n3. Pull slack out of bar, depress shoulders, wedge hips into position with neutral spine.\n4. Push floor away through mid-foot and lock out hips and knees simultaneously.', '• [Lower Back] Mistake: Rounding lumbar spine (spinal flexion) under load | Fix: Lock lats, set chest proud, and brace core 360° before pulling.\n• [Bar Path] Mistake: Bar drifting away from shins | Fix: Keep bar in continuous contact with legs throughout entire lift.\n• [Hips] Mistake: Hips shooting up before bar leaves floor | Fix: Push floor away with legs; shoulders and hips rise simultaneously.', '• [High-Risk Joints] L4-L5 / L5-S1 Lumbar Discs, Bicep Tendons, and Sacroiliac Joint.\n• [Safety Precautions] Never yank or jerk bar off floor; pull slack out of bar first. Keep arms straight (never curl with mixed grip).\n• [Regressions & Stop Signs] If lumbar rounding occurs, switch to Trap Bar Deadlift or Romanian Deadlift.', '• Inhale: Inhale deeply through the belly at the bar setup, packing the lats and locking lumbar neutral.\n• Hold: Hold the breath during the initial floor pull or hinge extension.\n• Exhale: Exhale smoothly at hip lockout; reset intra-abdominal brace before the next repetition.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This advanced-level compound movement primarily targets the Back, while engaging Glutes, Hamstrings, Erector Spinae, Traps, Forearms as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar disc herniation, sacroiliac joint sprain, distal biceps tendon tear with mixed grip.', 'Romanian Deadlift (Barbell/Dumbbell) or Heavy Barbell Hip Thrusts.', 'Reserved for advanced strength athletes. Contraindicated for active lumbar disc vulnerabilities.', 'assets/barbell_deadlift.gif', 'exercises/barbell_deadlift.gif', NULL, 'assets/dumbbell_single_leg_deadlift_with_stepbox_support.gif', 'dual', '[\"lats\",\"traps\"]', '[\"forearms_front\",\"lower_back\",\"glutes\",\"hamstrings\",\"biceps\",\"triceps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(45, 1, 'Strict Standing Overhead Barbell Press', 'Uncompromising vertical overhead military press testing pure shoulder strength and whole-body core stability with zero leg drive.', 'Shoulders', 'compound', 'gym', 'Triceps, Upper Chest, Core', 'barbell', 'Olympic Barbell, Power Rack, Weight Plates', 'advanced', 4, '6-8 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Unrack bar across anterior deltoids and clavicles, elbows slightly forward of bar.\n2. Squeeze glutes, quads, and abs as tight as possible.\n3. Press bar directly vertical, pulling chin back to clear face.\n4. Lock bar out directly above crown of head and shrug up slightly at peak.', '• [Lower Back] Mistake: Arching lower back excessively to turn lift into incline press | Fix: Lock glutes and brace abdominals firmly.\n• [Bar Path] Mistake: Pushing bar around face | Fix: Pull chin back so bar ascends vertically, pushing head through window at top.\n• [Elbows] Mistake: Elbows dropping behind bar | Fix: Keep elbows slightly in front of bar at start position.', '• [High-Risk Joints] Supraspinatus Tendon, Acromioclavicular Joint, and Lumbar Spine.\n• [Safety Precautions] Requires full active shoulder overhead flexion; do not perform if painful arc exists.\n• [Regressions & Stop Signs] Switch to Seated Dumbbell Press or High Incline Press.', '• Inhale: Take a diaphragmatic breath at shoulder level, squeezing glutes and engaging abs.\n• Exhale: Exhale through pursed lips as the bar clears your face and locks out overhead.\n• Bracing: Prevent lumbar hyperextension by keeping pelvis neutral throughout the press.', '• Deltoid Cap Development: Targets lateral, anterior, and posterior deltoid heads for upper body definition.\n• Overhead Mobility: Enhances scapulohumeral rhythm and thoracic extension capacity.\n• Rotator Cuff Support: Conditions stabilizing musculature around the shoulder capsule.', 'This advanced-level compound movement primarily targets the Shoulders, while engaging Triceps, Upper Chest, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Subacromial impingement; lumbar facet joint compression.', 'Seated Dumbbell Lateral Raises or Cable Lateral Raises.', 'Advanced upper-body pressing benchmark.', 'assets/barbell_standing_wide_military_press.gif', 'exercises/barbell_standing_wide_military_press.gif', NULL, 'assets/dumbbell_standing_overhead_press.gif', 'dual', '[\"deltoids_front\",\"deltoids_rear\"]', '[\"chest\",\"triceps\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(46, 1, 'Weighted Pull-ups / Dips', 'Overloaded calisthenic compound movements utilizing a dip belt with weight plates to drive upper body hypertrophy and raw pulling/pushing power.', 'Back', 'compound', 'gym', 'Chest, Triceps, Biceps, Forearms', 'barbell', 'Pull-up Bar / Dip Station, Dip Belt with Weight Plates', 'advanced', 4, '6-8 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Secure dip belt with weight plates around waist.\n2. Mount pull-up bar or parallel dip bars with firm grip.\n3. Execute movement through complete range of motion with a strict 3-second negative.\n4. Avoid all kipping, swinging, or jerky motions.', '• [Shoulders] Mistake: Allowing extra weight to yank shoulders into passive hyperextension | Fix: Keep muscular tension throughout entire range.\n• [Momentum] Mistake: Kipping or violently jerking legs | Fix: Maintain dead-hang control with stable pelvic position.\n• [Belt Setup] Mistake: Plates swinging unpredictably | Fix: Secure weight belt tightly with chain between thighs.', '• [High-Risk Joints] Sternum, Rotator Cuff, and Bicep tendon.\n• [Safety Precautions] High joint stress; warm up with bodyweight sets before adding external load.\n• [Regressions & Stop Signs] Strip external weight; perform bodyweight Pull-ups or Dips.', '• Inhale: Inhale steadily as you lower the weight or body toward chest level, expanding the ribcage.\n• Exhale: Exhale smoothly as you drive upward or press away during the concentric exertion.\n• Bracing: Maintain abdominal wall pressure to stabilize shoulder girdle and lumbar spine.', '• Postural Correction: Strengthens rhomboids and lower trapezius to counteract rounded shoulders and forward head posture.\n• Pulling Power: Stimulates latissimus dorsi thickness and functional grip endurance.\n• Spine Protection: Promotes balanced muscular symmetry protecting the glenohumeral joint from pressing dominance.', 'This advanced-level compound movement primarily targets the Back, while engaging Chest, Triceps, Biceps, Forearms as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Rotator cuff tear, sternum costochondritis, golfer\'s elbow tendonitis.', 'Lat Pulldown Machine or Chest Press Machine.', 'For advanced athletes aiming for maximal strength-to-weight ratio.', 'assets/weighted_pull_up.gif', 'exercises/weighted_pull_up.gif', NULL, NULL, 'dual', '[\"lats\"]', '[\"biceps\",\"deltoids_rear\",\"traps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(47, 1, 'Heavy Barbell Hip Thrusts', 'Horizontal posterior chain overload loading the gluteus maximus across full hip extension with near-zero spinal axial compressive load.', 'Glutes', 'compound', 'gym', 'Hamstrings, Adductors, Core', 'barbell', 'Barbell, Thick Barbell Foam Pad, Flat Bench', 'advanced', 4, '6-8 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Sit on floor with upper back against bench edge, roll padded barbell over hips.\n2. Plant feet flat, shoulder-width apart, shins vertical at top.\n3. Drive through heels to extend hips until thighs and torso form a straight horizontal table.\n4. Posteriorly tilt pelvis and contract glutes hard for 1-2 seconds before lowering.', '• [Lower Back] Mistake: Hyperextending lumbar spine at top lock | Fix: Keep chin tucked and ribs down; movement pivots strictly at hips.\n• [Neck] Mistake: Tilting head backward over bench | Fix: Keep eyes looking forward toward knees throughout entire rep.\n• [Feet] Mistake: Feet placed too far forward or too close | Fix: Set feet so shins are perpendicular (90°) to floor at top lockout.', '• [High-Risk Joints] Lumbar Intervertebral Discs and Hip Capsule.\n• [Safety Precautions] Always use thick barbell pad to cushion anterior pelvic crest.\n• [Regressions & Stop Signs] Regress to Dumbbell Hip Thrusts or Bilateral Glute Bridges.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This advanced-level compound movement primarily targets the Glutes, while engaging Hamstrings, Adductors, Core as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar hyperextension ache if tilting pelvis anteriorly.', 'Glute Bridges or Romanian Deadlift (Barbell/Dumbbell).', 'The single highest glute EMG activation exercise in exercise science.', 'assets/barbell_glute_bridge.gif', 'exercises/barbell_glute_bridge_two_legs_on_bench_male.gif', NULL, NULL, 'posterior', '[\"glutes\"]', '[\"hamstrings\",\"lower_back\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(48, 1, 'Heavy Bulgarian Split Squats', 'Rear-foot-elevated unilateral split squat loaded with dumbbells or barbell, delivering intense quadriceps and glute hypertrophy with hip stability.', 'Quads', 'compound', 'gym', 'Glutes, Hamstrings, Adductors', 'dumbbell', 'Dumbbells or Barbell, Flat Bench', 'advanced', 3, '8-12 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 60, '1. Place top of rear foot laces-down on flat bench behind you.\n2. Step front foot out roughly 2.5 to 3 feet.\n3. Lower body vertically until front thigh is parallel to ground and rear knee hovers above floor.\n4. Drive through front heel to return to top.', '• [Knees] Mistake: Front knee wobbling inward (valgus collapse) | Fix: Screw front foot into floor and track knee over second toe.\n• [Torso] Mistake: Severe forward torso slump or twisting hips | Fix: Keep hips squared forward with chest tall.\n• [Rear Leg] Mistake: Pushing predominantly with rear foot | Fix: Front leg does 85% of work; rear leg is strictly for balance.', '• [High-Risk Joints] Patellar Tendon, Hip Flexor insertion, and Lower Back.\n• [Safety Precautions] Tremendous unilateral leg power with significantly lower spinal load than back squats.\n• [Regressions & Stop Signs] Regress to Standard Split Squats (both feet on floor) or Goblet Squats.', '• Inhale: Take a deep diaphragmatic breath at the standing top position and lock 360-degree core tension.\n• Hold/Descent: Maintain intra-abdominal pressure as hips descend below parallel.\n• Exhale: Exhale forcefully past the sticking point as you drive through mid-foot back to standing.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This advanced-level compound movement primarily targets the Quads, while engaging Glutes, Hamstrings, Adductors as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Patellar tendinitis if knee drives excessively forward; hip flexor strain on rear leg.', 'Leg Press Machine or Goblet Squat.', 'Top unilateral leg hypertrophy builder for advanced gym athletes.', 'assets/dumbbell_single_leg_split_squat.gif', 'exercises/dumbbell_single_leg_split_squat.gif', NULL, NULL, 'dual', '[\"quads\"]', '[\"glutes\",\"hamstrings\",\"abs\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(49, 1, 'Preacher Curls (Barbell/EZ Bar)', 'Strict arm curl utilizing a slanted preacher bench to immobilize the shoulder joint, completely isolating the short head of the biceps brachii.', 'Arms', 'isolation', 'gym', 'Biceps Brachii, Brachialis', 'barbell', 'Preacher Curl Bench, EZ-Curl Bar, Plates', 'advanced', 4, '6-8 reps', 'Empty Barbell (20 kg / 45 lbs) or Light Training Bar', 90, '1. Sit on preacher bench with armpits snug against top edge of pad.\n2. Grasp EZ-bar with underhand grip on inner angled knurling.\n3. Curl bar up to chin level, contracting biceps firmly.\n4. Lower bar with strict 3-second control, stopping 2-3 degrees before full elbow lockout.', '• [Elbows] Mistake: Hyperextending and bouncing at bottom lockout | Fix: Stop 1-2 inches before full lockout to protect bicep tendon.\n• [Shoulders] Mistake: Lifting elbows or shoulders off preacher pad | Fix: Keep triceps and chest firmly anchored against angled pad.\n• [Wrists] Mistake: Over-cocking wrists backward | Fix: Use EZ-bar with neutral angled grips to preserve wrists.', '• [High-Risk Joints] Distal Biceps Tendon and Medial Epicondyle.\n• [Safety Precautions] High tendon stress at bottom extension; never drop weight quickly into bottom.\n• [Regressions & Stop Signs] Regress to Standing EZ-bar Curls or Incline Dumbbell Curls.', '• Inhale: Inhale during the controlled 2-3 second eccentric lengthening phase.\n• Exhale: Exhale smoothly as you contract and squeeze the prime mover at peak flexion or extension.\n• Bracing: Keep shoulder blades pinned and avoid torso momentum or swinging.', '• Arm Hypertrophy: Directly isolates bicep brachii, brachialis, and tricep heads.\n• Elbow Health: Strengthens tendon insertions across the medial and lateral epicondyles.\n• Grip & Forearm Endurance: Reinforces wrist flexors and extensors for improved compound lift stability.', 'This advanced-level isolation movement primarily targets the Arms, while engaging Biceps Brachii, Brachialis as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Distal biceps tendon rupture under heavy eccentric stretch at full extension.', 'Dumbbell Bicep Curls or Incline Dumbbell Curls.', 'Strict isolation for biceps peak. Extreme caution against ballistic bottom stretching.', 'assets/barbell_preacher_curl.gif', 'exercises/barbell_preacher_curl.gif', NULL, NULL, 'dual', '[\"biceps\",\"triceps\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(50, 1, 'Standing Calf Raises (Smith Machine with Step)', 'High-load calf overload utilizing the guided vertical track of a Smith machine and an elevated step block for maximum stretch and contraction.', 'Calves', 'isolation', 'gym', 'Gastrocnemius, Soleus', 'machine', 'Smith Machine, Elevated Calf Block, Weight Plates', 'advanced', 3, '10-12 reps', 'Pin 2-3 (15 - 20 kg / 30 - 45 lbs)', 60, '1. Position calf block under Smith bar. Rest bar across upper traps.\n2. Place balls of feet on block edge, letting heels drop down for deep calf stretch.\n3. Pause for 2 seconds in the full deep stretch to eliminate elastic recoil.\n4. Explode upward onto toes, holding peak contraction for 1 second.', '• [Achilles] Mistake: Rapid bouncy cadence at bottom stretch | Fix: Pause 2 seconds at deep stretch and 1 second at peak contraction.\n• [Ankles] Mistake: Supinating onto outer foot edges | Fix: Drive evenly through balls of big toes.\n• [Knees] Mistake: Bending knees to push with quads | Fix: Keep knees in soft locked extension.', '• [High-Risk Joints] Achilles Tendon and Plantar Fascia.\n• [Safety Precautions] Step gives deep stretch below horizontal; avoid ballistic drop to prevent Achilles rupture.\n• [Regressions & Stop Signs] Regress to Flat Ground Calf Raises without step deficit.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Ankle Stability: Bolsters gastrocnemius and soleus muscular endurance for sprinting and deceleration.\n• Achilles Tendon Health: Improves elastic energy storage and ankle plantarflexion power.\n• Balance & Agility: Fosters lower kinetic chain equilibrium.', 'This advanced-level isolation movement primarily targets the Calves, while engaging Gastrocnemius, Soleus as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Achilles tendinitis or plantar fasciitis from ballistic bouncing.', 'Standing Calf Raises (Flat Ground) or Seated Calf Machine.', 'Maximum mechanical loading for hypertrophy of stubborn calf musculature.', 'assets/smith_reverse_calf_raises_proper.gif', 'exercises/smith_reverse_calf_raises_proper.gif', NULL, NULL, 'dual', '[\"calves_rear\",\"calves_front\"]', '[]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(51, 1, 'Incline Rear Delt Flyes', 'Prone transverse abduction on an incline bench isolating the posterior deltoid, infraspinatus, and mid-trapezius for shoulder joint health.', 'Shoulders', 'isolation', 'gym', 'Rear Delts, Rhomboids, Traps', 'dumbbell', 'Incline Bench (30-45 degrees), Dumbbells (Pair)', 'advanced', 3, '12-15 reps', 'Light Dumbbells (2.5 - 5 kg / 5 - 10 lbs)', 45, '1. Set bench to 30-45 degrees. Lie chest-down on pad with dumbbells hanging down.\n2. With slight bend in elbows, raise arms wide in transverse plane leading with pinkies.\n3. Squeeze rear deltoids at peak contraction for 1 second.\n4. Lower weights with control over 3 seconds.', '• [Traps] Mistake: Squeezing shoulder blades together and using upper traps | Fix: Keep scapulae relatively still; drive pinkies out and wide.\n• [Torso] Mistake: Jerking torso up off bench | Fix: Keep chest glued flat against incline pad.\n• [Elbows] Mistake: Bending elbows excessively into a row | Fix: Maintain wide fixed arc with soft elbow bend.', '• [High-Risk Joints] Posterior Shoulder Capsule and Cervical Spine.\n• [Safety Precautions] Light weights are mandatory for rear deltoid isolation.\n• [Regressions & Stop Signs] Switch to Cable Face Pulls or Prone Cobra.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Deltoid Cap Development: Targets lateral, anterior, and posterior deltoid heads for upper body definition.\n• Overhead Mobility: Enhances scapulohumeral rhythm and thoracic extension capacity.\n• Rotator Cuff Support: Conditions stabilizing musculature around the shoulder capsule.', 'This advanced-level isolation movement primarily targets the Shoulders, while engaging Rear Delts, Rhomboids, Traps as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Cervical neck spasm from trap over-activation.', 'Seated Cable Rows or Prone Cobra / Superman Holds.', 'Essential structural balance movement protecting against internal rotation.', 'assets/dumbbell_rear_delt_raise.gif', 'exercises/dumbbell_rear_delt_raise.gif', NULL, NULL, 'dual', '[\"deltoids_front\",\"deltoids_rear\"]', '[\"traps\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12'),
(52, 1, 'Cable Glute Kickbacks', 'Direct isolated hip extension using a low cable pulley and ankle strap, providing continuous resistance for the gluteus maximus.', 'Glutes', 'isolation', 'gym', 'Gluteus Maximus, Hamstrings', 'cable', 'Low Cable Pulley, Ankle Strap', 'advanced', 3, '12-15 reps', 'Pin 1-2 (5 - 10 kg / 10 - 20 lbs)', 45, '1. Attach ankle cuff to low cable. Face tower with hands holding frame for support.\n2. Hinge forward 30-45 degrees at hips with neutral spine.\n3. Kick working leg back and slightly outward by contracting glute.\n4. Squeeze glute at peak extension for 1 second, then control return without touching floor.', '• [Lower Back] Mistake: Hyperextending lower back to swing leg high | Fix: Lock lumbar spine neutral; kickback movement comes strictly from hip extension.\n• [Torso] Mistake: Twisting hips open to side | Fix: Keep hips squared forward parallel to cable machine.\n• [Cadence] Mistake: Letting weight yank foot forward | Fix: Squeeze glute at peak for 1 second, control return over 2 seconds.', '• [High-Risk Joints] Lumbar Spine and Sacroiliac Joint.\n• [Safety Precautions] Eliminates spinal compression; isolate gluteus maximus without lumbar contribution.\n• [Regressions & Stop Signs] Regress to Bodyweight Glute Bridges or Quadruped Kickbacks.', '• Inhale: Inhale deeply through your nose during the lowering (eccentric) phase.\n• Exhale: Exhale smoothly through your mouth during the exertion (concentric) phase.\n• Bracing: Maintain core stability and avoid holding your breath under strain.', '• Lower Body Drive: Develops tremendous kinetic power across hip and knee extensors.\n• Bone Mineral Density: Provides healthy axial compressive stimulus supporting hip and femoral bone strength.\n• Knee & Hip Resilience: Reinforces vastus medialis, gluteal stabilizers, and hamstring deceleration strength.', 'This advanced-level isolation movement primarily targets the Glutes, while engaging Gluteus Maximus, Hamstrings as synergistic stabilizers. Controlled eccentric cadence maximizes muscle fiber recruitment, joint congruence, and metabolic stimulus.', 'Lumbar hyperextension pain if spine hinges instead of hip.', 'Glute Bridges or Heavy Barbell Hip Thrusts.', 'Superior isolated gluteus maximus hypertrophy without loading the spine.', 'assets/cable_kickback.gif', 'exercises/cable_kickback.gif', NULL, NULL, 'posterior', '[\"glutes\"]', '[\"hamstrings\"]', 'high', '2026-10-14 21:38:29', '2026-10-19 23:32:12');

-- --------------------------------------------------------

--
-- Table structure for table `exercise_contraindications`
--

CREATE TABLE `exercise_contraindications` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `exercise_id` bigint(20) UNSIGNED NOT NULL,
  `limitation_id` bigint(20) UNSIGNED NOT NULL,
  `alternative_exercise_id` bigint(20) UNSIGNED DEFAULT NULL,
  `risk_mechanism` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `exercise_contraindications`
--

INSERT INTO `exercise_contraindications` (`id`, `exercise_id`, `limitation_id`, `alternative_exercise_id`, `risk_mechanism`, `created_at`, `updated_at`) VALUES
(1, 44, 1, 27, 'Heavy spinal shear and unsupported lumbar flexion torque under maximal axial load.', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(2, 26, 1, 12, 'Compressive axial loading on lumbar vertebrae and L4-L5 disc compression risk.', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(3, 29, 1, 31, 'High eccentric torque on lumbar erectors during forward hip hinge.', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(4, 28, 1, 18, 'Lumbar hyperlordosis and facet joint compression under vertical load.', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(5, 45, 1, 18, 'Strict standing axial spinal compression without leg damping.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(6, 42, 1, 5, 'Extreme anterior pelvic tilt and lumbar hyperextension torque.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(7, 26, 2, 3, 'Acute deep knee flexion past 90 degrees induces high patellofemoral compressive force.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(8, 35, 2, 3, 'Extreme unilateral patellofemoral shear and acute meniscus compression.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(9, 19, 2, 5, 'High deceleration shear on anterior cruciate ligament and patellar tendon.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(10, 48, 2, 12, 'Severe unilateral knee shear under heavy loading.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(11, 17, 2, 12, 'High patellofemoral joint compressive force at terminal 30 degrees of extension.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(12, 25, 3, 10, 'Extreme external rotation under barbell load compresses anterior shoulder capsule.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(13, 1, 3, 2, 'Anterior capsule shear and rotator cuff pinching under full floor bodyweight.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(14, 28, 3, 18, 'Pinching of supraspinatus tendon in subacromial space during vertical press.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(15, 45, 3, 30, 'Severe subacromial impingement during strict overhead barbell pressing.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(16, 38, 3, 9, 'Shoulder hyperextension past 90 degrees strains anterior capsule and rotator cuff.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(17, 36, 3, 18, 'Heavy compressive overhead loading with restricted scapular movement.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(18, 25, 4, 10, 'Heavy backward wrist extension torque under rigid straight barbell.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(19, 1, 4, 2, '90-degree wrist extension under bodyweight compresses carpal tunnel canal.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(20, 39, 4, 2, 'High-velocity impact shock and violent hyperextension upon landing.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(21, 49, 4, 15, 'Fixed supination under barbell load strains radial and ulnar collateral ligaments.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(22, 36, 4, 18, 'Entire bodyweight compressing hyperextended wrist joints against the floor.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(23, 26, 5, 13, 'Direct barbell bar compression on C7 vertebra and upper cervical trapezius.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(24, 45, 5, 18, 'Heavy levator scapulae and upper trapezius tension triggering cervical spasm.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(25, 35, 6, 3, 'High unilateral balance demand and acute dorsiflexion torque causing ankle collapse.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(26, 19, 6, 3, 'Ankle wobble and lateral inversion sprain risk during dynamic deceleration.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(27, 50, 6, 7, 'Deep deficit stretch on edge of step risks acute Achilles sprain.', '2026-10-14 21:38:30', '2026-10-14 21:38:30');

-- --------------------------------------------------------

--
-- Table structure for table `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `connection` text NOT NULL,
  `queue` text NOT NULL,
  `payload` longtext NOT NULL,
  `exception` longtext NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--

CREATE TABLE `jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `queue` varchar(255) NOT NULL,
  `payload` longtext NOT NULL,
  `attempts` tinyint(3) UNSIGNED NOT NULL,
  `reserved_at` int(10) UNSIGNED DEFAULT NULL,
  `available_at` int(10) UNSIGNED NOT NULL,
  `created_at` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `job_batches`
--

CREATE TABLE `job_batches` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `total_jobs` int(11) NOT NULL,
  `pending_jobs` int(11) NOT NULL,
  `failed_jobs` int(11) NOT NULL,
  `failed_job_ids` longtext NOT NULL,
  `options` mediumtext DEFAULT NULL,
  `cancelled_at` int(11) DEFAULT NULL,
  `created_at` int(11) NOT NULL,
  `finished_at` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `meal_logs`
--

CREATE TABLE `meal_logs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `log_date` date NOT NULL,
  `meal_type` enum('breakfast','lunch','dinner','snack') NOT NULL DEFAULT 'lunch',
  `food_name` varchar(150) NOT NULL,
  `calories` int(11) NOT NULL DEFAULT 0,
  `protein_grams` decimal(5,2) NOT NULL DEFAULT 0.00,
  `carbs_grams` decimal(5,2) NOT NULL DEFAULT 0.00,
  `fat_grams` decimal(5,2) NOT NULL DEFAULT 0.00,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `meal_logs`
--

INSERT INTO `meal_logs` (`id`, `user_id`, `log_date`, `meal_type`, `food_name`, `calories`, `protein_grams`, `carbs_grams`, `fat_grams`, `created_at`, `updated_at`) VALUES
(1, 3, '2026-10-15', 'snack', '150g Fresh Strawberries, Medium (182g), 2x Whole Avocado (200g)', 783, 9.60, 70.60, 59.60, '2026-10-14 23:28:09', '2026-10-14 23:28:09');

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '0001_01_01_000000_create_users_table', 1),
(2, '0001_01_01_000001_create_cache_table', 1),
(3, '0001_01_01_000002_create_jobs_table', 1),
(4, '2026_08_24_000001_create_user_profiles_table', 1),
(5, '2026_08_24_000002_create_physical_limitations_table', 1),
(6, '2026_08_24_000003_create_user_limitations_table', 1),
(7, '2026_08_24_000004_create_exercises_table', 1),
(8, '2026_08_24_000005_create_exercise_contraindications_table', 1),
(9, '2026_08_24_000006_create_workout_plans_table', 1),
(10, '2026_08_24_000007_create_workout_logs_table', 1),
(11, '2026_08_24_000008_create_meal_and_activity_logs_table', 1),
(12, '2026_08_24_070725_create_personal_access_tokens_table', 1),
(13, '2026_09_22_000001_add_assessment_v2_fields_to_user_profiles_table', 1),
(14, '2026_10_13_000001_add_cms_fields_to_exercises_table', 1),
(15, '2026_10_15_052920_add_catalog_v3_fields_to_exercises_table', 1),
(16, '2026_10_19_093356_add_guide_system_fields_to_exercises_table', 2),
(17, '2026_10_20_000001_add_anatomy_mapping_fields_to_exercises_table', 3);

-- --------------------------------------------------------

--
-- Table structure for table `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) NOT NULL,
  `tokenable_id` bigint(20) UNSIGNED NOT NULL,
  `name` text NOT NULL,
  `token` varchar(64) NOT NULL,
  `abilities` text DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `personal_access_tokens`
--

INSERT INTO `personal_access_tokens` (`id`, `tokenable_type`, `tokenable_id`, `name`, `token`, `abilities`, `last_used_at`, `expires_at`, `created_at`, `updated_at`) VALUES
(1, 'App\\Models\\User', 3, 'mobile-auth-token', '39a7c1a735277a51efbf771b82678a058daed56e9a859f21be19d0dc92f44c4e', '[\"*\"]', '2026-10-20 00:02:59', NULL, '2026-10-14 21:52:34', '2026-10-20 00:02:59'),
(2, 'App\\Models\\User', 4, 'mobile-auth-token', '6818bea9709e5fcaddf2aab90e3a7299a6bee95a359da052957903bf3c5d9556', '[\"*\"]', '2026-10-20 00:13:55', NULL, '2026-10-20 00:08:27', '2026-10-20 00:13:55');

-- --------------------------------------------------------

--
-- Table structure for table `physical_limitations`
--

CREATE TABLE `physical_limitations` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `affected_body_part` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `physical_limitations`
--

INSERT INTO `physical_limitations` (`id`, `code`, `name`, `description`, `affected_body_part`, `created_at`, `updated_at`) VALUES
(1, 'LOWER_BACK_PAIN', 'Lower Back Pain / Lumbar Disc Vulnerability', 'Discomfort or history of herniated disc in lumbar spine. Requires avoidance of heavy axial spinal loading and unsupported spinal flexion.', 'Lower Back', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(2, 'KNEE_PAIN', 'Knee Pain / Meniscus / Patellofemoral', 'Chondromalacia, meniscus tear, or patellar tendinitis. Requires avoiding deep acute knee angles and high-impact jumping.', 'Knees', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(3, 'SHOULDER_IMPINGEMENT', 'Shoulder Impingement / Rotator Cuff', 'Subacromial impingement or supraspinatus strain. Requires avoiding extreme overhead pressing with internal rotation.', 'Shoulders', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(4, 'WRIST_TENDINITIS', 'Wrist Pain / Carpal Tunnel / Tendinitis', 'Wrist extension strain under heavy barbell loads. Requires neutral-grip or EZ-bar substitutions.', 'Wrists', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(5, 'CERVICAL_NECK_STRAIN', 'Neck / Cervical Spine Tension', 'Upper trap stiffness or forward head posture strain. Requires avoiding heavy shrugs and behind-the-neck movements.', 'Neck', '2026-10-14 21:38:29', '2026-10-14 21:38:29'),
(6, 'ANKLE_INSTABILITY', 'Ankle Mobility Limitation / Instability', 'Limited dorsiflexion or chronic sprains. Requires elevated heel or stable machine variants.', 'Ankles', '2026-10-14 21:38:29', '2026-10-14 21:38:29');

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `id` varchar(255) NOT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` text DEFAULT NULL,
  `payload` longtext NOT NULL,
  `last_activity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`id`, `user_id`, `ip_address`, `user_agent`, `payload`, `last_activity`) VALUES
('5zhRlB6v70185FUNYdDCbxRz29HEpHwZW3ZIHYP7', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiUW5XRFNhblZRd2M1OTBLc3hVWHZCUENaN3dBM0Z3TTJndFFyTWhISSI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792483002),
('BiRh9mIgUJbrcu91NdcOCWiBj8nLO98ENUl70D9x', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiUFIzRnpkZVNEelh5a3hKWDdSSjBCSGFWWE1qUlB3dm14YjdORHlEaCI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvY3JlYXRlIjtzOjU6InJvdXRlIjtzOjIyOiJhZG1pbi5leGVyY2lzZXMuY3JlYXRlIjt9czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319fQ==', 1792474705),
('drSMzwrHnhf0XjJDNEPVi4pCUmtAsxB9dLbtSr2J', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiWGZaenJqdW8wOVU0ZWhTTDZoSHRXZlhBR0czM1E4aFQxU3E5UzdZTyI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvY3JlYXRlIjtzOjU6InJvdXRlIjtzOjIyOiJhZG1pbi5leGVyY2lzZXMuY3JlYXRlIjt9czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319fQ==', 1792405532),
('eLJ9Ua7BSqaCoQRhWlECw7UnJlouhTKWVKHzZwBv', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiR0ZWTXpSWDhNWlhqNUl4dzV5WTRaR0tIUGV1UkpMRXpydmR6MkpqeiI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792483022),
('fx2DRDYoJRXGd0tr2Sz9y1wcGuCKtx8srDVxIu0k', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoieW1aeUxyU2FnT2h0dENLSzZ6V1lDaHlVdGh3dTVYc2JvekN4c3FBcSI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792483014),
('iOHXfkfeO8GLZx7OQYmfvivmyWUh8NWS1lYtO7Cj', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoia0J6b1YzYmxCZFY4Y3pvR0IzcUdlallFZU9NU2ZGd21wQ1NCQVVJVyI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6Mzk6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMSI7czo1OiJyb3V0ZSI7czoyMDoiYWRtaW4uZXhlcmNpc2VzLnNob3ciO31zOjY6Il9mbGFzaCI7YToyOntzOjM6Im9sZCI7YTowOnt9czozOiJuZXciO2E6MDp7fX19', 1792483036),
('kiooICzUznXmQWYWknWo7dnYiXic8A781GOHmqD8', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoic3ZlcXUzdnNYNXhRTVF6RlU5MHhIQkF5TllYeWdORkNkSWQ2Zng5UiI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792482973),
('mOJkv96YyP1BIyNEle41l1UK0zx6XgPIj4yuOwd8', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoid09uc1o4MmJaWkhSUkdUVVg2d1hDcWtwelh1bVE5TGJoZHFQYnR2cCI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792482966),
('rhYAsC9s8mb5EqshXS25s3Y1k3RN7zcLJO0QMQs6', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiRGFmTlJVR2pNZGNPQURmYktSUFRuODM2TTlpYmVKSlRUcGpSTzZ2SyI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792482982),
('vGoG6SitKw3CgxGh1HarlXwDJdxj41PdktHWlcXe', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiWVdNamZLeHExcm42UFdPQUY2bjA0REVOWXd2NXZjWWdrSGZaVXlkdCI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvMS9lZGl0IjtzOjU6InJvdXRlIjtzOjIwOiJhZG1pbi5leGVyY2lzZXMuZWRpdCI7fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792483029),
('Z0KIiy4WDl8petZ3nquOa7Y4LIpNCs1BthvA4QuZ', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiTXJyaW9oVGJvNWtJSGg1SGo5cU0yQ1BJUHE1akdsMVRHUTY4TjBlYyI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDQ6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hZG1pbi9leGVyY2lzZXMvY3JlYXRlIjtzOjU6InJvdXRlIjtzOjIyOiJhZG1pbi5leGVyY2lzZXMuY3JlYXRlIjt9czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319fQ==', 1792483036),
('ZI91bqfqkAseoWuo7n7acxVwpHIY3AMDw0wTl2dy', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; en-US) WindowsPowerShell/5.1.26100.9711', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiMVZHM3BlTElram42dWR5Smlta2FHTUdRNWc3aXRQeDJkMXVRdVc1RSI7czo5OiJfcHJldmlvdXMiO2E6Mjp7czozOiJ1cmwiO3M6NDY6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMC9hc3NldHMvcHVzaHVwX3Byb3Blci5naWYiO3M6NToicm91dGUiO047fXM6NjoiX2ZsYXNoIjthOjI6e3M6Mzoib2xkIjthOjA6e31zOjM6Im5ldyI7YTowOnt9fX0=', 1792398819);

-- --------------------------------------------------------

--
-- Table structure for table `step_logs`
--

CREATE TABLE `step_logs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `log_date` date NOT NULL,
  `step_count` int(11) NOT NULL DEFAULT 0,
  `distance_km` decimal(6,2) NOT NULL DEFAULT 0.00,
  `calories_burned` decimal(6,2) NOT NULL DEFAULT 0.00,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('app_user','admin') NOT NULL DEFAULT 'app_user',
  `remember_token` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `email_verified_at`, `password`, `role`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'System Coach / Admin', 'admin@personafit.com', NULL, '$2y$12$GH7oHHTYOZ4lyNBtPl0T..Ndo5B1bi.eGNNMTRyYpraTnfJ5XiPM6', 'admin', NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(2, 'Ralph Mobile User', 'ralph@example.com', NULL, '$2y$12$aQFkvaKB/fXs1ywT9h7R1u/ffJtKijC6gq9bUgRT8m3U7wkFXH9M.', 'app_user', NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(3, 'magno dendrin', 'dendrin@gmail.com', NULL, '$2y$12$JKDFxItjBxgYpajnZA0zReUvXBxX70GR0tMf4XumUTqk8bHIuaI5i', 'app_user', NULL, '2026-10-14 21:52:34', '2026-10-14 21:52:34'),
(4, 'Andrie Luiz Rosete', 'lulu@gmail.com', NULL, '$2y$12$D8C45ydp5uP8p0HHT9NU.OQosY3ikMoT/n3tewATMAE9nubAjOVde', 'app_user', NULL, '2026-10-20 00:08:27', '2026-10-20 00:08:27');

-- --------------------------------------------------------

--
-- Table structure for table `user_limitations`
--

CREATE TABLE `user_limitations` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `limitation_id` bigint(20) UNSIGNED NOT NULL,
  `severity` enum('mild','moderate','severe') NOT NULL DEFAULT 'moderate',
  `notes` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user_limitations`
--

INSERT INTO `user_limitations` (`id`, `user_id`, `limitation_id`, `severity`, `notes`, `created_at`, `updated_at`) VALUES
(1, 2, 1, 'moderate', 'Twinge in lumbar during axial spinal loads.', '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(2, 1, 1, 'moderate', NULL, '2026-10-19 01:42:24', '2026-10-19 01:42:24');

-- --------------------------------------------------------

--
-- Table structure for table `user_profiles`
--

CREATE TABLE `user_profiles` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `gender` enum('male','female') NOT NULL,
  `age` int(11) NOT NULL,
  `height_cm` decimal(5,2) NOT NULL,
  `weight_kg` decimal(5,2) NOT NULL,
  `target_weight_kg` decimal(5,2) DEFAULT NULL,
  `activity_level` enum('sedentary','light','moderate','very_active') NOT NULL DEFAULT 'sedentary',
  `fitness_goal` enum('fat_loss','maintenance','muscle_gain') NOT NULL DEFAULT 'maintenance',
  `available_days` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`available_days`)),
  `training_experience` varchar(50) DEFAULT 'intermediate',
  `exercise_type` varchar(50) DEFAULT 'compound',
  `workout_location` varchar(50) DEFAULT 'gym',
  `joint_health_status` varchar(50) DEFAULT 'No Limitations',
  `joint_limitations` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`joint_limitations`)),
  `persona_archetype` enum('busy_pro','gentle_starter','home_warrior','athlete') NOT NULL DEFAULT 'gentle_starter',
  `daily_calories` int(11) NOT NULL DEFAULT 2000,
  `protein_grams` int(11) NOT NULL DEFAULT 150,
  `carbs_grams` int(11) NOT NULL DEFAULT 200,
  `fat_grams` int(11) NOT NULL DEFAULT 60,
  `water_ml_target` int(11) NOT NULL DEFAULT 2500,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user_profiles`
--

INSERT INTO `user_profiles` (`id`, `user_id`, `gender`, `age`, `height_cm`, `weight_kg`, `target_weight_kg`, `activity_level`, `fitness_goal`, `available_days`, `training_experience`, `exercise_type`, `workout_location`, `joint_health_status`, `joint_limitations`, `persona_archetype`, `daily_calories`, `protein_grams`, `carbs_grams`, `fat_grams`, `water_ml_target`, `created_at`, `updated_at`) VALUES
(1, 2, 'male', 25, 178.00, 78.50, 74.00, 'moderate', 'fat_loss', '[\"Monday\",\"Tuesday\",\"Wednesday\",\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\"]', 'intermediate', 'compound', 'gym', 'No Limitations', NULL, 'busy_pro', 2150, 155, 230, 65, 2800, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(2, 3, 'male', 24, 173.00, 65.00, 60.00, 'moderate', 'fat_loss', '[\"Monday\",\"Thursday\",\"Tuesday\"]', 'beginner', 'compound', 'home', 'No Limitations', '[]', 'home_warrior', 2005, 130, 246, 56, 2775, '2026-10-14 21:53:39', '2026-10-19 21:42:17'),
(3, 4, 'male', 18, 178.00, 50.00, 67.00, 'moderate', 'maintenance', '[\"Monday\",\"Wednesday\",\"Friday\",\"Tuesday\",\"Thursday\",\"Saturday\"]', 'intermediate', 'compound', 'gym', 'No Limitations', '[]', 'busy_pro', 2368, 100, 344, 66, 2250, '2026-10-20 00:11:54', '2026-10-20 00:11:54');

-- --------------------------------------------------------

--
-- Table structure for table `water_logs`
--

CREATE TABLE `water_logs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `log_date` date NOT NULL,
  `amount_ml` int(11) NOT NULL DEFAULT 250,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `water_logs`
--

INSERT INTO `water_logs` (`id`, `user_id`, `log_date`, `amount_ml`, `created_at`, `updated_at`) VALUES
(1, 3, '2026-10-15', 346, '2026-10-15 00:21:12', '2026-10-15 00:21:12'),
(2, 3, '2026-10-15', 346, '2026-10-15 00:21:14', '2026-10-15 00:21:14'),
(3, 3, '2026-10-15', 346, '2026-10-15 00:21:22', '2026-10-15 00:21:22'),
(4, 3, '2026-10-15', 346, '2026-10-15 00:21:24', '2026-10-15 00:21:24'),
(5, 3, '2026-10-15', 346, '2026-10-15 00:21:25', '2026-10-15 00:21:25'),
(6, 3, '2026-10-15', 346, '2026-10-15 00:21:25', '2026-10-15 00:21:25'),
(7, 3, '2026-10-15', 346, '2026-10-15 00:21:26', '2026-10-15 00:21:26'),
(8, 3, '2026-10-15', 346, '2026-10-15 00:21:27', '2026-10-15 00:21:27');

-- --------------------------------------------------------

--
-- Table structure for table `workout_logs`
--

CREATE TABLE `workout_logs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `workout_plan_id` bigint(20) UNSIGNED DEFAULT NULL,
  `session_name` varchar(150) NOT NULL DEFAULT 'Workout Session',
  `started_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `completed_at` timestamp NULL DEFAULT NULL,
  `total_duration_seconds` int(11) NOT NULL DEFAULT 0,
  `status` enum('in_progress','completed','abandoned') NOT NULL DEFAULT 'completed',
  `total_exercises_completed` int(11) NOT NULL DEFAULT 0,
  `total_volume_kg` decimal(8,2) NOT NULL DEFAULT 0.00,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `workout_logs`
--

INSERT INTO `workout_logs` (`id`, `user_id`, `workout_plan_id`, `session_name`, `started_at`, `completed_at`, `total_duration_seconds`, `status`, `total_exercises_completed`, `total_volume_kg`, `notes`, `created_at`, `updated_at`) VALUES
(1, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:21:35', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-18 22:21:35', '2026-10-18 22:21:35'),
(2, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:21:36', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-18 22:21:36', '2026-10-18 22:21:36'),
(3, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:26:41', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-18 22:26:41', '2026-10-18 22:26:41'),
(4, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:26:41', '2026-10-18 22:27:32', 49, 'completed', 3, 0.00, NULL, '2026-10-18 22:26:41', '2026-10-18 22:27:32'),
(5, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:27:52', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-18 22:27:52', '2026-10-18 22:27:52'),
(6, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:27:52', '2026-10-18 22:28:30', 37, 'completed', 1, 0.00, NULL, '2026-10-18 22:27:52', '2026-10-18 22:28:30'),
(7, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:40:05', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-18 22:40:05', '2026-10-18 22:40:05'),
(8, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:40:05', '2026-10-18 22:41:20', 74, 'completed', 1, 0.00, NULL, '2026-10-18 22:40:05', '2026-10-18 22:41:20'),
(9, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:44:19', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-18 22:44:19', '2026-10-18 22:44:19'),
(10, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-18 22:44:19', '2026-10-18 22:53:59', 575, 'completed', 1, 0.00, NULL, '2026-10-18 22:44:19', '2026-10-18 22:53:59'),
(11, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 00:01:07', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 00:01:07', '2026-10-19 00:01:07'),
(12, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 00:01:07', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 00:01:07', '2026-10-19 00:01:07'),
(13, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 00:05:32', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 00:05:32', '2026-10-19 00:05:32'),
(14, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 00:05:32', '2026-10-19 00:17:45', 729, 'completed', 3, 0.00, NULL, '2026-10-19 00:05:32', '2026-10-19 00:17:45'),
(15, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 00:46:08', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 00:46:08', '2026-10-19 00:46:08'),
(16, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 00:46:08', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 00:46:08', '2026-10-19 00:46:08'),
(17, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 01:25:28', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 01:25:28', '2026-10-19 01:25:28'),
(18, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 01:25:28', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 01:25:28', '2026-10-19 01:25:28'),
(19, 3, 4, 'Gentle Starter - Day 1: Low-Impact Foundation (Workout Day 1 of 3)', '2026-10-19 02:34:53', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 02:34:53', '2026-10-19 02:34:53'),
(20, 3, 4, 'Gentle Starter - Day 1: Low-Impact Foundation (Workout Day 1 of 3)', '2026-10-19 02:34:53', '2026-10-19 02:36:16', 81, 'completed', 4, 0.00, NULL, '2026-10-19 02:34:53', '2026-10-19 02:36:16'),
(21, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 02:37:07', NULL, 0, 'in_progress', 0, 0.00, NULL, '2026-10-19 02:37:07', '2026-10-19 02:37:07'),
(22, 3, 6, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split (Workout Day 1 of 3)', '2026-10-19 02:37:07', '2026-10-19 02:37:50', 42, 'completed', 3, 0.00, NULL, '2026-10-19 02:37:07', '2026-10-19 02:37:50');

-- --------------------------------------------------------

--
-- Table structure for table `workout_log_items`
--

CREATE TABLE `workout_log_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `workout_log_id` bigint(20) UNSIGNED NOT NULL,
  `exercise_id` bigint(20) UNSIGNED NOT NULL,
  `set_number` int(11) NOT NULL DEFAULT 1,
  `weight_kg` decimal(5,2) NOT NULL DEFAULT 0.00,
  `reps_completed` int(11) NOT NULL DEFAULT 0,
  `is_completed` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `workout_log_items`
--

INSERT INTO `workout_log_items` (`id`, `workout_log_id`, `exercise_id`, `set_number`, `weight_kg`, `reps_completed`, `is_completed`, `created_at`, `updated_at`) VALUES
(1, 4, 1, 1, 0.00, 10, 1, '2026-10-18 22:27:32', '2026-10-18 22:27:32'),
(2, 4, 4, 1, 0.00, 10, 1, '2026-10-18 22:27:32', '2026-10-18 22:27:32'),
(3, 4, 3, 1, 0.00, 10, 1, '2026-10-18 22:27:32', '2026-10-18 22:27:32'),
(4, 6, 1, 1, 0.00, 10, 1, '2026-10-18 22:28:30', '2026-10-18 22:28:30'),
(5, 8, 1, 1, 0.00, 10, 1, '2026-10-18 22:41:20', '2026-10-18 22:41:20'),
(6, 10, 1, 1, 0.00, 10, 1, '2026-10-18 22:53:59', '2026-10-18 22:53:59'),
(7, 14, 1, 1, 0.00, 10, 1, '2026-10-19 00:17:45', '2026-10-19 00:17:45'),
(8, 14, 4, 1, 0.00, 10, 1, '2026-10-19 00:17:45', '2026-10-19 00:17:45'),
(9, 14, 3, 1, 0.00, 10, 1, '2026-10-19 00:17:45', '2026-10-19 00:17:45'),
(10, 20, 10, 1, 0.00, 10, 1, '2026-10-19 02:36:16', '2026-10-19 02:36:16'),
(11, 20, 11, 1, 0.00, 10, 1, '2026-10-19 02:36:16', '2026-10-19 02:36:16'),
(12, 20, 3, 1, 0.00, 10, 1, '2026-10-19 02:36:16', '2026-10-19 02:36:16'),
(13, 20, 5, 1, 0.00, 10, 1, '2026-10-19 02:36:16', '2026-10-19 02:36:16'),
(14, 22, 1, 1, 0.00, 10, 1, '2026-10-19 02:37:50', '2026-10-19 02:37:50'),
(15, 22, 4, 1, 0.00, 10, 1, '2026-10-19 02:37:50', '2026-10-19 02:37:50'),
(16, 22, 3, 1, 0.00, 10, 1, '2026-10-19 02:37:50', '2026-10-19 02:37:50');

-- --------------------------------------------------------

--
-- Table structure for table `workout_plans`
--

CREATE TABLE `workout_plans` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `persona_archetype` enum('busy_pro','gentle_starter','home_warrior','athlete') NOT NULL,
  `day_of_week` int(11) NOT NULL DEFAULT 1,
  `focus_area` varchar(100) NOT NULL,
  `estimated_duration_minutes` int(11) NOT NULL DEFAULT 35,
  `is_template` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `workout_plans`
--

INSERT INTO `workout_plans` (`id`, `user_id`, `name`, `persona_archetype`, `day_of_week`, `focus_area`, `estimated_duration_minutes`, `is_template`, `created_at`, `updated_at`) VALUES
(1, NULL, 'Busy Pro - Day 1: Upper Body & Posture', 'busy_pro', 1, 'Upper Body & Posture Alignment', 30, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(2, NULL, 'Busy Pro - Day 2: Lower Body & Core', 'busy_pro', 3, 'Lower Body Strength & Core', 30, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(3, NULL, 'Busy Pro - Day 3: Full Body Conditioning', 'busy_pro', 5, 'Full Body Efficiency Split', 30, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(4, NULL, 'Gentle Starter - Day 1: Low-Impact Foundation', 'gentle_starter', 1, 'Joint Mobility & Low Impact Movement', 25, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(5, NULL, 'Gentle Starter - Day 2: Posture & Core Stability', 'gentle_starter', 3, 'Gentle Calisthenics & Core', 25, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(6, NULL, 'Home Warrior - Day 1: Calisthenics & Dumbbell Split', 'home_warrior', 1, 'Home Dumbbell & Bodyweight Conditioning', 35, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(7, NULL, 'Home Warrior - Day 2: Upper Pull & Lower Unilateral', 'home_warrior', 3, 'Unilateral Balance & Calisthenic Pull', 35, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(8, NULL, 'Dedicated Athlete - Day 1: Push Hypertrophy', 'athlete', 1, 'Chest, Delts & Triceps Intensity', 50, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(9, NULL, 'Dedicated Athlete - Day 2: Pull Hypertrophy', 'athlete', 2, 'Back, Rear Delts & Biceps Intensity', 50, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(10, NULL, 'Dedicated Athlete - Day 3: Lower Body Power', 'athlete', 4, 'Quad, Hamstring & Glute Power', 50, 1, '2026-10-14 21:38:30', '2026-10-14 21:38:30');

-- --------------------------------------------------------

--
-- Table structure for table `workout_plan_exercises`
--

CREATE TABLE `workout_plan_exercises` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `workout_plan_id` bigint(20) UNSIGNED NOT NULL,
  `exercise_id` bigint(20) UNSIGNED NOT NULL,
  `order_in_routine` int(11) NOT NULL DEFAULT 1,
  `target_sets` int(11) NOT NULL DEFAULT 3,
  `target_reps_min` int(11) NOT NULL DEFAULT 8,
  `target_reps_max` int(11) NOT NULL DEFAULT 12,
  `rest_seconds` int(11) NOT NULL DEFAULT 60,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `workout_plan_exercises`
--

INSERT INTO `workout_plan_exercises` (`id`, `workout_plan_id`, `exercise_id`, `order_in_routine`, `target_sets`, `target_reps_min`, `target_reps_max`, `rest_seconds`, `notes`, `created_at`, `updated_at`) VALUES
(1, 1, 27, 1, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(2, 1, 25, 2, 3, 8, 10, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(3, 1, 30, 3, 3, 12, 15, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(4, 1, 16, 4, 3, 12, 15, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(5, 2, 12, 1, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(6, 2, 29, 2, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(7, 2, 5, 3, 3, 12, 15, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(8, 2, 7, 4, 3, 15, 20, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(9, 3, 13, 1, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(10, 3, 11, 2, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(11, 3, 28, 3, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(12, 3, 15, 4, 3, 10, 12, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(13, 4, 10, 1, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(14, 4, 11, 2, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(15, 4, 3, 3, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(16, 4, 5, 4, 2, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(17, 5, 2, 1, 3, 8, 10, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(18, 5, 4, 2, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(19, 5, 8, 3, 3, 15, 20, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(20, 5, 7, 4, 2, 12, 15, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(21, 6, 1, 1, 3, 10, 15, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(22, 6, 4, 2, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(23, 6, 3, 3, 3, 12, 15, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(24, 6, 5, 4, 3, 15, 20, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(25, 7, 21, 1, 3, 8, 10, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(26, 7, 19, 2, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(27, 7, 20, 3, 3, 10, 12, 45, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(28, 7, 23, 4, 3, 10, 12, 30, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(29, 8, 25, 1, 4, 6, 8, 90, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(30, 8, 45, 2, 3, 8, 10, 90, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(31, 8, 32, 3, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(32, 8, 16, 4, 3, 12, 15, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(33, 9, 44, 1, 3, 5, 5, 120, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(34, 9, 37, 2, 4, 6, 8, 90, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(35, 9, 27, 3, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(36, 9, 33, 4, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(37, 10, 26, 1, 4, 6, 8, 120, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(38, 10, 48, 2, 3, 8, 10, 90, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(39, 10, 29, 3, 3, 8, 10, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30'),
(40, 10, 47, 4, 3, 10, 12, 60, NULL, '2026-10-14 21:38:30', '2026-10-14 21:38:30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cache`
--
ALTER TABLE `cache`
  ADD PRIMARY KEY (`key`),
  ADD KEY `cache_expiration_index` (`expiration`);

--
-- Indexes for table `cache_locks`
--
ALTER TABLE `cache_locks`
  ADD PRIMARY KEY (`key`),
  ADD KEY `cache_locks_expiration_index` (`expiration`);

--
-- Indexes for table `exercises`
--
ALTER TABLE `exercises`
  ADD PRIMARY KEY (`id`),
  ADD KEY `exercises_is_active_index` (`is_active`);

--
-- Indexes for table `exercise_contraindications`
--
ALTER TABLE `exercise_contraindications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ex_contra_unique` (`exercise_id`,`limitation_id`),
  ADD KEY `exercise_contraindications_limitation_id_foreign` (`limitation_id`),
  ADD KEY `exercise_contraindications_alternative_exercise_id_foreign` (`alternative_exercise_id`);

--
-- Indexes for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indexes for table `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `jobs_queue_index` (`queue`);

--
-- Indexes for table `job_batches`
--
ALTER TABLE `job_batches`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `meal_logs`
--
ALTER TABLE `meal_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `meal_logs_user_id_foreign` (`user_id`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`email`);

--
-- Indexes for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  ADD KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`),
  ADD KEY `personal_access_tokens_expires_at_index` (`expires_at`);

--
-- Indexes for table `physical_limitations`
--
ALTER TABLE `physical_limitations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `physical_limitations_code_unique` (`code`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sessions_user_id_index` (`user_id`),
  ADD KEY `sessions_last_activity_index` (`last_activity`);

--
-- Indexes for table `step_logs`
--
ALTER TABLE `step_logs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `step_logs_user_id_log_date_unique` (`user_id`,`log_date`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- Indexes for table `user_limitations`
--
ALTER TABLE `user_limitations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_limitations_user_id_limitation_id_unique` (`user_id`,`limitation_id`),
  ADD KEY `user_limitations_limitation_id_foreign` (`limitation_id`);

--
-- Indexes for table `user_profiles`
--
ALTER TABLE `user_profiles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_profiles_user_id_foreign` (`user_id`);

--
-- Indexes for table `water_logs`
--
ALTER TABLE `water_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `water_logs_user_id_foreign` (`user_id`);

--
-- Indexes for table `workout_logs`
--
ALTER TABLE `workout_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `workout_logs_user_id_foreign` (`user_id`),
  ADD KEY `workout_logs_workout_plan_id_foreign` (`workout_plan_id`);

--
-- Indexes for table `workout_log_items`
--
ALTER TABLE `workout_log_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `workout_log_items_workout_log_id_foreign` (`workout_log_id`),
  ADD KEY `workout_log_items_exercise_id_foreign` (`exercise_id`);

--
-- Indexes for table `workout_plans`
--
ALTER TABLE `workout_plans`
  ADD PRIMARY KEY (`id`),
  ADD KEY `workout_plans_user_id_foreign` (`user_id`);

--
-- Indexes for table `workout_plan_exercises`
--
ALTER TABLE `workout_plan_exercises`
  ADD PRIMARY KEY (`id`),
  ADD KEY `workout_plan_exercises_workout_plan_id_foreign` (`workout_plan_id`),
  ADD KEY `workout_plan_exercises_exercise_id_foreign` (`exercise_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `exercises`
--
ALTER TABLE `exercises`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `exercise_contraindications`
--
ALTER TABLE `exercise_contraindications`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jobs`
--
ALTER TABLE `jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `meal_logs`
--
ALTER TABLE `meal_logs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `physical_limitations`
--
ALTER TABLE `physical_limitations`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `step_logs`
--
ALTER TABLE `step_logs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user_limitations`
--
ALTER TABLE `user_limitations`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_profiles`
--
ALTER TABLE `user_profiles`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `water_logs`
--
ALTER TABLE `water_logs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `workout_logs`
--
ALTER TABLE `workout_logs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `workout_log_items`
--
ALTER TABLE `workout_log_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `workout_plans`
--
ALTER TABLE `workout_plans`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `workout_plan_exercises`
--
ALTER TABLE `workout_plan_exercises`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `exercise_contraindications`
--
ALTER TABLE `exercise_contraindications`
  ADD CONSTRAINT `exercise_contraindications_alternative_exercise_id_foreign` FOREIGN KEY (`alternative_exercise_id`) REFERENCES `exercises` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `exercise_contraindications_exercise_id_foreign` FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `exercise_contraindications_limitation_id_foreign` FOREIGN KEY (`limitation_id`) REFERENCES `physical_limitations` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `meal_logs`
--
ALTER TABLE `meal_logs`
  ADD CONSTRAINT `meal_logs_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `step_logs`
--
ALTER TABLE `step_logs`
  ADD CONSTRAINT `step_logs_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `user_limitations`
--
ALTER TABLE `user_limitations`
  ADD CONSTRAINT `user_limitations_limitation_id_foreign` FOREIGN KEY (`limitation_id`) REFERENCES `physical_limitations` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_limitations_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `user_profiles`
--
ALTER TABLE `user_profiles`
  ADD CONSTRAINT `user_profiles_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `water_logs`
--
ALTER TABLE `water_logs`
  ADD CONSTRAINT `water_logs_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `workout_logs`
--
ALTER TABLE `workout_logs`
  ADD CONSTRAINT `workout_logs_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `workout_logs_workout_plan_id_foreign` FOREIGN KEY (`workout_plan_id`) REFERENCES `workout_plans` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `workout_log_items`
--
ALTER TABLE `workout_log_items`
  ADD CONSTRAINT `workout_log_items_exercise_id_foreign` FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `workout_log_items_workout_log_id_foreign` FOREIGN KEY (`workout_log_id`) REFERENCES `workout_logs` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `workout_plans`
--
ALTER TABLE `workout_plans`
  ADD CONSTRAINT `workout_plans_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `workout_plan_exercises`
--
ALTER TABLE `workout_plan_exercises`
  ADD CONSTRAINT `workout_plan_exercises_exercise_id_foreign` FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `workout_plan_exercises_workout_plan_id_foreign` FOREIGN KEY (`workout_plan_id`) REFERENCES `workout_plans` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
