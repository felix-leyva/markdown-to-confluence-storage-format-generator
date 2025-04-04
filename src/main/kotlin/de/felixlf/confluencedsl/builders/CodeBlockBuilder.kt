package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class CodeBlockBuilder(private val writer: XMLStreamWriter, language: String) {
    init {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", "code")

        if (language.isNotEmpty()) {
            writer.writeStartElement("ac:parameter")
            writer.writeAttribute("ac:name", "language")
            writer.writeCharacters(language)
            writer.writeEndElement() // ac:parameter
        }

        writer.writeStartElement("ac:plain-text-body")
        writer.writeCData("")  // Placeholder for content
    }

    fun content(code: String) {
        // Reemplazar el CDATA vacío
        writer.writeCharacters(code)
    }

    fun close() {
        writer.writeEndElement() // ac:plain-text-body
        writer.writeEndElement() // ac:structured-macro
    }
}