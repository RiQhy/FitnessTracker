package com.example.fitnesstracker

import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnesstracker.Dataprovider.programs
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme
import com.example.fitnesstracker.ViewModel


class ProgramView : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: ViewModel by viewModels()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            FitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "List") {
                        composable("List") {
                            List(viewModel = viewModel, name = ""){
                                    name -> navController.navigate("ExerciseSelect/$name")
                            }
                            ExerciseSelect(viewModel = viewModel,name = "")
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun ProgramsList (name:String, viewModel: ViewModel, onNavigateToDetails: (String) -> Unit ){
    Card (modifier = Modifier
        .size(width = 240.dp, height = 100.dp)
        .padding(10.dp)
        .clickable { viewModel.getPrograms(name) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp),)
    {
        Text(modifier = Modifier.align(Alignment.CenterHorizontally),
            text = name
        )
    }
}
@Composable
fun List (name:String, viewModel: ViewModel, onNavigateToDetails: (String) -> Unit ) {
    LazyColumn (modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        items(programs) { program ->
            ProgramsList(name = program.name, viewModel , onNavigateToDetails = onNavigateToDetails)
        }
    }
}

@Composable
fun ExerciseSelect (name:String, modifier: Modifier = Modifier, viewModel: ViewModel) {
    Text(text = "${viewModel.search}: kamta: ${viewModel.uiState.value}")
}

   // @Preview(showBackground = true)
  //  @Composable
  //  fun ProgramPreview() {
   //     FitnessTrackerTheme {
     //       List(viewModel = ViewModel())
   //     }
   // }

@Preview(showBackground = true)
@Composable
fun ExercisePreview() {
    FitnessTrackerTheme {
        ExerciseSelect(viewModel = ViewModel(),name ="boi")

    }
}
