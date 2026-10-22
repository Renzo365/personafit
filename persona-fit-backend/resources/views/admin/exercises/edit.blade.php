@extends('layouts.admin')

@section('content')
<div class="max-w-4xl mx-auto space-y-6">
    <div class="flex items-center space-x-4">
        <a href="{{ route('admin.exercises.index') }}" class="p-2 bg-gray-800 hover:bg-gray-700 rounded-lg text-gray-400 hover:text-white transition">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
        <div>
            <h1 class="text-2xl font-bold text-white flex items-center gap-2">
                <i class="fa-solid fa-pen-to-square text-teal-400"></i>
                <span>Edit Movement: {{ $exercise->name }}</span>
            </h1>
            <p class="text-sm text-gray-400">Update movement biomechanics, classification, environment compatibility, and injury safeguards</p>
        </div>
    </div>

    <form method="POST" action="{{ route('admin.exercises.update', $exercise) }}" enctype="multipart/form-data" class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-6">
        @csrf
        @method('PUT')

        <!-- Basic Movement Info -->
        <div class="border-b border-gray-700/60 pb-6 space-y-6">
            <div class="flex items-center justify-between">
                <h2 class="text-sm font-bold text-teal-400 uppercase tracking-wider flex items-center gap-2">
                    <i class="fa-solid fa-dumbbell text-xs"></i> 1. Movement Classification & Details
                </h2>
                <!-- Active Status Toggle -->
                <label class="flex items-center gap-2 cursor-pointer bg-gray-900 px-3 py-1.5 rounded-lg border border-gray-700">
                    <input type="checkbox" name="is_active" value="1" {{ old('is_active', $exercise->is_active) ? 'checked' : '' }} class="w-4 h-4 rounded text-teal-600 bg-gray-800 border-gray-700 focus:ring-teal-500">
                    <span class="text-xs font-semibold text-gray-200">Active in Library & Engine</span>
                </label>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- 3-Tab Movement Animation Asset Pickers -->
                <div class="md:col-span-2 bg-gray-900/90 border border-gray-700/80 rounded-xl p-4 space-y-4">
                    <div class="flex items-center justify-between border-b border-gray-800 pb-2">
                        <div>
                            <label class="block text-xs font-bold text-teal-400 uppercase tracking-wider flex items-center gap-2">
                                <i class="fa-solid fa-photo-film"></i>
                                <span>Interactive Demonstration GIF Assets (Per Exercise ID)</span>
                            </label>
                            <p class="text-[11px] text-gray-400 mt-0.5">Assign distinct looping GIF assets for each interactive tab. You can browse existing assets or upload new GIF/video files directly.</p>
                        </div>
                    </div>

                    <!-- 1. Proper Form Asset -->
                    <div class="bg-gray-950/80 border border-gray-800 rounded-xl p-3 flex flex-col sm:flex-row gap-3 items-center">
                        <div class="flex-1 w-full space-y-2">
                            <div class="flex items-center justify-between mb-1">
                                <span class="text-xs font-semibold text-teal-300 flex items-center gap-1.5">
                                    <i class="fa-solid fa-circle-check text-[10px]"></i> 1. Proper Form Animation Asset *
                                </span>
                                <button type="button" onclick="openAssetPicker('proper_form_asset')" class="px-2.5 py-1 bg-teal-600/80 hover:bg-teal-500 text-white text-[11px] font-semibold rounded shadow transition flex items-center gap-1">
                                    <i class="fa-solid fa-images"></i> Browse Assets
                                </button>
                            </div>
                            <input type="text" name="proper_form_asset" id="proper_form_asset_input" value="{{ old('proper_form_asset', $exercise->proper_form_asset ?: $exercise->image_url) }}" placeholder="e.g. assets/pushup_proper.gif"
                                oninput="updateAssetPreview(this.value, 'proper_form_asset')"
                                class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-1.5 text-xs text-white focus:outline-none focus:border-teal-500 font-mono">
                            <div class="flex items-center gap-2 pt-1">
                                <span class="text-[10px] text-gray-400 font-semibold shrink-0"><i class="fa-solid fa-upload"></i> Or Upload GIF:</span>
                                <input type="file" name="proper_form_file" accept=".gif,.mp4,.webm,.png,.jpg,.jpeg" class="text-[11px] text-gray-400 file:mr-2 file:py-1 file:px-2.5 file:rounded-md file:border-0 file:text-[11px] file:bg-gray-800 file:text-teal-400 hover:file:bg-gray-700 cursor-pointer">
                            </div>
                            <input type="hidden" name="image_url" id="image_url_input" value="{{ old('image_url', $exercise->image_url) }}">
                        </div>
                        <div class="w-20 h-20 bg-gray-900 border border-gray-800 rounded-lg flex items-center justify-center overflow-hidden shrink-0">
                            <img id="proper_form_asset_preview" src="{{ ($exercise->proper_form_asset ?: $exercise->image_url) ? asset($exercise->proper_form_asset ?: $exercise->image_url) : '' }}" class="h-full w-full object-contain p-1 {{ ($exercise->proper_form_asset ?: $exercise->image_url) ? '' : 'hidden' }}">
                            <span id="proper_form_asset_placeholder" class="{{ ($exercise->proper_form_asset ?: $exercise->image_url) ? 'hidden' : 'block' }} text-[10px] text-gray-500">No Asset</span>
                        </div>
                    </div>

                    <!-- 2. Common Mistakes Asset -->
                    <div class="bg-gray-950/80 border border-gray-800 rounded-xl p-3 flex flex-col sm:flex-row gap-3 items-center">
                        <div class="flex-1 w-full space-y-2">
                            <div class="flex items-center justify-between mb-1">
                                <span class="text-xs font-semibold text-rose-400 flex items-center gap-1.5">
                                    <i class="fa-solid fa-triangle-exclamation text-[10px]"></i> 2. Common Mistakes Animation (Optional)
                                </span>
                                <button type="button" onclick="openAssetPicker('common_mistakes_asset')" class="px-2.5 py-1 bg-rose-600/80 hover:bg-rose-500 text-white text-[11px] font-semibold rounded shadow transition flex items-center gap-1">
                                    <i class="fa-solid fa-images"></i> Browse Assets
                                </button>
                            </div>
                            <input type="text" name="common_mistakes_asset" id="common_mistakes_asset_input" value="{{ old('common_mistakes_asset', $exercise->common_mistakes_asset) }}" placeholder="e.g. assets/pushup_mistake.gif (leave empty for graceful overlay fallback)"
                                oninput="updateAssetPreview(this.value, 'common_mistakes_asset')"
                                class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-1.5 text-xs text-white focus:outline-none focus:border-rose-500 font-mono">
                            <div class="flex items-center gap-2 pt-1">
                                <span class="text-[10px] text-gray-400 font-semibold shrink-0"><i class="fa-solid fa-upload"></i> Or Upload GIF:</span>
                                <input type="file" name="common_mistakes_file" accept=".gif,.mp4,.webm,.png,.jpg,.jpeg" class="text-[11px] text-gray-400 file:mr-2 file:py-1 file:px-2.5 file:rounded-md file:border-0 file:text-[11px] file:bg-gray-800 file:text-rose-400 hover:file:bg-gray-700 cursor-pointer">
                            </div>
                        </div>
                        <div class="w-20 h-20 bg-gray-900 border border-gray-800 rounded-lg flex items-center justify-center overflow-hidden shrink-0">
                            <img id="common_mistakes_asset_preview" src="{{ $exercise->common_mistakes_asset ? asset($exercise->common_mistakes_asset) : '' }}" class="h-full w-full object-contain p-1 {{ $exercise->common_mistakes_asset ? '' : 'hidden' }}">
                            <span id="common_mistakes_asset_placeholder" class="{{ $exercise->common_mistakes_asset ? 'hidden' : 'block' }} text-[10px] text-gray-500 text-center px-1">Kinematic Fallback</span>
                        </div>
                    </div>

                    <!-- 3. Injury Prevention Asset -->
                    <div class="bg-gray-950/80 border border-gray-800 rounded-xl p-3 flex flex-col sm:flex-row gap-3 items-center">
                        <div class="flex-1 w-full space-y-2">
                            <div class="flex items-center justify-between mb-1">
                                <span class="text-xs font-semibold text-emerald-400 flex items-center gap-1.5">
                                    <i class="fa-solid fa-shield-halved text-[10px]"></i> 3. Injury Prevention Animation (Optional)
                                </span>
                                <button type="button" onclick="openAssetPicker('injury_prevention_asset')" class="px-2.5 py-1 bg-emerald-600/80 hover:bg-emerald-500 text-white text-[11px] font-semibold rounded shadow transition flex items-center gap-1">
                                    <i class="fa-solid fa-images"></i> Browse Assets
                                </button>
                            </div>
                            <input type="text" name="injury_prevention_asset" id="injury_prevention_asset_input" value="{{ old('injury_prevention_asset', $exercise->injury_prevention_asset) }}" placeholder="e.g. assets/pushup_incline_prevention.gif (leave empty for graceful overlay fallback)"
                                oninput="updateAssetPreview(this.value, 'injury_prevention_asset')"
                                class="w-full bg-gray-900 border border-gray-700 rounded-lg px-3 py-1.5 text-xs text-white focus:outline-none focus:border-emerald-500 font-mono">
                            <div class="flex items-center gap-2 pt-1">
                                <span class="text-[10px] text-gray-400 font-semibold shrink-0"><i class="fa-solid fa-upload"></i> Or Upload GIF:</span>
                                <input type="file" name="injury_prevention_file" accept=".gif,.mp4,.webm,.png,.jpg,.jpeg" class="text-[11px] text-gray-400 file:mr-2 file:py-1 file:px-2.5 file:rounded-md file:border-0 file:text-[11px] file:bg-gray-800 file:text-emerald-400 hover:file:bg-gray-700 cursor-pointer">
                            </div>
                        </div>
                        <div class="w-20 h-20 bg-gray-900 border border-gray-800 rounded-lg flex items-center justify-center overflow-hidden shrink-0">
                            <img id="injury_prevention_asset_preview" src="{{ $exercise->injury_prevention_asset ? asset($exercise->injury_prevention_asset) : '' }}" class="h-full w-full object-contain p-1 {{ $exercise->injury_prevention_asset ? '' : 'hidden' }}">
                            <span id="injury_prevention_asset_placeholder" class="{{ $exercise->injury_prevention_asset ? 'hidden' : 'block' }} text-[10px] text-gray-500 text-center px-1">Kinematic Fallback</span>
                        </div>
                    </div>
                </div>

                <!-- Exercise Name -->
                <div class="md:col-span-2">
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Exercise Name *</label>
                    <input type="text" name="name" value="{{ old('name', $exercise->name) }}" required
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500 @error('name') border-red-500 @enderror">
                    @error('name') <p class="text-xs text-red-400 mt-1">{{ $message }}</p> @enderror
                </div>

                <!-- Description -->
                <div class="md:col-span-2">
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Description</label>
                    <textarea name="description" rows="2" placeholder="Brief clinical / biomechanical overview of movement mechanics..."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500 @error('description') border-red-500 @enderror">{{ old('description', $exercise->description) }}</textarea>
                    @error('description') <p class="text-xs text-red-400 mt-1">{{ $message }}</p> @enderror
                </div>

                <!-- Primary Target Muscle -->
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Primary Target Muscle *</label>
                    <select name="target_muscle" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500 @error('target_muscle') border-red-500 @enderror">
                        @foreach($targetMuscles as $muscle)
                            <option value="{{ $muscle }}" {{ old('target_muscle', $exercise->target_muscle) === $muscle ? 'selected' : '' }}>{{ $muscle }}</option>
                        @endforeach
                    </select>
                    @error('target_muscle') <p class="text-xs text-red-400 mt-1">{{ $message }}</p> @enderror
                </div>

                <!-- Secondary Muscles -->
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Secondary Muscles</label>
                    <input type="text" name="secondary_muscles" id="secondary_muscles_input" value="{{ old('secondary_muscles', $exercise->secondary_muscles) }}" placeholder="e.g. Front Delts, Triceps"
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                </div>

                <!-- DYNAMIC ANATOMY HIGHLIGHT & MUSCLE MAPPING SECTION -->
                <div class="md:col-span-2 bg-gray-900/90 border border-gray-700/80 rounded-xl p-5 space-y-4">
                    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-gray-800 pb-3">
                        <div>
                            <label class="block text-xs font-bold text-cyan-400 uppercase tracking-wider flex items-center gap-2">
                                <i class="fa-solid fa-person-running"></i>
                                <span>Automatic Anatomy Highlight & Biomechanical Mapping</span>
                            </label>
                            <p class="text-[11px] text-gray-400 mt-0.5">
                                Displays verified target muscle highlights on your permanent anatomy template. Auto-detects highlights from the exercise GIF or customize them below.
                            </p>
                        </div>
                        <div class="flex items-center gap-2">
                            <span id="confidence_badge" class="px-2.5 py-1 text-[11px] font-bold rounded-lg border {{ ($exercise->muscle_mapping_confidence ?? 'high') === 'high' ? 'bg-emerald-950/80 text-emerald-400 border-emerald-700/60' : (($exercise->muscle_mapping_confidence ?? '') === 'medium' ? 'bg-amber-950/80 text-amber-400 border-amber-700/60' : 'bg-red-950/80 text-red-400 border-red-700/60') }}">
                                <i class="fa-solid fa-circle-check text-[10px]"></i>
                                <span id="confidence_text">{{ strtoupper($exercise->muscle_mapping_confidence ?? 'HIGH') }} CONFIDENCE</span>
                            </span>
                            <button type="button" onclick="autoAnalyzeCurrentGif()" class="px-3 py-1.5 bg-gradient-to-r from-teal-600 to-cyan-600 hover:from-teal-500 hover:to-cyan-500 text-white text-xs font-bold rounded-lg shadow-md transition flex items-center gap-1.5 cursor-pointer">
                                <i class="fa-solid fa-wand-magic-sparkles"></i> Auto-Map from GIF
                            </button>
                        </div>
                    </div>

                    <!-- Hidden Inputs -->
                    <input type="hidden" name="anatomy_view" id="anatomy_view_input" value="{{ old('anatomy_view', $exercise->anatomy_view ?? 'dual') }}">
                    <input type="hidden" name="muscle_mapping_confidence" id="muscle_mapping_confidence_input" value="{{ old('muscle_mapping_confidence', $exercise->muscle_mapping_confidence ?? 'high') }}">

                    <div class="grid grid-cols-1 lg:grid-cols-12 gap-5 items-start">
                        <!-- Anatomy Diagram Preview Viewport (lg:col-span-7) -->
                        <div class="lg:col-span-7 bg-gray-950 border border-gray-800 rounded-xl p-3 flex flex-col items-center">
                            <!-- View Controls -->
                            <div class="w-full flex items-center justify-between mb-2 px-1">
                                <span class="text-[11px] font-bold text-gray-400 uppercase tracking-wider">Template Preview</span>
                                <div class="inline-flex rounded-lg bg-gray-900 p-0.5 border border-gray-800 text-[11px] font-medium" role="group">
                                    <button type="button" onclick="setAnatomyView('dual')" id="view_btn_dual" class="px-2.5 py-1 rounded-md transition font-semibold text-teal-400 bg-gray-800 shadow-sm cursor-pointer">Dual (Front & Back)</button>
                                    <button type="button" onclick="setAnatomyView('anterior')" id="view_btn_anterior" class="px-2.5 py-1 rounded-md transition font-semibold text-gray-400 hover:text-white cursor-pointer">Anterior (Front)</button>
                                    <button type="button" onclick="setAnatomyView('posterior')" id="view_btn_posterior" class="px-2.5 py-1 rounded-md transition font-semibold text-gray-400 hover:text-white cursor-pointer">Posterior (Back)</button>
                                </div>
                            </div>

                            <!-- Anatomy Canvas Container -->
                            <div id="anatomy_container" class="relative w-full max-w-[480px] aspect-[883/655] bg-gray-900/60 rounded-lg overflow-hidden border border-gray-800 shadow-inner flex items-center justify-center">
                                <!-- Base clean template -->
                                <img id="anatomy_base_img" src="{{ asset('assets/anatomy/anatomy_template_clean.png') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none select-none transition-transform duration-300 origin-center" alt="Anatomy Template">

                                <!-- Dynamic Mask Layers Overlay -->
                                <div id="anatomy_masks_layer" class="absolute inset-0 w-full h-full pointer-events-none select-none transition-transform duration-300 origin-center">
                                    <!-- Masks are injected dynamically via JS -->
                                </div>

                                <!-- Dynamic Muscle Name Labels Overlay -->
                                <div id="anatomy_labels_layer" class="absolute inset-0 w-full h-full pointer-events-none select-none transition-transform duration-300 origin-center z-10">
                                    <!-- Active hit muscle labels injected dynamically via JS -->
                                </div>

                                <!-- Legend Indicator -->
                                <div class="absolute bottom-2 left-2 flex items-center gap-3 bg-gray-950/85 backdrop-blur-sm border border-gray-800 px-2.5 py-1 rounded-lg text-[10px] z-20">
                                    <span class="flex items-center gap-1 text-emerald-400 font-semibold"><span class="w-2.5 h-2.5 rounded-full bg-emerald-500 shadow-sm shadow-emerald-500/50"></span> Primary Target</span>
                                    <span class="flex items-center gap-1 text-cyan-400 font-semibold"><span class="w-2.5 h-2.5 rounded-full bg-cyan-400 shadow-sm shadow-cyan-400/50"></span> Secondary</span>
                                </div>
                            </div>

                            <!-- Active Hit Muscles Summary Tags -->
                            <div class="w-full mt-3 pt-2.5 border-t border-gray-800/80 flex flex-col gap-1.5">
                                <div class="flex items-center justify-between text-[11px] px-1">
                                    <span class="font-bold text-gray-400 uppercase tracking-wider flex items-center gap-1.5">
                                        <i class="fa-solid fa-bullseye text-emerald-400 text-xs"></i> Muscles Hit On Exercise:
                                    </span>
                                    <span id="active_muscles_count" class="text-[10px] font-semibold text-teal-400">0 Muscles</span>
                                </div>
                                <div id="active_muscles_summary" class="flex flex-wrap items-center gap-1.5 min-h-[26px] px-1">
                                    <span class="text-[11px] text-gray-500 italic">No muscle regions selected yet</span>
                                </div>
                            </div>
                        </div>

                        <!-- Muscle Region Selection Chips (lg:col-span-5) -->
                        <div class="lg:col-span-5 space-y-3">
                            <div class="text-xs text-gray-400 flex items-center justify-between">
                                <span class="font-bold text-gray-300 uppercase tracking-wider">Target Muscle Regions</span>
                                <span class="text-[10px] text-gray-500">Click: Off → Primary → Secondary</span>
                            </div>

                            <!-- Anterior Muscle Chips -->
                            <div class="bg-gray-950/70 border border-gray-800/80 rounded-lg p-3">
                                <span class="block text-[11px] font-bold text-teal-400 uppercase tracking-wider mb-2 flex items-center gap-1.5">
                                    <i class="fa-solid fa-person text-[10px]"></i> Anterior (Front) Muscles
                                </span>
                                <div class="flex flex-wrap gap-1.5" id="anterior_chips">
                                    @foreach($availableRegions['anterior'] ?? [] as $key => $label)
                                        <button type="button" onclick="cycleRegionState('{{ $key }}')" id="chip_{{ $key }}" data-region="{{ $key }}"
                                            class="px-2 py-1 rounded-md text-[11px] font-medium border transition flex items-center gap-1.5 bg-gray-900 text-gray-400 border-gray-800 hover:border-gray-700 cursor-pointer">
                                            <span id="dot_{{ $key }}" class="w-2 h-2 rounded-full border border-gray-600 bg-transparent"></span>
                                            <span>{{ $label }}</span>
                                        </button>
                                    @endforeach
                                </div>
                            </div>

                            <!-- Posterior Muscle Chips -->
                            <div class="bg-gray-950/70 border border-gray-800/80 rounded-lg p-3">
                                <span class="block text-[11px] font-bold text-cyan-400 uppercase tracking-wider mb-2 flex items-center gap-1.5">
                                    <i class="fa-solid fa-person-running text-[10px]"></i> Posterior (Back) Muscles
                                </span>
                                <div class="flex flex-wrap gap-1.5" id="posterior_chips">
                                    @foreach($availableRegions['posterior'] ?? [] as $key => $label)
                                        <button type="button" onclick="cycleRegionState('{{ $key }}')" id="chip_{{ $key }}" data-region="{{ $key }}"
                                            class="px-2 py-1 rounded-md text-[11px] font-medium border transition flex items-center gap-1.5 bg-gray-900 text-gray-400 border-gray-800 hover:border-gray-700 cursor-pointer">
                                            <span id="dot_{{ $key }}" class="w-2 h-2 rounded-full border border-gray-600 bg-transparent"></span>
                                            <span>{{ $label }}</span>
                                        </button>
                                    @endforeach
                                </div>
                            </div>

                            <!-- Hidden Form Inputs for Primary & Secondary Regions -->
                            <div id="hidden_region_inputs"></div>
                        </div>
                    </div>
                </div>

                <!-- Exercise Type: Compound / Isolation -->
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Exercise Type *</label>
                    <select name="exercise_type" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                        <option value="compound" {{ old('exercise_type', $exercise->exercise_type) === 'compound' ? 'selected' : '' }}>Compound (Multi-joint movement)</option>
                        <option value="isolation" {{ old('exercise_type', $exercise->exercise_type) === 'isolation' ? 'selected' : '' }}>Isolation (Single-joint movement)</option>
                    </select>
                </div>

                <!-- Workout Environment: Home, Gym, Both -->
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Workout Environment *</label>
                    <select name="workout_environment" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                        <option value="both" {{ old('workout_environment', $exercise->workout_environment) === 'both' ? 'selected' : '' }}>Both (Home & Gym compatible)</option>
                        <option value="home" {{ old('workout_environment', $exercise->workout_environment) === 'home' ? 'selected' : '' }}>Home Workout Only</option>
                        <option value="gym" {{ old('workout_environment', $exercise->workout_environment) === 'gym' ? 'selected' : '' }}>Gym Workout Only</option>
                    </select>
                </div>

                <!-- Equipment Category -->
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Equipment Category *</label>
                    <select name="equipment_type" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                        @foreach(['bodyweight', 'dumbbell', 'barbell', 'machine', 'cable', 'bands'] as $eq)
                            <option value="{{ $eq }}" {{ old('equipment_type', $exercise->equipment_type) === $eq ? 'selected' : '' }}>{{ ucfirst($eq) }}</option>
                        @endforeach
                    </select>
                </div>

                <!-- Required Equipment Specifics -->
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Required Equipment Specifics</label>
                    <input type="text" name="required_equipment" value="{{ old('required_equipment', $exercise->required_equipment) }}" placeholder="e.g. Flat Bench, Pair of Dumbbells, or Cable Column"
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                </div>

                <!-- Difficulty Level -->
                <div class="md:col-span-2">
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Difficulty Level *</label>
                    <select name="difficulty" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">
                        <option value="beginner" {{ old('difficulty', $exercise->difficulty) === 'beginner' ? 'selected' : '' }}>Beginner</option>
                        <option value="intermediate" {{ old('difficulty', $exercise->difficulty) === 'intermediate' ? 'selected' : '' }}>Intermediate</option>
                        <option value="advanced" {{ old('difficulty', $exercise->difficulty) === 'advanced' ? 'selected' : '' }}>Advanced</option>
                    </select>
                </div>

                <!-- Safe Baseline & Volume Guidance -->
                <div class="md:col-span-2 bg-gray-900/60 border border-gray-700/60 rounded-xl p-4 space-y-3">
                    <h3 class="text-xs font-bold text-teal-400 uppercase tracking-wider flex items-center gap-2">
                        <i class="fa-solid fa-sliders"></i>
                        <span>Recommended Baseline & Volume Guidance</span>
                    </h3>
                    <div class="grid grid-cols-1 sm:grid-cols-4 gap-4">
                        <div>
                            <label class="block text-[11px] font-semibold text-gray-300 uppercase mb-1">Target Sets</label>
                            <input type="number" name="default_sets" min="1" max="10" value="{{ old('default_sets', $exercise->default_sets ?? 3) }}"
                                class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                        </div>
                        <div>
                            <label class="block text-[11px] font-semibold text-gray-300 uppercase mb-1">Target Reps / Duration</label>
                            <input type="text" name="default_reps" value="{{ old('default_reps', $exercise->default_reps ?? '8-12 reps') }}" placeholder="e.g. 8-12 reps or 30-45s"
                                class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                        </div>
                        <div>
                            <label class="block text-[11px] font-semibold text-gray-300 uppercase mb-1">Safe Starting Load</label>
                            <input type="text" name="safe_starting_load" value="{{ old('safe_starting_load', $exercise->safe_starting_load) }}" placeholder="e.g. Bodyweight or 15-20 kg"
                                class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                        </div>
                        <div>
                            <label class="block text-[11px] font-semibold text-gray-300 uppercase mb-1">Rest Interval (Seconds)</label>
                            <input type="number" name="default_rest_seconds" min="0" max="600" value="{{ old('default_rest_seconds', $exercise->default_rest_seconds ?? 60) }}"
                                class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Contraindications & Safety Limitations -->
        <div class="border-b border-gray-700/60 pb-6 space-y-4">
            <div>
                <h2 class="text-sm font-bold text-teal-400 uppercase tracking-wider flex items-center gap-2">
                    <i class="fa-solid fa-triangle-exclamation text-xs text-amber-400"></i> 2. Contraindications & Safety Limitations
                </h2>
                <p class="text-xs text-gray-400 mt-1">
                    Manage injury restrictions for this exercise. When an assessed user has one of these conditions, the recommendation engine will safeguard them by prescribing safe alternative substitutions.
                </p>
            </div>

            <div class="space-y-3">
                @foreach($limitations as $limitation)
                    @php
                        $rule = $activeContraindications->get($limitation->id);
                        $isSelected = old('contraindication_limitations') !== null 
                            ? in_array($limitation->id, old('contraindication_limitations', []))
                            : ($rule !== null);
                        $currentRisk = old('risk_mechanisms.' . $limitation->id, $rule?->risk_mechanism);
                        $currentAlt = old('alternative_exercise_ids.' . $limitation->id, $rule?->alternative_exercise_id);
                    @endphp
                    <div class="bg-gray-900/70 border border-gray-700/70 rounded-xl p-4 transition" id="limitation-card-{{ $limitation->id }}">
                        <div class="flex items-start gap-3">
                            <input type="checkbox" name="contraindication_limitations[]" value="{{ $limitation->id }}" id="lim-{{ $limitation->id }}"
                                {{ $isSelected ? 'checked' : '' }}
                                onchange="toggleContraindicationFields({{ $limitation->id }})"
                                class="mt-1 w-4 h-4 rounded text-teal-600 bg-gray-800 border-gray-700 focus:ring-teal-500">
                            <label for="lim-{{ $limitation->id }}" class="flex-1 cursor-pointer">
                                <span class="text-sm font-semibold text-white">{{ $limitation->name }}</span>
                                <span class="text-xs px-2 py-0.5 rounded bg-red-950/60 text-red-300 border border-red-800/40 ml-2">
                                    {{ $limitation->affected_body_part }}
                                </span>
                                <p class="text-xs text-gray-400 mt-0.5">{{ $limitation->description }}</p>
                            </label>
                        </div>

                        <!-- Expanded Fields when Checked -->
                        <div id="contra-fields-{{ $limitation->id }}" class="{{ $isSelected ? 'block' : 'hidden' }} mt-4 pt-3 border-t border-gray-800 grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label class="block text-[11px] font-semibold text-gray-300 uppercase mb-1">Risk Mechanism / Reason</label>
                                <input type="text" name="risk_mechanisms[{{ $limitation->id }}]" 
                                    value="{{ $currentRisk }}" 
                                    placeholder="e.g. Axial loading exacerbates lumbar disc compression"
                                    class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                            </div>
                            <div>
                                <label class="block text-[11px] font-semibold text-gray-300 uppercase mb-1">Recommended Safe Alternative</label>
                                <select name="alternative_exercise_ids[{{ $limitation->id }}]" 
                                    class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                                    <option value="">No specific alternative (use general safe exercises)</option>
                                    @foreach($allExercises as $alt)
                                        <option value="{{ $alt->id }}" {{ $currentAlt == $alt->id ? 'selected' : '' }}>
                                            {{ $alt->name }}
                                        </option>
                                    @endforeach
                                </select>
                            </div>
                        </div>
                    </div>
                @endforeach
            </div>
        </div>

        <!-- Instructions, Mistakes & Prevention Cues -->
        <div class="border-b border-gray-700/60 pb-6 space-y-6">
            <h2 class="text-sm font-bold text-teal-400 uppercase tracking-wider flex items-center gap-2">
                <i class="fa-solid fa-list-check text-xs"></i> 3. Biomechanical Instructions & Safety Cues
            </h2>

            <div>
                <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Step-by-Step Instructions *</label>
                <textarea name="instructions" rows="4" required class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500 @error('instructions') border-red-500 @enderror">{{ old('instructions', $exercise->instructions) }}</textarea>
                @error('instructions') <p class="text-xs text-red-400 mt-1">{{ $message }}</p> @enderror
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Common Mistakes</label>
                    <textarea name="common_mistakes" rows="3" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('common_mistakes', $exercise->common_mistakes) }}</textarea>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Injury Prevention Cues</label>
                    <textarea name="injury_prevention_cues" rows="3" class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('injury_prevention_cues', $exercise->injury_prevention_cues) }}</textarea>
                </div>

                <div class="md:col-span-2">
                    <label class="block text-xs font-semibold text-teal-400 uppercase tracking-wider mb-2 flex items-center gap-1.5">
                        <i class="fa-solid fa-lungs"></i> Breathing Methods & Tips
                    </label>
                    <textarea name="breathing_tips" rows="3" placeholder="e.g. • Inhale: Steady eccentric breath as weight lowers. • Exhale: Drive up with pursed lips on concentric exertion."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('breathing_tips', $exercise->breathing_tips) }}</textarea>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Key Movement Benefits</label>
                    <textarea name="benefits" rows="3" placeholder="e.g. • Posture: Strengthens scapular retractors. • Hypertrophy: High mechanical tension on prime movers."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('benefits', $exercise->benefits) }}</textarea>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Biomechanical Muscle Explanation</label>
                    <textarea name="muscle_explanation" rows="3" placeholder="e.g. Coordinates prime mover contraction with secondary stabilizers to promote joint symmetry."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('muscle_explanation', $exercise->muscle_explanation) }}</textarea>
                </div>
            </div>
        </div>

        <!-- Clinical Risks, Safer Alternatives & BMI Rules -->
        <div class="space-y-6">
            <h2 class="text-sm font-bold text-teal-400 uppercase tracking-wider flex items-center gap-2">
                <i class="fa-solid fa-heart-pulse text-xs text-rose-400"></i> 4. Clinical Risks, Alternatives & Recommendation Rules
            </h2>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Common Injuries / Biomechanical Risks</label>
                    <textarea name="common_injuries" rows="3" placeholder="e.g. Wrist hyperextension, anterior shoulder impingement, lower back rounding..."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('common_injuries', $exercise->common_injuries) }}</textarea>
                </div>

                <div>
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">Safer Alternatives & Regressions (Text)</label>
                    <textarea name="safer_alternatives_text" rows="3" placeholder="e.g. Incline Push-ups, Dumbbell Floor Press, Wall Push-ups..."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('safer_alternatives_text', $exercise->safer_alternatives_text) }}</textarea>
                </div>

                <div class="md:col-span-2">
                    <label class="block text-xs font-semibold text-gray-300 uppercase tracking-wider mb-2">BMI & Recommendation Logic Rules</label>
                    <textarea name="recommendation_rules" rows="3" placeholder="e.g. Recommended for beginners with normal BMI. Regress for BMI >= 30 or wrist pain. Avoid for acute knee pain."
                        class="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2.5 text-sm text-white focus:outline-none focus:border-teal-500">{{ old('recommendation_rules', $exercise->recommendation_rules) }}</textarea>
                </div>
            </div>
        </div>

        <!-- Form Actions -->
        <div class="flex justify-end space-x-3 pt-6 border-t border-gray-700">
            <a href="{{ route('admin.exercises.index') }}" class="px-5 py-2.5 rounded-lg text-sm text-gray-300 hover:bg-gray-700 transition">Cancel</a>
            <button type="submit" class="px-6 py-2.5 bg-teal-600 hover:bg-teal-500 text-white text-sm font-semibold rounded-lg shadow-lg shadow-teal-500/20 transition flex items-center gap-2">
                <i class="fa-solid fa-floppy-disk"></i>
                <span>Update Exercise Movement</span>
            </button>
        </div>
    </form>
