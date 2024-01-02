package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

/**
 * Interfaz para comunicar los datos desde un fragmento a otro
 */
interface ComunicadorJuego {
    fun abrirClasificacion(jugadores: List<JugadorEnPartida>, context : Context)
}
