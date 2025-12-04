package com.example

import com.example.config.configureRouting
import com.example.config.configureSerialization
import com.example.config.database
import com.example.config.statusPage
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 9090, host = "0.0.0.0"){
        configureSerialization()
        database()
        configureRouting()
        statusPage()
    }.start(wait = true)


}