</div>

<!-- Asset Library Modal -->
<div id="assetPickerModal" class="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm hidden flex items-center justify-center p-4">
    <div class="bg-gray-900 border border-gray-700 rounded-2xl w-full max-w-4xl max-h-[85vh] flex flex-col shadow-2xl overflow-hidden">
        <!-- Modal Header -->
        <div class="p-4 border-b border-gray-800 flex items-center justify-between bg-gray-950">
            <div>
                <h3 class="text-base font-bold text-white flex items-center gap-2">
                    <i class="fa-solid fa-photo-film text-teal-400"></i>
                    <span>Exercise Asset Library (1,323 Movement GIFs)</span>
                </h3>
                <p class="text-xs text-gray-400 mt-0.5">Search or filter by target muscle & difficulty to select and update exercise attributes.</p>
            </div>
            <button type="button" onclick="closeAssetPicker()" class="p-2 text-gray-400 hover:text-white rounded-lg hover:bg-gray-800 transition">
                <i class="fa-solid fa-xmark text-lg"></i>
            </button>
        </div>

        <!-- Filters & Search -->
        <div class="p-3 border-b border-gray-800 bg-gray-900/90 grid grid-cols-1 sm:grid-cols-4 gap-2.5">
            <div class="sm:col-span-2">
                <input type="text" id="assetSearchInput" placeholder="Search exercise name or filename..." oninput="filterAssets()"
                    class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white placeholder-gray-500 focus:outline-none focus:border-teal-500">
            </div>
            <div>
                <select id="assetMuscleFilter" onchange="filterAssets()" class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                    <option value="">All Muscles</option>
                </select>
            </div>
            <div>
                <select id="assetLevelFilter" onchange="filterAssets()" class="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-xs text-white focus:outline-none focus:border-teal-500">
                    <option value="">All Difficulties</option>
                    <option value="beginner">Beginner</option>
                    <option value="intermediate">Intermediate</option>
                    <option value="advance">Advance</option>
                </select>
            </div>
        </div>

        <!-- Asset Grid -->
        <div class="p-4 overflow-y-auto flex-1 min-h-[300px]">
            <div id="assetsLoading" class="text-center py-16 text-gray-400 text-sm flex flex-col items-center">
                <i class="fa-solid fa-spinner fa-spin text-3xl text-teal-400 mb-3"></i>
                <span>Loading exercise movement catalog...</span>
            </div>
            <div id="assetsGrid" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3 hidden">
                <!-- Dynamically populated -->
            </div>
            <div id="assetsEmpty" class="text-center py-16 text-gray-400 text-sm hidden">
                <i class="fa-solid fa-magnifying-glass text-3xl text-gray-600 mb-3"></i>
                <p>No matching exercise assets found.</p>
            </div>
        </div>

        <!-- Modal Footer -->
        <div class="p-3 border-t border-gray-800 bg-gray-950 flex items-center justify-between text-xs text-gray-400">
            <span id="assetCountLabel">Showing 0 assets</span>
            <button type="button" onclick="closeAssetPicker()" class="px-4 py-1.5 bg-gray-800 hover:bg-gray-700 text-gray-300 rounded-lg transition">
                Close
            </button>
        </div>
    </div>
