# PersonaFit - System Features Specification

> **PersonaFit**: An injury-screened, persona-driven smart fitness & nutrition mobile application with a Laravel 11 backend, native Android (Jetpack Compose) frontend, and a web admin portal.

---

## 1. Authentication & Session Management

* **User Registration**:
  * Name, email, and password registration with form validation.
  * Automatic generation of initial user profile record.
  * Direct handoff to the Onboarding Assessment Wizard for first-time users.
* **User Login**:
  * Secure credential validation via **Laravel Sanctum** token-based authentication.
  * Intelligent session routing: Directs returning users with completed profiles to the **Motivational Welcome Screen**, and incomplete profiles to the **Onboarding Wizard**.
* **Motivational Post-Login Welcome Screen**:
  * Personalized greeting with user's name and behavioral persona badge.
  * Breathing glow-pulse animation on the Electric Mint barbell emblem.
  * Curated rotating motivational fitness & discipline quotes.
  * 3-second auto-forward timer with instant "Let's Go ⚡" bypass CTA.
* **Token & Session Persistence**:
  * Local token storage using encrypted `SessionManager` (SharedPreferences).
  * Auto-login on app relaunch via Splash Screen session verification.
  * Complete session clearing on **Log Out** with navigation back-stack clearance.

---

## 2. Onboarding & Holistic Assessment Wizard

A streamlined 2-step interactive assessment that collects biometrics, workout preferences, weekly availability, and orthopedic joint health:

* **Step 1 of 2: Biometrics, Goal & Workout Preferences**:
  * Biological sex selection (Male / Female for Mifflin-St Jeor metabolic formulas).
  * Age, height (cm), current weight (kg), and optional target weight (kg).
  * Primary fitness objective selection (*Fat Loss*, *Maintenance*, *Muscle Hypertrophy*).
  * Exercise Type preference (*Compound Exercise* vs. *Isolation Exercise*).
  * Workout Location (*Commercial Gym* vs. *Home Workout*).
  * **Weekly Workout Availability**:
    * Multi-select day cards for Monday through Sunday.
    * Persisted as `available_days` to drive the rule-based schedule engine.
  * **Strength Training Experience**:
    * Single-select experience level: *Beginner (< 6 months)*, *Intermediate (6 months - 2 years)*, or *Advanced (2+ years)*.
* **Step 2 of 2: Joint Health & Injury Prevention**:
  * **PersonaFit Safety Rules** card with medical disclaimer.
  * **Mutually Exclusive "No Limitations" option**:
    * `"I have NO joint pain or physical limitations"` (unchecks all limitations, sets `joint_health_status = "No Limitations"`).
  * **Multi-select Joint Limitation Checkboxes**:
    * `Lower Back Pain / Lumbar Disc Vulnerability`
    * `Knee Pain / Meniscus / Patellofemoral`
    * `Shoulder Impingement / Rotator Cuff`
    * `Wrist Pain / Carpal Tunnel / Tendinitis`
  * Automatically maps joint limits to biomechanical contraindication rules.
  * Submission via **"Finish Assessment →"** with validation and instant transition to recommendation summary.
* **Rule-Based Workout Scheduling**:
  * The system strictly schedules workouts on the user's selected `available_days`.
  * Generates an N-day schedule (e.g., 3 days: Monday → Day 1, Wednesday → Day 2, Friday → Day 3).
  * Automatically marks unselected days as designated **Rest & Active Recovery** days with hydration and mobility cues.
* **Assessment Summary & Persona Reveal**:
  * Post-onboarding results screen revealing:
    * Assigned **Behavioral Persona Archetype** with customized training strategy.
    * Daily caloric target (BMR & TDEE calculated).
    * Macronutrient distribution (Protein, Carbohydrates, Fats in grams).
    * Daily hydration target (ml).
    * Active injury safety filters summary.

---

## 3. Deterministic Prescription & Classification Engine (Backend Services)

