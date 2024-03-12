package com.example.fitnesstracker

import android.graphics.drawable.Drawable

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.db.AppDatabase
import com.example.fitnesstracker.db.UserRepository
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log

class MainActivity : ComponentActivity() {
    val viewModel: ViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (!isGranted) {
            // Inform the user that the permission is needed.
            // You can use a Toast, Dialog, or another method to communicate this.
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionsGranted = HashMap<String, Boolean>()
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        val viewModel: MyViewModel by viewModels()
        val gattClientCallback = GattClientCallback(this, viewModel)
        val requiredPermissions: Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        val requestPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { entry ->
                    if (entry.value) {
                        permissionsGranted[entry.key] = entry.value
                        Log.d("PermissionGranted", "Permission ${entry.key} is granted")
                    } else {
                        Log.d("PermissionDenied", "Permission ${entry.key} is denied")
                    }
                }
            }

        setContent {
            FitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()

                }
                requestPermissionsLauncher.launch(requiredPermissions)
            }
        }
    }

    //Navigation for frontview
    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        val userDao = AppDatabase.getInstance(LocalContext.current).userDao()
        val userRepository = UserRepository(userDao)
        val suViewModel: SignUpViewModel = remember { SignUpViewModel(userRepository) }

        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") { SplashScreen(navController) }
            composable("signUp") { SignUpScreen(navController, suViewModel) } // <- Corrected
            composable("frontView/{username}") { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: ""
                FrontView(navController)
            }
            composable("settings") { SettingsScreen(navController) }
            composable("exerciseProgramsView") {ProgramView().BothProgramsWorking(navController, viewModel)}
            composable("statsView") { StatsViewScreen(navController) }
        }
    }
}

// Button to take you to exercise programs view
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

// Button to take you to status view
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

// Button to take you to settings view
@Composable
fun SettingsButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Settings, "Settings menu") },
        text = { Text(text = "Settings") },
        modifier = Modifier.padding(10.dp)
    )
}


// Frontview
@Composable
fun FrontView(navController: NavController) {
    val username = UserSession.username

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.gym_back_image), // Replace 'your_background_image' with your actual image resource
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent overlay
        Surface(
            color = Color.Black.copy(alpha = 0.5f), // Adjust the alpha value for the desired transparency
            modifier = Modifier.fillMaxSize()
        ) {
            // Your content here
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Content at the top
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(16.dp)

                ) {
                    Text(
                        text = "Hello, $username",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.White // Text color
                    )

                    Image(
                        painter = painterResource(id = R.drawable.icon_fire),
                        contentDescription = "Fire icon",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Buttons at the bottom
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    ProgramsButton { navController.navigate("exerciseProgramsView") }
                    StatsButton { navController.navigate("statsView") }
                }
            }
        }

        // Settings button
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            SettingsButton { navController.navigate("settings") }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun FrontViewPreview() {
    FitnessTrackerTheme {
//        Navigation()
    }
}
