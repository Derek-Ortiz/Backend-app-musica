package com.example.config

import com.example.tables.AlbumTable
import com.example.tables.ArtistaTable
import com.example.tables.TrackTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.database() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/EXTENSION"
        driverClassName = "org.postgresql.Driver"
        username = "postgres"
        password = "2006"
        maximumPoolSize = 10
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
         SchemaUtils.create(ArtistaTable, AlbumTable, TrackTable)
    }
}