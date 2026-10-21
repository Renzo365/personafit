package com.personafit.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.personafit.app.PersonaFitApp
import com.personafit.app.presentation.auth.AuthViewModel
import com.personafit.app.presentation.auth.LoginScreen
import com.personafit.app.presentation.auth.MotivationalWelcomeScreen
import com.personafit.app.presentation.auth.RegisterScreen
import com.personafit.app.presentation.auth.SplashScreen
import com.personafit.app.presentation.dashboard.DashboardViewModel
import com.personafit.app.presentation.dashboard.MainDashboardScreen
import com.personafit.app.presentation.nutrition.NutritionScreen
import com.personafit.app.presentation.nutrition.NutritionViewModel
import com.personafit.app.presentation.onboarding.AssessmentSummaryScreen
import com.personafit.app.presentation.onboarding.OnboardingViewModel
import com.personafit.app.presentation.onboarding.OnboardingWizardScreen
import com.personafit.app.presentation.progress.ProgressScreen
import com.personafit.app.presentation.progress.ProgressViewModel
import com.personafit.app.presentation.settings.SettingsScreen
import com.personafit.app.presentation.settings.SettingsViewModel
import com.personafit.app.presentation.workout.ActiveWorkoutScreen
import com.personafit.app.presentation.workout.ExerciseDetailScreen
import com.personafit.app.presentation.workout.TodayWorkoutScreen
import com.personafit.app.presentation.workout.WorkoutViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun PersonaFitNavGraph(
    navController: NavHostController,
    app: PersonaFitApp
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                sessionManager = app.sessionManager,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.OnboardingWizard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.MainDashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.Factory(app.authRepository)
            )
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToWelcome = {
                    navController.navigate(Screen.MotivationalWelcome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.OnboardingWizard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Motivational Post-Login Welcome Screen
        composable(Screen.MotivationalWelcome.route) {
            MotivationalWelcomeScreen(
                sessionManager = app.sessionManager,
                onContinue = {
                    navController.navigate(Screen.MainDashboard.route) {
                        popUpTo(Screen.MotivationalWelcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.Factory(app.authRepository)
            )
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.OnboardingWizard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // Onboarding Assessment Wizard (Screens 03, 04, 05)
        composable(Screen.OnboardingWizard.route) {
            val onboardingViewModel: OnboardingViewModel = viewModel(
                factory = OnboardingViewModel.Factory(app.assessmentRepository)
            )
            OnboardingWizardScreen(
                viewModel = onboardingViewModel,
                onNavigateToSummary = {
                    navController.navigate(Screen.AssessmentSummary.route)
                }
            )
        }

        // Assessment Summary & Persona Reveal (Screen 06)
        composable(Screen.AssessmentSummary.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.OnboardingWizard.route)
                } catch (e: Exception) {
                    null
                }
            }
            val onboardingViewModel: OnboardingViewModel = if (parentEntry != null) {
                viewModel(parentEntry, factory = OnboardingViewModel.Factory(app.assessmentRepository))
            } else {
                viewModel(factory = OnboardingViewModel.Factory(app.assessmentRepository))
            }
            AssessmentSummaryScreen(
                viewModel = onboardingViewModel,
                onNavigateToDashboard = {
                    navController.navigate(Screen.MainDashboard.route) {
                        popUpTo(Screen.OnboardingWizard.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Dashboard Hub (Screen 07)
        composable(Screen.MainDashboard.route) {
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModel.Factory(
                    sessionManager = app.sessionManager,
                    workoutRepository = app.workoutRepository,
                    nutritionRepository = app.nutritionRepository,
                    activityRepository = app.activityRepository,
                    stepSensorManager = app.stepSensorManager
                )
            )
            MainDashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToWorkout = {
                    navController.navigate(Screen.TodayWorkout.route)
                },
                onNavigateToNutrition = {
                    navController.navigate(Screen.Nutrition.route)
                },
                onNavigateToProgress = {
                    navController.navigate(Screen.Progress.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    runBlocking { app.sessionManager.clearSession() }
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Today's Prescribed Routine (Screen 08)
        composable(Screen.TodayWorkout.route) {
            val workoutViewModel: WorkoutViewModel = viewModel(
                factory = WorkoutViewModel.Factory(app.workoutRepository)
            )
            TodayWorkoutScreen(
                viewModel = workoutViewModel,
                onStartWorkout = { planId, sessionName ->
                    workoutViewModel.startWorkoutSession(planId, sessionName)
                    navController.navigate(Screen.ActiveWorkout.createRoute(planId, sessionName))
                },
                onNavigateToExerciseDetail = { exerciseId ->
                    navController.navigate(Screen.ExerciseDetail.createRoute(exerciseId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Dedicated Exercise Detail & Biomechanics Screen (Interactive Upgrade)
        composable(
            route = Screen.ExerciseDetail.route,
            arguments = listOf(
                navArgument("exerciseId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "personafit://exercise_detail/{exerciseId}" }
            )
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.TodayWorkout.route)
                } catch (e: Exception) {
                    null
                }
            }
            val workoutViewModel: WorkoutViewModel = if (parentEntry != null) {
                viewModel(parentEntry, factory = WorkoutViewModel.Factory(app.workoutRepository))
            } else {
                viewModel(factory = WorkoutViewModel.Factory(app.workoutRepository))
            }

            val exerciseId = backStackEntry.arguments?.getLong("exerciseId") ?: 0L

            ExerciseDetailScreen(
                exerciseId = exerciseId,
                viewModel = workoutViewModel,
                onStartWorkoutSession = { planId, sessionName, targetExerciseId ->
                    workoutViewModel.startWorkoutSession(planId, sessionName, targetExerciseId)
                    navController.navigate(Screen.ActiveWorkout.createRoute(planId, sessionName))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Active Workout Execution Mode (Screen 10)
        composable(
            route = Screen.ActiveWorkout.route,
            arguments = listOf(
                navArgument("planId") {
                    type = NavType.LongType
                    defaultValue = 0L
                },
                navArgument("sessionName") {
                    type = NavType.StringType
                    defaultValue = "Daily Workout Session"
                }
            )
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.TodayWorkout.route)
                } catch (e: Exception) {
                    null
                }
            }
            val workoutViewModel: WorkoutViewModel = if (parentEntry != null) {
                viewModel(parentEntry, factory = WorkoutViewModel.Factory(app.workoutRepository))
            } else {
                viewModel(factory = WorkoutViewModel.Factory(app.workoutRepository))
            }

            val planId = backStackEntry.arguments?.getLong("planId")?.takeIf { it > 0L }
            val sessionName = backStackEntry.arguments?.getString("sessionName") ?: "Daily Workout Session"

            LaunchedEffect(Unit) {
                if (workoutViewModel.activeWorkoutLogId.value == null || workoutViewModel.activeExercises.value.isEmpty()) {
                    workoutViewModel.startWorkoutSession(planId, sessionName)
                }
            }

            ActiveWorkoutScreen(
                viewModel = workoutViewModel,
                onNavigateToDashboard = {
                    navController.navigate(Screen.MainDashboard.route) {
                        popUpTo(Screen.TodayWorkout.route) { inclusive = true }
                    }
                },
                onNavigateToExerciseDetail = { exerciseId ->
                    navController.navigate(Screen.ExerciseDetail.createRoute(exerciseId))
                }
            )
        }

        // Nutrition & Hydration Tracker (Screen 13)
        composable(Screen.Nutrition.route) {
            val nutritionViewModel: NutritionViewModel = viewModel(
                factory = NutritionViewModel.Factory(app.nutritionRepository)
            )
            NutritionScreen(
                viewModel = nutritionViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Progress & BMI Analytics (Screen 14)
        composable(Screen.Progress.route) {
            val progressViewModel: ProgressViewModel = viewModel(
                factory = ProgressViewModel.Factory(app.activityRepository)
            )
            ProgressScreen(
                viewModel = progressViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Settings & Personalization (Screen 15)
        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(app.settingsRepository)
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    runBlocking { app.sessionManager.clearSession() }
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
