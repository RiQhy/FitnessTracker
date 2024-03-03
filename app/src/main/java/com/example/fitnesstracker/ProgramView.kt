package com.example.fitnesstracker

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnesstracker.Dataprovider.programs
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme


class ProgramView : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}


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
    LazyColumn {
        items(programs) { program ->
            ProgramsList(name = program.name)
        }
    }
    /*Button(
        onClick = {navController.popBackStack()},
        modifier = Modifier
            .padding(10.dp)
    ){
        Text(text = "Back")
    }*/
}

@Preview(showBackground = true)
@Composable
fun ProgramPreview() {
    FitnessTrackerTheme {

    }
}
