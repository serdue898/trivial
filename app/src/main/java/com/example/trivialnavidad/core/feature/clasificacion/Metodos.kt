package com.example.trivialnavidad.core.feature.clasificacion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.Juego

class Metodos: Comunicador {
    override fun volver( context: Context) {
        if (context is AppCompatActivity) {
            val juego = MainActivity.juego as Juego
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }
}