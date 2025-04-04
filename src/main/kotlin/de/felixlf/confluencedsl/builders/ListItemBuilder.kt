package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class ListItemBuilder(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("li")
    }

    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun paragraph(init: ParagraphBuilder.() -> Unit) {
        val paragraph = ParagraphBuilder(writer)
        paragraph.init()
        paragraph.close()
    }

    fun close() {
        writer.writeEndElement() // li
    }
}