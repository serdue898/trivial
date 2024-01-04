package com.example.trivialnavidad.core.feature.seleccionJugadores.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego

class MetodosSeleccion {
    fun empezarPartida(contexto: Context,id_partida: Int) {
        if (contexto is AppCompatActivity) {
            val juego = Juego(id_partida)
            MainActivity.juego = juego
            val fragmentManager = contexto.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }
}