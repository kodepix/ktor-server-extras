package io.github.kodepix.ktor.dsl.routing

import io.github.kodepix.ktor.*
import io.github.kodepix.ktor.serialization.jackson.*
import io.github.smiley4.ktoropenapi.config.*
import io.ktor.http.ContentType.Application.Xml
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.Gone
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK


/**
 * Generates a description of the `200 OK` response with a body for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { ok(Any()) { mediaTypes = setOf(Xml) } }
 *     }) {}
 * }
 * ```
 *
 * @param example example of response body
 * @param init optional block for additional description
 *
 * @sample io.github.kodepix.ktor.samples.okSample1
 */
@OpenApiDslMarker
inline fun <reified TBody : Any> ResponsesConfig.ok(example: TBody, crossinline init: SimpleBodyConfig.() -> Unit = {}) = ok("1: ${Messages.example}" to example, init = init)


/**
 * Generates a description of the `200 OK` response with a body for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { ok("1: example 1" to Any(), "2: example 2" to Any()) }
 *     }) {}
 * }
 * ```
 *
 * @param examples list of examples of request body represented in <name, example> pairs
 * @param init optional block for additional description
 *
 * @sample io.github.kodepix.ktor.samples.okSample2
 */
@OpenApiDslMarker
inline fun <reified TBody : Any> ResponsesConfig.ok(vararg examples: Pair<String, TBody>, crossinline init: SimpleBodyConfig.() -> Unit = {}) = OK to {
    description = Messages.succeededWithPossibleBody.toString()
    body<TBody> {
        description = Messages.operationResult.toString()
        init()
        examples.forEach { (name, example) ->
            example(name) {
                value = if (mediaTypes.contains(Xml)) example.toXml() else example
            }
        }
    }
}


/**
 * Generates a description of the `201 CREATED` response for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { created }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.createdSample
 */
@OpenApiDslMarker
val ResponsesConfig.created get() = Created to { description = Messages.resourceCreated.toString() }


/**
 * Generates a description of the `204 NO CONTENT` response for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { noContent }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.noContentSample1
 */
@OpenApiDslMarker
val ResponsesConfig.noContent get() = NoContent to { description = Messages.succeededWithoutBody.toString() }


/**
 * Generates a description of the `404 NOT FOUND` response for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { notFound }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.notFoundSample
 */
@OpenApiDslMarker
val ResponsesConfig.notFound get() = NotFound to { description = Messages.resourceNotFound.toString() }


/**
 * Generates a description of the `202 ACCEPTED` response for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { accepted }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.acceptedSample1
 */
@OpenApiDslMarker
val ResponsesConfig.accepted get() = Accepted to { description = Messages.requestAccepted.toString() }


/**
 * Generates a description of the `410 GONE` response for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         response { gone }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.goneSample
 */
@OpenApiDslMarker
val ResponsesConfig.gone get() = Gone to { description = Messages.resourceUnavailable.toString() }
