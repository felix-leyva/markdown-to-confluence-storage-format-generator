package de.felixlf.confluencemd.core.example

import de.felixlf.confluencemd.core.ConfluenceDSL
import java.io.File

fun main() {
    // Example 1: Creating content using the DSL
    val confluenceContent = ConfluenceDSL().apply {
        heading(1) {
            text("Main Title")
        }

        paragraph {
            text("This is a paragraph with ")
            bold("bold text")
            text(" and ")
            italic("italic text")
            text(".")
        }

        list {
            item {
                text("List item 1")
            }
            item {
                text("List item 2")
            }
            item {
                paragraph {
                    text("Item 3 with ")
                    bold("formatting")
                }
            }
        }

        heading(2) {
            text("Table Example")
        }

        table {
            header {
                header { text("Column 1") }
                header { text("Column 2") }
            }
            body {
                row {
                    cell { text("Cell 1,1") }
                    cell { text("Cell 1,2") }
                }
                row {
                    cell { text("Cell 2,1") }
                    cell { paragraph { bold("Cell 2,2") } }
                }
            }
        }

        heading(2) {
            text("Code Example")
        }

        code("kotlin") {
            """
                fun helloWorld() {
                    println("Hello, Confluence!")
                }
            """.trimIndent()
        }

        note {
            paragraph {
                text("This is an important note.")
            }
        }

        infoPanel("Information") {
            paragraph {
                text("This is an information panel.")
            }
        }

        macro("jira") {
            parameter("server", "JIRA")
            parameter("serverId", "12345")
            parameter("key", "PROJECT-123")
        }
    }.build()

    println(confluenceContent)
    File("confluence_output.xml").writeText(confluenceContent)

    // Example 2: Converting Markdown to Confluence storage format
    val markdownText = """
        # Title from Markdown
        
        This is a paragraph with **bold text** and *italic text*.
        
        ## Subsection
        
        * Item 1
        * Item 2
        * Item 3
        
        ```kotlin
        fun fromMarkdown() {
            println("This is code from Markdown")
        }
        ```
        
        [Link to Google](https://www.google.com)
    """.trimIndent()

    val confluenceFromMarkdown = ConfluenceDSL.fromMarkdown(markdownText)
    println("\n\nConversion from Markdown:")
    println(confluenceFromMarkdown)
    File("confluence_from_markdown.xml").writeText(confluenceFromMarkdown)

    // Example 3: Converting a Markdown file to Confluence storage format
    // Assuming we have an "example.md" file in the root directory
    try {
        val confluenceFromFile = ConfluenceDSL.fromMarkdownFile("example.md")
        println("\n\nConversion from Markdown file:")
        println(confluenceFromFile)
        File("confluence_from_file.xml").writeText(confluenceFromFile)
    } catch (e: Exception) {
        println("Could not read Markdown file: ${e.message}")
    }
}