package com.example.fitnesstracker

import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.util.query
import com.example.fitnesstracker.Dataprovider.programs
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme
import com.example.fitnesstracker.ViewModel


class ProgramView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: ViewModel by viewModels()

        Log.d("DBG1", viewModel.uiState.value.toString())
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            FitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )  {
                    Scaffold(floatingActionButton =  {FloatingActionButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack,"Back Button")
                    } }) { innerPadding ->
                    NavHost(modifier = Modifier.padding(paddingValues = innerPadding),
                        navController = navController, startDestination = "List") {
                        composable("List") {
                            List(modifier = Modifier, viewModel = viewModel, name = String()) { name ->
                                navController.navigate("ExerciseSelect/$name")
                            }
                        }
                        composable(
                            "ExerciseSelect/{name}",
                            arguments = listOf(navArgument("name") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            ExerciseSelect(modifier = Modifier,viewModel = viewModel, name = name)
                        }
                    }
                    }
                }
            }
        }
    }
}


@Composable
fun ProgramsList(name: String, viewModel: ViewModel, onNavigateToDetails: (String) -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 280.dp, height = 170.dp)
            .padding(10.dp)
            .clickable { viewModel.getPrograms(name); onNavigateToDetails.invoke(name) }, // Pass the program name to the click listener
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    )  {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ,verticalArrangement = Arrangement.Center
        ){
            Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize=20.sp, fontWeight=FontWeight.Bold, fontStyle = FontStyle.Normal,
            text = name
        )}

    }
}

@Composable
fun List(modifier: Modifier,name: String,viewModel: ViewModel, onNavigateToDetails: (String) -> Unit) {
    LazyColumn(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,) {
        items(programs) { program ->
            ProgramsList(name = program.name, viewModel, onNavigateToDetails = onNavigateToDetails)
        }
    }
}

@Composable
fun ExerciseSelect(name: String, modifier: Modifier = Modifier, viewModel: ViewModel) {
    Log.d("DBG", viewModel.uiState.value.toString())
    var selected = listOf<String>()
    viewModel.uiState.value?.forEach { item ->
        if (item.name == name) {
        selected = item.exercises
        }
    }
    Column{
    Text(fontSize=25.sp, fontWeight=FontWeight.Bold ,text = "${viewModel.search}")

    LazyColumn (contentPadding = PaddingValues(16.dp)){
        items(selected){exercise ->
            var isChecked by remember { mutableStateOf(false) }
            Box(){
                ElevatedCard (modifier = Modifier
                    .background(if (isChecked) Color.Gray else Color.White)
                    .fillMaxWidth()
                    .clickable { isChecked = !isChecked; Log.d("DBG", isChecked.toString()) }
                    .padding(14.dp)){
                Text(fontSize=25.sp ,text = "Exercise: $exercise")
                if (isChecked) {
                    // Overlay a check mark when the card is checked
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Checked",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                            .background(Color.White, CircleShape)
                    )
                }
                }
            }

        }
    }
    }

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
        ExerciseSelect(viewModel = ViewModel(), name = "boi")

    }
}
