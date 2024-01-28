package com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego

class MetodosAdivina :ComunicadorAdivina{
    override fun volver( context: Context , ganado: Boolean) {
        if (context is AppCompatActivity) {
            val juego = MainActivity.juego as Juego
            juego.resultadoMiniJuego(ganado)
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }
}