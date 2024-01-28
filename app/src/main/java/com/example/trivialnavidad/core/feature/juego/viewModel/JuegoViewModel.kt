package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class JuegoViewModel: ViewModel() {
    var partida: Int? = null
    var jugador: Int = 0
    var cargar: Boolean = false
    var jugadorActual: JugadorEnPartida? = null
    var jugadoresEnPartida: List<JugadorEnPartida> = emptyList()
    var partidaActual: Int = 1
    private lateinit var metodosTablero: Tablero

    // Agrega otras variables y lógica relacionada con el juego según sea necesario

    fun inicializarJuego(contexto: Context) {
        // Lógica para inicializar el juego, obtener datos, etc.
        val conexion = Conexion(contexto)
        if (partida != null) partidaActual = partida!!
        jugadoresEnPartida = conexion.obtenerJugadoresEnPartida(partidaActual)
        jugador = jugadoresEnPartida.indexOf(jugadoresEnPartida.find { it.jugadorActual == true })
        // Agrega otras inicializaciones según sea necesario
    }
}