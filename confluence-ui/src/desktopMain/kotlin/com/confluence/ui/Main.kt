package com.confluence.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import de.felixlf.confluencemd.core.DesktopFileOperations
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Confluence Writer",
        state = rememberWindowState()
    ) {
        window.minimumSize = Dimension(600, 450)
        val desktopFileOperations = DesktopFileOperations()
        App(desktopFileOperations)
    }
}
