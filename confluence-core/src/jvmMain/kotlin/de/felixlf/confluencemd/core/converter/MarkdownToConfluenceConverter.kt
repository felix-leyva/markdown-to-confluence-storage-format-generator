package de.felixlf.confluencemd.core.converter

import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.gfm.strikethrough.Strikethrough
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.*
import org.commonmark.ext.task.list.items.TaskListItemMarker
import org.commonmark.ext.task.list.items.TaskListItemsExtension
import org.commonmark.node.*
import org.commonmark.parser.Parser
import org.commonmark.renderer.NodeRenderer
import org.commonmark.renderer.html.HtmlNodeRendererContext
import org.commonmark.renderer.html.HtmlRenderer
import org.commonmark.renderer.html.HtmlWriter
import java.io.File

/**
 * Enhanced Markdown to Confluence storage format converter
 */
class MarkdownToConfluenceConverter {
    companion object {
        /**
         * Converts a text in Markdown format to Confluence storage format
         *
         * @param markdownText Text in Markdown format
         * @return String in Confluence storage format
         */
        fun convert(markdownText: String): String {
            // Configure Markdown extensions
            val extensions = listOf(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                AutolinkExtension.create(),
                TaskListItemsExtension.create()
            )

            val parser = Parser.builder()
                .extensions(extensions)
                .build()

            val document = parser.parse(markdownText)

            val htmlRenderer = HtmlRenderer.builder()
                .extensions(extensions)
                .nodeRendererFactory { context -> ConfluenceNodeRenderer(context) }
                .build()

            return htmlRenderer.render(document)
        }

        /**
         * Converts a Markdown file to Confluence storage format
         *
         * @param filePath Path to the Markdown file
         * @return String in Confluence storage format
         */
        fun convertFile(filePath: String): String {
            val markdownText = File(filePath).readText()
            return convert(markdownText)
        }
    }

    /**
     * Custom renderer to convert Markdown nodes directly to Confluence format
     */
    private class ConfluenceNodeRenderer(private val context: HtmlNodeRendererContext) : NodeRenderer {
        private val html: HtmlWriter = context.writer

        override fun getNodeTypes(): Set<Class<out Node>> {
            return setOf(
                Document::class.java,
                Heading::class.java,
                Paragraph::class.java,
                Text::class.java,
                Emphasis::class.java,
                StrongEmphasis::class.java,
                Link::class.java,
                BulletList::class.java,
                OrderedList::class.java,
                ListItem::class.java,
                FencedCodeBlock::class.java,
                IndentedCodeBlock::class.java,
                Code::class.java,
                ThematicBreak::class.java,
                Image::class.java,
                BlockQuote::class.java,
                HtmlBlock::class.java,
                HtmlInline::class.java,
                TableBlock::class.java,
                TableHead::class.java,
                TableBody::class.java,
                TableRow::class.java,
                TableCell::class.java,
                Strikethrough::class.java,
                TaskListItemMarker::class.java
            )
        }

        override fun render(node: Node) {
            when (node) {
                is Document -> renderDocument(node)
                is Heading -> renderHeading(node)
                is Paragraph -> renderParagraph(node)
                is Text -> renderText(node)
                is Emphasis -> renderEmphasis(node)
                is StrongEmphasis -> renderStrongEmphasis(node)
                is Link -> renderLink(node)
                is BulletList -> renderBulletList(node)
                is OrderedList -> renderOrderedList(node)
                is ListItem -> renderListItem(node)
                is FencedCodeBlock -> renderFencedCodeBlock(node)
                is IndentedCodeBlock -> renderIndentedCodeBlock(node)
                is Code -> renderCode(node)
                is ThematicBreak -> renderThematicBreak()
                is Image -> renderImage(node)
                is BlockQuote -> renderBlockQuote(node)
                is HtmlBlock -> renderHtmlBlock(node)
                is HtmlInline -> renderHtmlInline(node)
                is TableBlock -> renderTableBlock(node)
                is TableHead -> renderTableHead(node)
                is TableBody -> renderTableBody(node)
                is TableRow -> renderTableRow(node)
                is TableCell -> renderTableCell(node)
                is Strikethrough -> renderStrikethrough(node)
                is TaskListItemMarker -> renderTaskListItem(node)
                else -> {
                    // Render children for any other node type
                    visitChildren(node)
                }
            }
        }

