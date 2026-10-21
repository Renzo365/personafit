# PersonaFit - Mobile UI/UX Wireframes & Interface Specification

> Comprehensive low-fidelity and mid-fidelity screen wireframes, thumb-zone mapping, component states, and interaction guidelines for the PersonaFit Native Android Application and Admin Web Portal.

---

## 1. UI/UX Core Principles & Mobile Ergonomics

### 1.1 Touch-First & Thumb-Zone Ergonomics
PersonaFit is designed for users operating their devices in dynamic environments (e.g., on a gym bench, walking outdoors, or in a kitchen logging meals).

```text
+-------------------------------+
|      HARD-TO-REACH ZONE       | <-- Top AppBar: Back button, Title, Status icons only
|                               |
+-------------------------------+
|          NATURAL ZONE         | <-- Content Viewport: Cards, Graphs, Form instructions
|   (Comfortable for reading)   |
|                               |
+-------------------------------+
|       OPTIMAL THUMB ZONE      | <-- Primary CTAs: "Start Workout", "Check Set", 
|    (Highest tap accuracy)     |     "Log Meal", "Swap Exercise", Bottom Navigation
+-------------------------------+
```

* **Minimum Touch Target**: All clickable elements (buttons, checkboxes, chips, list rows) have a minimum touch target of **$48\text{dp} \times 48\text{dp}$** (Material Design 3 standard).
* **Defensive Spacing**: Minimum $8\text{dp}$ to $16\text{dp}$ gutter between adjacent interactive elements to prevent accidental taps during workouts.
* **Gym-Glanceable Typography**: High-contrast typography with oversized digits ($32\text{sp}+$ headline style) for the **Rest Countdown Timer** and **Weight/Reps counters** so users can read them from 2–3 feet away while resting.

---

### 1.2 Status & State Handling (Feedback First)
Every screen implements 4 explicit UI states:
1. **Loading State**: Subtle shimmer skeleton placeholders rather than blocking full-screen spinners.
2. **Success State**: Smooth transition into populated data with micro-feedback (subtle haptic vibration on set completion).
3. **Empty State**: Action-oriented illustrations and clear next steps (e.g., *"No meals logged yet today. Tap + to log breakfast."*).
4. **Error & Offline State**: Non-intrusive top banner (`"Offline mode • Changes saved locally"`) and inline retry buttons for network operations.

---

## 2. Master Screen Flow Diagram

```text
[01. Splash Screen]
        |
        v
[02. Login / Register Screen]
        |
        +----------------------------------------+
        | (First-Time User)                      | (Returning User)
        v                                        v
[03. Onboarding 1: Biometrics]             [07. Main Dashboard]
        |                                        |
        v                                        +---> [08. Workout Screen]
[04. Onboarding 2: Persona Quiz]                 |        |
        |                                        |        v
        v                                        |     [09. Exercise Guide]
[05. Onboarding 3: Injury Screening]             |        |
        |                                        |        v
        v                                        |     [10. Active Workout Session]
[06. Assessment Summary]                         |        |
        |                                        |        +---> [11. Live Swap Modal]
        +----------------------------------------+        |
                                                 |        v
                                                 +---> [12. Nutrition & Water]
                                                 |
                                                 +---> [13. Progress & Analytics]
                                                 |
                                                 +---> [14. Profile & Settings]
```

---

## 3. Detailed Screen Wireframes

---

### Screen 01 & 02: Authentication & Gateway

#### Wireframe: Login & Registration
```text
+------------------------------------------+
|  12:00                    [Wi-Fi] [85%]  |
|                                          |
|                 [LOGO]                   |
|               PERSONAFIT                 |
|       Smart Fitness & Injury Prevention  |
|                                          |
|  +------------------------------------+  |
|  |  [ Log In ]       |   Sign Up      |  | <-- Segmented Tab Switcher
|  +------------------------------------+  |
|                                          |
|  Email Address                           |
|  +------------------------------------+  |
|  | [icon] user@example.com            |  |
|  +------------------------------------+  |
|                                          |
|  Password                                |
|  +------------------------------------+  |
|  | [icon] ••••••••••••••         [eye]|  |
|  +------------------------------------+  |
|                                          |
|                      Forgot Password?    |
|                                          |
|  +------------------------------------+  |
|  |                                    |  |
|  |             CONTINUE               |  | <-- Primary Button (Electric Mint)
|  |                                    |  |
|  +------------------------------------+  |
|                                          |
|  ---------- or continue with ----------  |
|                                          |
|  +------------------------------------+  |
|  | [G] Continue with Google           |  |
|  +------------------------------------+  |
|                                          |
|  By continuing, you agree to our Terms   |
|  and Physical Activity (PAR-Q) Policy.   |
+------------------------------------------+
```

