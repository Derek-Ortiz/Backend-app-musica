package com.example.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/api") {
            call.respondText("Hello World!")
        }
    }
}
