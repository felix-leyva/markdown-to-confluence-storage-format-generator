package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class HeadingBuilder(private val writer: XMLStreamWriter, level: Int) {
    init {
        writer.writeStartElement("h$level")
    }

    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun close() {
        writer.writeEndElement() // h{level}
    }
}