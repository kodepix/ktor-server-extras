package io.github.kodepix.ktor.dsl.routing

import io.github.kodepix.*
import io.github.kodepix.ktor.*
import io.github.smiley4.ktoropenapi.config.*
import io.github.smiley4.ktoropenapi.config.descriptors.*
import io.ktor.http.ContentType.Application.OctetStream
import io.ktor.http.HttpHeaders.Referrer
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.net.*


/**
 * Generates a summary and description for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         info("Book list | Returns a list of books.")
 *     }) {}
 * }
 * ```
 *
 * @param info string that contains summary and description delimited by symbol `|`
 * @param isDeprecated indicates that the route is deprecated, will be indicated in the summary
 *
 * @sample io.github.kodepix.ktor.samples.infoSample
 */
@OpenApiDslMarker
fun RouteConfig.info(info: String, isDeprecated: Boolean = false) {
    val strings = info.split("|")
    summary = (if (isDeprecated) "DEPRECATED " else "") + strings[0].trim()
    description = strings.getOrNull(1)?.trim()
    deprecated = isDeprecated
}


/**
 * Generates a request body description for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request {
 *             body(byteArrayExample) {
 *                 description = "video (*.mp4 | *.mkv | *.avi)"
 *                 mediaTypes = setOf(OctetStream)
 *             }
 *         }
 *     }) {}
 * }
 * ```
 *
 * @param example example of request body
 * @param init optional block for additional description
 *
 * @sample io.github.kodepix.ktor.samples.bodySample1
 */
@OpenApiDslMarker
inline fun <reified TRequest : Any> RequestConfig.body(example: TRequest, crossinline init: SimpleBodyConfig.() -> Unit = {}) = body("1: ${Messages.example}" to example, init = init)


/**
 * Generates a request body description for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request { body("1: int example 1" to 123, "2: int example 2" to 456) }
 *     }) {}
 * }
 * ```
 *
 * @param examples list of examples of request body represented in <name, example> pairs
 * @param init optional block for additional description
 *
 * @sample io.github.kodepix.ktor.samples.bodySample2
 */
@OpenApiDslMarker
inline fun <reified TRequest : Any> RequestConfig.body(vararg examples: Pair<String, TRequest>, crossinline init: SimpleBodyConfig.() -> Unit = {}) {
    body<TRequest> {
        description = Messages.bodyData.toString()
        examples.forEach { (name, example) ->
            example(name) {
                value = example
            }
        }
        init()
    }
}


/**
 * Generates an [OctetStream] request body description for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request { octetStreamBody { description = "video (*.mp4 | *.mkv | *.avi)" } }
 *     }) {}
 * }
 * ```
 *
 * @param init optional block for additional description
 *
 * @sample io.github.kodepix.ktor.samples.octetStreamBodySample
 */
@OpenApiDslMarker
fun RequestConfig.octetStreamBody(init: SimpleBodyConfig.() -> Unit = {}) = body(byteArrayExample) {
    mediaTypes = setOf(OctetStream)
    init()
}


/**
 * Generates an identifier path parameter description for the route.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         request { id }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.idSample
 */
@OpenApiDslMarker
val RequestConfig.id
    get() = pathParameter<Int>("id") {
        description = Messages.identifier.toString()
        required = true
        example(ValueExampleDescriptor(Messages.identifier.toString(), 1))
    }


/**
 * Generates a page query parameter description for the route. Parameter name is `p`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         request { page }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.pageSample
 */
@OpenApiDslMarker
val RequestConfig.page get() = queryParameter<Int>(PAGE_PARAM) { description = Messages.resultPageNumber.toString() }


/**
 * Generates a sort query parameter description for the route. Parameter name is `sort`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         request { sort }
 *     }) {}
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.sortSample
 */
@OpenApiDslMarker
val RequestConfig.sort get() = queryParameter<String>(SORT_PARAM) { description = Messages.resultSortParams.toString() }


/**
 * Returns [io.ktor.http.HttpHeaders.Referrer] header value from request.
 *
 * Or `null` if not found.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *     }) {
 *         call.request.findRefererHost()
 *     }
 * }
 * ```
 *
 * @sample io.github.kodepix.ktor.samples.findRefererHostSample
 */
fun ApplicationRequest.findRefererHost() = header(Referrer)
    ?.let { URI(it) }
    ?.let { uri ->
        val port = if (uri.port > 0)
            ":${uri.port}"
        else
            ""
        "${uri.scheme}://${uri.host}$port"
    }


/**
 * Gets query parameter with [name] from request as list.
 *
 * Parameter format should be equals `param=value1,value2,value3`
 *
 * @param name parameter name
 */
fun RoutingContext.queryParamList(name: String) = call.request.queryParameters[name]?.split(",").orEmpty()


/**
 * Gets page parameter (`p`) value from request query.
 *
 * If parameter is not found returns `0`.
 */
val RoutingContext.page get() = call.request.queryParameters[PAGE_PARAM]?.toInt() ?: 0


/**
 * Gets sort parameter value (e.g. `sort=title:desc,status,user:asc`) as list from request query.
 *
 * Returns list of [Sort].
 */
@Suppress("unused")
val RoutingContext.sort
    get() = queryParamList(SORT_PARAM).map {
        val params = it.split(":")
        Sort(column = params[0].lowercase(), order = params.getOrNull(1)?.lowercase())
    }


/**
 * Sort parameter.
 *
 * @property column sortable column
 * @property order sort order
 */
data class Sort(
    val column: String,
    val order: String?,
)


/**
 * Page query parameter name = `p`.
 */
const val PAGE_PARAM = "p"

/**
 * Sort query parameter name = `sort`.
 */
const val SORT_PARAM = "sort"
