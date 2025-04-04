package de.felixlf.confluencedsl.example

import de.felixlf.confluencedsl.ConfluenceDSL

fun main() {
    val dsl = ConfluenceDSL().apply {
        heading(1) {
            text("Testing Confluence DSL")
        }
        
        paragraph {
            text("This is a test paragraph with ")
            bold("bold formatting")
            text(" and ")
            italic("italic text")
            text(".")
        }
        
        list {
            item { text("First element") }
            item { text("Second element") }
            item { 
                paragraph {
                    text("Element with ")
                    bold("formatting")
                } 
            }
        }
        
        code("kotlin") {
            """
                fun test() {
                    println("Successful test")
                }
            """.trimIndent()
        }
    }
    
    val output = dsl.build()
    println(output)
}