</div>

<script>
function toggleContraindicationFields(id) {
    const cb = document.getElementById('lim-' + id);
    const fields = document.getElementById('contra-fields-' + id);
    if (fields) {
        if (cb.checked) {
            fields.classList.remove('hidden');
            fields.classList.add('block');
        } else {
            fields.classList.remove('block');
            fields.classList.add('hidden');
        }
    }
}

let allAssetsData = [];
let filteredAssets = [];
let currentTargetAssetField = 'proper_form_asset';

function openAssetPicker(targetField = 'proper_form_asset') {
    currentTargetAssetField = targetField;
    const modal = document.getElementById('assetPickerModal');
    modal.classList.remove('hidden');
    if (allAssetsData.length === 0) {
        loadAssets();
    } else {
        filterAssets();
    }
}

function closeAssetPicker() {
    document.getElementById('assetPickerModal').classList.add('hidden');
}

function loadAssets() {
    fetch('{{ route("admin.exercises.assets-catalog") }}')
        .then(res => res.json())
        .then(data => {
            allAssetsData = data;
            const muscles = [...new Set(data.map(d => d.target_muscle))].sort();
            const muscleSelect = document.getElementById('assetMuscleFilter');
            muscles.forEach(m => {
                const opt = document.createElement('option');
                opt.value = m;
                opt.textContent = m;
                muscleSelect.appendChild(opt);
            });
            document.getElementById('assetsLoading').classList.add('hidden');
            document.getElementById('assetsGrid').classList.remove('hidden');
            filterAssets();
        })
        .catch(err => {
            console.error('Failed to load assets catalog:', err);
            document.getElementById('assetsLoading').innerHTML = '<span class="text-rose-400">Failed to load asset catalog.</span>';
        });
}

