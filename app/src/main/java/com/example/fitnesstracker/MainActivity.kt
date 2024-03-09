package com.example.fitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme

class MainActivity : ComponentActivity() {
    val viewModel: ViewModel by viewModels()
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

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController)}
        composable("signUp") { SignUpScreen(navController) { username -> navController.navigate("frontView/$username") } }
        composable("frontView/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            Frontview(navController, username)
        }
        composable("settings") { SettingsScreen(navController)}
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
fun Frontview(navController: NavController, username: String) {
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Display the username
        Text(text = "Hello, $username!", modifier = Modifier.align(Alignment.TopStart))

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
            Image(painter = painterResource(R.drawable.fbft), contentDescription = "Frontview background")
            ProgramsButton {navController.navigate("exerciseProgramsView")}
            StatsButton {navController.navigate("statsView")}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrontviewPreview() {
    FitnessTrackerTheme {
//        Navigation()
    }
}