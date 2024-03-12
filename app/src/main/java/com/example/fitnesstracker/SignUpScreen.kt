package com.example.fitnesstracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnesstracker.db.User

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel) {
    var name by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.on_boarding_image3),
            contentDescription = "dumBells_image_back",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Title "Enter your parameters"
        Text(
            text = "Enter your parameters",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Form content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Name TextField
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Height TextField
            TextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Weight TextField
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Age TextField
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Gender selection buttons
            Text("Gender")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                // Man button
                Button(
                    onClick = { gender = "Man" },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Man",
                        color = if (gender == "Man") Color.Red else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Woman button
                Button(
                    onClick = { gender = "Woman" },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp) // Increase padding to make button larger
                ) {
                    Text(
                        "Woman",
                        color = if (gender == "Woman") Color.Red else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    // Validate input fields
                    val heightValue = height.toIntOrNull()
                    val weightValue = weight.toIntOrNull()
                    val ageValue = age.toIntOrNull()

                    if (name.isBlank() || name.split("\\s+".toRegex()).size < 1 || height.isBlank() || heightValue == null || heightValue !in 120..250 || weight.isBlank() || weightValue == null || weightValue !in 40..300 || age.isBlank() || ageValue == null || ageValue !in 18..100 || gender.isBlank()) {
                        errorMessage = "Invalid input. Please check your entries."
                    } else {
                        // All input fields are valid, proceed with user registration
                        viewModel.signUpUser(
                            User(
                                name = name,
                                height = heightValue,
                                weight = weightValue,
                                age = ageValue,
                                gender = gender
                            )
                        ) { username ->
                            navController.navigate("frontView/$username")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(
                    "Submit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Display Error Message
            errorMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = message,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
