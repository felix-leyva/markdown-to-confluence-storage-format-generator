package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableCellBuilder(private val writer: XMLStreamWriter) {
    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun paragraph(init: ParagraphBuilder.() -> Unit) {
        val paragraph = ParagraphBuilder(writer)
        paragraph.init()
        paragraph.close()
    }
}