
package com.example.fitnesstracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Composable function for a labeled text field
@Composable
fun LabeledTextField(label: String, textFieldValue: String, onValueChange: (String) -> Unit) {
    TextField(
        value = textFieldValue,
        onValueChange = onValueChange,
        placeholder = { Text("Write here") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .height(52.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        ),
        leadingIcon = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}



// Composable function for settings screen
@Composable
fun SettingsScreen(navController: NavController) {
    // State variables for settings
    var weightFieldValue by remember { mutableStateOf("") }
    var heightFieldValue by remember { mutableStateOf("") }
    var genderFieldValue by remember { mutableStateOf("") }

    // Surface for displaying settings
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Bottom bar navigation
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { navController.navigate("frontView/{username}") }) {
                                Icon(Icons.Filled.Home, contentDescription = "Takes you to frontpage")
                            }
                            IconButton(onClick = { navController.navigate("settings") }) {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = "Takes you to settings page",
                                )
                            }
                            IconButton(onClick = { navController.navigate("exerciseProgramsView") }) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Takes you to exercise programs page",
                                )
                            }
                            IconButton(onClick = { navController.navigate("statsView") }) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Takes you to status page",
                                )
                            }
                        }
                    },
                )
            },
        ) { innerPadding ->
            // Column for arranging elements vertically
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {

                // Labeled text fields for weight, height, and gender
                LabeledTextField(
                    label = "Weight:",
                    textFieldValue = weightFieldValue
                ) { weightFieldValue = it }
                LabeledTextField(
                    label = "Height:",
                    textFieldValue = heightFieldValue
                ) { heightFieldValue = it }
                LabeledTextField(
                    label = "Man/Woman:",
                    textFieldValue = genderFieldValue
                ) { genderFieldValue = it }
            }
        }
    }
}

// Preview function for settings screen
@Preview
@Composable
fun SettingsScreenPreview() {

}
