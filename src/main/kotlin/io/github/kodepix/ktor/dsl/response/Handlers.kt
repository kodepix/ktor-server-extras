package io.github.kodepix.ktor.dsl.response

import io.github.kodepix.*
import io.github.kodepix.ktor.*
import io.github.kodepix.ktor.dsl.routing.*
import io.konform.validation.*
import io.konform.validation.constraints.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*


/**
 * Returns a request result with a status of `200 OK`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({ }) {
 *         ok { -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.okSample3
 */
@HandlersDsl
suspend inline fun <reified TResult : Any> RoutingContext.ok(fn: () -> TResult) {
    val result = fn()
    call.respond(result)
}


/**
 * Returns a request result with a status of `200 OK`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         request { id }
 *     }) {
 *         ok { id -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.okSample4
 */
@HandlersDsl
suspend inline fun <reified TResult : Any> RoutingContext.ok(fn: (Id) -> TResult) {
    val id: Int by call.parameters
    val result = fn(Id(id))
    call.respond(result)
}


/**
 * Returns a result of validated request with a status of `200 OK`.
 *
 * If the request model does not pass validation, then `422 UNPROCESSABLE ENTITY` are returned.
 *
 * Usage:
 *
 * ```kotlin
 * val someValidation = Validation {
 *     String::length { minimum(0) }
 * }
 *
 * routing {
 *     get({
 *         request { body("example") }
 *     }) {
 *         ok(someValidation) { str -> Any() }
 *     }
 * }
 * ```
 *
 * @param validation validation applied
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.okSample5
 */
@HandlersDsl
suspend inline fun <reified TModel : Any, reified TResult : Any> RoutingContext.ok(validation: Validation<TModel> = Validation {}, fn: (TModel) -> TResult) {

    val model = call.receive<TModel>()

    whenValidated(pageValidation(page), validation(model)) {
        val result = fn(model)
        call.respond(result)
    }
}

@PublishedApi
internal val pageValidation = Validation<Int> {
    minimum(0) hint Messages.pageNumberMustBeAtLeast0.toString()
}


/**
 * Returns a request result with a status of `200 OK`.
 *
 * If the result is null, then `404 NOT FOUND` is returned.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({ }) {
 *         okOrNotFound { -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.okOrNotFoundSample1
 */
@HandlersDsl
suspend inline fun <reified TResult : Any?> RoutingContext.okOrNotFound(fn: () -> TResult) {
    internalOkOrNotFound { fn() }
}


/**
 * Returns a request result with a status of `200 OK`.
 *
 * If the result is null, then `404 NOT FOUND` is returned.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     get({
 *         request { id }
 *     }) {
 *         okOrNotFound { id -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.okOrNotFoundSample2
 */
@HandlersDsl
suspend inline fun <reified TResult : Any?> RoutingContext.okOrNotFound(fn: (Id) -> TResult) {
    val id: Int by call.parameters
    internalOkOrNotFound { fn(Id(id)) }
}

@PublishedApi
internal suspend inline fun <reified TResult> RoutingContext.internalOkOrNotFound(fn: () -> TResult) {
    val result = fn()
    if (result != null)
        call.respond(result)
    else
        call.respond(NotFound)
}


/**
 * Returns a result of validated request with a status of `200 OK`.
 *
 * If the request model does not pass validation, then `422 UNPROCESSABLE ENTITY` are returned.
 *
 * If the result is null, then `401 UNAUTHORIZED` is returned.
 *
 * Usage:
 *
 * ```kotlin
 * val loginValidation = Validation {
 *     String::toString { notBlank() }
 * }
 *
 * routing {
 *     post({
 *         request { body("example") }
 *     }) {
 *         okOrUnauthorized(loginValidation) { str -> Any() }
 *     }
 * }
 * ```
 *
 * @param validation validation applied
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.okOrUnauthorizedSample
 */
