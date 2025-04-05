package com.confluence.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
actual fun PreviewDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit,
    title: String,
    content: @Composable (() -> Unit)
) {
    Dialog(
        onDismissRequest = onCloseRequest
    ) {
        content()
    }
}