        private fun renderDocument(document: Document) {
            visitChildren(document)
        }

        private fun renderHeading(heading: Heading) {
            val level = heading.level
            html.line()
            html.tag("h$level")
            visitChildren(heading)
            html.tag("/h$level")
            html.line()
        }

        private fun renderParagraph(paragraph: Paragraph) {
            html.line()
            html.tag("p")
            visitChildren(paragraph)
            html.tag("/p")
            html.line()
        }

        private fun renderText(text: Text) {
            html.text(text.literal)
        }

        private fun renderEmphasis(emphasis: Emphasis) {
            html.tag("em")
            visitChildren(emphasis)
            html.tag("/em")
        }

        private fun renderStrongEmphasis(strongEmphasis: StrongEmphasis) {
            html.tag("strong")
            visitChildren(strongEmphasis)
            html.tag("/strong")
        }

        private fun renderLink(link: Link) {
            html.line()
            html.raw("<ac:link><ri:url ri:value=\"${link.destination}\"/>")
            html.raw("<ac:plain-text-link-body><![CDATA[")
            visitChildren(link)
            html.raw("]]></ac:plain-text-link-body></ac:link>")
        }

        private fun renderBulletList(bulletList: BulletList) {
            html.line()
            html.tag("ul")
            html.line()
            visitChildren(bulletList)
            html.line()
            html.tag("/ul")
            html.line()
        }

        private fun renderOrderedList(orderedList: OrderedList) {
            html.line()
            html.tag("ol")
            html.line()
            visitChildren(orderedList)
            html.line()
            html.tag("/ol")
            html.line()
        }

        private fun renderListItem(listItem: ListItem) {
            html.tag("li")
            visitChildren(listItem)
            html.tag("/li")
            html.line()
        }

        private fun renderFencedCodeBlock(fencedCodeBlock: FencedCodeBlock) {
            val language = fencedCodeBlock.info ?: ""
            html.line()
            html.raw("<ac:structured-macro ac:name=\"code\">")

            if (language.isNotEmpty()) {
                html.raw("<ac:parameter ac:name=\"language\">$language</ac:parameter>")
            }

            html.raw("<ac:plain-text-body><![CDATA[${fencedCodeBlock.literal}]]></ac:plain-text-body>")
            html.raw("</ac:structured-macro>")
            html.line()
        }

        private fun renderIndentedCodeBlock(indentedCodeBlock: IndentedCodeBlock) {
            html.line()
            html.raw("<ac:structured-macro ac:name=\"code\">")
            html.raw("<ac:plain-text-body><![CDATA[${indentedCodeBlock.literal}]]></ac:plain-text-body>")
            html.raw("</ac:structured-macro>")
            html.line()
        }

        private fun renderCode(code: Code) {
            html.raw("<code>${code.literal}</code>")
        }

        private fun renderThematicBreak() {
            html.line()
            html.tag("hr")
            html.line()
        }

        private fun renderImage(image: Image) {
            html.line()
            html.raw("<ac:image>")
            html.raw("<ri:url ri:value=\"${image.destination}\"/>")
            if (image.title != null && image.title.isNotEmpty()) {
                html.raw("<ac:parameter ac:name=\"alt\">${image.title}</ac:parameter>")
            }
            html.raw("</ac:image>")
            html.line()
        }

        private fun renderBlockQuote(blockQuote: BlockQuote) {
            html.line()
            html.raw("<ac:structured-macro ac:name=\"info\">")
            html.raw("<ac:rich-text-body>")
            visitChildren(blockQuote)
            html.raw("</ac:rich-text-body>")
            html.raw("</ac:structured-macro>")
            html.line()
        }

