package com.example.trivialnavidad.core.feature.minijuegos.test.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.clasificacion.view.Clasifiaccion
import com.example.trivialnavidad.core.feature.juego.view.Juego

class MetodosTest : ComunicadorTest {
    override fun volver(context: Context, ganado: Boolean, final: Boolean,jugadorEnPartida: JugadorEnPartida,tipo:String) {
        if (context is AppCompatActivity) {
            var fragment :Fragment? = null
            if (final && ganado) {
                if (tipo=="online") {
                    val socket = MainActivity.socket
                    socket?.emit("partidaGanada", jugadorEnPartida)
                }else{

                            val jugadores =
                                MainActivity.juego?.jugadoresEnPartida as List<JugadorEnPartida>
                            fragment = Clasifiaccion(jugadores, true)
                            val conexion = Conexion(context)
                            val partida =
                                conexion.obtenerPartida(MainActivity.juego?.partidaActual!!)
                            partida.finalizada = true
                            conexion.actualizarPartida(partida)

                    }
            } else {
                fragment = MainActivity.juego as Juego
                fragment.resultadoMiniJuego(ganado)
            }
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, fragment!!)
                .commit()

        }

    }
}