---

### Screens 03, 04, 05: Multi-Step Onboarding Wizard

#### Screen 03: Biometrics & Goals (Step 1 of 3)
```text
+------------------------------------------+
|  [<- Back]       STEP 1 OF 3     [Skip]  |
|  [======----------] (33% Complete)       |
|                                          |
|  Tell us about yourself                  |
|  We use this to calculate your exact     |
|  metabolic calorie & nutrient baseline.  |
|                                          |
|  Biological Sex                          |
|  +------------------+ +----------------+ |
|  | [icon] Male      | | [icon] Female  | | <-- Big Radio Selection Cards
|  +------------------+ +----------------+ |
|                                          |
|  Age                 Height (cm)         |
|  +----------------+  +-----------------+ |
|  | 25 yrs         |  | 178 cm          | |
|  +----------------+  +-----------------+ |
|                                          |
|  Current Weight      Target Weight (Opt) |
|  +----------------+  +-----------------+ |
|  | 78.5 kg        |  | 72.0 kg         | |
|  +----------------+  +-----------------+ |
|                                          |
|  Primary Fitness Goal                    |
|  ( ) Lose Body Fat (Caloric Deficit)     |
|  (•) Build Muscle & Strength             |
|  ( ) Maintain & Improve Mobility         |
|                                          |
|  +------------------------------------+  |
|  |              NEXT STEP  ->         |  | <-- Primary CTA (Thumb Zone)
|  +------------------------------------+  |
+------------------------------------------+
```

#### Screen 04: Lifestyle & Persona Quiz (Step 2 of 3)
```text
+------------------------------------------+
|  [<- Back]       STEP 2 OF 3             |
|  [==============--] (66% Complete)       |
|                                          |
|  Your Lifestyle & Routine                |
|  This assigns your Behavioral Persona.   |
|                                          |
|  1. How much time can you spend per day? |
|  [ ] < 25 mins (Fast & Efficient)        |
|  [•] 30 - 45 mins (Balanced)             |
|  [ ] 60+ mins (Comprehensive)            |
|                                          |
|  2. Where do you usually work out?       |
|  [•] Commercial Gym (Machines & Free Wts)|
|  [ ] Home Gym (Dumbbells / Resistance)   |
|  [ ] Bodyweight / Outdoor Park           |
|                                          |
|  3. What describes your daily activity?  |
|  [•] Sedentary (Desk Job / Office)       |
|  [ ] Moderately Active (Standing/Walking)|
|  [ ] Heavy Labor                         |
|                                          |
|  +------------------------------------+  |
|  |              NEXT STEP  ->         |  |
|  +------------------------------------+  |
+------------------------------------------+
```

#### Screen 05: Injury & Joint Screening (Step 3 of 3)
```text
+------------------------------------------+
|  [<- Back]       STEP 3 OF 3             |
|  [================] (100% Complete)      |
|                                          |
|  Physical Limitations Screening          |
|  Select any past injuries or joint pain. |
|  The system will filter unsafe exercises.|
|                                          |
|  [ ! ] Select all areas that apply:      |
|                                          |
|  +------------------------------------+  |
|  | [X] Lower Back Pain / Lumbar Disc  |  | <-- High-risk Tag (Amber alert)
|  +------------------------------------+  |
|  | [ ] Knee Pain / Meniscus / Patella  |  |
|  +------------------------------------+  |
|  | [X] Shoulder Impingement / Rotator |  | <-- Active Flag
|  +------------------------------------+  |
|  | [ ] Wrist Tendinitis / Carpal      |  |
|  +------------------------------------+  |
|  | [ ] Neck / Cervical Spine Tension  |  |
|  +------------------------------------+  |
|  | [ ] None - I have no limitations   |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |    GENERATE MY SAFE ROUTINE ->     |  |
|  +------------------------------------+  |
+------------------------------------------+
```

---

### Screen 06: Assessment Result & Persona Summary

