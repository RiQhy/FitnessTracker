package com.example.fitnesstracker

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.UUID

class GattClientCallback(private val context: Context, private val viewModel: MyViewModel) :
    BluetoothGattCallback() {
    private fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

    private val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
    private val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
    private val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)

    /* Generates 128-bit UUID from the Protocol Indentifier (16-bit number)
    * and the BASE_UUID (00000000-0000-1000-8000-00805F9B34FB)
    */
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d("DBG", "GATT connection failure")
            return
        } else if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d("DBG", "GATT connection success")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            gatt.device.createBond()
        }
        //Joku kusee tässä se ei saa oikeaa profiilia(?)
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.d("DBG", "Connected GATT service")

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("DBG", "No Permission")
                return
            }
            gatt.discoverServices()
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // Handle disconnection state if needed

        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d("DBG", "GATT Failed")
            return
        }
        Log.d("DBG", "onServicesDiscovered()")
        for (gattService in gatt.services) {
            Log.d("DBG", "Service ${gattService.uuid}")
            if (gattService.uuid == HEART_RATE_SERVICE_UUID) {
                Log.d("DBG", "BINGO!!!")
                for (gattCharacteristic in gattService.characteristics) {
                    Log.d("DBG", "Characteristic ${gattCharacteristic.uuid}")
                }
                // setup the system for the notification messages
                val characteristic = gatt.getService(HEART_RATE_SERVICE_UUID)
                    .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                gatt.setCharacteristicNotification(characteristic, true)
                val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        }
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {
        Log.d("DBG", "onDescriptorWrite")
    }

    @Deprecated("Deprecated in Java")
    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        Log.d("DBG","Pls show stuff ${characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1)}")
        if (characteristic.uuid == HEART_RATE_MEASUREMENT_CHAR_UUID) {
            val rateValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1)
            if (rateValue != null) {
                val rate = rateValue.toInt()
                viewModel.onHeartRateChanged(rate)
            } else {
                Log.e("DBG", "Heart rate characteristic value is null")
            }
        }
    }


}