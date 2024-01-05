package com.example.trivialnavidad.core.feature.cargarPartida.viewModel

import android.content.Context

/**
 * Interfaz para comunicar los datos desde un fragmento a otro
 */
interface ComunicadorPartida {
    fun cargarPartida(id: Int)
    fun volver( )
}
