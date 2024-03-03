package com.example.fitnesstracker


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text


class StatsView : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            StatsViewScreen(totalSteps.toInt() - previousTotalSteps.toInt())
        }

        loadData()
        resetSteps()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun resetSteps() {

    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("Key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("Key1", 0f)
        previousTotalSteps = savedNumber
    }
}

@Composable
fun StatsViewScreen(currentSteps: Int, totalSteps: Int = 10000) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        // Convert currentSteps to progress between 0f and 1f
        val progress = currentSteps.toFloat() / totalSteps.toFloat()

        CircularProgressIndicator(
            progress = progress,
            color = Color(0xFFF44336),
            strokeWidth = 8.dp, // Specify the stroke width here directly
            modifier = Modifier.size(200.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$currentSteps",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "/$totalSteps",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}




