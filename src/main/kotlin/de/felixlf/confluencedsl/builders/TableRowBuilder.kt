package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableRowBuilder(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("tr")
    }

    fun header(init: TableCellBuilder.() -> Unit) {
        writer.writeStartElement("th")
        val cell = TableCellBuilder(writer)
        cell.init()
        writer.writeEndElement() // th
    }

    fun cell(init: TableCellBuilder.() -> Unit) {
        writer.writeStartElement("td")
        val cell = TableCellBuilder(writer)
        cell.init()
        writer.writeEndElement() // td
    }

    fun close() {
        writer.writeEndElement() // tr
    }
}