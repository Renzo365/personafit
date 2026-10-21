package com.personafit.app.presentation.theme

import androidx.compose.ui.graphics.Color

// ==================== OBSIDIAN & ELECTRIC MINT PALETTE ====================

// Primary Branding & High-Energy Accents
val ElectricMint = Color(0xFF00E599)       // Primary action & active highlight
val NeonCyan = Color(0xFF00D2FF)           // Secondary highlight & metric ring
val DeepTeal = Color(0xFF00E599)           // Kept for backward compatibility
val LightSeaGreen = Color(0xFF00E599)      // Kept for backward compatibility
val DarkTeal = Color(0xFF00A86B)
val TealContainer = Color(0xFF102820)      // Subtle dark mint container tint

// Secondary & Functional Accents
val CoralAccent = Color(0xFFFF6B4A)        // Solar Flame (Calories, streaks, volume)
val SafeGreen = Color(0xFF10B981)          // Clinical Emerald (Injury safeguards & substitutions)
val SafeGreenDark = Color(0xFF047857)
val CautionAmber = Color(0xFFF59E0B)       // Form alert & warning
val WarningRed = Color(0xFFEF4444)         // Error / Contraindication flag

// Surfaces & Containers (Obsidian Theme)
val DarkSlateBg = Color(0xFF0B0F12)        // Deep Obsidian Slate (Main background)
val SurfaceCardDark = Color(0xFF151C22)    // Night Charcoal (Cards, rows, dialogs)
val SurfaceElevatedDark = Color(0xFF1E2730)// Elevated pill containers, input backgrounds
val BorderDark = Color(0xFF263540)         // Crisp 1dp card borders
val BorderHighlight = Color(0xFF00E599)    // Accent active border

// High-Contrast Typography Tokens
val TextPrimary = Color(0xFFFFFFFF)        // 100% Pure White (Titles, numbers, active labels)
val TextSecondary = Color(0xFF94A3B8)      // Cool Silver (Body copy, subheads, units)
val TextMuted = Color(0xFF64748B)          // Muted Charcoal (Inactive icons, captions)
val TextOnAccent = Color(0xFF0B0F12)       // Ultra-dark text for high contrast on Electric Mint

// Light Theme Fallback (Unified with high contrast)
val LightBg = Color(0xFF0B0F12)
val LightSurface = Color(0xFF151C22)
val TextPrimaryLight = Color(0xFFFFFFFF)
val TextSecondaryLight = Color(0xFF94A3B8)
