package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.widget.FrameLayout
import android.widget.GridLayout
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class Casilla( contexto: Context, val posicionX: Int, val posicionY: Int ) : GridLayout(contexto) {
    var jugadores :List<JugadorEnPartida> = listOf()
    var dificultad : Int = 0

}