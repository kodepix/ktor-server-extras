package io.github.kodepix.ktor.serialization.jackson

import com.fasterxml.jackson.annotation.JsonInclude.Include.*
import com.fasterxml.jackson.databind.SerializationFeature.*
import com.fasterxml.jackson.dataformat.xml.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import io.ktor.http.ContentType.Application.Xml
import io.ktor.serialization.*
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.contentnegotiation.*


/**
 * Registers the `application/xml` content type in the [ContentNegotiation] plugin using `Jackson`.
 *
 * For more information, see [Content negotiation and serialization](https://ktor.io/docs/serialization.html).
 *
 * Usage:
 *
 * ```kotlin
 * install(ContentNegotiation) {
 *     jacksonXml()
 * }
 * ```
 *
 * @param streamRequestBody if set to true, will stream request body, without keeping it whole in memory. This will set `Transfer-Encoding: chunked` header.
 * @param block block for optional configuration [XmlMapper]
 *
 * @sample io.github.kodepix.ktor.samples.jacksonXmlSample
 */
fun Configuration.jacksonXml(streamRequestBody: Boolean = true, block: XmlMapper.() -> Unit = {}) {
    xmlMapper.apply(block)
    val converter = JacksonConverter(xmlMapper, streamRequestBody)
    register(Xml, converter)
}


/**
 * Serializes any [this] object as `xml` string.
 */
fun Any.toXml() = xmlMapper.writeValueAsString(this)!!


private val xmlMapper = XmlMapper().apply {
    registerKotlinModule()
    registerModule(JavaTimeModule())
    setSerializationInclusion(NON_NULL)
    enable(INDENT_OUTPUT)
    disable(WRITE_DATES_AS_TIMESTAMPS)
}