        private fun renderHtmlBlock(htmlBlock: HtmlBlock) {
            // Try to convert common HTML to Confluence format
            val html = htmlBlock.literal

            when {
                // Notes (divs with class=note)
                html.contains("<div class=\"note\">") -> {
                    this.html.raw("<ac:structured-macro ac:name=\"note\">")
                    this.html.raw("<ac:rich-text-body>")
                    // Extract content between divs
                    val content = html.substringAfter("<div class=\"note\">").substringBefore("</div>")
                    this.html.raw(content)
                    this.html.raw("</ac:rich-text-body>")
                    this.html.raw("</ac:structured-macro>")
                }
                // Warnings (divs with class=warning)
                html.contains("<div class=\"warning\">") -> {
                    this.html.raw("<ac:structured-macro ac:name=\"warning\">")
                    this.html.raw("<ac:rich-text-body>")
                    // Extract content between divs
                    val content = html.substringAfter("<div class=\"warning\">").substringBefore("</div>")
                    this.html.raw(content)
                    this.html.raw("</ac:rich-text-body>")
                    this.html.raw("</ac:structured-macro>")
                }
                // Tips (divs with class=tip)
                html.contains("<div class=\"tip\">") -> {
                    this.html.raw("<ac:structured-macro ac:name=\"tip\">")
                    this.html.raw("<ac:rich-text-body>")
                    // Extract content between divs
                    val content = html.substringAfter("<div class=\"tip\">").substringBefore("</div>")
                    this.html.raw(content)
                    this.html.raw("</ac:rich-text-body>")
                    this.html.raw("</ac:structured-macro>")
                }
                // Other HTML blocks
                else -> {
                    // Pass the HTML as is, though it may not be compatible with Confluence
                    this.html.raw(html)
                }
            }
        }

        private fun renderHtmlInline(htmlInline: HtmlInline) {
            // Try to handle common inline HTML
            val html = htmlInline.literal

            when {
                // Handle basic elements
                html.startsWith("<br>") || html.startsWith("<br/>") || html.startsWith("<br />") -> {
                    this.html.raw("<br/>")
                }

                html.startsWith("<span") -> {
                    // Pass span as is
                    this.html.raw(html)
                }

                else -> {
                    // Other inline HTML elements
                    this.html.raw(html)
                }
            }
        }

        private fun renderTableBlock(tableBlock: TableBlock) {
            html.line()
            html.raw("<table class=\"confluenceTable\">")
            visitChildren(tableBlock)
            html.raw("</table>")
            html.line()
        }

        private fun renderTableHead(tableHead: TableHead) {
            html.raw("<thead>")
            visitChildren(tableHead)
            html.raw("</thead>")
        }

        private fun renderTableBody(tableBody: TableBody) {
            html.raw("<tbody>")
            visitChildren(tableBody)
            html.raw("</tbody>")
        }

        private fun renderTableRow(tableRow: TableRow) {
            html.raw("<tr>")
            visitChildren(tableRow)
            html.raw("</tr>")
        }

        private fun renderTableCell(tableCell: TableCell) {
            val tag = if (tableCell.isHeader) "th" else "td"
            val cssClass = if (tableCell.isHeader) "confluenceTh" else "confluenceTd"

            html.raw("<$tag class=\"$cssClass\">")
            visitChildren(tableCell)
            html.raw("</$tag>")
        }

        private fun renderStrikethrough(strikethrough: Strikethrough) {
            html.tag("s")
            visitChildren(strikethrough)
            html.tag("/s")
        }

        private fun renderTaskListItem(taskListItem: TaskListItemMarker) {
            html.tag("li")

            // Create checkbox for tasks
            if (taskListItem.isChecked) {
                html.raw("<ac:task-status>complete</ac:task-status> ")
            } else {
                html.raw("<ac:task-status>incomplete</ac:task-status> ")
            }

            // Render the rest of the item content
            visitChildren(taskListItem)

            html.tag("/li")
            html.line()
        }

        private fun visitChildren(parent: Node) {
            var node: Node? = parent.firstChild
            while (node != null) {
                val next = node.next
                context.render(node)
                node = next
            }
        }
    }
}