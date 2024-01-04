package com.example.trivialnavidad.core.feature.principal.viewModel

import Partidas
import android.content.Context
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.juego.view.Juego
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.app.MainActivity


class MetodosPrincipal: ComunicadorPrincipal {

    override fun abrir_juego(datos: String, context: Context) {
        if (context is AppCompatActivity) {
            if (datos == "cargar") {
                val partidas = Partidas()
                val fragmentManager = context.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contenedor, partidas)
                    .commit()
            } else if (datos == "nueva") {
                // Código para el caso "online", si es necesario
            }
        }
    }
}