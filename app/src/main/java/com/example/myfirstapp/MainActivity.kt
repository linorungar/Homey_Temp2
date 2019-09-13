package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import java.math.BigInteger

import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    var global_string : String = ""

    fun onLabelClick()
    {
        val myToast = Toast.makeText(this, "Hello Toast", Toast.LENGTH_SHORT)
        myToast.show()
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