function filterAssets() {
    const search = document.getElementById('assetSearchInput').value.toLowerCase().trim();
    const muscle = document.getElementById('assetMuscleFilter').value;
    const level = document.getElementById('assetLevelFilter').value;

    filteredAssets = allAssetsData.filter(item => {
        if (search && !item.exercise_name.toLowerCase().includes(search) && !item.new_filename.toLowerCase().includes(search)) {
            return false;
        }
        if (muscle && item.target_muscle !== muscle) {
            return false;
        }
        if (level && item.exercise_level !== level) {
            return false;
        }
        return true;
    });

    renderAssetGrid(filteredAssets.slice(0, 60));
    document.getElementById('assetCountLabel').textContent = `Showing ${Math.min(60, filteredAssets.length)} of ${filteredAssets.length} assets`;
}

function renderAssetGrid(items) {
    const grid = document.getElementById('assetsGrid');
    const empty = document.getElementById('assetsEmpty');
    grid.innerHTML = '';

    if (items.length === 0) {
        empty.classList.remove('hidden');
        return;
    }
    empty.classList.add('hidden');

    items.forEach(item => {
        const card = document.createElement('div');
        card.className = 'bg-gray-950 border border-gray-800 hover:border-teal-500 rounded-xl p-2.5 cursor-pointer transition group flex flex-col justify-between';
        
        const assetPath = 'assets/' + item.new_filename;
        const assetUrl = '{{ asset("") }}' + assetPath;

        const levelBadgeClass = item.exercise_level === 'beginner' 
            ? 'bg-emerald-950/80 text-emerald-300 border-emerald-800/60' 
            : (item.exercise_level === 'advance' ? 'bg-purple-950/80 text-purple-300 border-purple-800/60' : 'bg-amber-950/80 text-amber-300 border-amber-800/60');

        card.innerHTML = `
            <div class="h-24 bg-gray-900 rounded-lg flex items-center justify-center overflow-hidden mb-2 relative">
                <img src="${assetUrl}" loading="lazy" class="h-full w-full object-contain p-1" alt="${item.exercise_name}">
                <span class="absolute top-1 right-1 text-[9px] font-semibold px-1.5 py-0.5 rounded border ${levelBadgeClass}">
                    ${item.exercise_level}
                </span>
            </div>
            <div>
                <p class="text-xs font-bold text-white group-hover:text-teal-400 transition truncate" title="${item.exercise_name}">${item.exercise_name}</p>
                <div class="flex items-center justify-between text-[10px] text-gray-400 mt-1">
                    <span class="truncate max-w-[80px]">${item.target_muscle}</span>
                    <span class="text-teal-400 font-medium">${item.exercise_type}</span>
                </div>
            </div>
        `;
        card.onclick = () => selectAsset(item);
        grid.appendChild(card);
    });
}

