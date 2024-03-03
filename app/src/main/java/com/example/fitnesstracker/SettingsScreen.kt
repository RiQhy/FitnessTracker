
package com.example.fitnesstracker


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstracker.ui.theme.ColorConstants

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
            unfocusedContainerColor = ColorConstants.backColor
        )
    )
}

// Composable function for dark theme switch
@Composable
fun DarkThemeSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    // Commented out code for future use
//    val backgroundColor = if (isChecked) Color.Black else Color(0xFFD9D9D9)
    val textToggle = if (isChecked) "Light Mode" else "Dark Mode"

    // Box for containing the switch
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 32.dp)
            .size(width = 253.dp, height = 52.dp)
            .background(Color(0xFFD9D9D9))
    ) {
        // Row for arranging elements horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text displaying the current mode
            Text(
                text = textToggle,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
//                    color = if (isChecked) Color.White else Color.Black
                )
            )
            // Switch for toggling between modes
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}

// Composable function for settings screen
@Composable
fun SettingsScreen() {
    // State variables for settings
    var isChecked by remember { mutableStateOf(false) }
    var weightFieldValue by remember { mutableStateOf("") }
    var heightFieldValue by remember { mutableStateOf("") }
    var genderFieldValue by remember { mutableStateOf("") }

    // Determine background color based on theme
    val backgroundColor = if (isChecked) Color.Black else Color.White

    // Surface for displaying settings
    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        // Column for arranging elements vertically
        Column {
            // Dark theme switch
            DarkThemeSwitch(isChecked = isChecked, onCheckedChange = { isChecked = it })
            // Labeled text fields for weight, height, and gender
            LabeledTextField(label = "Weight:", textFieldValue = weightFieldValue) { weightFieldValue = it }
            LabeledTextField(label = "Height:", textFieldValue = heightFieldValue) { heightFieldValue = it }
            LabeledTextField(label = "Man/Woman:", textFieldValue = genderFieldValue) { genderFieldValue = it }
        }
    }
}

// Preview function for settings screen
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
