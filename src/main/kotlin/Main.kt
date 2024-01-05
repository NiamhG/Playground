package org.example

import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.http4k.util.Appendable
import swaggerUi

fun main() {
    val api = routes(
        swaggerUi(),
        contract {
            renderer = OpenApi3(ApiInfo("Swagger", "v1.0", "Swagger Description"), CustomJackson)
            descriptionPath = "/swagger.json"
            routes = Appendable(
                mutableListOf(
                    postExample()
                )
            )
        }
    )

    api.asServer(Netty(9000)).start()
}