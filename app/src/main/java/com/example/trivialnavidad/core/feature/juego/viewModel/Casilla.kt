package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.widget.GridLayout
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class Casilla(contexto: Context, val fila: Int, val columna: Int ) : GridLayout(contexto) {
    var jugadores :List<JugadorEnPartida> = listOf()
    var dificultad : Int = 0
    var color : Int= 0
    fun addJugador(jugador: JugadorEnPartida){
        jugadores = jugadores.plus(jugador)
    }
    fun removeJugador(jugador: JugadorEnPartida){
        jugadores = jugadores.minus(jugador)
    }
}