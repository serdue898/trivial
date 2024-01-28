package com.example.trivialnavidad.core.conexion.online.model
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class JugadorEnPartidaOnline(
    var id_jugador: Int,
    var partida: Int,
    var casillaActual: String,
    var jugadorActual: Boolean,
    var juegos: MutableList<Boolean>,
    val avatar: String
) {
    var jugador: Jugador? = null

    fun puntosJugador(): Int {
        var puntos = 0
        for (juego in juegos) {
            if (juego) {
                puntos++
            }
        }
        return puntos
    }
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(json: String): JugadorEnPartidaOnline {
            return Json.decodeFromString(json)
        }
    }
}


