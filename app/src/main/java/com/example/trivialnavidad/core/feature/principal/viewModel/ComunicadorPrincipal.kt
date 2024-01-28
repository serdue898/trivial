package com.example.trivialnavidad.core.feature.principal.viewModel

import android.content.Context

/**
 * Interfaz para comunicar los datos desde un fragmento a otro
 */
interface ComunicadorPrincipal {
    fun abrirJuego(datos: String, context: Context)
}
