package com.personafit.app.presentation.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val workoutLocation by viewModel.workoutLocation.collectAsState()
    val exerciseType by viewModel.exerciseType.collectAsState()
    val availableDays by viewModel.availableDays.collectAsState()
    val jointHealthStatus by viewModel.jointHealthStatus.collectAsState()
    val jointLimitations by viewModel.jointLimitations.collectAsState()
    val availableLimitations by viewModel.availableLimitations.collectAsState()

    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val personaArchetype by viewModel.personaArchetype.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()

    val notifWorkoutEnabled by viewModel.notifWorkoutEnabled.collectAsState()
    val notifMealEnabled by viewModel.notifMealEnabled.collectAsState()
    val notifProgressEnabled by viewModel.notifProgressEnabled.collectAsState()
    val notifRecoveryEnabled by viewModel.notifRecoveryEnabled.collectAsState()

    val workoutTimeHour by viewModel.workoutTimeHour.collectAsState()
    val workoutTimeMinute by viewModel.workoutTimeMinute.collectAsState()

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.rescheduleAllReminders(context)
        }
    }

    val checkAndRequestPermission = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val isUpdating by viewModel.isUpdating.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val showConfirmDialog by viewModel.showConfirmDialog.collectAsState()
    val confirmDialogTitle by viewModel.confirmDialogTitle.collectAsState()
    val confirmDialogMessage by viewModel.confirmDialogMessage.collectAsState()

    val showChangePasswordDialog by viewModel.showChangePasswordDialog.collectAsState()
    val showAboutDialog by viewModel.showAboutDialog.collectAsState()
    val showJointHealthDialog by viewModel.showJointHealthDialog.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSlateBg
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkSlateBg
    ) { paddingValues ->
        if (uiState is SettingsUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ElectricMint)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                // ==================== 1. WORKOUT PREFERENCES ====================
                SectionHeader(title = "WORKOUT PREFERENCES")

                Text(
                    text = "Workout Environment",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
                )

                PreferenceSelectableCard(
                    title = "🏠 Home Workout",
                    description = "Bodyweight, bands, dumbbells, and home-friendly exercises",
                    isSelected = workoutLocation == "home",
                    onClick = { viewModel.selectWorkoutLocation("home") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                PreferenceSelectableCard(
                    title = "🏢 Gym Workout",
                    description = "Access to gym equipment, machines, cables, and free weights",
                    isSelected = workoutLocation == "gym",
                    onClick = { viewModel.selectWorkoutLocation("gym") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Exercise Type",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                PreferenceSelectableCard(
                    title = "💪 Compound Exercises",
                    description = "Multi-joint movements that train multiple muscle groups",
                    isSelected = exerciseType == "compound",
                    onClick = { viewModel.selectExerciseType("compound") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                PreferenceSelectableCard(
                    title = "🎯 Isolation Exercises",
                    description = "Exercises that primarily target a specific muscle group",
                    isSelected = exerciseType == "isolation",
                    onClick = { viewModel.selectExerciseType("isolation") }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ==================== 2. WORKOUT SCHEDULE ====================
                SectionHeader(title = "WORKOUT SCHEDULE")

                Text(
                    text = "Select the days you are available to exercise.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Calendar-style Day Grid
                val allDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                allDays.chunked(3).forEach { rowDays ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (day in rowDays) {
                            val isSelected = availableDays.contains(day)
                            DayCalendarCard(
                                dayName = day,
                                isSelected = isSelected,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleAvailableDay(day) }
                            )
                        }
                        // Pad row if last chunk has fewer than 3
                        if (rowDays.size < 3) {
                            Spacer(modifier = Modifier.weight((3 - rowDays.size).toFloat()))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Workout Reminder Time Selector (right below weekly day schedule)
                val formattedTime = remember(workoutTimeHour, workoutTimeMinute) {
                    val period = if (workoutTimeHour >= 12) "PM" else "AM"
                    val displayHour = when {
                        workoutTimeHour == 0 -> 12
                        workoutTimeHour > 12 -> workoutTimeHour - 12
                        else -> workoutTimeHour
                    }
                    String.format("%02d:%02d %s", displayHour, workoutTimeMinute, period)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            checkAndRequestPermission()
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    viewModel.updateWorkoutTime(hour, minute, context)
                                },
                                workoutTimeHour,
                                workoutTimeMinute,
                                false
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceCardDark,
                    border = BorderStroke(1.dp, BorderDark)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(ElectricMint.copy(alpha = 0.12f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = ElectricMint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Workout Reminder Time",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Alerts on scheduled workout days",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TealContainer,
                            border = BorderStroke(1.dp, ElectricMint.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = formattedTime,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricMint,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Apply schedule button
                Button(
                    onClick = { viewModel.promptSaveSchedule() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevatedDark),
                    border = BorderStroke(1.dp, BorderDark)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Apply Schedule Changes",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ==================== 3. SAFETY & INJURY PREVENTION ====================
                SectionHeader(title = "SAFETY & INJURY PREVENTION")

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showJointHealthDialog.value = true },
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceCardDark,
                    border = BorderStroke(1.dp, BorderDark)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(SafeGreen.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = SafeGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Joint Health & Limitations",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            val statusSummary = if (jointHealthStatus == "No Limitations" || jointLimitations.isEmpty()) {
                                "No Limitations • Unrestricted Catalog"
                            } else {
                                "${jointLimitations.size} Active Limitation Safeguards"
                            }
                            Text(
                                text = statusSummary,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (jointHealthStatus == "No Limitations") SafeGreen else CoralAccent
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Edit Limitations",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ==================== 4. ACCOUNT ====================
                SectionHeader(title = "ACCOUNT")

                Spacer(modifier = Modifier.height(10.dp))

                // Profile Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceCardDark,
                    border = BorderStroke(1.dp, BorderDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(ElectricMint.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = ElectricMint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (userName.isNotBlank()) userName else "PersonaFit Athlete",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = userEmail,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceElevatedDark
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Current Archetype",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = personaArchetype.replace('_', ' ').uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricMint,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Change Password Button
                SettingsActionRow(
                    icon = Icons.Default.Lock,
                    iconTint = NeonCyan,
                    label = "Change Password",
                    onClick = { viewModel.showChangePasswordDialog.value = true }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Logout Button
                SettingsActionRow(
                    icon = Icons.Default.Logout,
                    iconTint = CoralAccent,
                    label = "Log Out",
                    labelColor = CoralAccent,
                    onClick = onLogout
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ==================== 5. NOTIFICATION PREFERENCES ====================
                SectionHeader(title = "NOTIFICATION PREFERENCES")

                Spacer(modifier = Modifier.height(10.dp))

                // 1. Workout Reminder
                NotificationToggleCard(
                    icon = Icons.Default.FitnessCenter,
                    iconTint = ElectricMint,
                    title = "Workout Reminders",
                    description = "Alerts on scheduled workout days ($formattedTime)",
                    isChecked = notifWorkoutEnabled,
                    onCheckedChange = {
                        checkAndRequestPermission()
                        viewModel.setWorkoutNotification(it, context)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Meal Logging Confirmation
                NotificationToggleCard(
                    icon = Icons.Default.Restaurant,
                    iconTint = NeonCyan,
                    title = "Meal Logging Confirmation",
                    description = "Play lightweight chime when food weight is saved",
                    isChecked = notifMealEnabled,
                    onCheckedChange = {
                        viewModel.setMealNotification(it)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Weekly Progress Reminder
                NotificationToggleCard(
                    icon = Icons.Default.TrendingUp,
                    iconTint = CoralAccent,
                    title = "Weekly Progress Reminder",
                    description = "Weekly workout performance and volume review",
                    isChecked = notifProgressEnabled,
                    onCheckedChange = {
                        checkAndRequestPermission()
                        viewModel.setProgressNotification(it, context)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 4. Recovery / Rest Day Reminder
                NotificationToggleCard(
                    icon = Icons.Default.Bedtime,
                    iconTint = SafeGreen,
                    title = "Recovery & Rest Day Reminder",
                    description = "Guidance on non-workout days to prevent burnout",
                    isChecked = notifRecoveryEnabled,
                    onCheckedChange = {
                        checkAndRequestPermission()
                        viewModel.setRecoveryNotification(it, context)
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ==================== 6. APP INFO ====================
                SectionHeader(title = "APP")

                Spacer(modifier = Modifier.height(10.dp))

                // About PersonaFit
                SettingsActionRow(
                    icon = Icons.Default.Info,
                    iconTint = TextSecondary,
                    label = "About PersonaFit",
                    onClick = { viewModel.showAboutDialog.value = true }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // ==================== CONFIRMATION DIALOG ====================
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelPendingUpdate() },
            containerColor = SurfaceCardDark,
            title = {
                Text(
                    text = confirmDialogTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = confirmDialogMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelPendingUpdate() }) {
                    Text(text = "Cancel", color = TextSecondary)
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmPendingUpdate(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            color = TextOnAccent,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Update Plan",
                            color = TextOnAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
    }

    // ==================== JOINT HEALTH DIALOG ====================
    if (showJointHealthDialog) {
        Dialog(onDismissRequest = { viewModel.showJointHealthDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                color = SurfaceCardDark,
                border = BorderStroke(1.dp, BorderDark)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Joint Health & Limitations",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                        IconButton(onClick = { viewModel.showJointHealthDialog.value = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }

                    Text(
                        text = "Select any areas requiring safe exercise modifications. Unselected days/safe areas enable full catalog.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    // 1. "No Limitations" option
                    val isNoLimitations = jointHealthStatus == "No Limitations"
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectNoLimitations() },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isNoLimitations) TealContainer else SurfaceElevatedDark,
                        border = BorderStroke(1.dp, if (isNoLimitations) ElectricMint else BorderDark)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isNoLimitations) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isNoLimitations) ElectricMint else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "I have NO joint pain or limitations",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Full unrestricted exercise catalog",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "OR SELECT ALL AREAS THAT APPLY:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val limitationOptions = listOf(
                        "Lower Back" to "Lower Back Pain / Lumbar Disc Vulnerability",
                        "Knee" to "Knee Pain / Meniscus / Patellofemoral",
                        "Shoulder" to "Shoulder Impingement / Rotator Cuff",
                        "Wrist" to "Wrist Pain / Carpal Tunnel / Tendinitis"
                    )

                    limitationOptions.forEach { (keyword, title) ->
                        val isChecked = jointLimitations.any { it.contains(keyword, ignoreCase = true) }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { viewModel.toggleJointLimitation(keyword) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isChecked) SurfaceElevatedDark else SurfaceElevatedDark.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, if (isChecked) CoralAccent else BorderDark)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { viewModel.toggleJointLimitation(keyword) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CoralAccent,
                                        checkmarkColor = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                        color = TextPrimary
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    PrimaryButton(
                        text = "Save & Apply Limitations",
                        onClick = { viewModel.promptSaveJointHealth() }
                    )
                }
            }
        }
    }

    // ==================== CHANGE PASSWORD DIALOG ====================
    if (showChangePasswordDialog) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { viewModel.showChangePasswordDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                color = SurfaceCardDark,
                border = BorderStroke(1.dp, BorderDark)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Change Password",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Current Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = BorderDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password (min 6 chars)") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = BorderDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = BorderDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { viewModel.showChangePasswordDialog.value = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { viewModel.changePassword(oldPassword, newPassword, confirmPassword) },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Update Password", color = TextOnAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // ==================== ABOUT DIALOG ====================
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showAboutDialog.value = false },
            containerColor = SurfaceCardDark,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = ElectricMint)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PersonaFit v1.0.0",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Deterministic Rule-Based Personalization Engine",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "PersonaFit crafts individualized fitness, hydration, and nutritional protocols tailored strictly to your environment, weekly schedule, and orthopedic safety rules without relying on unverified external models.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Clinical joint screening and automated substitution\n• Smart Home vs Gym equipment adaptation\n• Native sensor step & metabolic tracking",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.showAboutDialog.value = false }) {
                    Text("Close", color = ElectricMint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// ==================== HELPER COMPOSABLES ====================

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            color = ElectricMint,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun PreferenceSelectableCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) TealContainer else SurfaceCardDark,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) ElectricMint else BorderDark
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                )
            }
            if (isSelected) {
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(ElectricMint, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = TextOnAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCalendarCard(
    dayName: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shortName = dayName.take(3).uppercase()
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) TealContainer else SurfaceElevatedDark,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) ElectricMint else BorderDark
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = shortName,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) ElectricMint else TextSecondary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = ElectricMint,
                    modifier = Modifier.size(14.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(BorderDark, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    labelColor: Color = TextPrimary,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = labelColor
                    )
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun NotificationToggleCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCardDark,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(iconTint.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.padding(end = 8.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkSlateBg,
                    checkedTrackColor = ElectricMint,
                    uncheckedTrackColor = SurfaceElevatedDark
                )
            )
        }
    }
}
