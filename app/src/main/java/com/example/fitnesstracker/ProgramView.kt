package com.example.fitnesstracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnesstracker.Dataprovider.programs
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme


class ProgramView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: ViewModel by viewModels()
        super.onCreate(savedInstanceState)
        setContent {
        }
    }


    @Composable
    fun BothProgramsWorking(navController: NavController, viewModel: ViewModel) {
        val nvController = rememberNavController()
        FitnessTrackerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,

            ) {
                Scaffold(floatingActionButtonPosition = FabPosition.End , floatingActionButton = {
                    FloatingActionButton(onClick = { nvController.popBackStack() }
                    ) {
                            Icon(Icons.Default.ArrowBack, "Back Button")
                        }

                }) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues = innerPadding),
                        navController = nvController, startDestination = "List"
                    ) {
                        composable("List") {
                            List(
                                navController,
                                modifier = Modifier,
                                viewModel = viewModel,
                                name = String()
                            ) { name ->
                                nvController.navigate("ExerciseSelect/$name")
                            }
                        }
                        composable(
                            "ExerciseSelect/{name}",
                            arguments = listOf(navArgument("name") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            ExerciseSelect(modifier = Modifier, viewModel = viewModel, name = name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramsList(name: String, viewModel: ViewModel, onNavigateToDetails: (String) -> Unit) {
    Box (){
    Card(
        modifier = Modifier
            .size(width = 280.dp, height = 150.dp)
            .padding(10.dp)
            .clickable { viewModel.getPrograms(name); onNavigateToDetails.invoke(name) }, // Pass the program name to the click listener
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )  {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            ,verticalArrangement = Arrangement.Center
        ){
            Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize=20.sp, fontWeight=FontWeight.Bold, fontStyle = FontStyle.Normal, color = MaterialTheme.colorScheme.primary,
            text = name
        )}

    }
}
}

@Composable
fun List(navController: NavController, modifier: Modifier,name: String,viewModel: ViewModel, onNavigateToDetails: (String) -> Unit) {
    // Bottom bar navigation
    Scaffold(
        bottomBar = {
            BottomAppBar(

                actions = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
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
        Box {
            Image(
                painter = painterResource(id = R.drawable.on_boarding_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        LazyColumn(
            modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            items(programs) { program ->
                ProgramsList(
                    name = program.name,
                    viewModel,
                    onNavigateToDetails = onNavigateToDetails
                )
            }
        }
    }
    }
}
@Composable
    fun ExerciseSelect(name: String, modifier: Modifier = Modifier, viewModel: ViewModel) {
        Log.d("DBG", viewModel.uiState.value.toString())
    //gets a list of all values under one spesific Program_.
        var selected = listOf<String>()
        viewModel.uiState.value?.forEach { item ->
            if (item.name == name) {
                selected = item.exercises
            }
        }
    Box (modifier = Modifier
        .fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.on_boarding_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary ,
                text = "${viewModel.search}")

            LazyColumn(modifier = Modifier.background(Color.Transparent),contentPadding = PaddingValues(16.dp)) {
                items(selected) { exercise ->
                    var isChecked by remember { mutableStateOf(false) }
                    Box() {
                        ElevatedCard(modifier = Modifier
                            .background(if (isChecked) MaterialTheme.colorScheme.secondary else Color.Transparent)
                            .fillMaxWidth()
                            .fillMaxWidth()
                            .clickable {
                                isChecked = !isChecked; Log.d(
                                "DBG",
                                isChecked.toString()
                            )
                            }
                            .padding(14.dp)) {
                            Text(fontSize = 25.sp,text = "Exercise: $exercise")
                            if (isChecked) {
                                // Overlay a check mark when the card is checked
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    tint = Color.Blue,
                                    contentDescription = "Checked",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(34.dp)
                                        .background(Color.White, CircleShape)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
    }
    @Preview(showBackground = true)
    @Composable
    fun ExercisePreview() {
        FitnessTrackerTheme {


        }
    }

