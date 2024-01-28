package com.example.trivialnavidad.core.feature.minijuegos.test.viewmodel

import android.content.Context
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

interface ComunicadorTest {
    fun volver(context: Context, ganado: Boolean,final:Boolean,jugadorEnPartida: JugadorEnPartida,tipo:String)
}