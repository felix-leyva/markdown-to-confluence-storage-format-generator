package de.felixlf.confluencemd.core

/**
 * Interface for handling file operations on different platforms
 */
interface FileOperations {
    /**
     * Read a file's content as string
     */
    suspend fun readFile(path: String): String

    /**
     * Write content to a file
     */
    suspend fun writeFile(path: String, content: String): Boolean
}
