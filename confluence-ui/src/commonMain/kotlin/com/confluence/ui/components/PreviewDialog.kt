package com.confluence.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.confluence.ui.PreviewDialog
import com.confluence.ui.copyToClipboardImpl

@Composable
fun PreviewDialog(
    visible: Boolean,
    previewText: String,
    setShowPreview: (Boolean) -> Unit
) {
    PreviewDialog(
        visible = visible,
        onCloseRequest = { setShowPreview(false) },
        previewText = previewText
    ) {
        Column {
            // Copy button at the top
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Call platform-specific implementation
                        copyToClipboardImpl(previewText)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy to clipboard"
                        )
                        Text("Copy to Clipboard", modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable preview text
            ScrollablePreviewText(previewText)

            // Close button
            Button(
                onClick = { setShowPreview(false) },
                modifier = Modifier.align(Alignment.End).padding(8.dp)
            ) {
                Text("Close")
            }
        }
    }
}