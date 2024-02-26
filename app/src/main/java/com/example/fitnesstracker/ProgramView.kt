package com.example.fitnesstracker

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
                    Programs("Boo")
                }
            }
        }
    }
}


@Composable
fun Programs (name:String, modifier: Modifier = Modifier){
    Card (modifier = Modifier.size(width = 240.dp, height = 100.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp))
    {
        Text(
            text = name
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramPreview(){
    FitnessTrackerTheme {
        Programs("Fat")
    }

}