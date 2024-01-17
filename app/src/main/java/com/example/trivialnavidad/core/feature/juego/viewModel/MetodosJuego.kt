package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.clasificacion.view.Clasifiaccion
import com.example.trivialnavidad.core.feature.principal.view.Principal

class MetodosJuego : ComunicadorJuego {
    override fun abrirClasificacion (jugadores: List<JugadorEnPartida>, context :Context){


        if (context is AppCompatActivity) {
            val clasificacion = Clasifiaccion(jugadores.sortedBy { it.puntosJugador() },false)
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, clasificacion)
                .commit()

        }


    }
    override fun salir(context: Context) {
        if (context is AppCompatActivity) {
            val inicio = Principal()
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, inicio)
                .commit()
        }
    }
}