@HandlersDsl
suspend inline fun <reified TModel : Any, reified TResult> RoutingContext.okOrUnauthorized(validation: Validation<TModel> = Validation {}, fn: (TModel) -> TResult) {

    val model = call.receive<TModel>()

    whenValidated(validation(model)) {

        val result = fn(model)

        if (result != null)
            call.respond(result)
        else
            call.respond(Unauthorized)
    }
}


/**
 * Returns a request result with a status of `201 CREATED`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request { body("example") }
 *     }) {
 *         created { str: String -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.createdSample1
 */
@HandlersDsl
suspend inline fun <reified TModel : Any, reified TResult : Any> RoutingContext.created(fn: (TModel) -> TResult) {
    val model = call.receive<TModel>()
    val result = fn(model)
    call.response.header(HttpHeaders.Location, "${call.request.path()}/$result")
    call.respond(Created, result)
}


/**
 * Returns a request result with a status of `201 CREATED`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request {
 *             body("example")
 *             id
 *         }
 *     }) {
 *         created { str: String, id -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.createdSample2
 */
@HandlersDsl
suspend inline fun <reified TModel : Any, reified TResult : Any> RoutingContext.created(fn: (TModel, Id) -> TResult) {
    val id: Int by call.parameters
    val model = call.receive<TModel>()
    val result = fn(model, Id(id))
    call.response.header(HttpHeaders.Location, "${call.request.path()}/$result")
    call.respond(Created, result)
}


/**
 * Returns a result of validated request with a status of `201 CREATED`.
 *
 * If the request model does not pass validation, then `422 UNPROCESSABLE ENTITY` are returned.
 *
 * Usage:
 *
 * ```kotlin
 * val someValidation = Validation {
 *     String::length { minimum(0) }
 * }
 *
 * routing {
 *     post({
 *         request { body("example") }
 *     }) {
 *         created(someValidation) { str -> Any() }
 *     }
 * }
 * ```
 *
 * @param validation validation applied
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.createdSample3
 */
@HandlersDsl
suspend inline fun <reified TModel : Any, reified TResult : Any> RoutingContext.created(validation: Validation<TModel> = Validation {}, fn: (TModel) -> TResult) {

    val model = call.receive<TModel>()

    whenValidated(validation(model)) {
        val result = fn(model)
        call.response.header(HttpHeaders.Location, "${call.request.path()}/$result")
        call.respond(Created, result)
    }
}


/**
 * Returns a request result with a status of `202 ACCEPTED`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request { body("example") }
 *     }) {
 *         accepted { str: String -> Any() }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.acceptedSample2
 */
@HandlersDsl
suspend inline fun <reified TModel : Any> RoutingContext.accepted(fn: (TModel) -> Unit) {
    val model = call.receive<TModel>()
    fn(model)
    call.respond(Accepted)
}


/**
 * Returns a request result with a status of `204 NO CONTENT`.
 *
 * If the result of [fn] is not equal to `1`, then `404 NOT FOUND` is returned.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request { id }
 *     }) {
 *         noContentOrNotFound { id -> 1 }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.noContentOrNotFoundSample1
 */
@HandlersDsl
suspend fun RoutingContext.noContentOrNotFound(fn: suspend (Id) -> Int) {
    val id: Int by call.parameters
    internalNoContentOrNotFound { fn(Id(id)) }
}


/**
 * Returns a result of validated request with a status of `204 NO CONTENT`.
 *
 * If the result of [fn] is not equal to `1`, then `404 NOT FOUND` is returned.
 *
 * If the request model does not pass validation, then `422 UNPROCESSABLE ENTITY` are returned.
 *
 * Usage:
 *
 * ```kotlin
 * val someValidation = Validation {
 *     Id::value { minimum(1) }
 * }
 *
 * routing {
 *     post({
 *         request { id }
 *     }) {
 *         noContentOrNotFound(someValidation) { id -> 1 }
 *     }
 * }
 * ```
 *
 * @param validation validation applied
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.noContentOrNotFoundSample2
 */