function selectAsset(item) {
    const assetPath = 'assets/' + item.new_filename;
    const input = document.getElementById(currentTargetAssetField + '_input');
    if (input) {
        input.value = assetPath;
    }
    if (currentTargetAssetField === 'proper_form_asset') {
        const legacyInput = document.getElementById('image_url_input');
        if (legacyInput) legacyInput.value = assetPath;
    }
    updateAssetPreview(assetPath, currentTargetAssetField);
    if (currentTargetAssetField === 'proper_form_asset') {
        autoAnalyzeCurrentGif(assetPath);
    }
    closeAssetPicker();
}

// ==================== DYNAMIC ANATOMY HIGHLIGHT MAPPING ====================
let primaryRegions = new Set({!! json_encode($exercise->primary_muscle_regions ?: []) !!});
let secondaryRegions = new Set({!! json_encode($exercise->secondary_muscle_regions ?: []) !!});
let currentAnatomyView = '{{ $exercise->anatomy_view ?: "dual" }}';

const MUSCLE_METADATA = {
    'chest': { name: 'Chest (Pectorals)', short: 'CHEST', x: 25.0, y: 22.5, side: 'anterior' },
    'abs': { name: 'Abdominals', short: 'ABS', x: 25.0, y: 38.5, side: 'anterior' },
    'deltoids_front': { name: 'Front Deltoids', short: 'DELTS', x: 12.0, y: 22.0, side: 'anterior' },
    'biceps': { name: 'Biceps', short: 'BICEPS', x: 12.5, y: 33.0, side: 'anterior' },
    'forearms_front': { name: 'Forearms', short: 'FOREARMS', x: 9.5, y: 44.0, side: 'anterior' },
    'obliques': { name: 'Obliques', short: 'OBLIQUES', x: 35.5, y: 38.0, side: 'anterior' },
    'quads': { name: 'Quadriceps', short: 'QUADS', x: 25.0, y: 59.0, side: 'anterior' },
    'calves_front': { name: 'Calves (Front)', short: 'CALVES', x: 25.0, y: 84.0, side: 'anterior' },
    'traps': { name: 'Trapezius', short: 'TRAPS', x: 74.2, y: 18.5, side: 'posterior' },
    'deltoids_rear': { name: 'Rear Deltoids', short: 'REAR DELTS', x: 87.5, y: 22.0, side: 'posterior' },
    'triceps': { name: 'Triceps', short: 'TRICEPS', x: 88.0, y: 32.5, side: 'posterior' },
    'lats': { name: 'Latissimus Dorsi', short: 'LATS', x: 74.2, y: 33.0, side: 'posterior' },
    'lower_back': { name: 'Lower Back', short: 'LOWER BACK', x: 74.2, y: 41.5, side: 'posterior' },
    'glutes': { name: 'Glutes', short: 'GLUTES', x: 74.2, y: 50.0, side: 'posterior' },
    'hamstrings': { name: 'Hamstrings', short: 'HAMSTRINGS', x: 74.2, y: 64.0, side: 'posterior' },
    'calves_rear': { name: 'Calves (Rear)', short: 'CALVES', x: 74.2, y: 84.0, side: 'posterior' }
};

