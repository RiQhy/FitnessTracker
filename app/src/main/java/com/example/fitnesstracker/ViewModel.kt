package com.example.fitnesstracker

import android.Manifest
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    private val mResults = HashMap<String, ScanResult>()
    val scanResults = MutableLiveData<List<ScanResult>>(null)
    val fScanning = MutableLiveData(false)
    val mBPM = MutableLiveData(0)


    fun scanDevices(scanner: BluetoothLeScanner, context: Context) {
        Log.d("DBG", scanResults.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            fScanning.postValue(true)
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@launch
            }
            scanner.startScan(null, settings, leScanCallback)
            delay(SCAN_PERIOD)
            scanner.stopScan(leScanCallback)
            scanResults.postValue(mResults.values.toList().sortedBy { it.rssi }.asReversed())
            fScanning.postValue(false)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val deviceAddress = device.address
            mResults[deviceAddress] = result
            Log.d("DBG", "Device address: $deviceAddress (${result.isConnectable})")
        }
    }

    fun onHeartRateChanged(heartRateValue: Int) {
        // Handle the received heart rate value here
        Log.d("DBG", "Heart rate changed: $heartRateValue")
        mBPM.postValue(heartRateValue)

    }

    companion object GattAttributes {
        const val SCAN_PERIOD: Long = 5000
    }
}
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.example.fitnesstracker.ApiData.service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class ViewModel : ViewModel() {
    private val repository : SrRepository = SrRepository()
    var search : String by mutableStateOf("")
    val uiState = MutableLiveData <List<TestItem>>()
    class SrRepository {
        suspend fun hitCountCheck(name: String):List<TestItem>{
            return service.getPrograms(name)
        }
    }

    fun getPrograms(name: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                search= name
                val serverResp = repository.hitCountCheck(name)
                Log.d("DBG2",serverResp.toString())
                println(serverResp)
                //println(serverResp.query.searchinfo.exercises)
                uiState.postValue(serverResp)
            } catch (e: Exception){
                Log.d("DBG",e.message.toString())
                println(e.stackTrace)
            }
        }
        Log.d("DBG3",uiState.value.toString())
    }
}

//serverResp.query.searchinfo.exercises
//postValue(ApiResponse)