<?php
$file = 'c:/xampp/htdocs/persona-fit/persona-fit-backend/resources/views/admin/exercises/show.blade.php';
$content = file_get_contents($file);

// Remove all anatomy blocks first
$pattern = '/\s*<!-- Verified Anatomy Highlight & Biomechanical Mapping Display -->.*?<\/div>\s*<\/div>\s*<\/div>/s';
$cleaned = preg_replace($pattern, '', $content);

// Now locate the 3-Tab Movement Animation Assets Gallery and insert the anatomy block right after it
$target = '<!-- Baseline Prescription Parameters Card -->';

$anatomyBlock = '
    <!-- Verified Anatomy Highlight & Biomechanical Mapping Display -->
    <div class="bg-gray-800/80 border border-gray-700/60 rounded-2xl p-6 shadow-xl space-y-4">
        <div class="flex items-center justify-between border-b border-gray-700/60 pb-3 flex-wrap gap-2">
            <div class="flex items-center gap-2">
                <i class="fa-solid fa-person-running text-teal-400"></i>
                <h2 class="text-base font-bold text-white">Verified Anatomy Highlights & Target Regions</h2>
            </div>
            <div class="flex items-center gap-2">
                <span class="text-xs text-gray-400">Anatomy View: <strong class="text-teal-400 font-semibold uppercase">{{ $exercise->anatomy_view ?? \'dual\' }}</strong></span>
                <span class="px-2.5 py-0.5 text-[11px] font-bold rounded-lg border {{ ($exercise->muscle_mapping_confidence ?? \'high\') === \'high\' ? \'bg-emerald-950/80 text-emerald-400 border-emerald-700/60\' : \'bg-amber-950/80 text-amber-400 border-amber-700/60\' }}">
                    <i class="fa-solid fa-circle-check text-[10px]"></i> {{ strtoupper($exercise->muscle_mapping_confidence ?? \'HIGH\') }} CONFIDENCE
                </span>
            </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-12 gap-6 items-center">
            <!-- Anatomy Preview Canvas -->
            <div class="md:col-span-6 flex justify-center">
                <div class="relative w-full max-w-[420px] aspect-[883/655] bg-gray-950 rounded-xl overflow-hidden border border-gray-800 shadow-inner flex items-center justify-center p-2">
                    <img src="{{ asset(\'assets/anatomy/anatomy_template_clean.png\') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none select-none" alt="Anatomy Template">
                    <!-- Overlay Secondary Masks (Cyan) -->
                    @foreach($exercise->secondary_muscle_regions ?? [] as $secRegion)
                        <img src="{{ asset(\'assets/anatomy/masks/mask_\' . $secRegion . \'.png\') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none" style="filter: brightness(0) saturate(100%) invert(80%) sepia(85%) saturate(3000%) hue-rotate(150deg) drop-shadow(0 0 6px rgba(0,229,255,0.7)); opacity: 0.85;">
                    @endforeach
                    <!-- Overlay Primary Masks (Electric Mint) -->
                    @foreach($exercise->primary_muscle_regions ?? [] as $primRegion)
                        <img src="{{ $primRegion === \'chest\' ? asset(\'assets/anatomy/mask_chest.png\') : asset(\'assets/anatomy/masks/mask_\' . $primRegion . \'.png\') }}" class="absolute inset-0 w-full h-full object-contain pointer-events-none" style="filter: brightness(0) saturate(100%) invert(85%) sepia(85%) saturate(4000%) hue-rotate(85deg) drop-shadow(0 0 8px rgba(0,255,136,0.9)); opacity: 0.95;">
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
                                {{ ucwords(str_replace(\'_\', \' \', $region)) }}
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
                                {{ ucwords(str_replace(\'_\', \' \', $region)) }}
                            </span>
                        @empty
                            <span class="text-xs text-gray-500">None specified</span>
                        @endforelse
                    </div>
                </div>

                <div class="bg-gray-900/80 rounded-xl p-3 border border-gray-800 text-xs text-gray-300 space-y-1">
                    <p><strong class="text-gray-400">Target Muscle:</strong> {{ $exercise->target_muscle }}</p>
                    <p><strong class="text-gray-400">Secondary Synergists:</strong> {{ $exercise->secondary_muscles ?: \'None\' }}</p>
                    <p><strong class="text-gray-400">Biomechanical Mapping:</strong> Verified against PersonaFit 3-template reference matrix.</p>
                </div>
            </div>
        </div>
    </div>
';

$cleaned = str_replace($target, $anatomyBlock . "\n    " . $target, $cleaned);
file_put_contents($file, $cleaned);
echo "Cleaned show.blade.php successfully.\n";
