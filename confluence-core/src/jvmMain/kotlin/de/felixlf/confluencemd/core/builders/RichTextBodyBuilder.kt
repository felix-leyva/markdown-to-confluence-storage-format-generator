package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class RichTextBodyBuilder(private val writer: XMLStreamWriter) {
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
}