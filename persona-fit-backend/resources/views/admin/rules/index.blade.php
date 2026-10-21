@extends('layouts.admin')

@section('content')
<div class="space-y-8">
    <!-- Header -->
    <div>
        <h1 class="text-2xl font-bold text-white">Injury Contraindication Rules Engine</h1>
        <p class="text-sm text-gray-400">Map vulnerable joint conditions to contraindicated movements and specify biomechanically safe substitutions</p>
    </div>

    <!-- Rule Creation Card -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl">
        <h2 class="text-lg font-bold text-white mb-4 flex items-center">
            <i class="fa-solid fa-plus-circle text-teal-400 mr-2"></i>
            <span>Add New Contraindication Mapping</span>
        </h2>

        <form method="POST" action="{{ route('admin.rules.store') }}" class="grid grid-cols-1 md:grid-cols-4 gap-4">
            @csrf

            <div>
                <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">If User Has Limitation *</label>
                <select name="limitation_id" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                    <option value="">Select Limitation...</option>
                    @foreach($limitations as $lim)
                        <option value="{{ $lim->id }}">{{ $lim->name }} ({{ $lim->affected_body_part }})</option>
                    @endforeach
                </select>
            </div>

            <div>
                <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Exclude This Movement *</label>
                <select name="exercise_id" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                    <option value="">Select Exercise to Exclude...</option>
                    @foreach($exercises as $ex)
                        <option value="{{ $ex->id }}">{{ $ex->name }} ({{ $ex->target_muscle }})</option>
                    @endforeach
                </select>
            </div>

            <div>
                <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Substitute With Safe Alternative *</label>
                <select name="alternative_exercise_id" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                    <option value="">Select Safe Substitute...</option>
                    @foreach($exercises as $ex)
                        <option value="{{ $ex->id }}">{{ $ex->name }} ({{ $ex->target_muscle }})</option>
                    @endforeach
                </select>
            </div>

            <div>
                <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Risk Mechanism / Rationale *</label>
                <input type="text" name="risk_mechanism" required placeholder="e.g. Heavy axial spinal shear load"
                    class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
            </div>

            <div class="md:col-span-4 flex justify-end">
                <button type="submit" class="px-6 py-2.5 bg-teal-600 hover:bg-teal-500 text-white text-sm font-semibold rounded-lg shadow-lg transition flex items-center space-x-2">
                    <i class="fa-solid fa-save"></i>
                    <span>Save Contraindication Rule</span>
                </button>
            </div>
        </form>
    </div>

    <!-- Active Rules Matrix -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl overflow-hidden shadow-xl">
        <div class="p-4 border-b border-gray-700 bg-gray-900/60 flex items-center justify-between">
            <h2 class="text-base font-bold text-white">Active Contraindication Rules ({{ $rules->count() }})</h2>
        </div>

        <div class="overflow-x-auto">
            <table class="w-full text-left text-sm text-gray-300">
                <thead class="text-xs uppercase bg-gray-900/80 text-gray-400 border-b border-gray-700">
                    <tr>
                        <th class="py-3.5 px-4">Limitation / Vulnerability</th>
                        <th class="py-3.5 px-4">Contraindicated Movement</th>
                        <th class="py-3.5 px-4">Safe Biomechanical Substitute</th>
                        <th class="py-3.5 px-4">Risk Mechanism</th>
                        <th class="py-3.5 px-4 text-right">Actions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-700/40">
                    @forelse($rules as $rule)
                        <tr class="hover:bg-gray-700/30 transition">
                            <td class="py-3.5 px-4">
                                <span class="px-2.5 py-1 text-xs font-bold rounded-lg bg-amber-500/20 text-amber-300 border border-amber-500/30">
                                    <i class="fa-solid fa-triangle-exclamation mr-1"></i>
                                    {{ $rule->limitation->name }}
                                </span>
                            </td>
                            <td class="py-3.5 px-4 font-semibold text-red-300">
                                <i class="fa-solid fa-ban text-xs mr-1"></i>
                                {{ $rule->exercise->name }}
                            </td>
                            <td class="py-3.5 px-4 font-semibold text-emerald-300">
                                @if($rule->alternativeExercise)
                                    <i class="fa-solid fa-circle-check text-xs mr-1"></i>
                                    {{ $rule->alternativeExercise->name }}
                                @else
                                    <span class="text-gray-500">Excluded (No direct single swap)</span>
                                @endif
                            </td>
                            <td class="py-3.5 px-4 text-xs text-gray-400 max-w-xs truncate" title="{{ $rule->risk_mechanism }}">
                                {{ $rule->risk_mechanism }}
                            </td>
                            <td class="py-3.5 px-4 text-right">
                                <form action="{{ route('admin.rules.destroy', $rule) }}" method="POST" class="inline-block" onsubmit="return confirm('Delete this injury rule?');">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="p-1.5 text-gray-400 hover:text-red-400 transition" title="Delete Rule">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="5" class="py-8 text-center text-gray-500">No contraindication rules mapped yet.</td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
</div>
@endsection
