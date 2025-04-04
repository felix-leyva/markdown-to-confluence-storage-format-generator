package de.felixlf.confluencedsl.builders

import javax.xml.stream.XMLStreamWriter

class ParagraphBuilder(private val writer: XMLStreamWriter) {
    init {
        writer.writeStartElement("p")
    }

    fun text(content: String) {
        writer.writeCharacters(content)
    }

    fun bold(content: String) {
        writer.writeStartElement("strong")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun italic(content: String) {
        writer.writeStartElement("em")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun underline(content: String) {
        writer.writeStartElement("u")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun strikethrough(content: String) {
        writer.writeStartElement("s")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun code(content: String) {
        writer.writeStartElement("code")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun superscript(content: String) {
        writer.writeStartElement("sup")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun subscript(content: String) {
        writer.writeStartElement("sub")
        writer.writeCharacters(content)
        writer.writeEndElement()
    }

    fun link(url: String, text: String) {
        writer.writeStartElement("ac:link")
        writer.writeStartElement("ri:url")
        writer.writeAttribute("ri:value", url)
        writer.writeEndElement() // ri:url
        writer.writeStartElement("ac:plain-text-link-body")
        writer.writeCData(text)
        writer.writeEndElement() // ac:plain-text-link-body
        writer.writeEndElement() // ac:link
    }

    fun pageLink(pageTitle: String, spaceKey: String? = null, text: String? = null) {
        writer.writeStartElement("ac:link")
        writer.writeStartElement("ri:page")
        writer.writeAttribute("ri:content-title", pageTitle)
        if (spaceKey != null) {
            writer.writeAttribute("ri:space-key", spaceKey)
        }
        writer.writeEndElement() // ri:page

        if (text != null) {
            writer.writeStartElement("ac:plain-text-link-body")
            writer.writeCData(text)
            writer.writeEndElement() // ac:plain-text-link-body
        }

        writer.writeEndElement() // ac:link
    }

    fun attachmentLink(filename: String, pageTitle: String? = null, spaceKey: String? = null, text: String? = null) {
        writer.writeStartElement("ac:link")
        writer.writeStartElement("ri:attachment")
        writer.writeAttribute("ri:filename", filename)

        if (pageTitle != null) {
            writer.writeAttribute("ri:content-title", pageTitle)
        }

        if (spaceKey != null) {
            writer.writeAttribute("ri:space-key", spaceKey)
        }

        writer.writeEndElement() // ri:attachment

        if (text != null) {
            writer.writeStartElement("ac:plain-text-link-body")
            writer.writeCData(text)
            writer.writeEndElement() // ac:plain-text-link-body
        }

        writer.writeEndElement() // ac:link
    }

    fun userMention(username: String, accountId: String? = null) {
        writer.writeStartElement("ac:link")
        writer.writeStartElement("ri:user")

        if (accountId != null) {
            writer.writeAttribute("ri:account-id", accountId)
        } else {
            writer.writeAttribute("ri:username", username)
        }

        writer.writeEndElement() // ri:user
        writer.writeEndElement() // ac:link
    }

    fun emoji(shortName: String) {
        writer.writeStartElement("ac:emoticon")
        writer.writeAttribute("ac:name", shortName)
        writer.writeEndElement() // ac:emoticon
    }

    fun color(content: String, color: String) {
        writer.writeStartElement("span")
        writer.writeAttribute("style", "color: $color;")
        writer.writeCharacters(content)
        writer.writeEndElement() // span
    }

    fun close() {
        writer.writeEndElement() // p
    }
}