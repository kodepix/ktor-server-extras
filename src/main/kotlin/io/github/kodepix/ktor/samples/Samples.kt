@file:Suppress("ktlint:standard:property-naming", "LocalVariableName", "unused", "UNUSED_ANONYMOUS_PARAMETER")

package io.github.kodepix.ktor.samples

import io.github.kodepix.*
import io.github.kodepix.ktor.dsl.response.*
import io.github.kodepix.ktor.dsl.routing.*
import io.github.kodepix.ktor.serialization.jackson.*
import io.github.smiley4.ktoropenapi.*
import io.konform.validation.*
import io.konform.validation.constraints.*
import io.ktor.http.ContentType.Application.OctetStream
import io.ktor.http.ContentType.Application.Xml
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*


internal fun Application.infoSample() {
    routing {
        get({
            info("Book list | Returns a list of books.")
        }) {}
    }
}


internal fun Application.bodySample1() {
    routing {
        post({
            request {
                body(byteArrayExample) {
                    description = "video (*.mp4 | *.mkv | *.avi)"
                    mediaTypes = setOf(OctetStream)
                }
            }
        }) {}
    }
}


internal fun Application.bodySample2() {
    routing {
        post({
            request { body("1: int example 1" to 123, "2: int example 2" to 456) }
        }) {}
    }
}


internal fun Application.octetStreamBodySample() {
    routing {
        post({
            request { octetStreamBody { description = "video (*.mp4 | *.mkv | *.avi)" } }
        }) {}
    }
}


internal fun Application.idSample() {
    routing {
        get({
            request { id }
        }) {}
    }
}


internal fun Application.pageSample() {
    routing {
        get({
            request { page }
        }) {}
    }
}


internal fun Application.sortSample() {
    routing {
        get({
            request { sort }
        }) {}
    }
}


internal fun Application.okSample1() {
    routing {
        get({
            response { ok(Any()) { mediaTypes = setOf(Xml) } }
        }) {}
    }
}


internal fun Application.okSample2() {
    routing {
        get({
            response { ok("1: example 1" to Any(), "2: example 2" to Any()) }
        }) {}
    }
}


internal fun Application.createdSample() {
    routing {
        get({
            response { created }
        }) {}
    }
}


internal fun Application.noContentSample1() {
    routing {
        get({
            response { noContent }
        }) {}
    }
}


internal fun Application.notFoundSample() {
    routing {
        get({
            response { notFound }
        }) {}
    }
}


internal fun Application.acceptedSample1() {
    routing {
        get({
            response { accepted }
        }) {}
    }
}


internal fun Application.goneSample() {
    routing {
        get({
            response { gone }
        }) {}
    }
}


internal fun Application.forAllOpenApiRoutesSample() {
    routing {
        forAllOpenApiRoutes {
            response { BadRequest to { description = "incorrect query syntax" } }
            response { InternalServerError to { description = "internal server error" } }
        }
    }
}


internal fun Application.okSample3() {
    routing {
        get({ }) {
            ok { -> Any() }
        }
    }
}


internal fun Application.okSample4() {
    routing {
        get({
            request { id }
        }) {
            ok { id -> Any() }
        }
    }
}


internal fun Application.okSample5() {

    val someValidation = Validation {
        String::length { minimum(0) }
    }

    routing {
        get({
            request { body("example") }
        }) {
            ok(someValidation) { str -> Any() }
        }
    }
}


internal fun Application.okOrNotFoundSample1() {
    routing {
        get({ }) {
            okOrNotFound { -> Any() }
        }
    }
}


internal fun Application.okOrNotFoundSample2() {
    routing {
        get({
            request { id }
        }) {
            okOrNotFound { id -> Any() }
        }
    }
}


internal fun Application.okOrUnauthorizedSample() {

    val loginValidation = Validation {
        String::toString { notBlank() }
    }

    routing {
        post({
            request { body("example") }
        }) {
            okOrUnauthorized(loginValidation) { str -> Any() }
        }
    }
}


internal fun Application.createdSample1() {
    routing {
        post({
            request { body("example") }
        }) {
            created { str: String -> Any() }
        }
    }
}


internal fun Application.createdSample2() {
    routing {
        post({
            request {
                body("example")
                id
            }
        }) {
            created { str: String, id -> Any() }
        }
    }
}


internal fun Application.createdSample3() {

    val someValidation = Validation {
        String::length { minimum(0) }
    }

    routing {
        post({
            request { body("example") }
        }) {
            created(someValidation) { str -> Any() }
        }
    }
}


internal fun Application.acceptedSample2() {
    routing {
        post({
            request { body("example") }
        }) {
            accepted { str: String -> Any() }
        }
    }
}


internal fun Application.noContentOrNotFoundSample1() {
    routing {
        post({
            request { id }
        }) {
            noContentOrNotFound { id -> 1 }
        }
    }
}


internal fun Application.noContentOrNotFoundSample2() {

    val someValidation = Validation {
        Id::value { minimum(1) }
    }

    routing {
        post({
            request { id }
        }) {
            noContentOrNotFound(someValidation) { id -> 1 }
        }
    }
}


internal fun Application.noContentOrNotFoundSample3() {
    routing {
        post({
            request {
                body("example")
                id
            }
        }) {
            noContentOrNotFound { str: String, id -> 1 }
        }
    }
}


internal fun Application.noContentOrNotFoundSample4() {

    val someValidation = Validation {
        String::length { minimum(0) }
    }

    routing {
        post({
            request {
                body("example")
                id
            }
        }) {
            noContentOrNotFound(someValidation) { str: String, id -> 1 }
        }
    }
}


internal fun Application.noContentSample2() {
    routing {
        post({
        }) {
            noContent {}
        }
    }
}


internal fun Application.noContentSample3() {
    routing {
        post({
            request { body("example") }
        }) {
            noContent { str: String -> }
        }
    }
}


internal fun Application.findRefererHostSample() {
    routing {
        get({
        }) {
            call.request.findRefererHost()
        }
    }
}


fun Application.jacksonXmlSample() {
    install(ContentNegotiation) {
        jacksonXml()
    }
}
