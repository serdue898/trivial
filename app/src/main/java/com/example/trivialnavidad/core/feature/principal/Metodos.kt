package com.example.trivialnavidad.core.feature.principal

import android.content.Context
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.juego.Juego
import androidx.appcompat.app.AppCompatActivity


class Metodos: ComunicadorPrincipal {

    override fun abrir_juego(datos: String, context: Context) {
        if (context is AppCompatActivity) {
            if (datos == "offline") {
                val juego = Juego()
                val fragmentManager = context.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contenedor, juego)
                    .commit()
            } else if (datos == "online") {
                // Código para el caso "online", si es necesario
            }
        }
    }
}