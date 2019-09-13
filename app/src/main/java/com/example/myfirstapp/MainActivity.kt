package com.example.myfirstapp

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import android.net.wifi.WifiManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kotlinpermissions.KotlinPermissions
import java.net.NetworkInterface
import java.util.jar.Manifest
import kotlin.experimental.and
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    val DEFAULT_MAC_WHEN_LACKING_PERMISSIONS = "02:00:00:00:00:00"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Location permissions are requested if needed to fetch the MAC (why? unclear)
        // Once granted they won't be requested again
        // (unless manually removed through the device's settings)
        askForLocationPermissionIfNeeded()
    }

    fun onAsyncButtonClick(view: View)
    {
    }


    fun onHelloButtonClick(view :View)
    {
        showToast("Hello Toast")
    }

    fun onMacButtonClick(view: View)
    {
        val macAddress = getConnectedWifiMac()

        if (macAddress.isNullOrEmpty())
        {
            showToast("Please connect to Wi-Fi")

        }
        else if (macAddress == DEFAULT_MAC_WHEN_LACKING_PERMISSIONS)
        {
            showToast("Please enable the location permission of the app through the device's settings")
        }

        else
        {
            showToast(macAddress)
        }
    }

    private suspend fun asyncFunc() = GlobalScope.launch(Dispatchers.Main)
    {
        var stam: String = ""
        val statusMap = withContext(Dispatchers.Default) { BackendClient.fetchGroupStatuses(5, 5) }
        for (key in statusMap.keys) {
            stam = key
        }

        // global_string = stam
    }

    private fun askForLocationPermissionIfNeeded()
    {
        // Available thanks to adding a dependency to build.gradle(Mobile: app):
        // implementation 'ru.superjob:kotlin-permissions:1.0.3'
        // https://github.com/superjobru/kotlin-permissions
        KotlinPermissions.with(this)
            .permissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            .ask()
    }

    // Returns an empty string if the device is not connected to a Wi-Fi network!
    private fun getConnectedWifiMac() : String?
    {
        val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        return info.bssid
    }

    private fun showToast(message : CharSequence)
    {
        val myToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        myToast.show()
    }
}
