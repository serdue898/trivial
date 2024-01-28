package com.example.trivialnavidad.core.feature.minijuegos.repaso.viewModel

import android.content.Context

interface ComunicadorRepaso {
    fun volver(context: Context, ganado: Boolean)
}