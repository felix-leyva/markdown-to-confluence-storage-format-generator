package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TocBuilder(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", "toc")
    }

    fun printable(value: Boolean = true) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "printable")
        writer.writeCharacters(value.toString())
        writer.writeEndElement() // ac:parameter
    }

    fun style(value: String) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "style")
        writer.writeCharacters(value) // "disc", "circle", "square", "none", "decimal", etc.
        writer.writeEndElement() // ac:parameter
    }

    fun maxLevel(value: Int) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "maxLevel")
        writer.writeCharacters(value.toString())
        writer.writeEndElement() // ac:parameter
    }

    fun minLevel(value: Int) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "minLevel")
        writer.writeCharacters(value.toString())
        writer.writeEndElement() // ac:parameter
    }

    fun include(value: String) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "include")
        writer.writeCharacters(value)
        writer.writeEndElement() // ac:parameter
    }

    fun exclude(value: String) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "exclude")
        writer.writeCharacters(value)
        writer.writeEndElement() // ac:parameter
    }

    fun close() {
        writer.writeEndElement() // ac:structured-macro
    }
}