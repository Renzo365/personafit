@extends('layouts.admin')

@section('content')
<div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
            <h1 class="text-2xl font-bold text-white flex items-center gap-2">
                <i class="fa-solid fa-dumbbell text-teal-400"></i>
                <span>Exercise Repository (CMS)</span>
            </h1>
            <p class="text-sm text-gray-400">Manage 52 official movements, biomechanics, difficulty, environment, and injury safeguards</p>
        </div>
        <div class="flex items-center gap-2 flex-wrap">
            <span class="text-xs px-3 py-1.5 rounded-lg bg-gray-800 border border-gray-700 text-gray-300">
                Total: <strong class="text-teal-400">{{ $totalCount }}</strong>
            </span>
            <span class="text-xs px-3 py-1.5 rounded-lg bg-emerald-950/40 border border-emerald-800/40 text-emerald-300">
                Active: <strong class="text-emerald-400">{{ $activeCount }}</strong>
            </span>
            @if($inactiveCount > 0)
                <span class="text-xs px-3 py-1.5 rounded-lg bg-gray-850 border border-gray-700 text-gray-400">
                    Archived: <strong class="text-amber-400">{{ $inactiveCount }}</strong>
                </span>
            @endif
            <a href="{{ route('admin.exercises.create') }}" class="px-4 py-2 bg-teal-600 hover:bg-teal-500 text-white text-sm font-semibold rounded-lg shadow-lg shadow-teal-500/20 transition flex items-center space-x-2">
                <i class="fa-solid fa-plus"></i>
                <span>Add Exercise</span>
            </a>
        </div>
    </div>

    <!-- Search & Multi-Filter Bar -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-5 shadow-xl">
        <form method="GET" action="{{ route('admin.exercises.index') }}" class="space-y-4">
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-3 items-center">
                <!-- Search input -->
                <div class="sm:col-span-2 lg:col-span-2 relative">
                    <i class="fa-solid fa-magnifying-glass absolute left-3.5 top-3 text-gray-400 text-sm"></i>
                    <input type="text" name="search" value="{{ request('search') }}" 
                        placeholder="Search name, muscle, equipment..." 
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg pl-10 pr-4 py-2.5 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-teal-500 focus:ring-1 focus:ring-teal-500 transition">
                </div>

                <!-- Difficulty Filter -->
                <div>
                    <select name="difficulty" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-xs text-white focus:outline-none focus:border-teal-500 transition">
                        <option value="">All Difficulties</option>
                        <option value="beginner" {{ request('difficulty') === 'beginner' ? 'selected' : '' }}>Beginner</option>
                        <option value="intermediate" {{ request('difficulty') === 'intermediate' ? 'selected' : '' }}>Intermediate</option>
                        <option value="advanced" {{ request('difficulty') === 'advanced' ? 'selected' : '' }}>Advanced</option>
                    </select>
                </div>

                <!-- Environment Filter -->
                <div>
                    <select name="workout_environment" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-xs text-white focus:outline-none focus:border-teal-500 transition">
                        <option value="">All Environments</option>
                        <option value="home" {{ request('workout_environment') === 'home' ? 'selected' : '' }}>Home Workout</option>
                        <option value="gym" {{ request('workout_environment') === 'gym' ? 'selected' : '' }}>Gym Workout</option>
                        <option value="both" {{ request('workout_environment') === 'both' ? 'selected' : '' }}>Both (Home & Gym)</option>
                    </select>
                </div>

                <!-- Exercise Type Filter -->
                <div>
                    <select name="exercise_type" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-xs text-white focus:outline-none focus:border-teal-500 transition">
                        <option value="">All Types</option>
                        <option value="compound" {{ request('exercise_type') === 'compound' ? 'selected' : '' }}>Compound</option>
                        <option value="isolation" {{ request('exercise_type') === 'isolation' ? 'selected' : '' }}>Isolation</option>
                    </select>
                </div>

                <!-- Target Muscle Filter -->
                <div>
                    <select name="muscle" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-xs text-white focus:outline-none focus:border-teal-500 transition">
                        <option value="">All Muscles</option>
                        @foreach($muscles as $m)
                            <option value="{{ $m }}" {{ request('muscle') === $m ? 'selected' : '' }}>{{ $m }}</option>
                        @endforeach
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 pt-1">
                <!-- Equipment Filter -->
                <div>
                    <select name="equipment" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500 transition">
                        <option value="">All Equipment Categories</option>
                        <option value="bodyweight" {{ request('equipment') === 'bodyweight' ? 'selected' : '' }}>Bodyweight / Calisthenics</option>
                        <option value="dumbbell" {{ request('equipment') === 'dumbbell' ? 'selected' : '' }}>Dumbbells</option>
                        <option value="barbell" {{ request('equipment') === 'barbell' ? 'selected' : '' }}>Barbell / Weight Plates</option>
                        <option value="machine" {{ request('equipment') === 'machine' ? 'selected' : '' }}>Machines (Chest/Lat/Leg Press)</option>
                        <option value="cable" {{ request('equipment') === 'cable' ? 'selected' : '' }}>Cable Tower</option>
                        <option value="bands" {{ request('equipment') === 'bands' ? 'selected' : '' }}>Resistance Bands</option>
                    </select>
                </div>

                <!-- Status Filter -->
                <div>
                    <select name="status" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500 transition">
                        <option value="">All Statuses (Active & Archived)</option>
                        <option value="active" {{ request('status') === 'active' ? 'selected' : '' }}>Active Only (In Active Recommendation Library)</option>
                        <option value="inactive" {{ request('status') === 'inactive' ? 'selected' : '' }}>Archived Only (Preserved for History)</option>
                    </select>
                </div>

                <!-- Actions / Reset -->
                <div class="flex items-center justify-end gap-2">
                    @if(request()->hasAny(['search', 'difficulty', 'workout_environment', 'exercise_type', 'muscle', 'equipment', 'status']))
                        <a href="{{ route('admin.exercises.index') }}" class="px-3 py-2 text-xs text-gray-400 hover:text-white bg-gray-700/50 hover:bg-gray-700 rounded-lg transition">
                            <i class="fa-solid fa-xmark mr-1"></i> Reset
                        </a>
                    @endif
                    <button type="submit" class="px-4 py-2 bg-teal-600 hover:bg-teal-500 text-white text-xs font-semibold rounded-lg shadow transition flex items-center gap-1.5">
                        <i class="fa-solid fa-filter text-[11px]"></i>
                        <span>Apply Filters</span>
                    </button>
                </div>
            </div>

            <!-- Total matching status text -->
            <div class="pt-2 border-t border-gray-700/50 flex items-center justify-between text-xs text-gray-400">
                <span>Showing <strong class="text-teal-400">{{ $exercises->total() }}</strong> matching movement records</span>
                <span>Page {{ $exercises->currentPage() }} of {{ $exercises->lastPage() }}</span>
            </div>
        </form>
    </div>

    <!-- Exercises Table -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl overflow-hidden shadow-xl">
        <div class="overflow-x-auto">
            <table class="w-full text-left text-sm text-gray-300">
                <thead class="text-xs uppercase bg-gray-900/90 text-gray-400 border-b border-gray-700 tracking-wider">
                    <tr>
                        <th class="py-4 px-4 font-semibold">Exercise Movement</th>
                        <th class="py-4 px-4 font-semibold">Target Muscle</th>
                        <th class="py-4 px-4 font-semibold">Type</th>
                        <th class="py-4 px-4 font-semibold">Environment</th>
                        <th class="py-4 px-4 font-semibold">Difficulty</th>
                        <th class="py-4 px-4 font-semibold">Equipment</th>
                        <th class="py-4 px-4 font-semibold">Safety & Limitations</th>
                        <th class="py-4 px-4 font-semibold">Status</th>
                        <th class="py-4 px-4 text-right font-semibold">Actions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-700/40">
                    @forelse($exercises as $exercise)
                        <tr class="hover:bg-gray-700/30 transition group {{ !$exercise->is_active ? 'opacity-70 bg-gray-900/40' : '' }}">
                            <!-- Exercise Name & Description -->
                            <td class="py-4 px-4 max-w-[280px]">
                                <div class="flex items-center gap-3">
                                    @if($exercise->image_url)
                                        <div class="w-10 h-10 rounded-lg bg-gray-950 border border-gray-700/80 overflow-hidden flex items-center justify-center shrink-0">
                                            <img src="{{ asset($exercise->image_url) }}" alt="{{ $exercise->name }}" class="h-full w-full object-contain p-0.5" loading="lazy">
                                        </div>
                                    @else
                                        <div class="w-10 h-10 rounded-lg bg-gray-950/60 border border-gray-800 flex items-center justify-center shrink-0 text-gray-600">
                                            <i class="fa-solid fa-dumbbell text-xs"></i>
                                        </div>
                                    @endif
                                    <div class="min-w-0 flex-1">
                                        <div class="font-bold text-white group-hover:text-teal-300 transition flex items-center gap-1.5">
                                            <a href="{{ route('admin.exercises.show', $exercise) }}" class="hover:underline truncate">
                                                {{ $exercise->name }}
                                            </a>
                                        </div>
                                        @if($exercise->description)
                                            <p class="text-xs text-gray-400 truncate mt-0.5" title="{{ $exercise->description }}">
                                                {{ $exercise->description }}
                                            </p>
                                        @endif
                                        @if($exercise->plan_exercises_count > 0 || $exercise->log_items_count > 0)
                                            <div class="flex items-center gap-1 mt-1">
                                                <span class="text-[10px] text-gray-400 bg-gray-900/80 px-1.5 py-0.5 rounded border border-gray-700/50" title="{{ $exercise->plan_exercises_count }} routines, {{ $exercise->log_items_count }} completed user logs">
                                                    <i class="fa-solid fa-link text-[9px] text-teal-400 mr-1"></i>In {{ $exercise->plan_exercises_count }} plan(s)
                                                </span>
                                            </div>
                                        @endif
                                    </div>
                                </div>
                            </td>

                            <!-- Target Muscle -->
                            <td class="py-4 px-4 whitespace-nowrap">
                                <span class="px-2.5 py-1 text-xs font-medium rounded-md bg-gray-900 text-gray-200 border border-gray-700">
                                    {{ $exercise->target_muscle }}
                                </span>
                            </td>

                            <!-- Exercise Type: Compound (Cyan) vs Isolation (Blue) -->
                            <td class="py-4 px-4 whitespace-nowrap">
                                @if($exercise->exercise_type === 'compound')
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-cyan-500/15 text-cyan-300 border border-cyan-500/30">
                                        <i class="fa-solid fa-arrows-to-dot mr-1 text-[10px]"></i>Compound
                                    </span>
                                @else
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-blue-500/15 text-blue-300 border border-blue-500/30">
                                        <i class="fa-solid fa-bullseye mr-1 text-[10px]"></i>Isolation
                                    </span>
                                @endif
                            </td>

                            <!-- Workout Environment: Home (Blue/Sky), Gym (Purple/Violet), Both (Teal) -->
                            <td class="py-4 px-4 whitespace-nowrap">
                                @if($exercise->workout_environment === 'home')
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-sky-500/15 text-sky-300 border border-sky-500/30" title="Home Workout only">
                                        <i class="fa-solid fa-house text-[10px] mr-1"></i>Home
                                    </span>
                                @elseif($exercise->workout_environment === 'gym')
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-purple-500/15 text-purple-300 border border-purple-500/30" title="Gym facility only">
                                        <i class="fa-solid fa-dumbbell text-[10px] mr-1"></i>Gym
                                    </span>
                                @else
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-teal-500/15 text-teal-300 border border-teal-500/30" title="Suitable for both Home and Gym">
                                        <i class="fa-solid fa-layer-group text-[10px] mr-1"></i>Both
                                    </span>
                                @endif
                            </td>

                            <!-- Difficulty Level: Beginner (Green), Intermediate (Amber/Yellow), Advanced (Purple) -->
                            <td class="py-4 px-4 whitespace-nowrap">
                                @if($exercise->difficulty === 'beginner')
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                                        <i class="fa-solid fa-shield text-[10px] mr-1"></i>Beginner
                                    </span>
                                @elseif($exercise->difficulty === 'intermediate')
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-amber-500/20 text-amber-300 border border-amber-500/30">
                                        <i class="fa-solid fa-gauge-simple-med text-[10px] mr-1"></i>Intermediate
                                    </span>
                                @else
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-purple-500/20 text-purple-300 border border-purple-500/30">
                                        <i class="fa-solid fa-bolt text-[10px] mr-1"></i>Advanced
                                    </span>
                                @endif
                            </td>

                            <!-- Required Equipment -->
                            <td class="py-4 px-4 whitespace-nowrap text-gray-300">
                                <span class="text-xs bg-gray-900/60 px-2 py-1 rounded border border-gray-700/60" title="{{ $exercise->required_equipment }}">
                                    {{ $exercise->required_equipment ?: ucfirst($exercise->equipment_type) }}
                                </span>
                            </td>

                            <!-- Contraindications / Safety Limitations -->
                            <td class="py-4 px-4 max-w-[200px]">
                                @if($exercise->contraindications->count() > 0)
                                    <div class="flex flex-wrap gap-1">
                                        @foreach($exercise->contraindications as $c)
                                            <span class="px-2 py-0.5 text-xs rounded bg-red-950/60 text-red-300 border border-red-800/40 inline-flex items-center" 
                                                title="{{ $c->risk_mechanism }}{{ $c->alternativeExercise ? ' (Alt: ' . $c->alternativeExercise->name . ')' : '' }}">
                                                <i class="fa-solid fa-triangle-exclamation mr-1 text-[10px] text-red-400"></i>
                                                {{ $c->limitation->affected_body_part ?? $c->limitation->name }}
                                            </span>
                                        @endforeach
                                    </div>
                                @else
                                    <span class="text-xs text-emerald-400 inline-flex items-center px-2 py-0.5 rounded bg-emerald-950/40 border border-emerald-800/30">
                                        <i class="fa-solid fa-shield-heart mr-1 text-emerald-400"></i> Safe
                                    </span>
                                @endif
                            </td>

                            <!-- Status Badge -->
                            <td class="py-4 px-4 whitespace-nowrap">
                                @if($exercise->is_active)
                                    <span class="px-2 py-0.5 text-[11px] font-semibold rounded-full bg-emerald-500/15 text-emerald-400 border border-emerald-500/30 inline-flex items-center gap-1">
                                        <span class="w-1.5 h-1.5 rounded-full bg-emerald-400"></span> Active
                                    </span>
                                @else
                                    <span class="px-2 py-0.5 text-[11px] font-semibold rounded-full bg-gray-700/60 text-gray-400 border border-gray-600/50 inline-flex items-center gap-1">
                                        <span class="w-1.5 h-1.5 rounded-full bg-gray-400"></span> Archived
                                    </span>
                                @endif
                            </td>

                            <!-- Actions -->
                            <td class="py-4 px-4 text-right whitespace-nowrap space-x-1.5">
                                <!-- View Details -->
                                <a href="{{ route('admin.exercises.show', $exercise) }}" 
                                    class="inline-flex items-center justify-center p-2 rounded-lg bg-gray-700/40 hover:bg-cyan-600/30 text-gray-400 hover:text-cyan-300 border border-gray-700 hover:border-cyan-500/40 transition" 
                                    title="View Full Movement & Safety Details">
                                    <i class="fa-solid fa-eye text-sm"></i>
                                </a>

                                <!-- Edit -->
                                <a href="{{ route('admin.exercises.edit', $exercise) }}" 
                                    class="inline-flex items-center justify-center p-2 rounded-lg bg-gray-700/40 hover:bg-teal-600/30 text-gray-400 hover:text-teal-300 border border-gray-700 hover:border-teal-500/40 transition" 
                                    title="Edit Movement & Biomechanics">
                                    <i class="fa-solid fa-pen-to-square text-sm"></i>
                                </a>

                                <!-- Toggle Active / Archive Status -->
                                <form action="{{ route('admin.exercises.toggle-status', $exercise) }}" method="POST" class="inline-block">
                                    @csrf
                                    <button type="submit" 
                                        class="inline-flex items-center justify-center p-2 rounded-lg bg-gray-700/40 {{ $exercise->is_active ? 'hover:bg-amber-600/30 text-teal-400 hover:text-amber-300 border-gray-700 hover:border-amber-500/40' : 'hover:bg-emerald-600/30 text-gray-500 hover:text-emerald-300 border-gray-700 hover:border-emerald-500/40' }} border transition" 
                                        title="{{ $exercise->is_active ? 'Deactivate / Archive Movement' : 'Activate Movement' }}">
                                        <i class="fa-solid {{ $exercise->is_active ? 'fa-toggle-on' : 'fa-toggle-off' }} text-sm"></i>
                                    </button>
                                </form>
                                
                                <!-- Safe Delete -->
                                <form action="{{ route('admin.exercises.destroy', $exercise) }}" method="POST" class="inline-block" 
                                    onsubmit="return confirmDeleteExercise('{{ addslashes($exercise->name) }}', {{ $exercise->plan_exercises_count }}, {{ $exercise->log_items_count }});">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" 
                                        class="inline-flex items-center justify-center p-2 rounded-lg bg-gray-700/40 hover:bg-red-600/30 text-gray-400 hover:text-red-300 border border-gray-700 hover:border-red-500/40 transition" 
                                        title="{{ ($exercise->plan_exercises_count > 0 || $exercise->log_items_count > 0) ? 'Referenced in workout plans/logs - delete blocked for safety' : 'Delete Exercise' }}">
                                        <i class="fa-solid fa-trash text-sm"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="9" class="py-12 text-center text-gray-400">
                                <div class="flex flex-col items-center justify-center space-y-3">
                                    <div class="w-12 h-12 rounded-full bg-gray-900 border border-gray-700 flex items-center justify-center text-gray-500 text-lg">
                                        <i class="fa-solid fa-magnifying-glass"></i>
                                    </div>
                                    <p class="font-medium text-gray-300">No exercises found matching your filter criteria.</p>
                                    <a href="{{ route('admin.exercises.index') }}" class="text-xs text-teal-400 hover:underline">
                                        Reset all filters
                                    </a>
                                </div>
                            </td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>

        <!-- Pagination -->
        @if($exercises->hasPages())
            <div class="p-4 border-t border-gray-700 bg-gray-900/50">
                {{ $exercises->links() }}
            </div>
        @endif
    </div>
</div>

<script>
function confirmDeleteExercise(name, planCount, logCount) {
    if (planCount > 0 || logCount > 0) {
        alert("⚠️ Deletion Safety Check:\n\n'" + name + "' is currently referenced by " + planCount + " workout plan routine(s) and " + logCount + " completed user log(s).\n\nThe backend will block this deletion to protect historical progress data and recommendation engine integrity. You can deactivate it instead.");
        return true; // Submit to controller so controller returns structured safety error flash message
    }
    return confirm("Are you sure you want to permanently delete '" + name + "' from the exercise library?\n\nThis movement will be completely removed.");
}
</script>
@endsection
