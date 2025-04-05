package de.felixlf.confluencemd.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Implementation of FileOperations for desktop platforms
 */
class DesktopFileOperations : FileOperations {
    override suspend fun readFile(path: String): String = withContext(Dispatchers.IO) {
        File(path).readText(StandardCharsets.UTF_8)
    }

    override suspend fun writeFile(path: String, content: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                File(path).writeText(content, StandardCharsets.UTF_8)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
}