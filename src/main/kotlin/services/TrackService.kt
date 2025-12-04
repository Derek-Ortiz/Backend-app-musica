package com.backend.services

import com.example.models.Track
import com.example.tables.TrackTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object TrackService {
    fun create(dto: Track): Track = transaction {
        val id = TrackTable.insertAndGetId {
            it[title] = dto.title
            it[duration] = dto.duration
            it[albumId] = UUID.fromString(dto.albumId)
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        dto.copy(id = id.toString())
    }

    fun getAll(): List<Track> = transaction {
        TrackTable.selectAll().map {
            Track(
                id = it[TrackTable.id].toString(),
                title = it[TrackTable.title],
                duration = it[TrackTable.duration],
                albumId = it[TrackTable.albumId].toString()
            )
        }
    }

    fun getById(id: String): Track? = transaction {
        TrackTable.select { TrackTable.id eq UUID.fromString(id) }
            .singleOrNull()?.let {
                Track(
                    id = it[TrackTable.id].toString(),
                    title = it[TrackTable.title],
                    duration = it[TrackTable.duration],
                    albumId = it[TrackTable.albumId].toString()
                )
            }
    }

    fun update(id: String, dto: Track): Boolean = transaction {
        TrackTable.update({ TrackTable.id eq UUID.fromString(id) }) {
            it[title] = dto.title
            it[duration] = dto.duration
            it[albumId] = UUID.fromString(dto.albumId)
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        TrackTable.deleteWhere { TrackTable.id eq UUID.fromString(id) } > 0
    }
}