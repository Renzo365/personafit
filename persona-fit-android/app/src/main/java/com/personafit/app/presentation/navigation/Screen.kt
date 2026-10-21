package com.personafit.app.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object MotivationalWelcome : Screen("motivational_welcome")
    object Register : Screen("register")
    object OnboardingWizard : Screen("onboarding_wizard")
    object AssessmentSummary : Screen("assessment_summary")
    object MainDashboard : Screen("main_dashboard")
    object TodayWorkout : Screen("today_workout")
    object ActiveWorkout : Screen("active_workout/{planId}/{sessionName}") {
        fun createRoute(planId: Long?, sessionName: String): String = "active_workout/${planId ?: 0}/$sessionName"
    }
    object ExerciseDetail : Screen("exercise_detail/{exerciseId}") {
        fun createRoute(exerciseId: Long): String = "exercise_detail/$exerciseId"
    }
    object ExerciseSwap : Screen("exercise_swap/{exerciseId}") {
        fun createRoute(exerciseId: Long): String = "exercise_swap/$exerciseId"
    }
    object Nutrition : Screen("nutrition")
    object Progress : Screen("progress")
    object Settings : Screen("settings")
}
