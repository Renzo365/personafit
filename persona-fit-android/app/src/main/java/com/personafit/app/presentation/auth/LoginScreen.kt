package com.personafit.app.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personafit.app.presentation.common.PasswordInputField
import com.personafit.app.presentation.common.PersonaInputField
import com.personafit.app.presentation.common.PrimaryButton
import com.personafit.app.presentation.theme.*

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val hasCompleted = (uiState as AuthUiState.Success).hasCompletedOnboarding
            viewModel.resetState()
            if (hasCompleted) {
                onNavigateToWelcome()
            } else {
                onNavigateToOnboarding()
            }
        }
    }

    Scaffold(
        containerColor = DarkSlateBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header Logo
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(ElectricMint, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = TextOnAccent,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Text(
                    text = "Sign in to continue your personalized routine",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
                )

                // Error Message Display
                if (uiState is AuthUiState.Error) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = WarningRed.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed)
                    ) {
                        Text(
                            text = (uiState as AuthUiState.Error).message,
                            color = WarningRed,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Email Input
                PersonaInputField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "e.g. user@example.com",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                PasswordInputField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    leadingIcon = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Sign In Button
                PrimaryButton(
                    text = "Sign In",
                    onClick = { viewModel.login(email, password) },
                    isLoading = uiState is AuthUiState.Loading
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Navigation to Register
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ElectricMint,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable {
                            viewModel.resetState()
                            onNavigateToRegister()
                        }
                    )
                }
            }
        }
    }
}
