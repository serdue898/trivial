package com.example.trivialnavidad.core.conexion.onffline.modelo

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Jugador (
    var id_jugador: Int,
    val nombre: String,
    val avatar: String
) {
    var partida: Int = 0
    var host = false
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(json: String): Jugador {
            return Json.decodeFromString(json)
        }
    }
}
