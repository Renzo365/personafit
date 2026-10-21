<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Exercise;
use App\Models\ExerciseContraindication;
use App\Models\PhysicalLimitation;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class InjuryRuleController extends Controller
{
    public function index(): View
    {
        $rules = ExerciseContraindication::with(['exercise', 'limitation', 'alternativeExercise'])
            ->orderBy('limitation_id')
            ->get();

        $exercises = Exercise::orderBy('name')->get();
        $limitations = PhysicalLimitation::orderBy('name')->get();

        return view('admin.rules.index', compact('rules', 'exercises', 'limitations'));
    }

    public function store(Request $request): RedirectResponse
    {
        $validated = $request->validate([
            'exercise_id' => ['required', 'exists:exercises,id'],
            'limitation_id' => ['required', 'exists:physical_limitations,id'],
            'alternative_exercise_id' => ['nullable', 'exists:exercises,id', 'different:exercise_id'],
            'risk_mechanism' => ['required', 'string', 'max:255'],
        ]);

        ExerciseContraindication::updateOrCreate(
            [
                'exercise_id' => $validated['exercise_id'],
                'limitation_id' => $validated['limitation_id'],
            ],
            [
                'alternative_exercise_id' => $validated['alternative_exercise_id'] ?? null,
                'risk_mechanism' => $validated['risk_mechanism'],
            ]
        );

        return redirect()->route('admin.rules.index')->with('success', 'Injury contraindication rule saved.');
    }

    public function destroy(ExerciseContraindication $rule): RedirectResponse
    {
        $rule->delete();

        return redirect()->route('admin.rules.index')->with('success', 'Injury rule deleted.');
    }
}
