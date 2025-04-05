package com.confluence.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.confluence.ui.PreviewDialog

@Composable
fun PreviewDialog(
    visible: Boolean,
    previewText: String,
    setShowPreview: (Boolean) -> Unit
) {
    PreviewDialog(
        visible = visible,
        onCloseRequest = { setShowPreview(false) },
        title = "Confluence XML Preview"
    ) {
        Column {
            ScrollablePreviewText(previewText)
            Button(onClick = { setShowPreview(false) }) {
                Text("Close")
            }
        }
    }
}