package de.felixlf.confluencemd.core.util

import java.io.StringReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamWriter

/**
 * Utilidad para procesar y transferir contenido XML
 */
object XmlUtils {
    /**
     * Transfiere contenido XML desde un string a un XMLStreamWriter
     *
     * @param xmlContent Contenido XML como string
     * @param writer XMLStreamWriter donde escribir el contenido
     * @param addWrapper Si es true, envuelve el contenido en un elemento contenedor
     */
    fun transferXmlContent(xmlContent: String, writer: XMLStreamWriter, addWrapper: Boolean = false) {
        try {
            // Añadir wrapper para asegurar un documento XML bien formado
            val contentToProcess = if (addWrapper) {
                "<wrapper>$xmlContent</wrapper>"
            } else {
                xmlContent
            }

            // Configurar el factory para StAX
            val factory = XMLInputFactory.newInstance()
            val reader = factory.createXMLStreamReader(StringReader(contentToProcess))

            // Procesar eventos del XML
            var depth = 0
            var skipDepth = -1

            while (reader.hasNext()) {
                when (reader.next()) {
                    XMLStreamConstants.START_ELEMENT -> {
                        depth++

                        // Saltar el elemento wrapper si existe
                        if (addWrapper && reader.localName == "wrapper" && depth == 1) {
                            skipDepth = depth
                            continue
                        }

                        // Si estamos dentro del elemento a saltar, continuamos
                        if (skipDepth > 0 && depth > skipDepth) {
                            continue
                        }

                        // Iniciar elemento
                        writer.writeStartElement(reader.localName)

                        // Procesar atributos
                        for (i in 0 until reader.attributeCount) {
                            writer.writeAttribute(
                                reader.getAttributeLocalName(i),
                                reader.getAttributeValue(i)
                            )
                        }
                    }

                    XMLStreamConstants.END_ELEMENT -> {
                        // Si estamos dentro del elemento a saltar, continuamos
                        if (skipDepth > 0 && depth >= skipDepth) {
                            depth--
                            if (depth == skipDepth - 1) {
                                skipDepth = -1
                            }
                            continue
                        }

                        // Cerrar elemento
                        writer.writeEndElement()
                        depth--
                    }

                    XMLStreamConstants.CHARACTERS -> {
                        // Si estamos dentro del elemento a saltar, continuamos
                        if (skipDepth > 0 && depth > skipDepth) {
                            continue
                        }

                        // Escribir texto
                        writer.writeCharacters(reader.text)
                    }

                    XMLStreamConstants.CDATA -> {
                        // Si estamos dentro del elemento a saltar, continuamos
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
            // Si hay algún error, simplemente escribimos el contenido como CDATA
            writer.writeCData(xmlContent)
        }
    }
}