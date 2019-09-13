package com.example.myfirstapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import android.net.wifi.WifiManager
import java.net.NetworkInterface
import kotlin.experimental.and
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    var global_string : String = ""

    fun onAsyncButtonClick(view: View)
    {
        val myToast = Toast.makeText(this, "Hello Toast", Toast.LENGTH_SHORT)
        myToast.show()
    }

    fun onUiButtonClick(view: View)
    {

    }

    fun onMacButtonClick(view: View)
    {
/*        val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        val address = info.macAddress*/

        val address = getMacAddr()

        val myToast = Toast.makeText(this, address, Toast.LENGTH_SHORT)
        myToast.show()
    }

    fun getMacAddr(): String?
    {
        var macAddr :String? = null

        try {
            val all = NetworkInterface.getNetworkInterfaces()
            for (nif in all)
            {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                val macBytes = nif.hardwareAddress ?: return ""

                val res1 = StringBuilder()
                for (b in macBytes)
                {
                    val b_int_val = b.toInt()
                    val after_and_value = b_int_val and 0xFF
                    var hex = Integer.toHexString(after_and_value)
                    if (hex.length == 1)
                        hex = "0$hex"
                    res1.append("$hex:")
                }

                if (res1.length > 0)
                {
                    res1.deleteCharAt(res1.length - 1)
                }

                macAddr = res1.toString()

            }
        } catch (ex: Exception) {
        }

        return macAddr
    }


    suspend fun asyncFunc() = GlobalScope.launch(Dispatchers.Main)
    {
        var stam: String = ""
        val statusMap = withContext(Dispatchers.Default) { BackendClient.fetchGroupStatuses(5, 5) }
        for (key in statusMap.keys) {
            stam = key
        }

        global_string = stam
    }
}