* **Behavioral Persona Classification (`PersonaClassificationService`)**:
  * Classifies users into one of four archetypes based on time availability and lifestyle:
    1. **The Busy Professional**: High-density 3-day superset split focused on posture and efficiency.
    2. **The Gentle Starter**: Low-impact, habit-building split focused on movement mechanics.
    3. **The Home Warrior**: Minimal-equipment dumbbell and bodyweight progression split.
    4. **The Focused Athlete**: Progressive overload split with comprehensive compound lifting.
* **Clinical Nutritional Calculator (`NutritionCalculatorService`)**:
  * **BMR Calculation**: Mifflin-St Jeor formula utilizing biological sex, age, weight, and height.
  * **TDEE Calculation**: Activity factor multiplier ($1.2\times$ to $1.9\times$).
  * **Calorie Goal Adjustment**: $-500\text{ kcal}$ for Fat Loss, $+300\text{ kcal}$ for Muscle Gain, $\pm 0\text{ kcal}$ for Maintenance.
  * **Macro Partitioning**: Protein ($1.8\text{g} - 2.2\text{g}/\text{kg}$), Fat ($25\% - 30\%$ of total calories), remaining calories allocated to complex carbohydrates.
  * **Hydration Baseline**: $35\text{ml}/\text{kg}$ of body weight.
* **Orthopedic Injury Prescription Engine (`InjuryPrescriptionEngine`)**:
  * **Dynamic Contraindication Filter**: Evaluates active user limitations and automatically excludes hazardous movements (e.g. excludes axial-loaded Barbell Squats for lower back issues).
  * **Rule-Based Safe Substitution**: Replaces contraindicated exercises with biomechanically safe alternatives (e.g. Chest-Supported DB Rows instead of Bent-Over Barbell Rows).
  * **Live Swap Engine**: Provides real-time substitute recommendations filtered by active limitations.

---

## 4. Main Dashboard (Central Health Hub)

* **Glanceable Header**:
  * User greeting and dynamic streak counter (🔥).
  * Active Behavioral Persona badge.
* **Today's Routine Action Card**:
  * Displays today's prescribed routine title, exercise count, duration, and safety status.
  * Direct one-tap launch into the routine overview and active workout mode.
* **Daily Calorie Energy Gauge**:
  * Circular progress ring tracking consumed vs. target daily calories.
  * Remaining calories breakdown.
* **Hardware Pedometer Card**:
  * Real-time step counter syncing with the phone's native hardware sensor.
  * Distance covered (km) and estimated active calories burned.
* **Hydration Quick-Add Bar**:
  * Daily water intake progress bar.
  * Quick-add action buttons (`+250ml Glass`, `+500ml Bottle`).
* **Bottom Navigation Bar**:
  * Fast navigation between **Dashboard**, **Workout**, **Nutrition**, and **Progress** screens.

---

## 5. Workout Prescription & Live Gym Execution Mode

* **Today's Routine Overview Screen**:
  * Routine metadata (focus muscle group, estimated duration, persona alignment).
  * Injury safety banner showing count and details of applied orthopedic substitutions.
  * Prescribed movements list displaying target sets, rep ranges, rest intervals, and target muscle tags.
  * Form guide modal trigger for each exercise.
* **Active Workout Execution Screen (Live Gym Mode)**:
  * **Live Stopwatch**: Real-time session elapsed timer (`MM:SS`).
  * **Granular Set Logging Table**:
    * Numerical inputs for Weight (kg) and Reps completed per set.
    * Completion toggle checkmark per set.
    * Automatic backend persistence via `POST /api/workouts/log-set`.
  * **3ft-Glanceable Rest Countdown Overlay**:
    * Auto-triggers upon completing a set.
    * Extra-large $48\text{sp}$ countdown digits visible from distance while resting.
    * Quick controls: `+30s` extension button and `Skip Rest` button.
  * **Live Exercise Swap Modal (Bottom Sheet)**:
    * One-tap swap button on the active exercise card.
    * Fetches and displays safe alternative movements with injury prevention tags.
    * Instantly replaces the current exercise in the active session without losing elapsed time.
  * **Exercise Form & Biomechanical Guide Dialog**:
    * Step-by-step posture and execution instructions.
    * Highlighted injury prevention cues (Spine alignment, joint stability).
    * Common biomechanical mistakes to avoid.
  * **Workout Completion Celebration**:
    * Confirmation modal to prevent accidental session termination.
    * Celebratory completion dialog displaying total duration, volume lifted (kg), and completed exercises.
    * Direct "Return to Dashboard" action.

