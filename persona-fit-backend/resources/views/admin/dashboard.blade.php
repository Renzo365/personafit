@extends('layouts.admin')

@section('content')
<div class="space-y-8">
    <!-- Header -->
    <div class="flex justify-between items-center">
        <div>
            <h1 class="text-2xl font-bold text-white">System Dashboard</h1>
            <p class="text-sm text-gray-400">Overview of users, exercise datasets, and injury engine status</p>
        </div>
        <div class="flex space-x-3">
            <a href="{{ route('admin.exercises.create') }}" class="px-4 py-2 bg-teal-600 hover:bg-teal-500 text-white text-sm font-semibold rounded-lg shadow-lg transition flex items-center space-x-2">
                <i class="fa-solid fa-plus"></i>
                <span>Add Exercise</span>
            </a>
        </div>
    </div>

    <!-- Metric Cards Grid -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Mobile Users</p>
                    <h3 class="text-3xl font-extrabold text-white mt-1">{{ $totalUsers }}</h3>
                </div>
                <div class="w-12 h-12 rounded-xl bg-blue-500/10 text-blue-400 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-users"></i>
                </div>
            </div>
            <p class="text-xs text-blue-400 mt-3 flex items-center">
                <i class="fa-solid fa-arrow-trend-up mr-1"></i> Active mobile clients
            </p>
        </div>

        <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Exercise Library</p>
                    <h3 class="text-3xl font-extrabold text-white mt-1">{{ $totalExercises }}</h3>
                </div>
                <div class="w-12 h-12 rounded-xl bg-teal-500/10 text-teal-400 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-dumbbell"></i>
                </div>
            </div>
            <p class="text-xs text-teal-400 mt-3 flex items-center">
                <i class="fa-solid fa-check mr-1"></i> Across all muscle groups
            </p>
        </div>

        <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Contraindication Rules</p>
                    <h3 class="text-3xl font-extrabold text-white mt-1">{{ $totalRules }}</h3>
                </div>
                <div class="w-12 h-12 rounded-xl bg-amber-500/10 text-amber-400 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                </div>
            </div>
            <p class="text-xs text-amber-400 mt-3 flex items-center">
                <i class="fa-solid fa-shield-halved mr-1"></i> Active injury filters
            </p>
        </div>

        <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Completed Sessions</p>
                    <h3 class="text-3xl font-extrabold text-white mt-1">{{ $totalWorkouts }}</h3>
                </div>
                <div class="w-12 h-12 rounded-xl bg-emerald-500/10 text-emerald-400 flex items-center justify-center text-xl">
                    <i class="fa-solid fa-fire"></i>
                </div>
            </div>
            <p class="text-xs text-emerald-400 mt-3 flex items-center">
                <i class="fa-solid fa-circle-check mr-1"></i> Logged from mobile
            </p>
        </div>
    </div>

    <!-- Active Limitation Distribution & Recent Sessions -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Physical Limitation Breakdown -->
        <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
            <h2 class="text-lg font-bold text-white mb-4 flex items-center">
                <i class="fa-solid fa-kit-medical mr-2 text-red-400"></i>
                <span>User Limitation Breakdown</span>
            </h2>
            <div class="space-y-3">
                @forelse($limitationsCount as $lim)
                    <div class="flex justify-between items-center p-3 rounded-xl bg-gray-900/60 border border-gray-700/40">
                        <div>
                            <p class="text-sm font-semibold text-gray-200">{{ $lim->name }}</p>
                            <p class="text-xs text-gray-400">{{ $lim->affected_body_part }}</p>
                        </div>
                        <span class="px-2.5 py-1 text-xs font-bold rounded-full bg-teal-500/20 text-teal-300">
                            {{ $lim->users_count }} users
                        </span>
                    </div>
                @empty
                    <p class="text-sm text-gray-500">No limitation data recorded yet.</p>
                @endforelse
            </div>
        </div>

        <!-- Recent Completed Workout Sessions -->
        <div class="lg:col-span-2 bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
            <h2 class="text-lg font-bold text-white mb-4 flex items-center">
                <i class="fa-solid fa-clock-rotate-left mr-2 text-teal-400"></i>
                <span>Recent Live Mobile Sessions</span>
            </h2>
            <div class="overflow-x-auto">
                <table class="w-full text-left text-sm text-gray-300">
                    <thead class="text-xs uppercase bg-gray-900/80 text-gray-400 border-b border-gray-700">
                        <tr>
                            <th class="py-3 px-4">User</th>
                            <th class="py-3 px-4">Session Name</th>
                            <th class="py-3 px-4">Duration</th>
                            <th class="py-3 px-4">Total Volume</th>
                            <th class="py-3 px-4">Completed</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-700/40">
                        @forelse($recentWorkouts as $workout)
                            <tr class="hover:bg-gray-700/30 transition">
                                <td class="py-3 px-4 font-semibold text-white">{{ $workout->user->name ?? 'Mobile User' }}</td>
                                <td class="py-3 px-4 text-teal-300">{{ $workout->session_name }}</td>
                                <td class="py-3 px-4">{{ round($workout->total_duration_seconds / 60) }} mins</td>
                                <td class="py-3 px-4 font-mono font-medium">{{ number_format($workout->total_volume_kg) }} kg</td>
                                <td class="py-3 px-4 text-xs text-gray-400">{{ $workout->completed_at ? $workout->completed_at->diffForHumans() : '-' }}</td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="5" class="py-6 text-center text-gray-500">No workout logs recorded yet.</td>
                            </tr>
                        @endforelse
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
@endsection
