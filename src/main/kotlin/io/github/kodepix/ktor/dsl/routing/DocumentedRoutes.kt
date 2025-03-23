package io.github.kodepix.ktor.dsl.routing

import io.github.smiley4.ktoropenapi.*
import io.github.smiley4.ktoropenapi.config.*
import io.ktor.server.routing.*


/**
 * Allows set common parameters for all routes.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     forAllOpenApiRoutes {
 *         response { BadRequest to { description = "incorrect query syntax" } }
 *         response { InternalServerError to { description = "internal server error" } }
 *     }
 * }
 * ```
 *
 * @param fn config function
 *
 * @sample io.github.kodepix.ktor.samples.forAllOpenApiRoutesSample
 */
fun Route.forAllOpenApiRoutes(fn: RouteConfig.() -> Unit) = documentedRouteSelectors().forEach { it.documentation.fn() }


private fun Route.documentedRouteSelectors() = (this as RoutingNode).getAllRoutes().mapNotNull { it.findDocumentedRouteSelector() }

private fun RoutingNode.findDocumentedRouteSelector(): DocumentedRouteSelector? =
    if (parent?.selector is DocumentedRouteSelector)
        parent?.selector as DocumentedRouteSelector
    else
        parent?.findDocumentedRouteSelector()
