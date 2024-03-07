package com.example.fitnesstracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnesstracker.Dataprovider.programs


@Composable
fun ProgramsList (name:String, modifier: Modifier = Modifier){
    Card (modifier = Modifier
        .size(width = 240.dp, height = 100.dp)
        .padding(10.dp)
        .clickable { TODO("ViewModel and api for clicking data") },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp)
            )
    {
        Text(
            text = name
        )
    }
}
@Composable
fun List (navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate("frontView") }) {
                        Icon(Icons.Filled.Home, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { navController.navigate("exerciseProgramsView") }) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { navController.navigate("statsView") }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Localized description",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(programs) { program ->
                ProgramsList(name = program.name)
            }
        }
    }
}

