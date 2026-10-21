<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>{{ $title ?? 'Admin Portal' }} - PersonaFit</title>
    <!-- Tailwind CSS CDN for Admin UI -->
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --color-primary: #008080;
            --color-primary-hover: #006666;
            --color-slate-dark: #12181B;
            --color-card-dark: #1E262C;
        }
    </style>
</head>
<body class="bg-gray-900 text-gray-100 font-sans min-h-screen flex">

    <!-- Sidebar Navigation -->
    <aside class="w-64 bg-gray-950 border-r border-gray-800 flex flex-col justify-between p-4 fixed h-full z-20">
        <div>
            <!-- Brand Logo -->
            <div class="flex items-center space-x-3 px-2 py-4 border-b border-gray-800 mb-6">
                <div class="w-10 h-10 rounded-xl bg-teal-600 flex items-center justify-center text-white font-bold text-xl shadow-lg shadow-teal-500/20">
                    <i class="fa-solid fa-shield-heart"></i>
                </div>
                <div>
                    <h1 class="font-bold text-lg text-white tracking-wide">PERSONAFIT</h1>
                    <p class="text-xs text-teal-400 font-medium">CMS & Trainer Portal</p>
                </div>
            </div>

            <!-- Navigation Links -->
            <nav class="space-y-1">
                <a href="{{ route('admin.dashboard') }}" class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium {{ request()->routeIs('admin.dashboard') ? 'bg-teal-600 text-white' : 'text-gray-400 hover:bg-gray-800 hover:text-gray-200' }} transition">
                    <i class="fa-solid fa-chart-pie w-5"></i>
                    <span>Dashboard</span>
                </a>
                <a href="{{ route('admin.exercises.index') }}" class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium {{ request()->routeIs('admin.exercises.*') ? 'bg-teal-600 text-white' : 'text-gray-400 hover:bg-gray-800 hover:text-gray-200' }} transition">
                    <i class="fa-solid fa-dumbbell w-5"></i>
                    <span>Exercise CMS</span>
                </a>
                <a href="{{ route('admin.rules.index') }}" class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium {{ request()->routeIs('admin.rules.*') ? 'bg-teal-600 text-white' : 'text-gray-400 hover:bg-gray-800 hover:text-gray-200' }} transition">
                    <i class="fa-solid fa-triangle-exclamation w-5"></i>
                    <span>Injury Rules Engine</span>
                </a>
                <a href="{{ route('admin.users.index') }}" class="flex items-center space-x-3 px-3 py-2.5 rounded-lg text-sm font-medium {{ request()->routeIs('admin.users.*') ? 'bg-teal-600 text-white' : 'text-gray-400 hover:bg-gray-800 hover:text-gray-200' }} transition">
                    <i class="fa-solid fa-users w-5"></i>
                    <span>User Directory</span>
                </a>
            </nav>
        </div>

        <!-- Footer / User Info -->
        <div class="border-t border-gray-800 pt-4 px-2">
            <div class="flex items-center justify-between text-xs text-gray-400">
                <div>
                    <p class="font-semibold text-gray-300">System Admin</p>
                    <p class="text-teal-400">Local MySQL Active</p>
                </div>
                <a href="/" class="p-2 hover:bg-gray-800 rounded-lg text-gray-400 hover:text-white" title="View Documentation">
                    <i class="fa-solid fa-book"></i>
                </a>
            </div>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="ml-64 flex-1 p-8 min-h-screen">
        <!-- Flash Messages -->
        @if(session('success'))
            <div class="mb-6 p-4 rounded-xl bg-teal-900/50 border border-teal-500 text-teal-200 flex items-center space-x-3">
                <i class="fa-solid fa-circle-check text-teal-400 text-lg"></i>
                <span>{{ session('success') }}</span>
            </div>
        @endif

        @if(session('error'))
            <div class="mb-6 p-4 rounded-xl bg-red-900/50 border border-red-500 text-red-200 flex items-center space-x-3">
                <i class="fa-solid fa-circle-exclamation text-red-400 text-lg"></i>
                <span>{{ session('error') }}</span>
            </div>
        @endif

        @yield('content')
    </main>

</body>
</html>
