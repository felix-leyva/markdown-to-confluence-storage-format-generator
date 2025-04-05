package com.confluence.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.confluence.core.AndroidFileOperations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity reference for file picking
        FilePickerManager.activity = this

        val androidFileOperations = AndroidFileOperations(applicationContext)

        setContent {
            App(androidFileOperations)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FilePickerManager.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            // Clear the activity reference when the activity is destroyed
            FilePickerManager.activity = null
        }
    }
}