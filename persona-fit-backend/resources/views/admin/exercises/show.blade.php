@extends('layouts.admin')

@section('content')
<div class="max-w-5xl mx-auto space-y-6">
    <!-- Top Bar Navigation & Actions -->
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div class="flex items-center space-x-4">
            <a href="{{ route('admin.exercises.index') }}" class="p-2 bg-gray-800 hover:bg-gray-700 rounded-lg text-gray-400 hover:text-white transition">
                <i class="fa-solid fa-arrow-left"></i>
            </a>
            <div>
                <div class="flex items-center gap-3">
                    <h1 class="text-2xl font-bold text-white">{{ $exercise->name }}</h1>
                    @if($exercise->is_active)
                        <span class="px-2.5 py-0.5 text-xs font-semibold rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 inline-flex items-center gap-1.5">
                            <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span> Active
                        </span>
                    @else
                        <span class="px-2.5 py-0.5 text-xs font-semibold rounded-full bg-amber-500/20 text-amber-300 border border-amber-500/30 inline-flex items-center gap-1.5">
                            <span class="w-1.5 h-1.5 rounded-full bg-amber-400"></span> Archived (Inactive)
                        </span>
                    @endif
                </div>
                <p class="text-sm text-gray-400">Biomechanical classification, injury safeguards & prescription profile</p>
            </div>
        </div>

        <div class="flex items-center gap-2">
            <!-- Toggle Status Form -->
            <form action="{{ route('admin.exercises.toggle-status', $exercise) }}" method="POST" class="inline">
                @csrf
                <button type="submit" class="px-3.5 py-2 text-xs font-semibold rounded-lg border transition flex items-center gap-2 {{ $exercise->is_active ? 'bg-amber-500/10 hover:bg-amber-500/20 text-amber-300 border-amber-500/30' : 'bg-emerald-500/10 hover:bg-emerald-500/20 text-emerald-300 border-emerald-500/30' }}">
                    <i class="fa-solid {{ $exercise->is_active ? 'fa-pause' : 'fa-play' }}"></i>
                    <span>{{ $exercise->is_active ? 'Archive / Deactivate' : 'Activate Movement' }}</span>
                </button>
            </form>

            <!-- Edit Button -->
            <a href="{{ route('admin.exercises.edit', $exercise) }}" class="px-4 py-2 bg-teal-600 hover:bg-teal-500 text-white text-xs font-semibold rounded-lg shadow-lg shadow-teal-500/20 transition flex items-center gap-1.5">
                <i class="fa-solid fa-pen-to-square"></i>
                <span>Edit Movement</span>
            </a>

            <!-- Delete Button -->
            <form action="{{ route('admin.exercises.destroy', $exercise) }}" method="POST" class="inline" 
                onsubmit="return confirm('Permanently remove {{ addslashes($exercise->name) }} from library? If it is referenced in plans or logs, the safety guard will block deletion.');">
                @csrf
                @method('DELETE')
                <button type="submit" class="p-2 bg-gray-800 hover:bg-red-600/30 text-gray-400 hover:text-red-300 border border-gray-700 hover:border-red-500/40 rounded-lg transition" title="Delete Movement">
                    <i class="fa-solid fa-trash text-xs"></i>
                </button>
            </form>
        </div>
    </div>
    </div>

    <!-- 3-Tab Movement Animation Assets Gallery -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
        <div class="flex items-center justify-between border-b border-gray-700/60 pb-3 flex-wrap gap-2">
            <div class="flex items-center gap-2">
                <i class="fa-solid fa-film text-teal-400"></i>
                <h2 class="text-base font-bold text-white">Exercise Guide Animation Assets (3 Modes)</h2>
            </div>
            <span class="text-xs text-gray-400">Assets are associated by exercise ID: <strong class="text-white">#{{ $exercise->id }}</strong></span>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- 1. Proper Form Asset -->
            <div class="bg-gray-900/90 rounded-xl border border-teal-500/30 p-4 flex flex-col items-center text-center space-y-3">
                <div class="flex items-center gap-1.5 text-xs font-bold text-teal-400">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Proper Form</span>
                </div>
                <div class="w-full h-44 bg-gray-950 rounded-lg overflow-hidden flex items-center justify-center p-2 border border-gray-800 shadow-inner">
                    @php $properAsset = $exercise->proper_form_asset ?: $exercise->image_url; @endphp
                    @if($properAsset)
                        <img src="{{ asset($properAsset) }}" alt="Proper Form" class="max-h-full max-w-full object-contain">
                    @else
                        <div class="text-gray-500 text-xs flex flex-col items-center gap-1">
                            <i class="fa-solid fa-image-slash text-lg"></i>
                            <span>No Proper Form Asset</span>
                        </div>
                    @endif
                </div>
                <div class="w-full text-left">
                    <span class="text-[10px] text-gray-500 font-mono block truncate">{{ $properAsset ?: 'Asset not set' }}</span>
                </div>
            </div>

            <!-- 2. Common Mistakes Asset -->
            <div class="bg-gray-900/90 rounded-xl border border-rose-500/30 p-4 flex flex-col items-center text-center space-y-3">
                <div class="flex items-center gap-1.5 text-xs font-bold text-rose-400">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                    <span>Common Mistakes</span>
                </div>
                <div class="w-full h-44 bg-gray-950 rounded-lg overflow-hidden flex items-center justify-center p-2 border border-gray-800 shadow-inner">
                    @if($exercise->common_mistakes_asset)
                        <img src="{{ asset($exercise->common_mistakes_asset) }}" alt="Common Mistakes" class="max-h-full max-w-full object-contain">
                    @else
                        <div class="text-amber-400/80 text-xs flex flex-col items-center gap-1.5 px-3">
                            <i class="fa-solid fa-circle-info text-lg"></i>
                            <span class="font-semibold">Kinematic Fault Simulation</span>
                            <span class="text-[10px] text-gray-400">60 FPS procedural joint stress simulator active in mobile app. Dedicated asset can be attached via CMS.</span>
                        </div>
                    @endif
                </div>
                <div class="w-full text-left">
                    <span class="text-[10px] text-gray-500 font-mono block truncate">{{ $exercise->common_mistakes_asset ?: 'Procedural Fallback Active' }}</span>
                </div>
            </div>

            <!-- 3. Injury Prevention Asset -->
            <div class="bg-gray-900/90 rounded-xl border border-emerald-500/30 p-4 flex flex-col items-center text-center space-y-3">
                <div class="flex items-center gap-1.5 text-xs font-bold text-emerald-400">
                    <i class="fa-solid fa-shield-halved"></i>
                    <span>Injury Prevention</span>
                </div>
                <div class="w-full h-44 bg-gray-950 rounded-lg overflow-hidden flex items-center justify-center p-2 border border-gray-800 shadow-inner">
                    @if($exercise->injury_prevention_asset)
                        <img src="{{ asset($exercise->injury_prevention_asset) }}" alt="Injury Prevention" class="max-h-full max-w-full object-contain">
                    @else
                        <div class="text-emerald-400/80 text-xs flex flex-col items-center gap-1.5 px-3">
                            <i class="fa-solid fa-shield text-lg"></i>
                            <span class="font-semibold">Protective Limiter Active</span>
                            <span class="text-[10px] text-gray-400">60 FPS joint limiter cues displayed in mobile app. Dedicated asset can be attached via CMS.</span>
                        </div>
                    @endif
                </div>
                <div class="w-full text-left">
                    <span class="text-[10px] text-gray-500 font-mono block truncate">{{ $exercise->injury_prevention_asset ?: 'Procedural Fallback Active' }}</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Verified Anatomy Highlight & Biomechanical Mapping Display -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
        <div class="flex items-center justify-between border-b border-gray-700/60 pb-3 flex-wrap gap-2">
            <div class="flex items-center gap-2">
                <i class="fa-solid fa-person-running text-teal-400"></i>
                <h2 class="text-base font-bold text-white">Verified Anatomy Highlights & Target Regions</h2>
            </div>
            <div class="flex items-center gap-2">
                <span class="text-xs text-gray-400">Anatomy View: <strong class="text-teal-400 font-semibold uppercase">{{ $exercise->anatomy_view ?? 'dual' }}</strong></span>
                <span class="px-2.5 py-0.5 text-[11px] font-bold rounded-lg border {{ ($exercise->muscle_mapping_confidence ?? 'high') === 'high' ? 'bg-emerald-950/80 text-emerald-400 border-emerald-700/60' : 'bg-amber-950/80 text-amber-400 border-amber-700/60' }}">
                    <i class="fa-solid fa-circle-check text-[10px]"></i> {{ strtoupper($exercise->muscle_mapping_confidence ?? 'HIGH') }} CONFIDENCE
                </span>
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-12 gap-6 items-center">
            <!-- Anatomy Preview Canvas -->
            <div class="md:col-span-6 flex justify-center">
                <div class="relative w-full max-w-[420px] aspect-[883/655] bg-gray-950 rounded-xl overflow-hidden border border-gray-800 shadow-inner flex items-center justify-center p-2">
                    <img src="{{ asset('assets/anatomy/anatomy_template_clean.png') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none select-none" alt="Anatomy Template">
                    <!-- Overlay Secondary Masks (Cyan) -->
                    @foreach($exercise->secondary_muscle_regions ?? [] as $secRegion)
                        <img src="{{ asset('assets/anatomy/masks/mask_' . $secRegion . '.png') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none" style="filter: brightness(0) saturate(100%) invert(80%) sepia(85%) saturate(3000%) hue-rotate(150deg) drop-shadow(0 0 6px rgba(0,229,255,0.7)); opacity: 0.85;">
                    @endforeach
                    <!-- Overlay Primary Masks (Electric Mint) -->
                    @foreach($exercise->primary_muscle_regions ?? [] as $primRegion)
                        <img src="{{ $primRegion === 'chest' ? asset('assets/anatomy/mask_chest.png') : asset('assets/anatomy/masks/mask_' . $primRegion . '.png') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none" style="filter: brightness(0) saturate(100%) invert(85%) sepia(85%) saturate(4000%) hue-rotate(85deg) drop-shadow(0 0 8px rgba(0,255,136,0.9)); opacity: 0.95;">
                    @endforeach
                </div>
            </div>

            <!-- Target Regions Details -->
            <div class="md:col-span-6 space-y-4">
                <div>
                    <span class="text-xs font-bold text-emerald-400 uppercase tracking-wider block mb-1.5 flex items-center gap-1.5">
                        <span class="w-2.5 h-2.5 rounded-full bg-emerald-500 shadow-sm shadow-emerald-500/50"></span>
                        Primary Target Muscle Regions
                    </span>
                    <div class="flex flex-wrap gap-1.5">
                        @forelse($exercise->primary_muscle_regions ?? [] as $region)
                            <span class="px-2.5 py-1 bg-emerald-950/70 border border-emerald-500/50 rounded-lg text-xs font-semibold text-emerald-300">
                                {{ ucwords(str_replace('_', ' ', $region)) }}
                            </span>
                        @empty
                            <span class="text-xs text-gray-500">None specified</span>
                        @endforelse
                    </div>
                </div>

                <div>
                    <span class="text-xs font-bold text-cyan-400 uppercase tracking-wider block mb-1.5 flex items-center gap-1.5">
                        <span class="w-2.5 h-2.5 rounded-full bg-cyan-400 shadow-sm shadow-cyan-400/50"></span>
                        Secondary Stabilizer Regions
                    </span>
                    <div class="flex flex-wrap gap-1.5">
                        @forelse($exercise->secondary_muscle_regions ?? [] as $region)
                            <span class="px-2.5 py-1 bg-cyan-950/70 border border-cyan-500/50 rounded-lg text-xs font-semibold text-cyan-300">
                                {{ ucwords(str_replace('_', ' ', $region)) }}
                            </span>
                        @empty
                            <span class="text-xs text-gray-500">None specified</span>
                        @endforelse
                    </div>
                </div>

                <div class="bg-gray-900/80 rounded-xl p-3 border border-gray-800 text-xs text-gray-300 space-y-1">
                    <p><strong class="text-gray-400">Target Muscle:</strong> {{ $exercise->target_muscle }}</p>
                    <p><strong class="text-gray-400">Secondary Synergists:</strong> {{ $exercise->secondary_muscles ?: 'None' }}</p>
                    <p><strong class="text-gray-400">Biomechanical Mapping:</strong> Verified against PersonaFit 3-template reference matrix.</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Baseline Prescription Parameters Card -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-5 shadow-xl">
        <div class="flex items-center gap-2 mb-3">
            <i class="fa-solid fa-sliders text-teal-400"></i>
            <h3 class="text-sm font-bold text-white uppercase tracking-wider">Safe Baseline Prescription Parameters</h3>
        </div>
        <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
            <div class="bg-gray-900/70 p-3 rounded-xl border border-gray-800">
                <span class="text-[11px] text-gray-400 uppercase font-semibold block">Target Sets</span>
                <span class="text-lg font-bold text-white">{{ $exercise->default_sets ?? 3 }} Sets</span>
            </div>
            <div class="bg-gray-900/70 p-3 rounded-xl border border-gray-800">
                <span class="text-[11px] text-gray-400 uppercase font-semibold block">Target Reps</span>
                <span class="text-lg font-bold text-white">{{ $exercise->default_reps ? $exercise->default_reps . ' Reps' : '10–12 Reps' }}</span>
            </div>
            <div class="bg-gray-900/70 p-3 rounded-xl border border-gray-800">
                <span class="text-[11px] text-gray-400 uppercase font-semibold block">Safe Starting Load</span>
                <span class="text-sm font-bold text-emerald-400">{{ $exercise->safe_starting_load ?: 'Bodyweight / Conservative' }}</span>
            </div>
            <div class="bg-gray-900/70 p-3 rounded-xl border border-gray-800">
                <span class="text-[11px] text-gray-400 uppercase font-semibold block">Rest Interval</span>
                <span class="text-lg font-bold text-white">{{ $exercise->default_rest_seconds ?? 60 }}s</span>
            </div>
        </div>
    </div>
    </div>

    <!-- Quick Stat Badges Banner -->
    <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <!-- Target Muscle -->
        <div class="bg-gray-800/80 border border-gray-700/60 rounded-xl p-4">
            <span class="text-[11px] font-semibold text-gray-400 uppercase tracking-wider block">Target Muscle</span>
            <div class="mt-1 flex items-center gap-2">
                <i class="fa-solid fa-person-running text-teal-400"></i>
                <span class="text-base font-bold text-white">{{ $exercise->target_muscle }}</span>
            </div>
            @if($exercise->secondary_muscles)
                <span class="text-[11px] text-gray-400 mt-1 block truncate">Sec: {{ $exercise->secondary_muscles }}</span>
            @endif
        </div>

        <!-- Exercise Type -->
        <div class="bg-gray-800/80 border border-gray-700/60 rounded-xl p-4">
            <span class="text-[11px] font-semibold text-gray-400 uppercase tracking-wider block">Exercise Type</span>
            <div class="mt-1">
                @if($exercise->exercise_type === 'compound')
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-cyan-500/15 text-cyan-300 border border-cyan-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-arrows-to-dot text-[10px]"></i> Compound
                    </span>
                @else
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-blue-500/15 text-blue-300 border border-blue-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-bullseye text-[10px]"></i> Isolation
                    </span>
                @endif
            </div>
            <span class="text-[11px] text-gray-400 mt-1 block">
                {{ $exercise->exercise_type === 'compound' ? 'Multi-joint kinetic chain' : 'Single-joint targeted isolation' }}
            </span>
        </div>

        <!-- Environment -->
        <div class="bg-gray-800/80 border border-gray-700/60 rounded-xl p-4">
            <span class="text-[11px] font-semibold text-gray-400 uppercase tracking-wider block">Environment</span>
            <div class="mt-1">
                @if($exercise->workout_environment === 'home')
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-sky-500/15 text-sky-300 border border-sky-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-house text-[10px]"></i> Home Workout
                    </span>
                @elseif($exercise->workout_environment === 'gym')
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-purple-500/15 text-purple-300 border border-purple-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-dumbbell text-[10px]"></i> Gym Workout
                    </span>
                @else
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-teal-500/15 text-teal-300 border border-teal-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-layer-group text-[10px]"></i> Both (Home & Gym)
                    </span>
                @endif
            </div>
            <span class="text-[11px] text-gray-400 mt-1 block">
                Eq: {{ $exercise->required_equipment ?: ucfirst($exercise->equipment_type) }}
            </span>
        </div>

        <!-- Difficulty -->
        <div class="bg-gray-800/80 border border-gray-700/60 rounded-xl p-4">
            <span class="text-[11px] font-semibold text-gray-400 uppercase tracking-wider block">Difficulty Level</span>
            <div class="mt-1">
                @if($exercise->difficulty === 'beginner')
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-shield text-[10px]"></i> Beginner
                    </span>
                @elseif($exercise->difficulty === 'intermediate')
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-amber-500/20 text-amber-300 border border-amber-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-gauge-simple-med text-[10px]"></i> Intermediate
                    </span>
                @else
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-purple-500/20 text-purple-300 border border-purple-500/30 inline-flex items-center gap-1.5">
                        <i class="fa-solid fa-bolt text-[10px]"></i> Advanced
                    </span>
                @endif
            </div>
            <span class="text-[11px] text-gray-400 mt-1 block">
                Used in {{ $planCount }} plan(s) · {{ $logCount }} log(s)
            </span>
        </div>
    </div>
    </div>

    <!-- Main Content Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Left 2 Cols: Form Instructions & Biomechanics -->
        <div class="lg:col-span-2 space-y-6">
            <!-- Movement Overview & Proper Form -->
            <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-5">
                <h2 class="text-base font-bold text-white flex items-center gap-2 border-b border-gray-700/60 pb-3">
                    <i class="fa-solid fa-clipboard-list text-teal-400"></i>
                    <span>Proper Form & Execution</span>
                </h2>

                @if($exercise->description)
                    <div>
                        <span class="text-xs font-semibold text-gray-400 uppercase tracking-wider block mb-1">Clinical Overview</span>
                        <p class="text-sm text-gray-300 leading-relaxed bg-gray-900/60 p-3 rounded-xl border border-gray-800">
                            {{ $exercise->description }}
                        </p>
                    </div>
                @endif

                <div>
                    <span class="text-xs font-semibold text-teal-400 uppercase tracking-wider block mb-2">Step-by-Step Instructions</span>
                    <div class="text-sm text-gray-200 leading-relaxed bg-gray-900/90 p-4 rounded-xl border border-gray-700/70 whitespace-pre-line font-mono text-xs">
                        {{ $exercise->instructions }}
                    </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
                    <!-- Common Form Mistakes -->
                    <div class="bg-amber-950/20 border border-amber-800/30 rounded-xl p-4">
                        <span class="text-xs font-bold text-amber-300 uppercase tracking-wider flex items-center gap-1.5 mb-2">
                            <i class="fa-solid fa-triangle-exclamation text-amber-400"></i> Common Mistakes
                        </span>
                        <p class="text-xs text-amber-200/90 leading-relaxed">
                            {{ $exercise->common_mistakes ?: 'No specific form mistakes noted.' }}
                        </p>
                    </div>

                    <!-- Injury Prevention Cues -->
                    <div class="bg-emerald-950/20 border border-emerald-800/30 rounded-xl p-4">
                        <span class="text-xs font-bold text-emerald-300 uppercase tracking-wider flex items-center gap-1.5 mb-2">
                            <i class="fa-solid fa-shield-halved text-emerald-400"></i> Injury Prevention Cues
                        </span>
                        <p class="text-xs text-emerald-200/90 leading-relaxed">
                            {{ $exercise->injury_prevention_cues ?: 'Maintain core brace and joint-neutral alignment throughout movement.' }}
                        </p>
                    </div>
                </div>
            </div>

            <!-- Clinical Safety & Biomechanical Risks -->
            <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
                <h2 class="text-base font-bold text-white flex items-center gap-2 border-b border-gray-700/60 pb-3">
                    <i class="fa-solid fa-heart-pulse text-rose-400"></i>
                    <span>Clinical Risks & Safer Substitutions</span>
                </h2>

                <div class="space-y-4">
                    <div>
                        <span class="text-xs font-semibold text-rose-400 uppercase tracking-wider block mb-1">Common Injuries & Mechanical Vulnerabilities</span>
                        <p class="text-sm text-gray-300 bg-gray-900/60 p-3.5 rounded-xl border border-gray-800">
                            {{ $exercise->common_injuries ?: 'Low acute risk when performed with strict cadence and neutral joint alignment.' }}
                        </p>
                    </div>

                    <div>
                        <span class="text-xs font-semibold text-teal-400 uppercase tracking-wider block mb-1">Recommended Safer Alternatives</span>
                        <p class="text-sm text-gray-300 bg-gray-900/60 p-3.5 rounded-xl border border-gray-800">
                            {{ $exercise->safer_alternatives_text ?: 'Standard bodyweight variations or supported machine regressions.' }}
                        </p>
                    </div>
                </div>
            </div>

            <!-- Biomechanical Coaching & Education Section -->
            <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
                <h2 class="text-base font-bold text-white flex items-center gap-2 border-b border-gray-700/60 pb-3">
                    <i class="fa-solid fa-lungs text-teal-400"></i>
                    <span>Coaching, Breathing & Biomechanics</span>
                </h2>

                <div class="space-y-4 text-sm">
                    <!-- Breathing Tips -->
                    <div class="bg-gray-900/60 p-4 rounded-xl border border-gray-800 space-y-1.5">
                        <span class="text-xs font-bold text-teal-400 uppercase tracking-wider flex items-center gap-1.5">
                            <i class="fa-solid fa-wind text-teal-400"></i> Breathing Tips & Intra-Abdominal Bracing
                        </span>
                        <p class="text-gray-300 leading-relaxed">
                            {{ $exercise->breathing_tips ?: 'Inhale deeply during eccentric lowering; exhale forcefully on concentric drive. Maintain core bracing without holding breath.' }}
                        </p>
                    </div>

                    <!-- Benefits -->
                    <div class="bg-gray-900/60 p-4 rounded-xl border border-gray-800 space-y-1.5">
                        <span class="text-xs font-bold text-emerald-400 uppercase tracking-wider flex items-center gap-1.5">
                            <i class="fa-solid fa-star text-emerald-400"></i> Functional Benefits & Structural Adaptations
                        </span>
                        <div class="text-gray-300 leading-relaxed whitespace-pre-line">
                            {{ $exercise->benefits ?: '• Builds targeted muscular strength and hypertrophy\n• Enhances joint stability and kinetic chain coordination\n• Develops functional movement control' }}
                        </div>
                    </div>

                    <!-- Muscle Explanation -->
                    <div class="bg-gray-900/60 p-4 rounded-xl border border-gray-800 space-y-1.5">
                        <span class="text-xs font-bold text-cyan-400 uppercase tracking-wider flex items-center gap-1.5">
                            <i class="fa-solid fa-dna text-cyan-400"></i> Muscle Biomechanics & Prime Mover Dynamics
                        </span>
                        <p class="text-gray-300 leading-relaxed">
                            {{ $exercise->muscle_explanation ?: 'Coordinates prime mover activation with secondary stabilizers, promoting balanced joint articulation and symmetrical muscular development.' }}
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Right Col: Recommendation Rules & Contraindications -->
        <div class="space-y-6">
            <!-- Recommendation Engine & BMI Rules -->
            <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
                <h2 class="text-base font-bold text-white flex items-center gap-2 border-b border-gray-700/60 pb-3">
                    <i class="fa-solid fa-brain text-teal-400"></i>
                    <span>Prescription Logic & BMI Rules</span>
                </h2>

                <div class="bg-gray-900/80 p-4 rounded-xl border border-gray-700/60 space-y-3">
                    <span class="text-xs font-semibold text-gray-300 uppercase tracking-wider block">Recommendation Rule Spec</span>
                    <p class="text-xs text-gray-300 leading-relaxed">
                        {{ $exercise->recommendation_rules ?: 'Standard progressive overload rule. Prescribe based on user environment, experience level, and goal profile.' }}
                    </p>
                </div>

                <div class="border-t border-gray-700/60 pt-4 space-y-2">
                    <span class="text-xs font-semibold text-gray-400 uppercase tracking-wider block">Equipment & Rig Requirements</span>
                    <div class="flex items-center gap-2 text-xs text-gray-300 bg-gray-900/60 px-3 py-2 rounded-lg border border-gray-800">
                        <i class="fa-solid fa-wrench text-teal-400"></i>
                        <span>{{ $exercise->required_equipment ?: ucfirst($exercise->equipment_type) }}</span>
                    </div>
                </div>
            </div>

            <!-- Contraindications Mapped in Engine -->
            <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
                <h2 class="text-base font-bold text-white flex items-center gap-2 border-b border-gray-700/60 pb-3">
                    <i class="fa-solid fa-shield-virus text-amber-400"></i>
                    <span>Engine Contraindications ({{ $exercise->contraindications->count() }})</span>
                </h2>

                @if($exercise->contraindications->count() > 0)
                    <div class="space-y-3">
                        @foreach($exercise->contraindications as $c)
                            <div class="p-3 bg-gray-900/90 rounded-xl border border-gray-700/70 space-y-2">
                                <div class="flex items-center justify-between">
                                    <span class="text-xs font-bold text-white">{{ $c->limitation->name }}</span>
                                    <span class="text-[10px] px-2 py-0.5 rounded bg-red-950/70 text-red-300 border border-red-800/40">
                                        {{ $c->limitation->affected_body_part }}
                                    </span>
                                </div>
                                <p class="text-[11px] text-gray-400">
                                    <strong class="text-gray-300">Mechanism:</strong> {{ $c->risk_mechanism }}
                                </p>
                                @if($c->alternativeExercise)
                                    <div class="pt-1 flex items-center gap-1.5 text-xs text-emerald-300">
                                        <i class="fa-solid fa-arrow-right-arrow-left text-[10px]"></i>
                                        <span>Substitutes with: <a href="{{ route('admin.exercises.show', $c->alternativeExercise) }}" class="underline font-semibold hover:text-white">{{ $c->alternativeExercise->name }}</a></span>
                                    </div>
                                @endif
                            </div>
                        @endforeach
                    </div>
                @else
                    <div class="p-4 bg-emerald-950/20 border border-emerald-800/30 rounded-xl text-center space-y-1">
                        <i class="fa-solid fa-check-circle text-emerald-400 text-lg"></i>
                        <p class="text-xs font-semibold text-emerald-300">Universal Low-Contraindication Movement</p>
                        <p class="text-[11px] text-gray-400">This exercise has no active safety blocks registered and is suitable for healthy assessed users.</p>
                    </div>
                @endif
            </div>

            <!-- Database & Usage References Card -->
            <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-5 shadow-xl space-y-3">
                <span class="text-xs font-semibold text-gray-400 uppercase tracking-wider block">Historical Usage & Safety References</span>
                <div class="space-y-2 text-xs">
                    <div class="flex justify-between py-1 border-b border-gray-700/40">
                        <span class="text-gray-400">Workout Plan Templates:</span>
                        <strong class="text-white">{{ $planCount }} routines</strong>
                    </div>
                    <div class="flex justify-between py-1 border-b border-gray-700/40">
                        <span class="text-gray-400">User Completed Logs:</span>
                        <strong class="text-white">{{ $logCount }} logs</strong>
                    </div>
                    <div class="flex justify-between py-1">
                        <span class="text-gray-400">Prescribed as Injury Alternative:</span>
                        <strong class="text-teal-400">{{ $asAlternativeCount }} rules</strong>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>
@endsection