function setAnatomyView(mode) {
    currentAnatomyView = mode;
    document.getElementById('anatomy_view_input').value = mode;

    const baseImg = document.getElementById('anatomy_base_img');
    const masksLayer = document.getElementById('anatomy_masks_layer');
    const labelsLayer = document.getElementById('anatomy_labels_layer');
    const btnDual = document.getElementById('view_btn_dual');
    const btnAnt = document.getElementById('view_btn_anterior');
    const btnPost = document.getElementById('view_btn_posterior');

    [btnDual, btnAnt, btnPost].forEach(b => {
        b.className = 'px-2.5 py-1 rounded-md transition font-semibold text-gray-400 hover:text-white cursor-pointer';
    });

    let transform = 'none';
    if (mode === 'dual') {
        btnDual.className = 'px-2.5 py-1 rounded-md transition font-semibold text-teal-400 bg-gray-800 shadow-sm cursor-pointer';
        transform = 'scale(1) translate(0, 0)';
    } else if (mode === 'anterior') {
        btnAnt.className = 'px-2.5 py-1 rounded-md transition font-semibold text-teal-400 bg-gray-800 shadow-sm cursor-pointer';
        transform = 'scale(1.75) translate(22%, 0)';
    } else if (mode === 'posterior') {
        btnPost.className = 'px-2.5 py-1 rounded-md transition font-semibold text-teal-400 bg-gray-800 shadow-sm cursor-pointer';
        transform = 'scale(1.75) translate(-22%, 0)';
    }

    if (baseImg) baseImg.style.transform = transform;
    if (masksLayer) masksLayer.style.transform = transform;
    if (labelsLayer) labelsLayer.style.transform = transform;
}

