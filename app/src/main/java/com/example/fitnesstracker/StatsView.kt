package com.example.fitnesstracker


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.absoluteValue
import android.Manifest
import android.app.AlertDialog
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.setValue

@Composable
fun StatsViewScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate("frontView/{username}") }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Takes you to the front page"
                        )
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Takes you to the settings page",
                        )
                    }
                    IconButton(onClick = { navController.navigate("exerciseProgramsView") }) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Takes you to the exercise programs page",
                        )
                    }
                    IconButton(onClick = { navController.navigate("statsView") }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Takes you to the status page",
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),

        ) {

            CalendarApp()
            HeartRateStuff(MyViewModel())
            StepCounterApp()



        }
    }
}
@Composable
fun StepCounterApp() {
    val stepCounterViewModel: StepCounterViewModel = viewModel()

    // Observe the steps count as a state and correctly reference
    val steps = stepCounterViewModel.steps.absoluteValue

    // Use DisposableEffect for lifecycle-aware operations
    DisposableEffect(key1 = Unit) {
        // Commands to execute when entering composition
        stepCounterViewModel.startListening()

        // Cleanup commands
        onDispose {
            stepCounterViewModel.stopListening()
        }
    }


    StepCounterDisplay(steps)
}

@Composable
fun StepCounterDisplay(steps: Float) { // Accepts steps directly
    Box(contentAlignment = Alignment.Center) {
        // Use the steps value directly in your Text composable
        CircularProgressIndicator(
            progress = steps,
            color = Color(0xFFF44336),
            strokeWidth = 8.dp, // Specify the stroke width here directly
            modifier = Modifier.size(200.dp)
        )
        Text(text = "Steps: ${steps.toInt()}/10000")
    }
}


@Composable
fun HeartRate(viewModel: MyViewModel, function: () -> Unit) {
    val bPM = viewModel.mBPM.observeAsState(initial = 0)
    Text(
        text = bPM.value.toString(),
        modifier = Modifier.clickable(
            enabled = bPM.value != 0,
            onClick = { function() })
    )
}

@Composable
fun HeartRateStuff(viewModel: MyViewModel) {
    // Assuming viewModel has a LiveData or State holding heart rate value
    val heartRate by viewModel.mBPM.observeAsState(initial = 0)

    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Heart Rate",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            // Display the heart rate value
            Text(
                text = "$heartRate BPM",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
    }
}




@Preview(showSystemUi = true)
@Composable
fun CalendarAppPreview() {
    FitnessTrackerTheme {
        CalendarApp(
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun CalendarApp(
    modifier: Modifier = Modifier,
) {
    val dataSource = CalendarDataSource()
    var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    Column(modifier = modifier) {
        Header(
            data = data,
            onPrevClickListener = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                data = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = data.selectedDate.date
                )
            },
            onNextClickListener = { endDate ->
                val finalStartDate = endDate.plusDays(2)
                data = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = data.selectedDate.date
                )
            }
        )
        Content(data = data) { date ->
            data = data.copy(
                selectedDate = date,
                visibleDates = data.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )
        }
    }
}

@Composable
fun Header(
    data: CalendarUiModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {
    Row {
        Text(
            text = if (data.selectedDate.isToday) {
                "Today"
            } else {
                data.selectedDate.date.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                )
            },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun Content(
    data: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit,
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 48.dp)) {
        items(data.visibleDates.size) { index ->
            ContentItem(
                date = data.visibleDates[index],
                onDateClickListener
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItem(
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                onClickListener(date)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .height(48.dp)
                .padding(4.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
