package com.example.fitnesstracker


import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

class StatsView : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var totalSteps = 0f
    private var previousTotalSteps = 0f


    private var running = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val permissionsGranted = HashMap<String, Boolean>()
         val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
         val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
         val viewModel: MyViewModel by viewModels()
         val gattClientCallback = GattClientCallback(this, viewModel)
         val requiredPermissions: Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT
        )


        val requestPermissionsLauncher =
            registerForActivityResult(RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { entry ->
                    if (entry.value) {
                        permissionsGranted[entry.key] = entry.value
                        Log.d("PermissionGranted", "Permission ${entry.key} is granted")
                    } else {
                        Log.d("PermissionDenied", "Permission ${entry.key} is denied")
                    }
                }
            }
        setContent {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding()
                    .fillMaxSize()
            ) {

                ListView(
                    viewModel,
                    requestPermissionsLauncher,
                    requiredPermissions,
                    permissionsGranted,
                    bluetoothAdapter,
                    gattClientCallback
                ) {

                }
            }
            requestPermissionsLauncher.launch(requiredPermissions)

        }

        loadData()
        resetSteps()
        saveData()

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

    @Composable
    fun StatsViewScreen(navController: NavController) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
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
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
            ){
                StatsViewStuff(totalSteps.toInt() - previousTotalSteps.toInt())
                HeartRateStuff()
            }
        }
    }


    @Composable
    fun ListView(
        viewModel: MyViewModel,
        requestPermissionsLauncher: ActivityResultLauncher<Array<String>>,
        requiredPermissions: Array<String>,
        permissionsGranted: HashMap<String, Boolean>,
        bluetoothAdapter: BluetoothAdapter?,
        gattClientCallback: GattClientCallback,
        function: () -> Unit,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SearchButton(bluetoothAdapter, viewModel, permissionsGranted)
            HeartRate(viewModel, function)
            StatsViewStuff(totalSteps.toInt() - previousTotalSteps.toInt())
            SearchList(
                viewModel,
                requestPermissionsLauncher,
                requiredPermissions,
                bluetoothAdapter,
                gattClientCallback
            )
        }

    }


    @Composable
    private fun SearchButton(
        bluetoothAdapter: BluetoothAdapter?,
        viewModel: MyViewModel,
        permissionsGranted: HashMap<String, Boolean>,
    ) {
        ElevatedButton(onClick = {
            if (bluetoothAdapter != null && permissionsGranted.containsKey("android.permission.BLUETOOTH_SCAN")) {
                viewModel.scanDevices(
                    bluetoothAdapter.bluetoothLeScanner, this
                )
            } else {
                AlertDialog.Builder(this).setTitle("Permission Required")
                    .setMessage("This app requires Bluetooth scanning permission to function properly.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }.show()
            }
        }) {
            val scanning by viewModel.fScanning.observeAsState(false)
            if (scanning) {
                Text(text = "Scanning..")
            } else {
                Text(text = "Start Scanning")
            }
        }
    }

    @Composable
    private fun SearchList(
        viewModel: MyViewModel,
        requestPermissionsLauncher: ActivityResultLauncher<Array<String>>,
        requiredPermissions: Array<String>,
        bluetoothAdapter: BluetoothAdapter?,
        gattClientCallback: GattClientCallback
    ) {
        val list by viewModel.scanResults.observeAsState(emptyList())
        if (ActivityCompat.checkSelfPermission(
                this@StatsView, Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionsLauncher.launch(requiredPermissions)
        }

        if (!list.isNullOrEmpty()) {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                items(list) { item ->
                    val itemName = if (!item.device.name.isNullOrEmpty()) {
                        item.device.name
                    } else ""
                    val connectable =
                        if (item.isConnectable) MaterialTheme.colorScheme.primary else Color.Gray
                    Surface(onClick = {
                        connectToDevice(
                            bluetoothAdapter, item.device.address, gattClientCallback, viewModel
                        )
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = itemName,
                                color = connectable,
                            )
                            Text(
                                text = "${item.device.address} ${item.rssi}dBm",
                                color = connectable,
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun HeartRate(viewModel: MyViewModel, function: () -> Unit) {
        val bPM = viewModel.mBPM.observeAsState(initial = 0)
        Text(
            text = bPM.value.toString(),
            modifier = Modifier.clickable(enabled = bPM.value != 0, onClick = { function() })
        )
    }

    private fun connectToDevice(
        bluetoothAdapter: BluetoothAdapter?,
        address: String,
        gattClientCallback: GattClientCallback,
        viewModel: MyViewModel
    ) {
        if (bluetoothAdapter == null) {
            Log.e("ConnectToDevice", "BluetoothAdapter is null")
            return
        }

        val bluetoothDevice: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(address)
        if (bluetoothDevice == null) {
            Log.e("ConnectToDevice", "BluetoothDevice is null")
            return
        }

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("ConnectToDevice", "Bluetooth permission not granted")
            return
        }
        viewModel.mBPM.value = 0
        bluetoothDevice.createBond()
        bluetoothDevice.connectGatt(this, false, gattClientCallback)
    }


    @Composable
    fun HeartRateStuff() {


    }


    @Composable
    fun StatsViewStuff(currentSteps: Int, totalSteps: Int = 10000) {



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
}














