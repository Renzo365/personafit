<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Exercise;
use App\Models\ExerciseContraindication;
use App\Models\PhysicalLimitation;
use App\Models\User;
use App\Models\WorkoutLog;
use Illuminate\View\View;

class AdminDashboardController extends Controller
{
    public function index(): View
    {
        $totalUsers = User::where('role', 'app_user')->count();
        $totalExercises = Exercise::count();
        $totalRules = ExerciseContraindication::count();
        $totalWorkouts = WorkoutLog::where('status', 'completed')->count();

        $recentWorkouts = WorkoutLog::with(['user', 'items.exercise'])
            ->where('status', 'completed')
            ->orderByDesc('completed_at')
            ->limit(5)
            ->get();

        $limitationsCount = PhysicalLimitation::withCount('users')->get();

        return view('admin.dashboard', compact(
            'totalUsers',
            'totalExercises',
            'totalRules',
            'totalWorkouts',
            'recentWorkouts',
            'limitationsCount'
        ));
    }
}
