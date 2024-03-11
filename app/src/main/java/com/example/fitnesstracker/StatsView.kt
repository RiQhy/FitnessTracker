package com.example.fitnesstracker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import kotlin.math.absoluteValue

@Composable
fun StatsViewScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate("frontView/{username}") }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Takes you to the front page"
                        )
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Takes you to the settings page",
                        )
                    }
                    IconButton(onClick = { navController.navigate("exerciseProgramsView") }) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Takes you to the exercise programs page",
                        )
                    }
                    IconButton(onClick = { navController.navigate("statsView") }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Takes you to the status page",
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),

        ) {

            StepCounterApp()



        }
    }
}
@Composable
fun StepCounterApp() {
    val stepCounterViewModel: StepCounterViewModel = viewModel()

    // Observe the steps count as a state and correctly reference
    val steps = stepCounterViewModel.steps.absoluteValue

    // Use DisposableEffect for lifecycle-aware operations
    DisposableEffect(key1 = Unit) {
        // Commands to execute when entering composition
        stepCounterViewModel.startListening()

        // Cleanup commands
        onDispose {
            stepCounterViewModel.stopListening()
        }
    }


    StepCounterDisplay(steps)
}

@Composable
fun StepCounterDisplay(steps: Float) { // Accepts steps directly
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        // Use the steps value directly in your Text composable
        Text(text = "Steps: ${steps.toInt()}/10000")
    }
}