```text
+------------------------------------------+
|  12:00                    [Wi-Fi] [85%]  |
|                                          |
|  ASSESSMENT COMPLETE!                    |
|  Here is your tailored prescription:     |
|                                          |
|  +------------------------------------+  |
|  | YOUR PERSONA:                      |  |
|  | [Icon] The Busy Professional       |  |
|  | Strategy: 3-Day High Density Split |  |
|  | Rest Focus: Posture & Decompression|  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | DAILY TARGETS (Mifflin-St Jeor)    |  |
|  | Calories:  2,150 kcal / day        |  |
|  | Protein:   155g   • Carbs: 230g    |  |
|  | Fats:      65g    • Water: 2,800ml |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | INJURY SAFETY FILTERS ACTIVE:      |  |
|  | [✓] Lumbar Protection Enabled      |  |
|  |     (Heavy axial squats excluded)  |  |
|  | [✓] Rotator Cuff Neutral Grip Mod  |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |       GO TO MY DASHBOARD  ->       |  |
|  +------------------------------------+  |
+------------------------------------------+
```

---

### Screen 07: Main Dashboard Screen

```text
+------------------------------------------+
|  12:00                    [Wi-Fi] [85%]  |
|  Hello, Ralph!            [Notification] |
|  Persona: The Busy Pro    [Streak: 🔥 5] |
|                                          |
|  +------------------------------------+  |
|  | TODAY'S WORKOUT                    |  |
|  | Upper Body & Posture (Low Strain)  |  |
|  | 5 Exercises • 35 Mins • 0 Risks    |  |
|  |                                    |  |
|  | [ START WORKOUT NOW ] (Electric Mint)|  |
|  +------------------------------------+  |
|                                          |
|  DAILY TARGETS OVERVIEW                  |
|  +------------------+ +----------------+ |
|  | CALORIES RING    | | STEPS (Sensor) | |
|  |     [ 1,450 ]    | |   [ 6,420 ]    | |
|  |   / 2,150 kcal   | |   / 8,000 steps| |
|  | Rem: 700 kcal    | | 4.8 km • 250cal| |
|  +------------------+ +----------------+ |
|                                          |
|  HYDRATION QUICK-ADD                     |
|  +------------------------------------+  |
|  | Water: 1,750 / 2,800 ml (62%)      |  |
|  | [ +250ml ]   [ +500ml ]  [ Reset ] |  |
|  +------------------------------------+  |
|                                          |
|  [Home]   [Workout]   [Diet]  [Progress] | <-- Bottom Navigation Bar (48dp)
+------------------------------------------+
```

---

### Screen 08 & 09: Workout Routine & Exercise Guide

#### Screen 08: Today's Workout Routine List
```text
+------------------------------------------+
|  [<- Back]  Today's Routine     [Filter] |
|  Upper Body Strength (Lumbar-Safe)       |
|  5 Exercises • Estimated: 35 Minutes     |
|                                          |
|  +------------------------------------+  |
|  | [i] INJURY FILTER APPLIED:         |  |
|  | Barbell Squats replaced with       |  |
|  | Supported Leg Press.               |  |
|  +------------------------------------+  |
|                                          |
|  1. Chest Supported DB Row               |
|  +------------------------------------+  |
|  | [IMG] 3 Sets x 10-12 Reps • 60s Rest|  |
|  | Target: Lats, Rhomboids            |  |
|  | Safety: [✓ Spine Neutral]          |  |
|  +------------------------------------+  |
|                                          |
|  2. Incline Neutral-Grip DB Press        |
|  +------------------------------------+  |
|  | [IMG] 3 Sets x 8-10 Reps • 60s Rest |  |
|  | Target: Upper Chest, Front Delts   |  |
|  | Safety: [✓ Shoulder Safe]          |  |
|  +------------------------------------+  |
|                                          |
|  3. Cable Face Pulls                     |
|  +------------------------------------+  |
|  | [IMG] 3 Sets x 15 Reps • 45s Rest  |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |      START ACTIVE SESSION          |  | <-- Large Primary CTA
|  +------------------------------------+  |
|  [Home]   [Workout]   [Diet]  [Progress] |
+------------------------------------------+
```

