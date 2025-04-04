package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class ListBuilder(private val writer: XMLStreamWriter, ordered: Boolean) {
    init {
        if (ordered) {
            writer.writeStartElement("ol")
        } else {
            writer.writeStartElement("ul")
        }
    }

    fun item(init: ListItemBuilder.() -> Unit) {
        val item = ListItemBuilder(writer)
        item.init()
        item.close()
    }

    fun close() {
        writer.writeEndElement() // ul or ol
    }
}