package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableRowBuilder2(private val writer: XMLStreamWriter, private val isHeader: Boolean) {
    fun cell(colSpan: Int = 1, rowSpan: Int = 1, align: String = "", init: TableCellBuilder2.() -> Unit) {
        val tag = if (isHeader) "th" else "td"
        writer.writeStartElement(tag)
        writer.writeAttribute("class", "confluenceTd")

        if (colSpan > 1) {
            writer.writeAttribute("colspan", colSpan.toString())
        }

        if (rowSpan > 1) {
            writer.writeAttribute("rowspan", rowSpan.toString())
        }

        if (align.isNotEmpty()) {
            writer.writeAttribute("style", "text-align: $align;")
        }

        val cell = TableCellBuilder2(writer)
        cell.init()

        writer.writeEndElement() // th or td
    }
}