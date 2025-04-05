package com.confluence.ui

import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

actual fun selectInputFile(onFileSelected: (String) -> Unit) {
    val fileChooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
        fileFilter = FileNameExtensionFilter("Markdown Files", "md", "markdown")
        dialogTitle = "Select Markdown File"
    }

    val result = fileChooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        onFileSelected(fileChooser.selectedFile.absolutePath)
    }
}

actual fun selectOutputFile(onFileSelected: (String) -> Unit) {
    val fileChooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
        fileFilter = FileNameExtensionFilter("XML Files", "xml")
        dialogTitle = "Save Confluence XML File"
    }

    val result = fileChooser.showSaveDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        var path = fileChooser.selectedFile.absolutePath
        if (!path.endsWith(".xml")) {
            path += ".xml"
        }
        onFileSelected(path)
    }
}