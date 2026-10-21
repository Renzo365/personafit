package com.personafit.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.personafit.app.presentation.navigation.PersonaFitNavGraph
import com.personafit.app.presentation.theme.PersonaFitTheme

class MainActivity : ComponentActivity() {
    private var activeNavController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as PersonaFitApp

        setContent {
            PersonaFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    activeNavController = navController

                    LaunchedEffect(Unit) {
                        intent?.let { navController.handleDeepLink(it) }
                    }

                    PersonaFitNavGraph(
                        navController = navController,
                        app = app
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        activeNavController?.handleDeepLink(intent)
    }
}

