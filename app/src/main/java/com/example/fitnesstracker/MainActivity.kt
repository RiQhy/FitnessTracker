package com.example.fitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "frontView") {
        composable("frontView") { Frontview(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("exerciseProgramsView") { List(navController) }
        composable("statsView") { StatsView().StatsViewScreen(navController) }
    }
}



@Composable
fun ProgramsButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Star, "Takes user to exercise programs view") },
        text = { Text(text = "Exercise Programs") },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    )
}
@Composable
fun StatsButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Favorite, "Takes user to a view that shows information about the user and training progress like step counter or users weight.") },
        text = { Text(text = "Status") },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    )
}
@Composable
fun SettingsButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Settings, "Settings menu") },
        text = { Text(text = "Settings") },
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun Frontview(navController: NavController) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            SettingsButton { navController.navigate("settings")}
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            ProgramsButton {navController.navigate("exerciseProgramsView")}
            StatsButton {navController.navigate("statsView")}
        }
    }
}



@Preview(showBackground = true)
@Composable
fun FrontviewPreview() {
    FitnessTrackerTheme {
        Navigation()
    }
}