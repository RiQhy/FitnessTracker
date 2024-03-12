package com.example.fitnesstracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme


@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.on_boarding_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // App name
        Text(
            text = "Fitness Tracker",
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        // Start button
        Button(
            onClick = {
                // Navigate to the onboarding screen
                navController.navigate("signUp")
            },
            modifier = Modifier.fillMaxWidth(0.8f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Start",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val navController = rememberNavController()
    FitnessTrackerTheme {
        SplashScreen(navController = navController)
    }
}
