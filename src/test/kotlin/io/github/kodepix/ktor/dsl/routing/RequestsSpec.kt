package io.github.kodepix.ktor.dsl.routing

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.ktor.http.HttpHeaders.Referrer
import io.ktor.server.request.*
import io.mockk.*


class RequestsSpec : ShouldSpec({

    context("findRefererHost") {

        val request = mockk<ApplicationRequest>()

        should("return the correct url with the relative host") {

            every { request.header(Referrer) } returns "https://example.com:8080/some-page"

            val host = request.findRefererHost()

            host shouldBe "https://example.com:8080"
        }

        should("return the null if no referrer header found") {

            every { request.header(Referrer) } returns null

            val host = request.findRefererHost()

            host shouldBe null
        }
    }
})