#### Screen 09: Exercise Detail & Biomechanical Guide
```text
+------------------------------------------+
|  [<- Back]  Exercise Details    [Save]   |
|                                          |
|  +------------------------------------+  |
|  |                                    |  |
|  |         [ EXERCISE GIF / DEMO ]    |  |
|  |                                    |  |
|  +------------------------------------+  |
|                                          |
|  Chest Supported Dumbbell Row            |
|  Target Muscle: Upper Back (Lats/Traps)  |
|  Equipment: Incline Bench + Dumbbells    |
|                                          |
|  HOW TO PERFORM (STEP-BY-STEP):          |
|  1. Set bench to a 30-45 degree angle.   |
|  2. Lie chest-down with neutral neck.    |
|  3. Row dumbbells towards your hips.     |
|                                          |
|  +------------------------------------+  |
|  | [ ! ] INJURY PREVENTION CUES:      |  | <-- Caution Banner
|  | - Do not hyperextend lower spine.  |  |
|  | - Keep core braced against bench.  |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |       ADD TO TODAY'S WORKOUT       |  |
|  +------------------------------------+  |
+------------------------------------------+
```

---

### Screen 10 & 11: Active Workout Session & Live Swap

#### Screen 10: Active Workout Execution (Gym Mode)
```text
+------------------------------------------+
|  [ X Quit ]   ACTIVE SESSION   [ 14:22 ] | <-- Live Workout Stopwatch
|                                          |
|  Exercise 1 of 5:                        |
|  Chest Supported Dumbbell Row            |
|                                          |
|  +------------------------------------+  |
|  | [SWAP EXERCISE] (If discomfort)    |  | <-- One-Tap Swap Button
|  +------------------------------------+  |
|                                          |
|  SET   TARGET      WEIGHT   REPS   DONE  |
|  ---   ------      ------   ----   ----  |
|  01    12 Reps     [20 kg]  [12]   [ X ] | <-- Completed (Green)
|  02    10 Reps     [22 kg]  [10]   [ X ] | <-- Completed (Green)
|  03    10 Reps     [22 kg]  [10]   [   ] | <-- Current Active Set
|                                          |
|  ======================================  |
|  REST COUNTDOWN TIMER (LARGE DIGITS)     |
|                                          |
|                00 : 45                   | <-- Glanceable from 3ft away
|                                          |
|  [ -15s ]       [ PAUSE ]       [ +30s ] |
|  ======================================  |
|                                          |
|  +------------------------------------+  |
|  |      COMPLETE WORKOUT SESSION      |  | <-- Large Bottom CTA
|  +------------------------------------+  |
+------------------------------------------+
```

#### Screen 11: Live Exercise Swap Modal (Bottom Sheet)
```text
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  | === SWAP EXERCISE (SAFE OPTIONS) = |  | <-- Swipe Down to Dismiss
|  | Reason: Feeling lower back strain? |  |
|  | Showing 100% biomechanically safe  |  |
|  | substitutes for Upper Back:        |  |
|  |                                    |  |
|  | (•) Cable Seated Row (Supported)   |  |
|  |     [✓ Zero Spinal Compression]    |  |
|  |                                    |  |
|  | ( ) Lat Pulldown to Upper Chest    |  |
|  |     [✓ Decompresses Lumbar]        |  |
|  |                                    |  |
|  | ( ) Machine Iso-Lateral Row        |  |
|  |                                    |  |
|  | +--------------------------------+ |  |
|  | |      CONFIRM SUBSTITUTION      | |  |
|  | +--------------------------------+ |  |
|  +------------------------------------+  |
+------------------------------------------+
```

---

### Screen 12: Nutrition & Hydration Tracker

```text
+------------------------------------------+
|  12:00                    [Wi-Fi] [85%]  |
|  Nutrition & Diet Log       [Date: Today]|
|                                          |
|  +------------------------------------+  |
|  | DAILY MACRONUTRIENT BALANCE        |  |
|  |                                    |  |
|  |   Consumed: 1,450 / 2,150 kcal     |  |
|  |                                    |  |
|  |   Protein: 110 / 155g   (71%) [=== ]|  |
|  |   Carbs:   160 / 230g   (70%) [=== ]|  |
|  |   Fats:     45 /  65g   (69%) [=== ]|  |
|  +------------------------------------+  |
|                                          |
|  MEAL LOGS                     [ + Add ] |
|  +------------------------------------+  |
|  | Breakfast: Oatmeal & Whey Protein  |  |
|  | 420 kcal • 35g P • 55g C • 6g F    |  |
|  +------------------------------------+  |
|  | Lunch: Grilled Chicken Rice Bowl   |  |
|  | 680 kcal • 52g P • 75g C • 14g F   |  |
|  +------------------------------------+  |
|  | Dinner: Not logged yet             |  |
|  | [ + Tap to log Dinner ]            |  |
|  +------------------------------------+  |
|                                          |
|  DAILY WATER INTAKE                      |
|  +------------------------------------+  |
|  | 1,750 / 2,800 ml (7 Glasses)       |  |
|  | [ + 250 ml Glass ] [ + 500 ml Btl ]|  |
|  +------------------------------------+  |
|                                          |
|  [Home]   [Workout]   [Diet]  [Progress] |
+------------------------------------------+
```

