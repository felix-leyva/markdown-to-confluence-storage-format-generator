package de.felixlf.confluencemd.core

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

/**
 * Converts Markdown text to Confluence storage format (XML)
 */
class MarkdownConverter {

    private val flavour = GFMFlavourDescriptor()
    private val parser = MarkdownParser(flavour)

    /**
     * Convert markdown text to Confluence storage format
     *
     * @param markdown The markdown text to convert
     * @return Confluence storage format XML as a string
     */
    fun convertToConfluence(markdown: String): String {
        val parsedTree = parser.buildMarkdownTreeFromString(markdown)

        // Use the custom Confluence HTML generator
        val generator = ConfluenceHtmlGenerator(markdown, parsedTree, flavour)
        return generator.generateHtml()
    }

    /**
     * Custom HTML generator that produces Confluence-compatible markup
     */
    private class ConfluenceHtmlGenerator(
        private val text: String,
        private val parsedTree: ASTNode,
        private val flavour: GFMFlavourDescriptor
    ) {
        fun generateHtml(): String {
            // Use the built-in HTML generator with our custom visitor
            val htmlGenerator = HtmlGenerator(text, parsedTree, flavour, false)

            // Convert standard HTML to Confluence format with basic replacements
            val html = htmlGenerator.generateHtml()

            return transformHtmlToConfluence(html)
        }

        /**
         * Transform HTML to Confluence storage format
         */
        private fun transformHtmlToConfluence(html: String): String {
            return html
                // Code blocks
                .replace(Regex("<pre><code( class=\"language-([^\"]+)\")?>([\\s\\S]*?)</code></pre>")) { matchResult ->
                    val language = matchResult.groupValues[2]
                    val codeContent = unescapeHtml(matchResult.groupValues[3])

                    val languageParam = if (language.isNotEmpty()) {
                        "<ac:parameter ac:name=\"language\">$language</ac:parameter>"
                    } else {
                        ""
                    }

                    "<ac:structured-macro ac:name=\"code\">$languageParam<ac:plain-text-body><![CDATA[$codeContent]]></ac:plain-text-body></ac:structured-macro>"
                }

                // Links
                .replace(Regex("<a href=\"([^\"]+)\">([\\s\\S]*?)</a>")) { matchResult ->
                    val url = matchResult.groupValues[1]
                    val text = matchResult.groupValues[2]

                    "<ac:link><ri:url ri:value=\"$url\"/><ac:plain-text-link-body><![CDATA[$text]]></ac:plain-text-link-body></ac:link>"
                }

                // Blockquotes to info panels
                .replace(Regex("<blockquote>([\\s\\S]*?)</blockquote>")) { matchResult ->
                    val content = matchResult.groupValues[1]

                    "<ac:structured-macro ac:name=\"info\"><ac:rich-text-body>$content</ac:rich-text-body></ac:structured-macro>"
                }

                // Images
                .replace(Regex("<img src=\"([^\"]+)\"( alt=\"([^\"]*)\")?>")) { matchResult ->
                    val src = matchResult.groupValues[1]
                    val alt = matchResult.groupValues[3]

                    val altParam = if (alt.isNotEmpty()) {
                        " ac:alt=\"$alt\""
                    } else {
                        ""
                    }

                    "<ac:image$altParam><ri:url ri:value=\"$src\"/></ac:image>"
                }
        }

        /**
         * Unescape HTML entities in code blocks
         */
        private fun unescapeHtml(text: String): String {
            return text
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
        }
    }
}