package com.example.fitnesstracker

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnesstracker.db.AppDatabase
import com.example.fitnesstracker.db.User
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController, onSignUpSuccess: (String)->Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userDao = AppDatabase.getInstance(context).userDao()

    var name by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.on_boarding_image3),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // User input fields
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                val selectedButtonColor = MaterialTheme.colorScheme.primary // Change to your desired color
                val unselectedButtonColor = MaterialTheme.colorScheme.secondary // Change to your desired color

                Button(
                    onClick = { gender = "Man" },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(if (gender == "Man") selectedButtonColor else unselectedButtonColor)
                ) {
                    Text("Man", color = if (gender == "Man") Color.White else Color.Black)
                }
                Button(
                    onClick = { gender = "Woman" },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(if (gender == "Woman") selectedButtonColor else unselectedButtonColor)
                ) {
                    Text("Woman", color = if (gender == "Woman") Color.White else Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                if (name.isNotBlank() && height.isNotBlank() && weight.isNotBlank() && age.isNotBlank() && gender.isNotBlank()) {
                    coroutineScope.launch {
                        // Perform database operation asynchronously
                        val trimmedHeight = height.trim()
                        val trimmedWeight = weight.trim()
                        val trimmedAge = age.trim()

                        val user = User(
                            name = name,
                            height = trimmedHeight.toInt(),
                            weight = trimmedWeight.toInt(),
                            age = trimmedAge.toInt(),
                            gender = gender
                        )
                        userDao.insertUser(user)
                        // Navigate to the desired screen upon successful insertion
                        onSignUpSuccess(name)
                    }
                } else {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Submit")
            }
        }
    }
}