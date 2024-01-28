package com.example.trivialnavidad.core.feature.clasificacion.viewModel

import android.content.Context

/**
 * Interfaz para comunicar los datos desde un fragmento a otro
 */
interface ComunicadorClasificacion {
    fun volver(context: Context)
    fun inicio(contexto: Context)
}
