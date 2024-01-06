package com.example.trivialnavidad.core.feature.cargarPartida.viewModel

import android.content.Context
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida

/**
 * Interfaz para comunicar los datos desde un fragmento a otro
 */
interface ComunicadorPartida {
    fun cargarPartida(partida: Partida)
    fun volver( )
}
