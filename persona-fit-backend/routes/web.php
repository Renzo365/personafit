<?php

use App\Http\Controllers\Admin\AdminDashboardController;
use App\Http\Controllers\Admin\ExerciseManagementController;
use App\Http\Controllers\Admin\InjuryRuleController;
use App\Http\Controllers\Admin\UserManagementController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web & Admin Portal Routes
|--------------------------------------------------------------------------
*/

Route::get('/', function () {
    return redirect()->route('admin.dashboard');
});

Route::prefix('admin')->name('admin.')->group(function () {
    Route::get('/', [AdminDashboardController::class, 'index'])->name('dashboard');

    // Exercise CMS Assets Catalog API
    Route::get('exercises/assets-catalog', [ExerciseManagementController::class, 'assetsCatalog'])->name('exercises.assets-catalog');
    Route::post('exercises/analyze-gif', [ExerciseManagementController::class, 'analyzeGif'])->name('exercises.analyze-gif');

    // Exercise CMS
    Route::post('exercises/{exercise}/toggle-status', [ExerciseManagementController::class, 'toggleStatus'])->name('exercises.toggle-status');
    Route::resource('exercises', ExerciseManagementController::class);

    // Injury Contraindication Rule Engine
    Route::get('rules', [InjuryRuleController::class, 'index'])->name('rules.index');
    Route::post('rules', [InjuryRuleController::class, 'store'])->name('rules.store');
    Route::delete('rules/{rule}', [InjuryRuleController::class, 'destroy'])->name('rules.destroy');

    // Users Directory
    Route::get('users', [UserManagementController::class, 'index'])->name('users.index');
});

// Serve anatomy templates and masks directly from /assets/anatomy directory
Route::get('assets/anatomy/{path}', function ($path) {
    $assetsBase = realpath(base_path('../assets/anatomy'));
    $targetPath = realpath(base_path('../assets/anatomy/' . $path));
    if ($targetPath && file_exists($targetPath) && str_starts_with($targetPath, $assetsBase)) {
        $ext = strtolower(pathinfo($targetPath, PATHINFO_EXTENSION));
        $mime = match ($ext) {
            'png' => 'image/png',
            'jpg', 'jpeg' => 'image/jpeg',
            'svg' => 'image/svg+xml',
            default => 'application/octet-stream',
        };
        return response()->file($targetPath, [
            'Content-Type' => $mime,
            'Cache-Control' => 'public, max-age=86400',
        ]);
    }
    abort(404);
})->where('path', '.*');

// Serve exercise animation GIFs directly from /assets directory
Route::get('assets/{filename}', function ($filename) {
    $assetsBase = realpath(base_path('../assets'));
    $targetPath = realpath(base_path('../assets/' . $filename));
    if ($targetPath && file_exists($targetPath) && str_starts_with($targetPath, $assetsBase)) {
        return response()->file($targetPath, [
            'Content-Type' => 'image/gif',
            'Cache-Control' => 'public, max-age=86400',
        ]);
    }
    abort(404);
})->where('filename', '.*\.gif$');

