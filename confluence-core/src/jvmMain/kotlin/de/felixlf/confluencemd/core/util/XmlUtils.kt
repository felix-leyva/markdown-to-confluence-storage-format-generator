package de.felixlf.confluencemd.core.util

import java.io.StringReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamWriter

/**
 * Utility class for XML operations
 */
object XmlUtils {
    /**
     * Transfers XML content from a string to the XMLStreamWriter
     *
     * @param xmlContent XML Content
     * @param writer XMLStreamWriter to write to
     * @param addWrapper Whether to add a wrapper element around the content
     */
    fun transferXmlContent(xmlContent: String, writer: XMLStreamWriter, addWrapper: Boolean = false) {
        try {
            val contentToProcess = if (addWrapper) {
                "<wrapper>$xmlContent</wrapper>"
            } else {
                xmlContent
            }

            val factory = XMLInputFactory.newInstance()
            val reader = factory.createXMLStreamReader(StringReader(contentToProcess))

            var depth = 0
            var skipDepth = -1

            while (reader.hasNext()) {
                when (reader.next()) {
                    XMLStreamConstants.START_ELEMENT -> {
                        depth++

                        if (addWrapper && reader.localName == "wrapper" && depth == 1) {
                            skipDepth = depth
                            continue
                        }

                        if (skipDepth > 0 && depth > skipDepth) {
                            continue
                        }

                        writer.writeStartElement(reader.localName)

                        for (i in 0 until reader.attributeCount) {
                            writer.writeAttribute(
                                reader.getAttributeLocalName(i),
                                reader.getAttributeValue(i)
                            )
                        }
                    }

                    XMLStreamConstants.END_ELEMENT -> {
                        if (skipDepth > 0 && depth >= skipDepth) {
                            depth--
                            if (depth == skipDepth - 1) {
                                skipDepth = -1
                            }
                            continue
                        }

                        writer.writeEndElement()
                        depth--
                    }

                    XMLStreamConstants.CHARACTERS -> {
                        if (skipDepth > 0 && depth > skipDepth) {
                            continue
                        }

                        writer.writeCharacters(reader.text)
                    }

                    XMLStreamConstants.CDATA -> {
                        if (skipDepth > 0 && depth > skipDepth) {
                            continue
                        }

                        // Escribir CDATA
                        writer.writeCData(reader.text)
                    }
                }
            }

            reader.close()
        } catch (e: Exception) {
            writer.writeCData(xmlContent)
        }
    }
}