function cycleRegionState(key) {
    if (primaryRegions.has(key)) {
        primaryRegions.delete(key);
        secondaryRegions.add(key);
    } else if (secondaryRegions.has(key)) {
        secondaryRegions.delete(key);
    } else {
        primaryRegions.add(key);
    }
    renderAnatomyState();
}

function renderAnatomyState() {
    // 1. Update chip styles
    document.querySelectorAll('[data-region]').forEach(chip => {
        const key = chip.getAttribute('data-region');
        const dot = document.getElementById('dot_' + key);
        if (primaryRegions.has(key)) {
            chip.className = 'px-2 py-1 rounded-md text-[11px] font-bold border transition flex items-center gap-1.5 bg-emerald-950/80 text-emerald-300 border-emerald-500/70 shadow-sm shadow-emerald-500/20 cursor-pointer';
            if (dot) dot.className = 'w-2 h-2 rounded-full bg-emerald-400 border border-emerald-300 shadow-sm shadow-emerald-400/80';
        } else if (secondaryRegions.has(key)) {
            chip.className = 'px-2 py-1 rounded-md text-[11px] font-bold border transition flex items-center gap-1.5 bg-cyan-950/80 text-cyan-300 border-cyan-500/70 shadow-sm shadow-cyan-500/20 cursor-pointer';
            if (dot) dot.className = 'w-2 h-2 rounded-full bg-cyan-400 border border-cyan-300 shadow-sm shadow-cyan-400/80';
        } else {
            chip.className = 'px-2 py-1 rounded-md text-[11px] font-medium border transition flex items-center gap-1.5 bg-gray-900 text-gray-400 border-gray-800 hover:border-gray-700 cursor-pointer';
            if (dot) dot.className = 'w-2 h-2 rounded-full border border-gray-600 bg-transparent';
        }
    });

    // 2. Update hidden form inputs
    const hiddenContainer = document.getElementById('hidden_region_inputs');
    if (hiddenContainer) {
        let html = '';
        primaryRegions.forEach(r => {
            html += `<input type="hidden" name="primary_muscle_regions[]" value="${r}">`;
        });
        secondaryRegions.forEach(r => {
            html += `<input type="hidden" name="secondary_muscle_regions[]" value="${r}">`;
        });
        hiddenContainer.innerHTML = html;
    }

    // 3. Render mask layers onto anatomy preview
    const masksLayer = document.getElementById('anatomy_masks_layer');
    if (masksLayer) {
        let masksHtml = '';
        // Render secondary masks first (Cyan tint)
        secondaryRegions.forEach(r => {
            const maskUrl = '{{ asset("assets/anatomy/masks") }}/mask_' + r + '.png';
            masksHtml += `<img src="${maskUrl}" class="absolute inset-0 w-full h-full object-contain pointer-events-none" style="filter: brightness(0) saturate(100%) invert(80%) sepia(85%) saturate(3000%) hue-rotate(150deg) drop-shadow(0 0 6px rgba(0,229,255,0.7)); opacity: 0.85;">`;
        });
        // Render primary masks (Electric Mint tint)
        primaryRegions.forEach(r => {
            const maskUrl = (r === 'chest') 
                ? '{{ asset("assets/anatomy/mask_chest.png") }}' 
                : '{{ asset("assets/anatomy/masks") }}/mask_' + r + '.png';
            masksHtml += `<img src="${maskUrl}" class="absolute inset-0 w-full h-full object-contain pointer-events-none" style="filter: brightness(0) saturate(100%) invert(85%) sepia(85%) saturate(4000%) hue-rotate(85deg) drop-shadow(0 0 8px rgba(0,255,136,0.9)); opacity: 0.95;">`;
        });
        masksLayer.innerHTML = masksHtml;
    }

    // 4. Render muscle callout labels directly over active hit muscle parts
    const labelsLayer = document.getElementById('anatomy_labels_layer');
    if (labelsLayer) {
        let labelsHtml = '';
        // Render secondary labels first
        secondaryRegions.forEach(r => {
            const meta = MUSCLE_METADATA[r];
            if (meta) {
                labelsHtml += `
                    <div class="absolute -translate-x-1/2 -translate-y-1/2 px-1.5 py-0.5 rounded-full text-[9px] font-black tracking-wider uppercase flex items-center gap-1 shadow-md backdrop-blur-md whitespace-nowrap bg-gray-950/90 text-cyan-300 border border-cyan-400 shadow-cyan-500/40 ring-1 ring-cyan-500/30" style="left: ${meta.x}%; top: ${meta.y}%;">
                        <span class="w-1.5 h-1.5 rounded-full bg-cyan-400"></span>
                        <span>${meta.short}</span>
                    </div>
                `;
            }
        });
        // Render primary labels on top
        primaryRegions.forEach(r => {
            const meta = MUSCLE_METADATA[r];
            if (meta) {
                labelsHtml += `
                    <div class="absolute -translate-x-1/2 -translate-y-1/2 px-1.5 py-0.5 rounded-full text-[9px] font-black tracking-wider uppercase flex items-center gap-1 shadow-md backdrop-blur-md whitespace-nowrap bg-gray-950/90 text-emerald-300 border border-emerald-400 shadow-emerald-500/40 ring-1 ring-emerald-500/30" style="left: ${meta.x}%; top: ${meta.y}%;">
                        <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
                        <span>${meta.short}</span>
                    </div>
                `;
            }
        });
        labelsLayer.innerHTML = labelsHtml;
    }

    // 5. Update active hit muscles summary tags
    const summaryContainer = document.getElementById('active_muscles_summary');
    const countContainer = document.getElementById('active_muscles_count');
    if (summaryContainer) {
        let summaryHtml = '';
        const totalCount = primaryRegions.size + secondaryRegions.size;
        if (countContainer) {
            countContainer.innerText = `${totalCount} Muscle${totalCount === 1 ? '' : 's'}`;
        }
        if (totalCount === 0) {
            summaryHtml = '<span class="text-[11px] text-gray-500 italic">No muscle regions selected yet</span>';
        } else {
            primaryRegions.forEach(r => {
                const meta = MUSCLE_METADATA[r] || { name: r.replace('_', ' ') };
                summaryHtml += `
                    <span class="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-md text-[11px] font-bold bg-emerald-950/80 text-emerald-300 border border-emerald-500/60 shadow-sm shadow-emerald-500/20">
                        <span class="w-1.5 h-1.5 rounded-full bg-emerald-400"></span>
                        <span>${meta.name}</span>
                        <span class="text-[9px] uppercase px-1 py-0.2 rounded bg-emerald-900/80 text-emerald-300 font-extrabold">Primary</span>
                    </span>
                `;
            });
            secondaryRegions.forEach(r => {
                const meta = MUSCLE_METADATA[r] || { name: r.replace('_', ' ') };
                summaryHtml += `
                    <span class="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-md text-[11px] font-bold bg-cyan-950/80 text-cyan-300 border border-cyan-500/60 shadow-sm shadow-cyan-500/20">
                        <span class="w-1.5 h-1.5 rounded-full bg-cyan-400"></span>
                        <span>${meta.name}</span>
                        <span class="text-[9px] uppercase px-1 py-0.2 rounded bg-cyan-900/80 text-cyan-300 font-extrabold">Secondary</span>
                    </span>
                `;
            });
        }
        summaryContainer.innerHTML = summaryHtml;
    }
}

