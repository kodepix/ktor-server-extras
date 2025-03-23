package io.github.kodepix.ktor.dsl.routing

import io.github.kodepix.ktor.*
import io.konform.validation.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity


/**
 * Validation error response message.
 *
 * Will be returned in response body when response status is `422 UNPROCESSABLE ENTITY`.
 *
 * @param results validation results
 */
class ValidationErrorResponse(results: List<ValidationResult<*>>) : ErrorResponse(
    message = "${Messages.validationErrors}: ${results.flatMap { it.errors }.joinToString { "[${it.dataPath} ${it.message}]" }}",
    status = UnprocessableEntity
)


/**
 * Error response message.
 *
 * @property message error message
 * @property status response status
 */
open class ErrorResponse(val message: String, val status: HttpStatusCode)
