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
@Composable
fun DarkThemeSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val backgroundColor = if (isChecked) Color.Black else Color(0xFFD9D9D9)
    val textToggle = if (isChecked) "Light Mode" else "Dark Mode"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 32.dp)
            .size(width = 253.dp, height = 52.dp)
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = textToggle,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = if (isChecked) Color.White else Color.Black // Adjust text color based on isChecked
                )
            )
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}


@Composable
fun SettingsScreen() {
    var isChecked by remember { mutableStateOf(false) }
    var weightFieldValue by remember { mutableStateOf("") }
    var heightFieldValue by remember { mutableStateOf("") }
    var genderFieldValue by remember { mutableStateOf("") }

    val backgroundColor = if (isChecked) Color.Black else Color.White // Determine background color based on isChecked

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            DarkThemeSwitch(isChecked = isChecked, onCheckedChange = { isChecked = it })
            LabeledTextField(label = "Weight:", textFieldValue = weightFieldValue) { weightFieldValue = it }
            LabeledTextField(label = "Height:", textFieldValue = heightFieldValue) { heightFieldValue = it }
            LabeledTextField(label = "Man/Woman:", textFieldValue = genderFieldValue) { genderFieldValue = it }
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}

