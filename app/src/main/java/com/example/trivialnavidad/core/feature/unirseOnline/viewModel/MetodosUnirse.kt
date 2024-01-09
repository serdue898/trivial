package com.example.trivialnavidad.core.feature.unirseOnline.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal

class MetodosUnirse {
    fun empezarPartida(contexto: Context,id_partida: Int) {
        if (contexto is AppCompatActivity) {
            val juego = Juego()
            juego.partida = id_partida

            MainActivity.juego = juego
            val fragmentManager = contexto.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }

    fun volver(contexto: Context) {
        if (contexto is AppCompatActivity) {
            val principal = Principal()
            val fragmentManager = contexto.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }

    }
}