package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableBodyBuilder(private val writer: XMLStreamWriter) {
    fun row(init: TableRowBuilder.() -> Unit) {
        val row = TableRowBuilder(writer)
        row.init()
        row.close()
    }
}