@HandlersDsl
suspend fun RoutingContext.noContentOrNotFound(validation: Validation<Id> = Validation {}, fn: suspend (Id) -> Int) {
    val id: Int by call.parameters
    whenValidated(validation(Id(id))) {
        internalNoContentOrNotFound { fn(Id(id)) }
    }
}


/**
 * Returns a request result with a status of `204 NO CONTENT`.
 *
 * If the result of [fn] is not equal to `1`, then `404 NOT FOUND` is returned.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request {
 *             body("example")
 *             id
 *         }
 *     }) {
 *         noContentOrNotFound { str: String, id -> 1 }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.noContentOrNotFoundSample3
 */
@HandlersDsl
suspend inline fun <reified TModel : Any> RoutingContext.noContentOrNotFound(crossinline fn: suspend (TModel, Id) -> Int) {
    val id: Int by call.parameters
    val model = call.receive<TModel>()
    internalNoContentOrNotFound { fn(model, Id(id)) }
}


/**
 * Returns a result of validated request with a status of `204 NO CONTENT`.
 *
 * If the result of [fn] is not equal to `1`, then `404 NOT FOUND` is returned.
 *
 * If the request model does not pass validation, then `422 UNPROCESSABLE ENTITY` are returned.
 *
 * Usage:
 *
 * ```kotlin
 * val someValidation = Validation {
 *     String::length { minimum(0) }
 * }
 *
 * routing {
 *     post({
 *         request {
 *             body("example")
 *             id
 *         }
 *     }) {
 *         noContentOrNotFound(someValidation) { str: String, id -> 1 }
 *     }
 * }
 * ```
 *
 * @param validation validation applied
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.noContentOrNotFoundSample4
 */
@HandlersDsl
suspend inline fun <reified TModel : Any> RoutingContext.noContentOrNotFound(validation: Validation<TModel> = Validation {}, crossinline fn: suspend (TModel, Id) -> Int) {
    val id: Int by call.parameters
    val model = call.receive<TModel>()
    whenValidated(validation(model)) {
        internalNoContentOrNotFound { fn(model, Id(id)) }
    }
}

@PublishedApi
internal suspend inline fun RoutingContext.internalNoContentOrNotFound(fn: () -> Int) {
    if (fn() == 1)
        call.respond(NoContent)
    else
        call.respond(NotFound)
}


/**
 * Returns a request result with a status of `204 NO CONTENT`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *     }) {
 *         noContent {}
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.noContentSample2
 */
@HandlersDsl
suspend inline fun RoutingContext.noContent(crossinline fn: suspend () -> Unit) {
    fn()
    call.respond(NoContent)
}


/**
 * Returns a request result with a status of `204 NO CONTENT`.
 *
 * Usage:
 *
 * ```kotlin
 * routing {
 *     post({
 *         request { body("example") }
 *     }) {
 *         noContent { str: String -> }
 *     }
 * }
 * ```
 *
 * @param fn request handler
 *
 * @sample io.github.kodepix.ktor.samples.noContentSample3
 */
@HandlersDsl
suspend inline fun <reified TModel : Any> RoutingContext.noContent(crossinline fn: suspend (TModel) -> Unit) {
    val model = call.receive<TModel>()
    fn(model)
    call.respond(NoContent)
}


/**
 * Checks validation results.
 *
 * If a not valid result is found, then `422 UNPROCESSABLE ENTITY` is returned.
 *
 * @param results validation results
 * @param fn request handler
 */
suspend inline fun <reified TResult : Any> RoutingContext.whenValidated(vararg results: ValidationResult<*>, fn: () -> TResult) {
    if (results.all { it is Valid })
        fn()
    else
        call.respond(UnprocessableEntity, ValidationErrorResponse(results.toList()))
}


@DslMarker
annotation class HandlersDsl
