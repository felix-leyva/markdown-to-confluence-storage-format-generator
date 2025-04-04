package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class TableCellBuilder2(private val writer: XMLStreamWriter) {
    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun paragraph(init: ParagraphBuilder.() -> Unit) {
        val paragraph = ParagraphBuilder(writer)
        paragraph.init()
        paragraph.close()
    }

    fun list(ordered: Boolean = false, init: ListBuilder.() -> Unit) {
        val list = ListBuilder(writer, ordered)
        list.init()
        list.close()
    }

    fun code(language: String = "", content: () -> String) {
        val codeBlock = CodeBlockBuilder(writer, language)
        codeBlock.content(content())
        codeBlock.close()
    }
}