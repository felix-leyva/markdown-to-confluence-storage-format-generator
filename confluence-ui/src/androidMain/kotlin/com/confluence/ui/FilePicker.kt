package com.confluence.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent

// For Android, we need a different approach for file selection
private var fileCallbackInput: ((String) -> Unit)? = null
private var fileCallbackOutput: ((String) -> Unit)? = null

actual fun selectInputFile(onFileSelected: (String) -> Unit) {
    fileCallbackInput = onFileSelected
    // This will be called from MainActivity
    FilePickerManager.launchInputFilePicker()
}

actual fun selectOutputFile(onFileSelected: (String) -> Unit) {
    fileCallbackOutput = onFileSelected
    // This will be called from MainActivity
    FilePickerManager.launchOutputFilePicker()
}

// Activity is being cleaned up in MainActivity onDestroy, best would be to use DI.
@SuppressLint("StaticFieldLeak")
object FilePickerManager {
    var activity: MainActivity? = null

    fun launchInputFilePicker() {
        activity?.let { act ->
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/markdown", "text/plain"))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            act.startActivityForResult(intent, INPUT_FILE_REQUEST_CODE)
        }
    }

    fun launchOutputFilePicker() {
        activity?.let { act ->
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/xml"
                putExtra(Intent.EXTRA_TITLE, "confluence_output.xml")
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            act.startActivityForResult(intent, OUTPUT_FILE_REQUEST_CODE)
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INPUT_FILE_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        activity?.contentResolver?.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        fileCallbackInput?.invoke(uri.toString())
                    }
                }

                OUTPUT_FILE_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        activity?.contentResolver?.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                        fileCallbackOutput?.invoke(uri.toString())
                    }
                }
            }
        }
    }

    private const val INPUT_FILE_REQUEST_CODE = 1001
    private const val OUTPUT_FILE_REQUEST_CODE = 1002
}
