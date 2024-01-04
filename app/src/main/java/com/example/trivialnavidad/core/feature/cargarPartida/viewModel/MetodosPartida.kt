package com.example.trivialnavidad.core.feature.cargarPartida.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego

class MetodosPartida(val context: Context): ComunicadorPartida {
    override fun cargarPartida(id: Int ) {
        if (context is AppCompatActivity) {
            MainActivity.juego = Juego(id)
            val juego = MainActivity.juego as Juego
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }
}