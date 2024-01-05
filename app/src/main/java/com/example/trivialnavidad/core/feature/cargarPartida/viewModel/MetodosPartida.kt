package com.example.trivialnavidad.core.feature.cargarPartida.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal

class MetodosPartida(val context: Context): ComunicadorPartida {
    override fun cargarPartida(id: Int ) {
        if (context is AppCompatActivity) {
            MainActivity.juego = Juego()
            MainActivity.juego?.partida = id
            val juego = MainActivity.juego as Juego
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }
    override fun volver( ) {
        if (context is AppCompatActivity) {
            val inicio = Principal()
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, inicio)
                .commit()
        }

    }

}