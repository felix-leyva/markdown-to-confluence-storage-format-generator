package de.felixlf.confluencemd.core.examples

import de.felixlf.confluencemd.core.ConfluenceDSL
import java.io.File

fun main() {
    // Example of a complete technical documentation page
    val technicalDoc = ConfluenceDSL(true).apply {
        // Table of contents
        toc {
            maxLevel(3)
            minLevel(1)
            style("disc")
        }

        heading(1) {
            text("API Technical Documentation")
        }

        paragraph {
            text("This documentation describes the usage of our REST API for system integration.")
        }

        status("Green", "Stable", true)

        heading(2) {
            text("Prerequisites")
        }

        list {
            item { text("System access") }
            item { text("OAuth2 credentials") }
            item { text("HTTP client compatible with JSON") }
        }

        note {
            paragraph {
                text("It's important to securely store your credentials.")
            }
        }

        heading(2) {
            text("Authentication")
        }

        paragraph {
            text("Authentication is done using the OAuth2 flow. Here's an example of a token request:")
        }

        code("bash") {
            """
                curl -X POST https://api.example.com/oauth/token \
                  -d "grant_type=client_credentials" \
                  -d "client_id=YOUR_CLIENT_ID" \
                  -d "client_secret=YOUR_CLIENT_SECRET"
            """.trimIndent()
        }

        heading(2) {
            text("Available Endpoints")
        }

        heading(3) {
            text("Get Users")
        }

        table2 {
            header {
                cell { text("Method") }
                cell { text("URL") }
                cell { text("Description") }
            }
            row {
                cell { text("GET") }
                cell { code { "https://api.example.com/v1/users" } }
                cell { text("Get list of users") }
            }
        }

        expand("Query Parameters") {
            paragraph {
                text("The API accepts the following parameters:")
            }

            table {
                header {
                    header { text("Parameter") }
                    header { text("Type") }
                    header { text("Required") }
                    header { text("Description") }
                }
                body {
                    row {
                        cell { text("limit") }
                        cell { text("integer") }
                        cell { text("No") }
                        cell { text("Maximum number of results (default: 20)") }
                    }
                    row {
                        cell { text("offset") }
                        cell { text("integer") }
                        cell { text("No") }
                        cell { text("Offset for pagination (default: 0)") }
                    }
                }
            }
        }

        paragraph {
            text("Example response:")
        }

        code("json") {
            """
                {
                  "users": [
                    {
                      "id": 1,
                      "name": "John Doe",
                      "email": "john@example.com"
                    },
                    {
                      "id": 2,
                      "name": "Jane Smith",
                      "email": "jane@example.com"
                    }
                  ],
                  "total": 2,
                  "limit": 20,
                  "offset": 0
                }
            """.trimIndent()
        }

        heading(3) {
            text("Create User")
        }

        table2 {
            header {
                cell { text("Method") }
                cell { text("URL") }
                cell { text("Description") }
            }
            row {
                cell { text("POST") }
                cell { code { "https://api.example.com/v1/users" } }
                cell { text("Create a new user") }
            }
        }

        paragraph {
            text("Request example:")
        }

        code("json") {
            """
                {
                  "name": "New User",
                  "email": "newuser@example.com",
                  "role": "user"
                }
            """.trimIndent()
        }

        warning {
            paragraph {
                text("Creating users requires administrator permissions.")
            }
        }

        heading(2) {
            text("Status Codes")
        }

        table2 {
            header {
                cell { text("Code") }
                cell { text("Description") }
            }
            row {
                cell { text("200 OK") }
                cell { text("The request has been processed successfully") }
            }
            row {
                cell { text("400 Bad Request") }
                cell { text("Incorrect or missing parameters") }
            }
            row {
                cell { text("401 Unauthorized") }
                cell { text("Authentication required or invalid") }
            }
            row {
                cell { text("403 Forbidden") }
                cell { text("The user doesn't have permission for the operation") }
            }
            row {
                cell { text("404 Not Found") }
                cell { text("The requested resource doesn't exist") }
            }
            row {
                cell { text("500 Internal Server Error") }
                cell { text("Internal server error") }
            }
        }

        heading(2) {
            text("Integration Examples")
        }

        macro("jira") {
            parameter("server", "JIRA")
            parameter("key", "API-123")
        }

        paragraph {
            text("For more details, contact ")
            userMention("admin")
            text(" or visit the ")
            pageLink("Integration Guide", "DOCS")
            text(".")
        }

        infoPanel("Updates") {
            paragraph {
                text("This documentation is regularly updated. Last update: ")
                text("April 2025")
                emoji("thumbsup")
            }
        }
    }.build()

    // Save the generated content
    File("technical_documentation.xml").writeText(technicalDoc)
    println("Technical documentation generated in 'technical_documentation.xml'")

    // Example of converting Markdown to Confluence
    val markdownExample = """
        # Quick Guide for New Developers
        
        This guide provides the initial steps to set up your development environment.
        
        ## System Requirements
        
        * Java 17 or higher
        * Maven 3.6+
        * Docker Desktop
        
        ## Environment Setup
        
        1. Clone the repository:
           ```bash
           git clone https://github.com/company/project.git
           cd project
           ```
           
        2. Build the project:
           ```bash
           mvn clean install
           ```
           
        3. Run the application:
           ```bash
           java -jar target/application.jar
           ```
        
        For more information, visit [our wiki](https://wiki.company.com).
        
        > Note: Make sure to set up the environment variables correctly.
    """.trimIndent()

    val confluenceFromMarkdown = ConfluenceDSL.fromMarkdown(markdownExample)
    File("developer_guide.xml").writeText(confluenceFromMarkdown)
    println("Developer guide converted from Markdown in 'developer_guide.xml'")

    // Example of a reusable template for release notes
    val releaseNotesDSL = { version: String, date: String, features: List<String>, fixes: List<String> ->
        ConfluenceDSL().apply {
            heading(1) {
                text("Release Notes $version")
            }

            status("Blue", "New Release", false)

            paragraph {
                text("Release Date: $date")
            }

            heading(2) {
                text("New Features")
            }

            list {
                features.forEach { feature ->
                    item { text(feature) }
                }
            }

            heading(2) {
                text("Bug Fixes")
            }

            table2 {
                header {
                    cell { text("Issue") }
                    cell { text("Solution") }
                }

                fixes.forEach { fix ->
                    row {
                        cell { text(fix.split(" - ")[0]) }
                        cell { text(fix.split(" - ")[1]) }
                    }
                }
            }

            note {
                paragraph {
                    text("Update to this version as soon as possible to benefit from the improvements.")
                }
            }
        }.build()
    }

    val releaseNotes = releaseNotesDSL(
        "2.3.0",
        "2025-04-10",
        listOf(
            "New admin dashboard",
            "Google Calendar integration",
            "Performance improvements in searches"
        ),
        listOf(
            "Bug #1234 - Fixed issue with large file uploads",
            "Bug #2345 - Fixed error in OAuth authentication",
            "Bug #3456 - Fixed cache issue in reports"
        )
    )

    File("release_notes.xml").writeText(releaseNotes)
    println("Release notes generated in 'release_notes.xml'")
}