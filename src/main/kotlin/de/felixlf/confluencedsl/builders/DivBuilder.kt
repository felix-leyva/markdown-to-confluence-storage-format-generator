package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class DivBuilder(private val writer: XMLStreamWriter, cssClass: String) {
    init {
        writer.writeStartElement("div")
        if (cssClass.isNotEmpty()) {
            writer.writeAttribute("class", cssClass)
        }
    }

    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun paragraph(init: ParagraphBuilder.() -> Unit) {
        val paragraph = ParagraphBuilder(writer)
        paragraph.init()
        paragraph.close()
    }

    fun heading(level: Int, init: HeadingBuilder.() -> Unit) {
        val heading = HeadingBuilder(writer, level)
        heading.init()
        heading.close()
    }

    fun div(cssClass: String = "", init: DivBuilder.() -> Unit) {
        val div = DivBuilder(writer, cssClass)
        div.init()
        div.close()
    }

    fun span(cssClass: String = "", init: SpanBuilder.() -> Unit) {
        val span = SpanBuilder(writer, cssClass)
        span.init()
        span.close()
    }

    fun list(ordered: Boolean = false, init: ListBuilder.() -> Unit) {
        val list = ListBuilder(writer, ordered)
        list.init()
        list.close()
    }

    fun table(init: TableBuilder.() -> Unit) {
        val table = TableBuilder(writer)
        table.init()
        table.close()
    }

    fun close() {
        writer.writeEndElement() // div
    }
}