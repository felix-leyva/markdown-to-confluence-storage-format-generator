package de.felixlf.confluencemd.core

import de.felixlf.confluencedsl.builders.*
import de.felixlf.confluencemd.core.builders.CodeBlockBuilder
import de.felixlf.confluencemd.core.converter.MarkdownToConfluenceConverter
import de.felixlf.confluencemd.core.util.XmlUtils
import java.io.StringWriter
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

/**
 * DSL Builder for generating Confluence storage format (XHTML)
 */
class ConfluenceDSL(private val skipWrapper: Boolean = false) {
    private val stringWriter = StringWriter()
    private val xmlFactory = XMLOutputFactory.newInstance()
    private val writer: XMLStreamWriter = xmlFactory.createXMLStreamWriter(stringWriter)

    init {
        writer.writeStartDocument("UTF-8", "1.0")
        writer.writeStartElement("p")
        if (!skipWrapper) {
            // Add the Confluence macro as root element
            writer.writeStartElement("ac:structured-macro")
            writer.writeAttribute("ac:name", "html")
            writer.writeStartElement("ac:rich-text-body")
            writer.writeStartElement("div")
            writer.writeAttribute("class", "content-wrapper")
        }
    }

    // Methods for basic elements

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

    fun code(language: String = "", init: () -> String) {
        val codeBlock = CodeBlockBuilder(writer, language)
        codeBlock.content(init())
        codeBlock.close()
    }

    fun infoPanel(title: String = "", init: InfoPanelBuilder.() -> Unit) {
        val infoPanel = InfoPanelBuilder(writer, title)
        infoPanel.init()
        infoPanel.close()
    }

    fun note(init: NoteBuilder.() -> Unit) {
        val note = NoteBuilder(writer)
        note.init()
        note.close()
    }

    fun warning(init: WarningBuilder.() -> Unit) {
        val warning = WarningBuilder(writer)
        warning.init()
        warning.close()
    }

    fun tip(init: TipBuilder.() -> Unit) {
        val tip = TipBuilder(writer)
        tip.init()
        tip.close()
    }

    fun expand(title: String, init: ExpandBuilder.() -> Unit) {
        val expand = ExpandBuilder(writer, title)
        expand.init()
        expand.close()
    }

    fun macro(name: String, init: MacroBuilder.() -> Unit = {}) {
        val macro = MacroBuilder(writer, name)
        macro.init()
        macro.close()
    }

    fun image(url: String, altText: String = "", width: Int? = null, height: Int? = null) {
        writer.writeStartElement("ac:image")

        if (width != null) {
            writer.writeAttribute("ac:width", width.toString())
        }

        if (height != null) {
            writer.writeAttribute("ac:height", height.toString())
        }

        if (altText.isNotEmpty()) {
            writer.writeAttribute("ac:alt", altText)
        }

        writer.writeStartElement("ri:url")
        writer.writeAttribute("ri:value", url)
        writer.writeEndElement() // ri:url

        writer.writeEndElement() // ac:image
    }

    fun attachmentImage(
        filename: String, pageTitle: String? = null, spaceKey: String? = null,
        altText: String = "", width: Int? = null, height: Int? = null
    ) {
        writer.writeStartElement("ac:image")

        if (width != null) {
            writer.writeAttribute("ac:width", width.toString())
        }

        if (height != null) {
            writer.writeAttribute("ac:height", height.toString())
        }

        if (altText.isNotEmpty()) {
            writer.writeAttribute("ac:alt", altText)
        }

        writer.writeStartElement("ri:attachment")
        writer.writeAttribute("ri:filename", filename)

        if (pageTitle != null) {
            writer.writeAttribute("ri:content-title", pageTitle)
        }

        if (spaceKey != null) {
            writer.writeAttribute("ri:space-key", spaceKey)
        }

        writer.writeEndElement() // ri:attachment

        writer.writeEndElement() // ac:image
    }

    fun status(color: String, title: String, subtle: Boolean = false) {
        writer.writeStartElement("ac:structured-macro")
        writer.writeAttribute("ac:name", "status")

        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "colour")
        writer.writeCharacters(color) // "Red", "Yellow", "Green", "Blue", "Grey"
        writer.writeEndElement() // ac:parameter

        writer.writeStartElement("ac:parameter")
        writer.writeAttribute("ac:name", "title")
        writer.writeCharacters(title)
        writer.writeEndElement() // ac:parameter

        if (subtle) {
            writer.writeStartElement("ac:parameter")
            writer.writeAttribute("ac:name", "subtle")
            writer.writeCharacters("true")
            writer.writeEndElement() // ac:parameter
        }

        writer.writeEndElement() // ac:structured-macro
    }

    fun toc(init: TocBuilder.() -> Unit = {}) {
        val toc = TocBuilder(writer)
        toc.init()
        toc.close()
    }

    fun table2(init: TableBuilder2.() -> Unit) {
        val table = TableBuilder2(writer)
        table.init()
        table.close()
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

    fun horizontalRule() {
        writer.writeEmptyElement("hr")
    }

    fun close() {
        if (!skipWrapper) {
            writer.writeEndElement() // div
            writer.writeEndElement() // ac:rich-text-body
            writer.writeEndElement() // ac:structured-macro
        }
        writer.writeEndDocument()
        writer.flush()
    }

    fun build(): String {
        close()
        return stringWriter.toString()
    }

    companion object {
        // Method to convert Markdown to Confluence storage format
        fun fromMarkdown(markdownText: String): String {
            return MarkdownToConfluenceConverter.Companion.convert(markdownText)
        }

        fun fromMarkdownFile(filePath: String): String {
            return MarkdownToConfluenceConverter.Companion.convertFile(filePath)
        }

        // Method to create a DSL instance directly from Markdown
        fun buildFromMarkdown(markdownText: String): ConfluenceDSL {
            val confluenceDSL = ConfluenceDSL()
            val markdownContent = fromMarkdown(markdownText)

            // Insert the already converted content inside the wrapper
            confluenceDSL.writer.writeStartElement("ac:rich-text-body")

            // We use the utility to transfer the XML content correctly
            XmlUtils.transferXmlContent(
                markdownContent,
                confluenceDSL.writer,
                true
            )

            confluenceDSL.writer.writeEndElement() // ac:rich-text-body

            return confluenceDSL
        }
    }
}