package com.example.trivialnavidad.app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal
import io.socket.client.Socket


class MainActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var juego=null as Juego?
        var configuracion = null as Configuracion?
        @SuppressLint("StaticFieldLeak")
        var reproductor = null as Reproductor?
        var socket: Socket? = null
        var jugadorActual = null as Jugador?

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        configuracion = Configuracion(this)
        reproductor = Reproductor(this, R.raw.lobby_music)
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main) // Reemplaza con tu diseño

        if (savedInstanceState == null) {
            var principal = Principal()
            principal.empezar()
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos cuando la actividad se destruye
        reproductor?.liberarRecursos()
    }




}