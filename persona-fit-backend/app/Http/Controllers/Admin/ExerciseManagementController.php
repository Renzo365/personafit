<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Exercise;
use App\Models\ExerciseContraindication;
use App\Models\PhysicalLimitation;
use App\Services\ExerciseGifAnalyzerService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class ExerciseManagementController extends Controller
{
    public static function getAvailableMuscleRegions(): array
    {
        return [
            'anterior' => [
                'chest' => 'Pectoralis Major (Chest)',
                'deltoids_front' => 'Anterior Deltoids (Front Shoulders)',
                'biceps' => 'Biceps Brachii',
                'forearms_front' => 'Forearms (Flexors)',
                'abs' => 'Rectus Abdominis (Core / Abs)',
                'obliques' => 'External Obliques',
                'quads' => 'Quadriceps (Front Thighs)',
                'calves_front' => 'Tibialis Anterior & Calves',
            ],
            'posterior' => [
                'traps' => 'Trapezius (Upper / Mid Back)',
                'deltoids_rear' => 'Posterior Deltoids (Rear Shoulders)',
                'triceps' => 'Triceps Brachii',
                'lats' => 'Latissimus Dorsi (Lats)',
                'lower_back' => 'Erector Spinae (Lower Back)',
                'glutes' => 'Gluteus Maximus (Glutes / Hips)',
                'hamstrings' => 'Hamstrings (Posterior Thighs)',
                'calves_rear' => 'Gastrocnemius & Soleus (Calves)',
            ],
        ];
    }
    public function index(Request $request): View
    {
        $query = Exercise::with(['contraindications.limitation', 'contraindications.alternativeExercise'])
            ->withCount(['planExercises', 'logItems']);

        // Search by exercise name, target muscle, equipment, or description
        if ($request->filled('search')) {
            $search = trim($request->input('search'));
            $query->where(function ($q) use ($search) {
                $q->where('name', 'like', "%{$search}%")
                  ->orWhere('description', 'like', "%{$search}%")
                  ->orWhere('target_muscle', 'like', "%{$search}%")
                  ->orWhere('required_equipment', 'like', "%{$search}%");
            });
        }

        // Filter by Difficulty Level (beginner, intermediate, advanced)
        if ($request->filled('difficulty')) {
            $query->where('difficulty', $request->input('difficulty'));
        }

        // Filter by Workout Environment (home, gym, both)
        if ($request->filled('workout_environment')) {
            $query->where('workout_environment', $request->input('workout_environment'));
        }

        // Filter by Exercise Type (compound, isolation)
        if ($request->filled('exercise_type')) {
            $query->where('exercise_type', $request->input('exercise_type'));
        }

        // Filter by Target Muscle
        if ($request->filled('muscle')) {
            $query->where('target_muscle', $request->input('muscle'));
        }

        // Filter by Equipment
        if ($request->filled('equipment')) {
            $eq = $request->input('equipment');
            $query->where(function ($q) use ($eq) {
                $q->where('equipment_type', $eq)
                  ->orWhere('required_equipment', 'like', "%{$eq}%");
            });
        }

        // Filter by Active/Inactive Status
        if ($request->filled('status')) {
            if ($request->input('status') === 'active') {
                $query->where('is_active', true);
            } elseif ($request->input('status') === 'inactive') {
                $query->where('is_active', false);
            }
        }

        $exercises = $query->orderByDesc('is_active')->orderBy('name')->paginate(15)->withQueryString();
        $muscles = Exercise::select('target_muscle')->distinct()->orderBy('target_muscle')->pluck('target_muscle');
        $totalCount = Exercise::count();
        $activeCount = Exercise::where('is_active', true)->count();
        $inactiveCount = Exercise::where('is_active', false)->count();

        return view('admin.exercises.index', compact('exercises', 'muscles', 'totalCount', 'activeCount', 'inactiveCount'));
    }

    public function create(): View
    {
        $limitations = PhysicalLimitation::orderBy('name')->get();
        $allExercises = Exercise::select('id', 'name')->orderBy('name')->get();
        $targetMuscles = ['Chest', 'Back', 'Quads', 'Hamstrings', 'Shoulders', 'Arms', 'Glutes', 'Core', 'Calves', 'Full Body'];
        $availableRegions = self::getAvailableMuscleRegions();

        return view('admin.exercises.create', compact('limitations', 'allExercises', 'targetMuscles', 'availableRegions'));
    }

    public function assetsCatalog(Request $request)
    {
        $path = public_path('assets/exercise_assets_mapping.json');
        if (!file_exists($path)) {
            $path = base_path('../assets/exercise_assets_mapping.json');
        }
        if (file_exists($path)) {
            return response()->file($path, ['Content-Type' => 'application/json']);
        }
        return response()->json([]);
    }

    public function analyzeGif(Request $request): JsonResponse
    {
        $analyzer = new ExerciseGifAnalyzerService();

        if ($request->hasFile('gif_file')) {
            $f = $request->file('gif_file');
            $destDir = public_path('assets/exercises');
            if (!file_exists($destDir)) {
                @mkdir($destDir, 0755, true);
            }
            $fn = time() . '_preview_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $analysis = $analyzer->analyzeGif($destDir . '/' . $fn);
            $analysis['uploaded_asset_path'] = 'assets/exercises/' . $fn;
            return response()->json($analysis);
        }

        $asset = $request->input('asset_filename');
        if (!$asset) {
            return response()->json(['success' => false, 'error' => 'No asset specified'], 400);
        }

        $analysis = $analyzer->analyzeGif($asset);
        return response()->json($analysis);
    }

    public function store(Request $request): RedirectResponse
    {
        $validated = $request->validate([
            'name' => ['required', 'string', 'max:150', 'unique:exercises,name'],
            'description' => ['nullable', 'string', 'max:1000'],
            'target_muscle' => ['required', 'string', 'max:50'],
            'exercise_type' => ['required', 'in:compound,isolation'],
            'workout_environment' => ['required', 'in:home,gym,both'],
            'secondary_muscles' => ['nullable', 'string', 'max:100'],
            'equipment_type' => ['required', 'in:bodyweight,dumbbell,barbell,machine,cable,bands'],
            'required_equipment' => ['nullable', 'string', 'max:150'],
            'difficulty' => ['required', 'in:beginner,intermediate,advanced'],
            'image_url' => ['nullable', 'string', 'max:255'],
            'proper_form_asset' => ['nullable', 'string', 'max:255'],
            'common_mistakes_asset' => ['nullable', 'string', 'max:255'],
            'injury_prevention_asset' => ['nullable', 'string', 'max:255'],
            'anatomy_view' => ['nullable', 'string', 'in:dual,anterior,posterior'],
            'primary_muscle_regions' => ['nullable'],
            'secondary_muscle_regions' => ['nullable'],
            'muscle_mapping_confidence' => ['nullable', 'string', 'in:high,medium,low'],
            'proper_form_file' => ['nullable', 'file', 'mimes:gif,mp4,webm,png,jpg,jpeg', 'max:25600'],
            'common_mistakes_file' => ['nullable', 'file', 'mimes:gif,mp4,webm,png,jpg,jpeg', 'max:25600'],
            'injury_prevention_file' => ['nullable', 'file', 'mimes:gif,mp4,webm,png,jpg,jpeg', 'max:25600'],
            'default_sets' => ['nullable', 'integer', 'min:1', 'max:10'],
            'default_reps' => ['nullable', 'string', 'max:50'],
            'safe_starting_load' => ['nullable', 'string', 'max:100'],
            'default_rest_seconds' => ['nullable', 'integer', 'min:0', 'max:600'],
            'instructions' => ['required', 'string'],
            'common_mistakes' => ['nullable', 'string'],
            'injury_prevention_cues' => ['nullable', 'string'],
            'common_injuries' => ['nullable', 'string'],
            'safer_alternatives_text' => ['nullable', 'string'],
            'recommendation_rules' => ['nullable', 'string'],
            'breathing_tips' => ['nullable', 'string'],
            'benefits' => ['nullable', 'string'],
            'muscle_explanation' => ['nullable', 'string'],
            'is_active' => ['nullable', 'boolean'],
            'contraindication_limitations' => ['nullable', 'array'],
            'contraindication_limitations.*' => ['exists:physical_limitations,id'],
            'risk_mechanisms' => ['nullable', 'array'],
            'alternative_exercise_ids' => ['nullable', 'array'],
        ]);

        $validated['is_active'] = $request->has('is_active') ? (bool) $request->input('is_active') : true;

        // Process direct file uploads if provided
        $destDir = public_path('assets/exercises');
        if (!file_exists($destDir)) {
            @mkdir($destDir, 0755, true);
        }
        if ($request->hasFile('proper_form_file')) {
            $f = $request->file('proper_form_file');
            $fn = time() . '_proper_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $validated['proper_form_asset'] = 'assets/exercises/' . $fn;
        }
        if ($request->hasFile('common_mistakes_file')) {
            $f = $request->file('common_mistakes_file');
            $fn = time() . '_mistake_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $validated['common_mistakes_asset'] = 'assets/exercises/' . $fn;
        }
        if ($request->hasFile('injury_prevention_file')) {
            $f = $request->file('injury_prevention_file');
            $fn = time() . '_prevention_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $validated['injury_prevention_asset'] = 'assets/exercises/' . $fn;
        }

        if (!empty($validated['proper_form_asset'])) {
            $validated['image_url'] = $validated['proper_form_asset'];
        } elseif (!empty($validated['image_url'])) {
            $validated['proper_form_asset'] = $validated['image_url'];
        }

        // Process muscle region arrays from inputs
        if (is_string($request->input('primary_muscle_regions'))) {
            $validated['primary_muscle_regions'] = json_decode($request->input('primary_muscle_regions'), true) 
                ?: array_values(array_filter(array_map('trim', explode(',', $request->input('primary_muscle_regions')))));
        } elseif (is_array($request->input('primary_muscle_regions'))) {
            $validated['primary_muscle_regions'] = array_values(array_filter($request->input('primary_muscle_regions')));
        }

        if (is_string($request->input('secondary_muscle_regions'))) {
            $validated['secondary_muscle_regions'] = json_decode($request->input('secondary_muscle_regions'), true) 
                ?: array_values(array_filter(array_map('trim', explode(',', $request->input('secondary_muscle_regions')))));
        } elseif (is_array($request->input('secondary_muscle_regions'))) {
            $validated['secondary_muscle_regions'] = array_values(array_filter($request->input('secondary_muscle_regions')));
        }

        // Automatic fallback analysis if primary regions are empty
        if (empty($validated['primary_muscle_regions'])) {
            $analyzer = new ExerciseGifAnalyzerService();
            $autoAsset = $validated['proper_form_asset'] ?? $validated['image_url'] ?? null;
            if ($autoAsset) {
                $auto = $analyzer->analyzeGif($autoAsset);
                if (!empty($auto['primary_muscle_regions'])) {
                    $validated['primary_muscle_regions'] = $auto['primary_muscle_regions'];
                    $validated['secondary_muscle_regions'] = $validated['secondary_muscle_regions'] ?? $auto['secondary_muscle_regions'];
                    $validated['anatomy_view'] = $validated['anatomy_view'] ?? $auto['anatomy_view'];
                    $validated['muscle_mapping_confidence'] = $validated['muscle_mapping_confidence'] ?? $auto['muscle_mapping_confidence'];
                }
            }
        }
        $validated['anatomy_view'] = $validated['anatomy_view'] ?? 'dual';
        $validated['muscle_mapping_confidence'] = $validated['muscle_mapping_confidence'] ?? 'high';

        $exercise = Exercise::create($validated);


        // Process contraindications if specified
        if ($request->has('contraindication_limitations')) {
            foreach ($request->input('contraindication_limitations', []) as $limitationId) {
                $riskMechanism = $request->input("risk_mechanisms.{$limitationId}") 
                    ?: 'Biomechanical stress or shear contraindicated for this limitation.';
                $altId = $request->input("alternative_exercise_ids.{$limitationId}") ?: null;
                if ($altId == $exercise->id) {
                    $altId = null;
                }

                ExerciseContraindication::create([
                    'exercise_id' => $exercise->id,
                    'limitation_id' => $limitationId,
                    'risk_mechanism' => $riskMechanism,
                    'alternative_exercise_id' => $altId,
                ]);
            }
        }

        return redirect()->route('admin.exercises.index')->with('success', "Exercise '{$exercise->name}' added to library successfully.");
    }

    public function show(Exercise $exercise): View
    {
        $exercise->load([
            'contraindications.limitation',
            'contraindications.alternativeExercise',
            'planExercises.workoutPlan',
        ]);

        $planCount = $exercise->planExercises()->count();
        $logCount = $exercise->logItems()->count();
        $asAlternativeCount = ExerciseContraindication::where('alternative_exercise_id', $exercise->id)->count();

        return view('admin.exercises.show', compact('exercise', 'planCount', 'logCount', 'asAlternativeCount'));
    }

    public function edit(Exercise $exercise): View
    {
        $exercise->load(['contraindications.limitation', 'contraindications.alternativeExercise']);
        $limitations = PhysicalLimitation::orderBy('name')->get();
        $allExercises = Exercise::where('id', '!=', $exercise->id)->select('id', 'name')->orderBy('name')->get();
        $targetMuscles = ['Chest', 'Back', 'Quads', 'Hamstrings', 'Shoulders', 'Arms', 'Glutes', 'Core', 'Calves', 'Full Body'];
        $availableRegions = self::getAvailableMuscleRegions();

        // Map existing contraindications indexed by limitation_id
        $activeContraindications = $exercise->contraindications->keyBy('limitation_id');

        return view('admin.exercises.edit', compact('exercise', 'limitations', 'allExercises', 'targetMuscles', 'activeContraindications', 'availableRegions'));
    }

    public function update(Request $request, Exercise $exercise): RedirectResponse
    {
        $validated = $request->validate([
            'name' => ['required', 'string', 'max:150', 'unique:exercises,name,' . $exercise->id],
            'description' => ['nullable', 'string', 'max:1000'],
            'target_muscle' => ['required', 'string', 'max:50'],
            'exercise_type' => ['required', 'in:compound,isolation'],
            'workout_environment' => ['required', 'in:home,gym,both'],
            'secondary_muscles' => ['nullable', 'string', 'max:100'],
            'equipment_type' => ['required', 'in:bodyweight,dumbbell,barbell,machine,cable,bands'],
            'required_equipment' => ['nullable', 'string', 'max:150'],
            'difficulty' => ['required', 'in:beginner,intermediate,advanced'],
            'image_url' => ['nullable', 'string', 'max:255'],
            'proper_form_asset' => ['nullable', 'string', 'max:255'],
            'common_mistakes_asset' => ['nullable', 'string', 'max:255'],
            'injury_prevention_asset' => ['nullable', 'string', 'max:255'],
            'anatomy_view' => ['nullable', 'string', 'in:dual,anterior,posterior'],
            'primary_muscle_regions' => ['nullable'],
            'secondary_muscle_regions' => ['nullable'],
            'muscle_mapping_confidence' => ['nullable', 'string', 'in:high,medium,low'],
            'proper_form_file' => ['nullable', 'file', 'mimes:gif,mp4,webm,png,jpg,jpeg', 'max:25600'],
            'common_mistakes_file' => ['nullable', 'file', 'mimes:gif,mp4,webm,png,jpg,jpeg', 'max:25600'],
            'injury_prevention_file' => ['nullable', 'file', 'mimes:gif,mp4,webm,png,jpg,jpeg', 'max:25600'],
            'default_sets' => ['nullable', 'integer', 'min:1', 'max:10'],
            'default_reps' => ['nullable', 'string', 'max:50'],
            'safe_starting_load' => ['nullable', 'string', 'max:100'],
            'default_rest_seconds' => ['nullable', 'integer', 'min:0', 'max:600'],
            'instructions' => ['required', 'string'],
            'common_mistakes' => ['nullable', 'string'],
            'injury_prevention_cues' => ['nullable', 'string'],
            'common_injuries' => ['nullable', 'string'],
            'safer_alternatives_text' => ['nullable', 'string'],
            'recommendation_rules' => ['nullable', 'string'],
            'breathing_tips' => ['nullable', 'string'],
            'benefits' => ['nullable', 'string'],
            'muscle_explanation' => ['nullable', 'string'],
            'is_active' => ['nullable', 'boolean'],
            'contraindication_limitations' => ['nullable', 'array'],
            'contraindication_limitations.*' => ['exists:physical_limitations,id'],
            'risk_mechanisms' => ['nullable', 'array'],
            'alternative_exercise_ids' => ['nullable', 'array'],
        ]);

        $validated['is_active'] = $request->has('is_active') ? (bool) $request->input('is_active') : true;

        // Process direct file uploads if provided
        $destDir = public_path('assets/exercises');
        if (!file_exists($destDir)) {
            @mkdir($destDir, 0755, true);
        }
        if ($request->hasFile('proper_form_file')) {
            $f = $request->file('proper_form_file');
            $fn = time() . '_proper_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $validated['proper_form_asset'] = 'assets/exercises/' . $fn;
        }
        if ($request->hasFile('common_mistakes_file')) {
            $f = $request->file('common_mistakes_file');
            $fn = time() . '_mistake_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $validated['common_mistakes_asset'] = 'assets/exercises/' . $fn;
        }
        if ($request->hasFile('injury_prevention_file')) {
            $f = $request->file('injury_prevention_file');
            $fn = time() . '_prevention_' . preg_replace('/[^a-zA-Z0-9_\.]/', '', $f->getClientOriginalName());
            $f->move($destDir, $fn);
            $validated['injury_prevention_asset'] = 'assets/exercises/' . $fn;
        }

        if (!empty($validated['proper_form_asset'])) {
            $validated['image_url'] = $validated['proper_form_asset'];
        } elseif (!empty($validated['image_url'])) {
            $validated['proper_form_asset'] = $validated['image_url'];
        }

        // Process muscle region arrays from inputs
        if (is_string($request->input('primary_muscle_regions'))) {
            $validated['primary_muscle_regions'] = json_decode($request->input('primary_muscle_regions'), true) 
                ?: array_values(array_filter(array_map('trim', explode(',', $request->input('primary_muscle_regions')))));
        } elseif (is_array($request->input('primary_muscle_regions'))) {
            $validated['primary_muscle_regions'] = array_values(array_filter($request->input('primary_muscle_regions')));
        }

        if (is_string($request->input('secondary_muscle_regions'))) {
            $validated['secondary_muscle_regions'] = json_decode($request->input('secondary_muscle_regions'), true) 
                ?: array_values(array_filter(array_map('trim', explode(',', $request->input('secondary_muscle_regions')))));
        } elseif (is_array($request->input('secondary_muscle_regions'))) {
            $validated['secondary_muscle_regions'] = array_values(array_filter($request->input('secondary_muscle_regions')));
        }

        // Automatic fallback analysis if primary regions are empty
        if (empty($validated['primary_muscle_regions'])) {
            $analyzer = new ExerciseGifAnalyzerService();
            $autoAsset = $validated['proper_form_asset'] ?? $validated['image_url'] ?? $exercise->proper_form_asset ?? null;
            if ($autoAsset) {
                $auto = $analyzer->analyzeGif($autoAsset);
                if (!empty($auto['primary_muscle_regions'])) {
                    $validated['primary_muscle_regions'] = $auto['primary_muscle_regions'];
                    $validated['secondary_muscle_regions'] = $validated['secondary_muscle_regions'] ?? $auto['secondary_muscle_regions'];
                    $validated['anatomy_view'] = $validated['anatomy_view'] ?? $auto['anatomy_view'];
                    $validated['muscle_mapping_confidence'] = $validated['muscle_mapping_confidence'] ?? $auto['muscle_mapping_confidence'];
                }
            }
        }
        $validated['anatomy_view'] = $validated['anatomy_view'] ?? $exercise->anatomy_view ?? 'dual';
        $validated['muscle_mapping_confidence'] = $validated['muscle_mapping_confidence'] ?? 'high';

        $exercise->update($validated);

        // Sync contraindications
        $selectedLimitations = $request->input('contraindication_limitations', []);

        // Delete unselected
        ExerciseContraindication::where('exercise_id', $exercise->id)
            ->whereNotIn('limitation_id', $selectedLimitations)
            ->delete();

        // Update or create selected
        foreach ($selectedLimitations as $limitationId) {
            $riskMechanism = $request->input("risk_mechanisms.{$limitationId}")
                ?: 'Biomechanical stress or shear contraindicated for this limitation.';
            $altId = $request->input("alternative_exercise_ids.{$limitationId}") ?: null;
            if ($altId == $exercise->id) {
                $altId = null;
            }

            ExerciseContraindication::updateOrCreate(
                [
                    'exercise_id' => $exercise->id,
                    'limitation_id' => $limitationId,
                ],
                [
                    'risk_mechanism' => $riskMechanism,
                    'alternative_exercise_id' => $altId,
                ]
            );
        }

        return redirect()->route('admin.exercises.index')->with('success', "Exercise '{$exercise->name}' updated successfully.");
    }

    public function toggleStatus(Exercise $exercise): RedirectResponse
    {
        $exercise->is_active = !$exercise->is_active;
        $exercise->save();

        $statusLabel = $exercise->is_active ? 'Activated (Active in library & recommendations)' : 'Deactivated (Archived from active use)';

        return redirect()->back()->with('success', "Exercise '{$exercise->name}' is now {$statusLabel}.");
    }

    public function destroy(Exercise $exercise): RedirectResponse
    {
        // Safety Check: Check references in workout plans, user workout logs, or as an alternative in injury rules
        $planCount = $exercise->planExercises()->count();
        $logCount = $exercise->logItems()->count();
        $asAlternativeCount = ExerciseContraindication::where('alternative_exercise_id', $exercise->id)->count();

        if ($planCount > 0 || $logCount > 0 || $asAlternativeCount > 0) {
            $reasons = [];
            if ($planCount > 0) {
                $reasons[] = "{$planCount} routine template(s)";
            }
            if ($logCount > 0) {
                $reasons[] = "{$logCount} completed user workout log(s)";
            }
            if ($asAlternativeCount > 0) {
                $reasons[] = "{$asAlternativeCount} injury substitution rule(s)";
            }

            return redirect()->route('admin.exercises.index')->with(
                'error',
                "Deletion Blocked for Safety: '{$exercise->name}' is actively referenced in " . implode(', ', $reasons) . ". To protect historical user records and workout recommendation integrity, this exercise cannot be deleted. Tip: Click 'Deactivate' instead to archive it from active recommendations."
            );
        }

        // Clean up its own contraindication rules safely
        $exercise->contraindications()->delete();
        $exerciseName = $exercise->name;
        $exercise->delete();

        return redirect()->route('admin.exercises.index')->with('success', "Exercise '{$exerciseName}' was permanently removed from the library.");
    }
}
