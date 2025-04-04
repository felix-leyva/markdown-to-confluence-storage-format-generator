package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class NoteBuilder(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", "note")
        writer.writeStartElement("ac:rich-text-body")
    }

    fun paragraph(init: ParagraphBuilder.() -> Unit) {
        val paragraph = ParagraphBuilder(writer)
        paragraph.init()
        paragraph.close()
    }

    fun close() {
        writer.writeEndElement() // ac:rich-text-body
        writer.writeEndElement() // ac:structured-macro
    }
}