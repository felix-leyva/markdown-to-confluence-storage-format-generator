package de.felixlf.confluencedsl.builders

import de.felixlf.confluencemd.core.builders.CodeBlockBuilder
import javax.xml.stream.XMLStreamWriter

class ExpandBuilder(private val writer: XMLStreamWriter, title: String) {
    init {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", "expand")

        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "title")
        writer.writeCharacters(title)
        writer.writeEndElement() // ac:parameter

        writer.writeStartElement("ac:rich-text-body")
    }

    fun paragraph(init: ParagraphBuilder.() -> Unit) {
        val paragraph = ParagraphBuilder(writer)
        paragraph.init()
        paragraph.close()
    }

    fun heading(level: Int, init: HeadingBuilder.() -> Unit) {
        val heading = HeadingBuilder(writer, level)
        heading.init()
        heading.close()
    }

    fun list(ordered: Boolean = false, init: ListBuilder.() -> Unit) {
        val list = ListBuilder(writer, ordered)
        list.init()
        list.close()
    }

    fun code(language: String = "", content: () -> String) {
        val codeBlock = CodeBlockBuilder(writer, language)
        codeBlock.content(content())
        codeBlock.close()
    }

    fun close() {
        writer.writeEndElement() // ac:rich-text-body
        writer.writeEndElement() // ac:structured-macro
    }
}