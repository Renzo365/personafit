<?php

namespace App\Services;

class PersonaClassificationService
{
    /**
     * Determine the user's Persona Archetype based on onboarding quiz answers.
     *
     * @param array $quizAnswers
     * [
     *   'time_available' => 'short' | 'medium' | 'long',      // short (<30m), medium (30-45m), long (60m+)
     *   'location' => 'gym' | 'home' | 'outdoor',
     *   'experience' => 'beginner' | 'intermediate' | 'advanced',
     *   'daily_lifestyle' => 'sedentary' | 'active',
     * ]
     */
    public function classifyPersona(array $quizAnswers): string
    {
        $time = $quizAnswers['time_available'] ?? 'medium';
        $location = $quizAnswers['location'] ?? 'gym';
        $experience = $quizAnswers['experience'] ?? 'beginner';

        // 1. Home-first preference
        if ($location === 'home' || $location === 'outdoor') {
            return 'home_warrior';
        }

        // 2. Severe time constraint (<30 mins)
        if ($time === 'short') {
            return 'busy_pro';
        }

        // 3. True beginner with low experience
        if ($experience === 'beginner') {
            return 'gentle_starter';
        }

        // 4. Advanced or long workout duration preference
        if ($experience === 'advanced' || $time === 'long') {
            return 'athlete';
        }

        // Default to Busy Pro if active lifestyle/medium time
        return 'busy_pro';
    }

    /**
     * Get descriptive metadata for the assigned Persona archetype.
     */
    public function getPersonaMetadata(string $archetype): array
    {
        return match ($archetype) {
            'busy_pro' => [
                'title' => 'The Busy Professional',
                'tagline' => 'High-Density, Maximum Efficiency',
                'description' => 'Tailored for individuals with demanding schedules. Focuses on 3-day superset splits, posture correction for desk workers, and low spinal fatigue.',
                'recommended_days_per_week' => 3,
                'target_session_minutes' => 30,
            ],
            'gentle_starter' => [
                'title' => 'The Gentle Starter',
                'tagline' => 'Low Impact, Joint-Safe Foundation',
                'description' => 'Designed for beginners or individuals returning from a long hiatus. Emphasizes machine guidance, bodyweight foundation, and progressive habit building.',
                'recommended_days_per_week' => 3,
                'target_session_minutes' => 25,
            ],
            'home_warrior' => [
                'title' => 'The Home Warrior',
                'tagline' => 'Calisthenics & Minimal Gear Power',
                'description' => 'Built for home setups with dumbbells and resistance bands. Focuses on functional movements and unilateral balance.',
                'recommended_days_per_week' => 3,
                'target_session_minutes' => 35,
            ],
            'athlete' => [
                'title' => 'The Dedicated Athlete',
                'tagline' => 'Hypertrophy & Progressive Strength',
                'description' => 'Optimized for gym regulars aiming for progressive overload and muscle building with compound barbell/dumbbell movements.',
                'recommended_days_per_week' => 4,
                'target_session_minutes' => 50,
            ],
            default => [
                'title' => 'Fitness Persona',
                'tagline' => 'Personalized Plan',
                'description' => 'Customized fitness regimen.',
                'recommended_days_per_week' => 3,
                'target_session_minutes' => 35,
            ],
        };
    }
}
