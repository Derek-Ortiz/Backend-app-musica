package com.example.services


import com.example.models.Album
import com.example.tables.AlbumTable
import com.example.tables.TrackTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object AlbumService {
    fun create(dto: Album): Album = transaction {
        val id = AlbumTable.insertAndGetId {
            it[title] = dto.title
            it[releaseYear] = dto.releaseYear
            it[artistId] = UUID.fromString(dto.artistId)
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        dto.copy(id = id.toString())
    }

    fun getAll(): List<Album> = transaction {
        AlbumTable.selectAll().map {
            Album(
                id = it[AlbumTable.id].toString(),
                title = it[AlbumTable.title],
                releaseYear = it[AlbumTable.releaseYear],
                artistId = it[AlbumTable.artistId].toString()
            )
        }
    }

    fun getById(id: String): Album? = transaction {
        AlbumTable.select { AlbumTable.id eq UUID.fromString(id) }
            .singleOrNull()?.let {
                Album(
                    id = it[AlbumTable.id].toString(),
                    title = it[AlbumTable.title],
                    releaseYear = it[AlbumTable.releaseYear],
                    artistId = it[AlbumTable.artistId].toString()
                )
            }
    }

    fun update(id: String, dto: Album): Boolean = transaction {
        AlbumTable.update({ AlbumTable.id eq UUID.fromString(id) }) {
            it[title] = dto.title
            it[releaseYear] = dto.releaseYear
            it[artistId] = UUID.fromString(dto.artistId)
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        val hasTracks = TrackTable.select { TrackTable.albumId eq UUID.fromString(id) }
            .count() > 0
        if (hasTracks) {
            throw IllegalStateException("No se puede eliminar: el álbum tiene tracks asociadas")
        }
        AlbumTable.deleteWhere { AlbumTable.id eq UUID.fromString(id) } > 0
    }
}