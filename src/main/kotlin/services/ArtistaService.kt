package com.example.services

import com.example.models.Artista
import com.example.tables.AlbumTable
import com.example.tables.ArtistaTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object ArtistaService {
    fun create(dto: Artista): Artista = transaction {
        val id = ArtistaTable.insertAndGetId {
            it[name] = dto.name
            it[genre] = dto.genre
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        dto.copy(id = id.toString())
    }

    fun getAll(): List<Artista> = transaction {
        ArtistaTable.selectAll().map {
            Artista(
                id = it[ArtistaTable.id].toString(),
                name = it[ArtistaTable.name],
                genre = it[ArtistaTable.genre]
            )
        }
    }

    fun getById(id: String): Artista? = transaction {
        ArtistaTable.select { ArtistaTable.id eq UUID.fromString(id) }
            .singleOrNull()?.let {
                Artista(
                    id = it[ArtistaTable.id].toString(),
                    name = it[ArtistaTable.name],
                    genre = it[ArtistaTable.genre]
                )
            }
    }

    fun update(id: String, dto: Artista): Boolean = transaction {
        ArtistaTable.update({ ArtistaTable.id eq UUID.fromString(id) }) {
            it[name] = dto.name
            it[genre] = dto.genre
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        val hasAlbums = AlbumTable.select { AlbumTable.artistId eq UUID.fromString(id) }
            .count() > 0
        if (hasAlbums) {
            throw IllegalStateException("No se puede eliminar: el artista tiene álbumes asociados")
        }
        ArtistaTable.deleteWhere { ArtistaTable.id eq UUID.fromString(id) } > 0
    }
}