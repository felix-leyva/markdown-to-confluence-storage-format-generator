package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class SpanBuilder(private val writer: XMLStreamWriter, cssClass: String) {
    init {
        writer.writeStartElement("span")
        if (cssClass.isNotEmpty()) {
            writer.writeAttribute("class", cssClass)
        }
    }

    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun bold(content: String) {
        writer.writeStartElement("strong")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun italic(content: String) {
        writer.writeStartElement("em")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun underline(content: String) {
        writer.writeStartElement("u")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun strikethrough(content: String) {
        writer.writeStartElement("s")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun code(content: String) {
        writer.writeStartElement("code")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun style(styleAttribute: String) {
        writer.writeAttribute("style", styleAttribute)
    }

    fun close() {
        writer.writeEndElement() // span
    }
}