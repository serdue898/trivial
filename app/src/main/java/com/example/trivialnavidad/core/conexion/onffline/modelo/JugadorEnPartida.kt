package com.example.trivialnavidad.core.conexion.onffline.modelo

data class JugadorEnPartida(
    var jugador: Jugador,
    var partida: Int,
    var casillaActual: String,
    var jugadorActual: Boolean,
    var juegos: MutableList<Boolean>,
    val avatar: String
) {
    fun puntosJugador(): Int {
        var puntos = 0
        for (juego in juegos) {
            if (juego) {
                puntos++
            }
        }
        return puntos
    }
}


