package de.felixlf.confluencedsl

import de.felixlf.confluencedsl.converter.MarkdownToConfluenceConverter
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ConfluenceDSLTest {
    
    @Test
    fun `test DSL creates valid XHTML structure`() {
        val confluenceContent = ConfluenceDSL().apply {
            heading(1) {
                text("Test title")
            }
            
            paragraph {
                text("This is a test paragraph with ")
                bold("bold text")
                text(".")
            }
        }.build()
        
        // Verify that it contains the expected basic elements
        assertTrue(confluenceContent.contains("<h1>"), "Should contain an h1 heading")
        assertTrue(confluenceContent.contains("Test title"), "Should contain the title text")
        assertTrue(confluenceContent.contains("<p>"), "Should contain a paragraph")
        assertTrue(confluenceContent.contains("<strong>"), "Should contain bold formatting")
        assertTrue(confluenceContent.contains("bold text"), "Should contain the bold text")
    }
    
    @Test
    fun `test code block creation`() {
        val confluenceContent = ConfluenceDSL().apply {
            code("kotlin") {
                """
                    fun test() {
                        println("Hello, Confluence!")
                    }
                """.trimIndent()
            }
        }.build()
        
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"code\">"), 
                  "Should contain the code macro")
        assertTrue(confluenceContent.contains("<ac:parameter ac:name=\"language\">kotlin</ac:parameter>"), 
                  "Should specify the kotlin language")
        assertTrue(confluenceContent.contains("println(\"Hello, Confluence!\")"), 
                  "Should contain the code")
    }
    
    @Test
    fun `test list creation`() {
        val confluenceContent = ConfluenceDSL().apply {
            list {
                item { text("Item 1") }
                item { text("Item 2") }
                item { 
                    paragraph {
                        text("Item 3 with ")
                        bold("special formatting")
                    }
                }
            }
            
            list(ordered = true) {
                item { text("First numbered item") }
                item { text("Second numbered item") }
            }
        }.build()
        
        assertTrue(confluenceContent.contains("<ul>"), "Should contain an unordered list")
        assertTrue(confluenceContent.contains("<ol>"), "Should contain an ordered list")
        assertTrue(confluenceContent.contains("<li>Item 1</li>"), "Should contain the first item")
        assertTrue(confluenceContent.contains("<strong>special formatting</strong>"), 
                  "Should contain bold formatting within an item")
    }
    
    @Test
    fun `test table creation`() {
        val confluenceContent = ConfluenceDSL().apply {
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
        }.build()
        
        assertTrue(confluenceContent.contains("<table>"), "Should contain a table")
        assertTrue(confluenceContent.contains("<thead>"), "Should contain a table header")
        assertTrue(confluenceContent.contains("<tbody>"), "Should contain a table body")
        assertTrue(confluenceContent.contains("<th>Column 1</th>"), "Should contain the header of column 1")
        assertTrue(confluenceContent.contains("<strong>Cell 2,2</strong>"), 
                  "Should contain bold formatting within a cell")
    }
    
    @Test
    fun `test markdown conversion`() {
        val markdownText = """
            # Title from Markdown
            
            This is a paragraph with **bold text** and *italic text*.
            
            ## List of items
            
            * Item 1
            * Item 2
            * Item 3
            
            ```kotlin
            fun testCode() {
                println("Test")
            }
            ```
            
            [Link to Google](https://www.google.com)
        """.trimIndent()
        
        val confluenceContent = MarkdownToConfluenceConverter.convert(markdownText)
        
        assertTrue(confluenceContent.contains("<h1>Title from Markdown</h1>"), 
                  "Should contain the converted title")
        assertTrue(confluenceContent.contains("<strong>bold text</strong>"), 
                  "Should contain bold text")
        assertTrue(confluenceContent.contains("<em>italic text</em>"), 
                  "Should contain italic text")
        assertTrue(confluenceContent.contains("<ul>"), "Should contain a list")
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"code\">"), 
                  "Should contain a code block")
        assertTrue(confluenceContent.contains("<ac:link>"), "Should contain a link")
    }
    
    @Test
    fun `test advanced macros`() {
        val confluenceContent = ConfluenceDSL().apply {
            // Information panel
            infoPanel("Important information") {
                paragraph {
                    text("This is an information panel with title")
                }
            }
            
            // Warning panel
            warning {
                paragraph {
                    text("This is a warning without title")
                }
            }
            
            // Expandable panel
            expand("See more details") {
                paragraph {
                    text("Hidden content that can be expanded")
                }
                list {
                    item { text("Detail 1") }
                    item { text("Detail 2") }
                }
            }
            
            // Table of contents
            toc {
                maxLevel(3)
                minLevel(1)
                style("disc")
            }
            
            // Custom status
            status("Green", "Completed", true)
        }.build()
        
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"info\">"), 
                  "Should contain info panel macro")
        assertTrue(confluenceContent.contains("<ac:parameter ac:name=\"title\">Important information</ac:parameter>"), 
                  "Should contain the info panel title")
        
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"warning\">"), 
                  "Should contain warning macro")
        
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"expand\">"), 
                  "Should contain expand macro")
        assertTrue(confluenceContent.contains("<ac:parameter ac:name=\"title\">See more details</ac:parameter>"), 
                  "Should contain the expand panel title")
        
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"toc\">"), 
                  "Should contain table of contents macro")
        assertTrue(confluenceContent.contains("<ac:parameter ac:name=\"maxLevel\">3</ac:parameter>"), 
                  "Should contain the maximum level for the table of contents")
        
        assertTrue(confluenceContent.contains("<ac:structured-macro ac:name=\"status\">"), 
                  "Should contain status macro")
        assertTrue(confluenceContent.contains("<ac:parameter ac:name=\"colour\">Green</ac:parameter>"), 
                  "Should contain the status color")
        assertTrue(confluenceContent.contains("<ac:parameter ac:name=\"subtle\">true</ac:parameter>"), 
                  "Should contain the subtle parameter")
    }
    
    @Test
    fun `test links and mentions`() {
        val confluenceContent = ConfluenceDSL().apply {
            paragraph {
                text("This paragraph contains an ")
                link("https://example.com", "external link")
                text(" and a link to an ")
                pageLink("Home Page", "SPACE", "internal page")
                text(" as well as a ")
                attachmentLink("document.pdf", "Page with attachments", "SPACE", "reference to an attachment")
                text(". It also mentions ")
                userMention("admin")
                text(" and includes an emoji ")
                emoji("thumbsup")
                text(".")
            }
        }.build()
        
        assertTrue(confluenceContent.contains("<ac:link><ri:url ri:value=\"https://example.com\"/><ac:plain-text-link-body>"), 
                  "Should contain an external link")
        
        assertTrue(confluenceContent.contains("<ac:link><ri:page ri:content-title=\"Home Page\" ri:space-key=\"SPACE\"/>"), 
                  "Should contain a page link")
        
        assertTrue(confluenceContent.contains("<ac:link><ri:attachment ri:filename=\"document.pdf\" ri:content-title=\"Page with attachments\" ri:space-key=\"SPACE\"/>"), 
                  "Should contain a reference to an attachment")
        
        assertTrue(confluenceContent.contains("<ac:link><ri:user ri:username=\"admin\"/></ac:link>"), 
                  "Should contain a user mention")
        
        assertTrue(confluenceContent.contains("<ac:emoticon ac:name=\"thumbsup\"/>"), 
                  "Should contain an emoji")
    }
    
    @Test
    fun `test from markdown file`() {
        // Create a temporary markdown file
        val tempFile = createTempFile("test", ".md")
        try {
            tempFile.writeText("""
                # Test from file
                
                This is test content.
                
                * Item 1
                * Item 2
            """.trimIndent())
            
            val confluenceContent = ConfluenceDSL.fromMarkdownFile(tempFile.absolutePath)
            
            assertTrue(confluenceContent.contains("<h1>Test from file</h1>"), 
                      "Should contain the file title")
            assertTrue(confluenceContent.contains("<ul>"), 
                      "Should contain an unordered list")
            
        } finally {
            tempFile.delete()
        }
    }
}