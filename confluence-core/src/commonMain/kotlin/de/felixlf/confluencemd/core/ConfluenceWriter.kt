package de.felixlf.confluencemd.core

/**
 * Main class for converting Markdown to Confluence format
 */
class ConfluenceWriter(private val fileOperations: FileOperations) {
    private val converter = MarkdownConverter()

    /**
     * Convert a markdown file to Confluence XML
     *
     * @param inputPath The path to the markdown file
     * @param outputPath The path where to save the converted file
     * @return Success or failure
     */
    suspend fun convertFile(inputPath: String, outputPath: String): Boolean {
        return try {
            val markdown = fileOperations.readFile(inputPath)
            val confluenceXml = converter.convertToConfluence(markdown)
            fileOperations.writeFile(outputPath, confluenceXml)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Convert markdown string to Confluence XML
     *
     * @param markdown The markdown string
     * @return The Confluence XML
     */
    fun convertString(markdown: String): String {
        return converter.convertToConfluence(markdown)
    }
}
