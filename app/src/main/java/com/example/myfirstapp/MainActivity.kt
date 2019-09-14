package com.example.myfirstapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import android.net.wifi.WifiManager
import com.kotlinpermissions.KotlinPermissions
import io.github.rybalkinsd.kohttp.ext.asString
import io.github.rybalkinsd.kohttp.ext.httpGet
import kotlinx.android.synthetic.main.activity_main.*

// Enables Http requests
import okhttp3.Response

// Enables super convenient usage of async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity()
{
    val DEFAULT_MAC_WHEN_LACKING_PERMISSIONS = "02:00:00:00:00:00"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup button onClicks operations - important to do here and not via the xml.
        // Mainly because async operations inside such onClick methods will crash the app,
        // while this way they won't.
        asyncOperationButton.setOnClickListener{
            onAsyncButtonClick()
        }

        helloButton.setOnClickListener{
            onHelloButtonClick()
        }

        macButton.setOnClickListener{
            onMacButtonClick()
        }

        // Location permissions are requested if needed to fetch the MAC (why? unclear)
        // Once granted they won't be requested again
        // (unless manually removed through the device's settings)
        askForLocationPermissionIfNeeded()
    }

    fun onAsyncButtonClick(){
        doAsync {
            var result = fetchStringFromWeb()

            uiThread {
                showToast(result.toString())
            }
        }
    }

    private fun fetchStringFromWeb(): String?
    {
        var data: String? = null

        try {
            val response : Response = "https://mark-api.azurewebsites.net/api/Test?ZUMO-API-VERSION=2.0.0&userId=53c08eb06894d20da22c7375b0dcfb6f&latitude=32.18346716&longitude=34.87212952".httpGet()
            data = response.asString()

        }
        catch(e: Exception){
            val stam = 6
        }

        return data
    }

    fun onHelloButtonClick()
    {
        showToast("Hello Toast")
    }

    fun onMacButtonClick()
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