---

## 6. Nutrition & Hydration Tracker

* **Daily Macronutrient Balance**:
  * Total consumed calories vs. daily target limit.
  * Visual progress bars for Protein (g), Carbohydrates (g), and Fats (g).
* **Meal Logger Module**:
  * Log Meal modal supporting Meal Type (`Breakfast`, `Lunch`, `Dinner`, `Snack`).
  * Inputs for Meal Name, Calories (kcal), Protein (g), Carbs (g), and Fat (g).
  * Chronological list of logged meals with macro summaries.
* **Water Intake Tracker**:
  * Current intake vs. daily goal progress tracker.
  * Quick-log buttons (`+250ml`, `+500ml`).

---

## 7. Progress, Analytics & BMI Module

* **BMI Assessment**:
  * Dynamic BMI calculation based on current height and weight.
  * Category indicator (`Underweight`, `Normal Weight`, `Overweight`, `Obese`).
* **Weekly Workout Adherence**:
  * Visual weekly adherence indicator ([M][T][W][T][F][S][S]).
  * Percentage consistency score.
* **7-Day Hardware Step Trend**:
  * Bar chart visualization of daily steps from the hardware pedometer.
  * 7-day daily average step calculation.
* **Weight Progression Tracker**:
  * Current weight vs. target weight delta metrics.

---

## 8. Native Hardware Integration

* **Android Hardware Pedometer (`StepSensorManager`)**:
  * Utilizes Android's `Sensor.TYPE_STEP_COUNTER` hardware sensor.
  * Event-driven step counting with zero CPU-wake battery drain.
  * Automatic synchronization with the backend (`POST /api/activity/sync-steps`).
  * Graceful fallback handling if device lacks onboard pedometer hardware.

---

## 9. Web Admin & CMS Management Portal (Laravel Blade)

* **Admin Dashboard**:
  * System-wide metrics: Total registered users, active exercises in database, logged workouts, active injury limitation counts.
* **Exercise CMS Directory**:
  * Master exercise list with target muscle, equipment required, difficulty, and biomechanical instructions.
  * Create, edit, and delete exercises.
* **Injury Rule & Contraindication Editor**:
  * Configure rule mappings: Select Physical Limitation $\rightarrow$ Select Excluded Hazardous Exercise $\rightarrow$ Select Safe Alternative.
* **User Management Directory**:
  * List of registered users, assigned fitness personas, biometrics, and active joint limitations.

---

## 10. UI/UX Design System (Obsidian & Electric Mint)

* **Color Tokens & High-Contrast Palette**:
  * `DarkSlateBg` (`#0B0F12`): Deep obsidian background (prevents glare, OLED power saver).
  * `SurfaceCardDark` (`#151C22`): Night Charcoal card surfaces.
  * `SurfaceElevatedDark` (`#1E2730`): Elevated inputs and action pills.
  * `BorderDark` (`#263540`): 1dp crisp structural card borders.
  * `ElectricMint` (`#00E599`): High-energy primary accent for buttons, active tabs, and rings.
  * `NeonCyan` (`#00D2FF`): Secondary accent for focus tags and hydration.
  * `CoralAccent` (`#FF6B4A`): Energy burn, heart rate, and streak indicators.
  * `SafeGreen` (`#10B981`): Clinical joint protection and safety tags.
  * `CautionAmber` (`#F59E0B`): Biomechanical warnings and form cues.
  * `TextPrimary` (`#FFFFFF`): 100% white for high legibility ($\ge 7:1$ WCAG AAA contrast).
  * `TextSecondary` (`#94A3B8`): Cool silver for units and secondary labels.
  * `TextOnAccent` (`#0B0F12`): High contrast dark text on Electric Mint surfaces.
* **Ergonomics**:
  * Material 3 minimum $48\text{dp}$ touch targets in optimal thumb zones.
  * Defensive spacing to prevent accidental touches during gym training.