async function autoAnalyzeCurrentGif(overrideAsset = null) {
    const assetInput = document.getElementById('proper_form_asset_input');
    const asset = overrideAsset || (assetInput ? assetInput.value.trim() : '');
    if (!asset) {
        alert('Please select or specify a Proper Form GIF asset first.');
        return;
    }

    const badge = document.getElementById('confidence_badge');
    const badgeText = document.getElementById('confidence_text');
    if (badgeText) badgeText.innerText = 'ANALYZING GIF BIOMECHANICS...';

    try {
        const formData = new FormData();
        formData.append('asset_filename', asset);
        formData.append('_token', '{{ csrf_token() }}');

        const res = await fetch('{{ route("admin.exercises.analyze-gif") }}', {
            method: 'POST',
            body: formData,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        const data = await res.json();
        if (data.success) {
            // Update regions
            primaryRegions = new Set(data.primary_muscle_regions || []);
            secondaryRegions = new Set(data.secondary_muscle_regions || []);
            if (data.anatomy_view) {
                setAnatomyView(data.anatomy_view);
            }
            renderAnatomyState();

            // Update confidence badge
            const conf = data.muscle_mapping_confidence || 'high';
            document.getElementById('muscle_mapping_confidence_input').value = conf;
            if (badge && badgeText) {
                if (conf === 'high') {
                    badge.className = 'px-2.5 py-1 text-[11px] font-bold rounded-lg border bg-emerald-950/80 text-emerald-400 border-emerald-700/60';
                    badgeText.innerText = 'HIGH CONFIDENCE (VERIFIED)';
                } else if (conf === 'medium') {
                    badge.className = 'px-2.5 py-1 text-[11px] font-bold rounded-lg border bg-amber-950/80 text-amber-400 border-amber-700/60';
                    badgeText.innerText = 'MEDIUM CONFIDENCE (REVIEWED)';
                } else {
                    badge.className = 'px-2.5 py-1 text-[11px] font-bold rounded-lg border bg-red-950/80 text-red-400 border-red-700/60';
                    badgeText.innerText = 'LOW CONFIDENCE (MANUAL CONFIRM)';
                }
            }

            // Sync text fields if empty
            const secInput = document.getElementById('secondary_muscles_input');
            if (secInput && !secInput.value && data.secondary_muscles) {
                secInput.value = data.secondary_muscles;
            }
        } else {
            if (badgeText) badgeText.innerText = 'ANALYSIS NOTICE: MANUAL';
        }
    } catch (err) {
        console.error('GIF analysis error:', err);
        if (badgeText) badgeText.innerText = 'ANALYSIS OFFLINE';
    }
}

// Initial state render
document.addEventListener('DOMContentLoaded', () => {
    setAnatomyView(currentAnatomyView);
    renderAnatomyState();
});

function updateAssetPreview(val, targetField = 'proper_form_asset') {
    const img = document.getElementById(targetField + '_preview');
    const placeholder = document.getElementById(targetField + '_placeholder');
    if (!img) return;

    if (!val || !val.trim()) {
        img.classList.add('hidden');
        if (placeholder) {
            placeholder.classList.remove('hidden');
            placeholder.classList.add('block');
        }
        return;
    }
    let url = val.trim();
    if (!url.startsWith('http://') && !url.startsWith('https://') && !url.startsWith('/')) {
        url = '{{ asset("") }}' + url;
    }
    img.src = url;
    img.classList.remove('hidden');
    if (placeholder) {
        placeholder.classList.add('hidden');
    }
}
</script>
@endsection

