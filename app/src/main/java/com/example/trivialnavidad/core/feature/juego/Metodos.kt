package com.example.trivialnavidad.core.feature.juego

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.clasificacion.Clasifiaccion

class Metodos : ComunicadorJuego{
    override fun abrirClasificacion (jugadores: List<JugadorEnPartida>, context :Context){


        if (context is AppCompatActivity) {
            val clasificacion = Clasifiaccion()
            clasificacion.actualizarLista(jugadores)
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, clasificacion)
                .commit()

        }


    }
}