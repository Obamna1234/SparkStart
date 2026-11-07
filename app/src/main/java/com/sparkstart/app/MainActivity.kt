package com.sparkstart.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sparkstart.app.data.Store
import com.sparkstart.app.ui.App
import com.sparkstart.app.ui.SparkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val store = Store(applicationContext)
        setContent {
            SparkTheme { App(store) }
        }
    }
}
