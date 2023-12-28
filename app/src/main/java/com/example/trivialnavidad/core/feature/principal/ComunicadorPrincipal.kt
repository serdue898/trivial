package com.example.trivialnavidad.core.feature.principal

import android.content.Context

/**
 * Interfaz para comunicar los datos desde un fragmento a otro
 */
interface ComunicadorPrincipal {
    fun abrir_juego(datos: String,context: Context)
}
