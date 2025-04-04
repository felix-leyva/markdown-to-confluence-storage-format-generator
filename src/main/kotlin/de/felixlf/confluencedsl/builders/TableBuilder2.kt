package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableBuilder2(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("table")
        writer.writeAttribute("class", "confluenceTable")
    }

    fun header(init: TableRowBuilder2.() -> Unit) {
        writer.writeStartElement("tr")
        val row = TableRowBuilder2(writer, true)
        row.init()
        writer.writeEndElement() // tr
    }

    fun row(init: TableRowBuilder2.() -> Unit) {
        writer.writeStartElement("tr")
        val row = TableRowBuilder2(writer, false)
        row.init()
        writer.writeEndElement() // tr
    }

    fun close() {
        writer.writeEndElement() // table
    }
}