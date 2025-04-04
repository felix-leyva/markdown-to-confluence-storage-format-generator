# Confluence Storage Format DSL

A DSL (Domain Specific Language) in Kotlin for generating content in Confluence storage format (XHTML).

## Features

- Builder-style generation for creating documents in Confluence storage format
- Support for all basic elements: paragraphs, headings, lists, tables, etc.
- Support for Confluence macros: code, note, warning, info, status, toc, etc.
- Conversion from Markdown to Confluence storage format
- Command-line tool (CLI) for converting Markdown files to Confluence XML

## Command-Line Tool Usage

You can convert Markdown files to Confluence storage format using the executable JAR:

```bash
# Show help
java -jar ConfluenceWriter-1.0-SNAPSHOT.jar --help

# Convert a Markdown file (output: README.xml)
java -jar ConfluenceWriter-1.0-SNAPSHOT.jar README.md

# Specify output file
java -jar ConfluenceWriter-1.0-SNAPSHOT.jar --mdfile README.md --output confluence.xml

# Convert Markdown text directly
java -jar ConfluenceWriter-1.0-SNAPSHOT.jar --mdstring "# Title" --output title.xml
```

### Building the JAR

To build the executable JAR:

```bash
# On Linux/macOS
./build_jar.sh

# On Windows
build_jar.bat
```

## Basic Usage

```kotlin
// Basic example
val content = ConfluenceDSL().apply {
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
        item { text("Item 1") }
        item { text("Item 2") }
    }
    
    code("kotlin") {
        content("""
            fun helloWorld() {
                println("Hello, Confluence!")
            }
        """.trimIndent())
    }
}.build()
```

## Conversion from Markdown

```kotlin
// Convert Markdown text
val markdownText = """
    # Title from Markdown
    
    This is a paragraph with **bold text** and *italic text*.
    
    * Item 1
    * Item 2
    
    ```kotlin
    fun fromMarkdown() {
        println("Code from Markdown")
    }
    ```
""".trimIndent()

val confluenceContent = ConfluenceDSL.fromMarkdown(markdownText)

// You can also convert from a file
val confluenceFromFile = ConfluenceDSL.fromMarkdownFile("document.md")
```

## Advanced Macros

```kotlin
// Advanced macros example
val advancedContent = ConfluenceDSL().apply {
    // Table of contents
    toc {
        maxLevel(3)
        minLevel(1)
    }
    
    // Info panel
    infoPanel("Important Information") {
        paragraph {
            text("This is an information panel with relevant data.")
        }
    }
    
    // Warning
    warning {
        paragraph {
            text("Be careful with this operation.")
        }
    }
    
    // Expandable panel
    expand("See details") {
        paragraph {
            text("Content that is initially hidden.")
        }
        list {
            item { text("Detail 1") }
            item { text("Detail 2") }
        }
    }
    
    // Status
    status("Green", "Completed", true)
    
    // Table with special formatting
    table2 {
        header {
            cell { text("Column 1") }
            cell { text("Column 2") }
        }
        row {
            cell(colSpan = 2, align = "center") { 
                text("Cell spanning two columns") 
            }
        }
    }
}.build()
```

## Confluence API Integration

```kotlin
// Publish content to Confluence
val publisher = ConfluencePublisher(
    baseUrl = "https://your-instance.atlassian.net/wiki",
    username = "your-email@example.com",
    apiToken = "your-api-token"
)

// Create a new page
val successCreate = publisher.publish(
    pageId = "new",
    content = confluenceContent,
    title = "My Confluence Page",
    spaceKey = "MYSPACE"
)

// Update an existing page
val successUpdate = publisher.publish(
    pageId = "123456",
    content = confluenceContent,
    title = "Updated Title"
)
```

## API Documentation Generation

```kotlin
// Document an API using annotations
@DocumentedApi(
    title = "User API",
    description = "This API allows managing users in the system."
)
object UserApi {
    @ApiEndpoint(
        method = "GET",
        path = "/api/v1/users",
        description = "List users"
    )
    fun listUsers(
        @ApiParam(name = "limit", type = "integer", description = "Maximum number of results")
        limit: Int = 20
    ): List<User> {
        // Actual implementation
        return emptyList()
    }
}

// Generate documentation automatically
val apiDocGenerator = ApiDocGenerator()
val userApiDoc = apiDocGenerator.generateApiDoc(UserApi::class)
```

## Requirements

- Kotlin 1.8.0 or higher
- JDK 11 or higher

## Dependencies

- javax.xml.stream:stax-api:1.0-2
- com.fasterxml.woodstox:woodstox-core:6.4.0
- org.commonmark:commonmark:0.21.0 (and extensions)
