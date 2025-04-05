package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class InfoPanelBuilder(private val writer: XMLStreamWriter, title: String) {
    init {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", "info")

        if (title.isNotEmpty()) {
            writer.writeStartElement("ac:parameter")
            writer.writeAttribute("ac:name", "title")
            writer.writeCharacters(title)
            writer.writeEndElement() // ac:parameter
        }

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