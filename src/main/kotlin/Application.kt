package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 9090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)


}

fun Application.module() {

    DatabaseFactory.init()

    configureSerialization()
    configureSecurity()
    configureRouting()
}
