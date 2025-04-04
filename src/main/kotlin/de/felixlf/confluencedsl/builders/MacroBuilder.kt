package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class MacroBuilder(private val writer: XMLStreamWriter, name: String) {
    init {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", name)
    }

    fun parameter(name: String, value: String) {
        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", name)
        writer.writeCharacters(value)
        writer.writeEndElement() // ac:parameter
    }

    fun body(content: String) {
        writer.writeStartElement("ac:plain-text-body")
        writer.writeCData(content)
        writer.writeEndElement() // ac:plain-text-body
    }

    fun richTextBody(init: RichTextBodyBuilder.() -> Unit) {
        writer.writeStartElement("ac:rich-text-body")
        val richText = RichTextBodyBuilder(writer)
        richText.init()
        writer.writeEndElement() // ac:rich-text-body
    }

    fun close() {
        writer.writeEndElement() // ac:structured-macro
    }
}