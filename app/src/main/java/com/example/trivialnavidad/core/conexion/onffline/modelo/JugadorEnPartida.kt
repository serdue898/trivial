package com.example.trivialnavidad.core.conexion.onffline.modelo

data class JugadorEnPartida(
    val jugador: Jugador,
    val partida: Int,
    val casillaActual: Int,
    val jugadorActual: Boolean
)
