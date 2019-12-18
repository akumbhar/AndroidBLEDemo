package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {


    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled
    private val REQUEST_ENABLE_BT = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //requestForPermission()
        checkBTPermissions()
    }

    private fun requestForPermission() {
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode != Activity.RESULT_OK) {
            showShortToast("Please enable bluetooth connection")
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck =
                ContextCompat.checkSelfPermission(this, "Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += ContextCompat.checkSelfPermission(
                this,
                "Manifest.permission.ACCESS_COARSE_LOCATION"
            )
            if (permissionCheck != 0) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                ) //Any number
            } else {
                //startBluetoothActivities()
            }
        } else {
            doLog("checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            for (i in 0 until permissions.size) {
//                var permission= permissions.get(i)
                var result = grantResults.get(i)
                if (result == PackageManager.PERMISSION_GRANTED) {
                    //startBluetoothActivities()
                } else {
                    showShortToast("Please enable the location service to discover the BLE devices around")
                }
            }
        }
    }
}
