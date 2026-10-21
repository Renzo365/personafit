@extends('layouts.admin')

@section('content')
<div class="space-y-6">
    <!-- Header -->
    <div>
        <h1 class="text-2xl font-bold text-white">Registered Mobile Users</h1>
        <p class="text-sm text-gray-400">View mobile client accounts, assigned personas, and physical limitations</p>
    </div>

    <!-- Users Table -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl overflow-hidden shadow-xl">
        <div class="overflow-x-auto">
            <table class="w-full text-left text-sm text-gray-300">
                <thead class="text-xs uppercase bg-gray-900/80 text-gray-400 border-b border-gray-700">
                    <tr>
                        <th class="py-3.5 px-4">User Name</th>
                        <th class="py-3.5 px-4">Email</th>
                        <th class="py-3.5 px-4">Assigned Persona</th>
                        <th class="py-3.5 px-4">Calorie Target</th>
                        <th class="py-3.5 px-4">Active Limitations</th>
                        <th class="py-3.5 px-4">Completed Sessions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-700/40">
                    @forelse($users as $u)
                        <tr class="hover:bg-gray-700/30 transition">
                            <td class="py-3.5 px-4 font-bold text-white flex items-center space-x-2">
                                <div class="w-8 h-8 rounded-full bg-teal-600/30 text-teal-300 flex items-center justify-center font-bold text-xs">
                                    {{ strtoupper(substr($u->name, 0, 2)) }}
                                </div>
                                <span>{{ $u->name }}</span>
                            </td>
                            <td class="py-3.5 px-4 text-gray-400">{{ $u->email }}</td>
                            <td class="py-3.5 px-4">
                                @if($u->profile)
                                    <span class="px-2.5 py-1 text-xs font-bold rounded-lg bg-teal-500/20 text-teal-300 border border-teal-500/30 capitalize">
                                        {{ str_replace('_', ' ', $u->profile->persona_archetype) }}
                                    </span>
                                @else
                                    <span class="text-xs text-gray-500">Not Completed</span>
                                @endif
                            </td>
                            <td class="py-3.5 px-4 font-mono">
                                {{ $u->profile ? number_format($u->profile->daily_calories) . ' kcal' : '-' }}
                            </td>
                            <td class="py-3.5 px-4">
                                @if($u->limitations->count() > 0)
                                    <div class="flex flex-wrap gap-1">
                                        @foreach($u->limitations as $lim)
                                            <span class="px-2 py-0.5 text-xs rounded bg-red-900/40 text-red-300 border border-red-700/40">
                                                {{ $lim->affected_body_part ?? $lim->name }}
                                            </span>
                                        @endforeach
                                    </div>
                                @else
                                    <span class="text-xs text-emerald-400">None (Healthy)</span>
                                @endif
                            </td>
                            <td class="py-3.5 px-4 font-semibold text-white">
                                {{ $u->workout_logs_count }}
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="6" class="py-8 text-center text-gray-500">No users found.</td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>

        @if($users->hasPages())
            <div class="p-4 border-t border-gray-700 bg-gray-900/50">
                {{ $users->links() }}
            </div>
        @endif
    </div>
</div>
@endsection
