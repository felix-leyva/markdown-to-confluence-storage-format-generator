package com.confluence.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
actual fun PreviewDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit,
    previewText: String,
    content: @Composable (() -> Unit)
) {
    if(visible) {
        Dialog(
            onDismissRequest = onCloseRequest
        ) {
            content()
        }
    }
}

// For Android, we need a context to access the clipboard service
// We'll use a companion object to hold a reference to the context
object ClipboardHelper {
    private var appContext: Context? = null

    fun setContext(context: Context) {
        appContext = context.applicationContext
    }

    fun copyText(text: String) {
        appContext?.let { context ->
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Confluence XML Preview", text)
            clipboard.setPrimaryClip(clip)
        }
    }
}

@Composable
actual fun RealtimeConvertorDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit,
    content: @Composable (() -> Unit)
) {
    if(visible) {
        Dialog(
            onDismissRequest = onCloseRequest
        ) {
            content()
        }
    }
}

// Implementation of clipboard functionality for Android
actual fun copyToClipboardImpl(text: String) {
    ClipboardHelper.copyText(text)
}