---

### Screen 13: Progress & Analytics Dashboard

```text
+------------------------------------------+
|  12:00                    [Wi-Fi] [85%]  |
|  Progress & Metrics        [Export PDF]  |
|                                          |
|  +------------------------------------+  |
|  | BODY WEIGHT PROGRESSION (kg)       |  |
|  |  82 |  *                           |  |
|  |  80 |   \   *                      |  |
|  |  78 |    \ / \   *  <- Today 77.2  |  |
|  |  76 |     *   \_/                  |  |
|  |     +-------------------------     |  |
|  |       W1  W2  W3  W4  (30 Days)    |  |
|  | Current BMI: 24.3 (Normal Weight)  |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | WORKOUT ADHERENCE & STREAK         |  |
|  | This Week: [M][T][W][T][F][S][S]   |  |
|  | Completed:  ✓  ✓  -  ✓  ✓  -  -    |  |
|  | Adherence: 85% Consistency Score   |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  | HARDWARE PEDOMETER (7-DAY AVG)     |  |
|  | Average: 7,450 Steps / Day         |  |
|  +------------------------------------+  |
|                                          |
|  [Home]   [Workout]   [Diet]  [Progress] |
+------------------------------------------+
```

---

### Screen 14: Profile & Injury Management

```text
+------------------------------------------+
|  12:00                    [Wi-Fi] [85%]  |
|  Profile & Settings                      |
|                                          |
|  [ Avatar ] Ralph S.                     |
|  ralph@example.com                       |
|  Assigned Persona: The Busy Pro          |
|                                          |
|  HEALTH & INJURY PROFILE                 |
|  +------------------------------------+  |
|  | Active Limitations:                |  |
|  | • Lower Back Pain (Active Filter)  |  |
|  | • Shoulder Impingement (Active)    |  |
|  |                                    |  |
|  | [ RE-TAKE INJURY ASSESSMENT ]      |  | <-- Update Injury Status
|  +------------------------------------+  |
|                                          |
|  APP SETTINGS                            |
|  +------------------------------------+  |
|  | Dark Mode                  [Toggle]|  |
|  | Offline Sync Status        [Synced]|  |
|  | Rest Timer Sound Cues      [  ON  ]|  |
|  | Step Counter Sensor        [ACTIVE]|  |
|  +------------------------------------+  |
|                                          |
|  [ LOG OUT ] (Destructive Red Outline)   |
|                                          |
|  [Home]   [Workout]   [Diet]  [Progress] |
+------------------------------------------+
```

---

## 4. Web Admin / CMS Portal Wireframe (Desktop View)

```text
+-----------------------------------------------------------------------------------+
| PERSONAFIT ADMIN PORTAL                Logged in: Coach / Admin | [Logout]        |
+-------------------+---------------------------------------------------------------+
| NAVIGATION        | EXERCISE & INJURY CONTRAINDICATION MANAGEMENT                 |
| - Dashboard       |                                                               |
| - Exercise CMS    | [+ Add New Exercise]    [Search: Row________________] [Filter]|
| - Injury Rules    +---------------------------------------------------------------+
| - User Directory  | ID | Exercise Name    | Target   | Equipment | Contraindicated For|
| - System Logs     |----+------------------+----------+-----------+--------------------|
|                   | 01 | Barbell Squat    | Quads    | Barbell   | [Lower Back][Knee] |
|                   | 02 | Leg Press (Supp) | Quads    | Machine   | [None - Safe]      |
|                   | 03 | DB Shoulder Press| Shoulders| Dumbbell  | [Shoulder Impinge] |
|                   | 04 | Seated Cable Row | Back     | Cable     | [None - Safe]      |
|                   +---------------------------------------------------------------+
|                   |                                                               |
|                   | RULE EDITOR (Contraindication Mapping):                       |
|                   | If User Limitation = [ Lower Back Pain             v ]        |
|                   | Exclude Movement   = [ Barbell Back Squat          v ]        |
|                   | Substitute With    = [ 45-Degree Leg Press (Supp)  v ]        |
|                   | [ SAVE RULE ]                                                 |
+-------------------+---------------------------------------------------------------+
```

