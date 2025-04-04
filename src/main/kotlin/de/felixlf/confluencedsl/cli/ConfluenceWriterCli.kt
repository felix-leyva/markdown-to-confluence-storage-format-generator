package de.felixlf.confluencedsl.cli

import de.felixlf.confluencedsl.ConfluenceDSL
import java.io.File
import java.nio.file.Paths
import kotlin.system.exitProcess

/**
 * Command-line interface for converting Markdown to Confluence XML format
 */
class ConfluenceWriterCli {
    companion object {
        private const val DEFAULT_OUTPUT_EXTENSION = ".xml"

        /**
         * Main entry point for the CLI application
         */
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isEmpty()) {
                printHelp()
                return
            }

            try {
                processArguments(args)
            } catch (e: Exception) {
                System.err.println("Error: ${e.message}")
                printHelp()
                exitProcess(1)
            }
        }

        /**
         * Process command line arguments
         */
        private fun processArguments(args: Array<String>) {
            var inputFile: String? = null
            var outputFile: String? = null
            var markdownContent: String? = null
            var mode = "file"

            var i = 0
            while (i < args.size) {
                when (args[i]) {
                    "--mdfile", "-f" -> {
                        i++
                        if (i < args.size) {
                            inputFile = args[i]
                            mode = "file"
                        } else {
                            throw IllegalArgumentException("Missing file path after --mdfile/-f")
                        }
                    }
                    "--mdstring", "-s" -> {
                        i++
                        if (i < args.size) {
                            markdownContent = args[i]
                            mode = "string"
                        } else {
                            throw IllegalArgumentException("Missing markdown content after --mdstring/-s")
                        }
                    }
                    "--output", "-o" -> {
                        i++
                        if (i < args.size) {
                            outputFile = args[i]
                        } else {
                            throw IllegalArgumentException("Missing output file path after --output/-o")
                        }
                    }
                    "--help", "-h" -> {
                        printHelp()
                        return
                    }
                    else -> {
                        // If no specific option is provided, treat first argument as input file
                        if (inputFile == null && markdownContent == null) {
                            inputFile = args[i]
                            mode = "file"
                        } else {
                            throw IllegalArgumentException("Unexpected argument: ${args[i]}")
                        }
                    }
                }
                i++
            }

            if (mode == "file" && inputFile == null) {
                throw IllegalArgumentException("Input file not specified")
            }
            
            if (mode == "string" && markdownContent == null) {
                throw IllegalArgumentException("Markdown content not specified")
            }

            // Convert input to Confluence XML
            val confluenceXml = when (mode) {
                "file" -> {
                    val file = File(inputFile!!)
                    if (!file.exists()) {
                        throw IllegalArgumentException("Input file does not exist: $inputFile")
                    }
                    
                    println("Converting file: $inputFile")
                    ConfluenceDSL.fromMarkdownFile(inputFile)
                }
                "string" -> {
                    println("Converting markdown string")
                    // Process the markdownContent to handle escaped newlines
                    val processedContent = markdownContent!!
                        .replace("\\n", "\n")  // Replace escaped newlines with actual newlines
                    ConfluenceDSL.fromMarkdown(processedContent)
                }
                else -> throw IllegalArgumentException("Invalid mode: $mode")
            }

            // Determine output file if not specified
            if (outputFile == null && mode == "file") {
                val inputPath = Paths.get(inputFile!!)
                val fileName = inputPath.fileName.toString()
                val baseName = fileName.substringBeforeLast(".")
                outputFile = "${baseName}$DEFAULT_OUTPUT_EXTENSION"
            } else if (outputFile == null) {
                outputFile = "confluence_output.xml"
            }

            // Write output
            File(outputFile).writeText(confluenceXml)
            println("Output written to: $outputFile")
        }

        /**
         * Print help information
         */
        private fun printHelp() {
            println("""
                |ConfluenceWriter - Convert Markdown to Confluence XML
                |
                |Usage:
                |  java -jar ConfluenceWriter.jar [options] [input-file]
                |
                |Options:
                |  --mdfile, -f <file>      Input markdown file (default if no option specified)
                |  --mdstring, -s <string>  Input markdown as a string
                |  --output, -o <file>      Output file (default: input-file-name.xml)
                |  --help, -h               Show this help message
                |
                |Examples:
                |  java -jar ConfluenceWriter.jar README.md
                |  java -jar ConfluenceWriter.jar --mdfile README.md --output confluence.xml
                |  java -jar ConfluenceWriter.jar --mdstring "# Title" --output output.xml
            """.trimMargin())
        }
    }
}
