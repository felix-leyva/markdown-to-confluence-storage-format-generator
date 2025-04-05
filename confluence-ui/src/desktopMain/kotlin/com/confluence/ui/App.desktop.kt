package com.confluence.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
actual fun PreviewDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit,
    previewText: String,
    content: @Composable (() -> Unit)
) {
    DialogWindow(
        visible = visible,
        onCloseRequest = onCloseRequest,
        title = "Confluence XML Preview",
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                color = MaterialTheme.colors.surface
            ) {
                content()
            }
        },
    )
}

// Implementation of clipboard functionality for desktop
actual fun copyToClipboardImpl(text: String) {
    val selection = StringSelection(text)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}