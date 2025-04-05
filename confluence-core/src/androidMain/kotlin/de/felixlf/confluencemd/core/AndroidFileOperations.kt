package de.felixlf.confluencemd.core

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * Implementation of FileOperations for Android platforms
 */
class AndroidFileOperations(private val context: Context) : FileOperations {

    override suspend fun readFile(path: String): String = withContext(Dispatchers.IO) {
        val uri = Uri.parse(path)
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append('\n')
        }

        reader.close()
        inputStream?.close()

        return@withContext stringBuilder.toString()
    }

    override suspend fun writeFile(path: String, content: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val uri = Uri.parse(path)
                val contentResolver = context.contentResolver
                val outputStream = contentResolver.openOutputStream(uri)
                val writer = OutputStreamWriter(outputStream)

                writer.write(content)
                writer.flush()
                writer.close()
                outputStream?.close()

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
}