---

## 5. UI Component Specifications & Design Tokens

### 5.1 Jetpack Compose Theme Definition (Obsidian & Electric Mint)

```kotlin
// com/personafit/app/presentation/theme/Color.kt
val ElectricMint = Color(0xFF00E599)       // Primary CTA, active tab indicators, glowing highlights
val MintSecondary = Color(0xFF05C785)      // Focused/pressed states
val NeonCyan = Color(0xFF00D2FF)           // Secondary accent, water metrics, focus tags
val CoralAccent = Color(0xFFFF6B4A)        // Energy burn, heart rate, streak flame (🔥)
val SafeGreen = Color(0xFF10B981)          // Clinical joint protection, active safeguards
val CautionAmber = Color(0xFFF59E0B)       // Biomechanical mistake warnings, form cues
val WarningRed = Color(0xFFEF4444)         // Severe injury exclusion tags, validation errors

// High-Contrast Dark Slate Background & Surfaces
val DarkSlateBg = Color(0xFF0B0F12)        // Pure obsidian canvas (zero glare, OLED power saver)
val SurfaceCardDark = Color(0xFF151C22)    // Night Charcoal cards & bottom sheets
val SurfaceElevatedDark = Color(0xFF1E2730)// Input fields, elevated tiles, segmented pills
val BorderDark = Color(0xFF263540)         // 1dp high-contrast structural borders
val BorderHighlight = Color(0xFF00E599)    // Active focus border

// Ultra-Crisp Typography Tokens (WCAG AAA Compliant >= 7:1 Contrast)
val TextPrimary = Color(0xFFFFFFFF)        // 100% Crisp White (Headlines & primary metrics)
val TextSecondary = Color(0xFF94A3B8)      // Cool Silver (Units, secondary instructions)
val TextMuted = Color(0xFF64748B)          // Slate (Subtle metadata, disabled indicators)
val TextOnAccent = Color(0xFF0B0F12)       // Dark text on Electric Mint buttons for maximum readability
```

### 5.2 Material 3 Component Guidelines

1. **TopAppBar**:
   * Centered or left-aligned title with `MaterialTheme.typography.titleMedium` (`TextPrimary` #FFFFFF).
   * Background: `DarkSlateBg` ($0xFF0B0F12$).
   * Displays dynamic offline indicator when network is disconnected.
2. **Cards (`ElevatedCard` / `OutlinedCard`)**:
   * Corner radius: `16.dp` for modern athletic performance aesthetic.
   * Container color: `SurfaceCardDark` ($0xFF151C22$) with `BorderDark` ($0xFF263540$) 1dp border.
3. **Buttons (`Button` / `OutlinedButton`)**:
   * Height: Minimum `52.dp` for primary action buttons in optimal thumb zones.
   * Shape: `RoundedCornerShape(12.dp)`.
   * Container: `ElectricMint` ($0xFF00E599$) with `TextOnAccent` ($0xFF0B0F12$) high-contrast text.
4. **Bottom Navigation (`NavigationBar`)**:
   * 4 key destinations: **Home**, **Workout**, **Nutrition**, **Progress**.
   * Container color: `DarkSlateBg` ($0xFF0B0F12$).
   * Selected icon and label indicator with `ElectricMint` ($0xFF00E599$).

---

## 6. UX Edge Cases & Error States

| Scenario | UX Handling Strategy |
|---|---|
| **User feels pain mid-workout** | Tapping `"Swap Exercise"` opens the bottom sheet with filtered safe movements without losing active workout time or previous sets. |
| **No hardware pedometer sensor** | The app detects `sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null` and gracefully displays a manual activity entry card instead of crashing. |
| **Offline during workout** | Workouts, reps, and hydration logs save directly to local **Room DB**. A subtle green snackbar notifies: *"Saved locally. Syncing when reconnected."* |
| **Accidental back button in workout** | An alert dialog interrupts: *"Exit workout? Your logged sets will be saved as an incomplete session."* with **[Resume]** and **[Discard]** options. |

---
*End of PersonaFit UI/UX Wireframe & Interface Documentation.*
