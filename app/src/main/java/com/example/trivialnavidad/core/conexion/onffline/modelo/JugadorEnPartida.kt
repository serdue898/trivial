package com.example.trivialnavidad.core.conexion.onffline.modelo

data class JugadorEnPartida(
    val jugador: Jugador,
    val partida: Int,
    val casillaActual: Int,
    val jugadorActual: Boolean,
    val juegos: List<Boolean>
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


