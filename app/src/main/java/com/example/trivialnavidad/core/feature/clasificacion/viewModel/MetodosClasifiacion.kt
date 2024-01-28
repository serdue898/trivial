package com.example.trivialnavidad.core.feature.clasificacion.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal

class MetodosClasifiacion: ComunicadorClasificacion {
    override fun volver( context: Context) {
        if (context is AppCompatActivity) {
            val juego = MainActivity.juego as Juego
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, juego)
                .commit()
        }

    }
    override fun inicio(contexto: Context) {
        if (contexto is AppCompatActivity) {
            val principal = Principal()
            val fragmentManager = contexto.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }
    }
}