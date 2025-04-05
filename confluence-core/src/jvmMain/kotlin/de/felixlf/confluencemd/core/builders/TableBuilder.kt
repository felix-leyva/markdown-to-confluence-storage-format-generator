package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableBuilder(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("table")
    }

    fun header(init: TableRowBuilder.() -> Unit) {
        writer.writeStartElement("thead")
        val row = TableRowBuilder(writer)
        row.init()
        row.close()
        writer.writeEndElement() // thead
    }

    fun body(init: TableBodyBuilder.() -> Unit) {
        writer.writeStartElement("tbody")
        val body = TableBodyBuilder(writer)
        body.init()
        writer.writeEndElement() // tbody
    }

    fun close() {
        writer.writeEndElement() // table
    }
}