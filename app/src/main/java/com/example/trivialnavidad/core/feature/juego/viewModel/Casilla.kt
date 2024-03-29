package com.example.trivialnavidad.core.feature.juego.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.widget.GridLayout
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

@SuppressLint("ViewConstructor")
class Casilla(contexto: Context, val fila: Int, val columna: Int ) : GridLayout(contexto) {
    var jugadores :MutableList<JugadorEnPartida> = mutableListOf()
    var dificultad : Int = 0
    var color : Int= 0
    fun addJugador(jugador: JugadorEnPartida) :Boolean{
        val posicion = jugadores.indexOfFirst { it.id_jugador == id }
        if (posicion == -1) {
            jugadores.add(jugador)
            return true
        }
        return false
    }
    fun removeJugador(jugador: JugadorEnPartida){
         jugadores.remove(jugador)
    }
    fun removeJugador(id: Int): Boolean{
        val posicion = jugadores.indexOfFirst { it.id_jugador == id }
        if (posicion != -1) {
            jugadores.removeAt(posicion)
            return true
        }
        return false




    }

}