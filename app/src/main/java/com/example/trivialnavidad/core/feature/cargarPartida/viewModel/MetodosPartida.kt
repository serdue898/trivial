package com.example.trivialnavidad.core.feature.cargarPartida.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida
import com.example.trivialnavidad.core.feature.clasificacion.view.Clasifiaccion
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal
import com.example.trivialnavidad.core.feature.unirseOnline.view.UnirseOnline

/**
 * Clase MetodosPartida: Implementa la interfaz ComunicadorPartida para gestionar la carga de partidas y la navegación
 * entre fragmentos relacionados con las partidas en la aplicación Trivial Navidad.
 *
 * @property context Contexto de la aplicación.
 */
class MetodosPartida(val context: Context) : ComunicadorPartida {

    /**
     * Carga la partida correspondiente según su estado y muestra el fragmento correspondiente.
     *
     * @param partida Partida a cargar.
     */
    override fun cargarPartida(partida: Partida) {
        val fragment: Fragment
        if (context is AppCompatActivity) {
            if (partida.finalizada) {
                val conexion = Conexion(context)
                val jugadores = conexion.obtenerJugadoresEnPartida(partida.idPartida).sortedBy { it.puntosJugador() }
                fragment = Clasifiaccion(jugadores, true)
            } else {
                MainActivity.juego = Juego()
                MainActivity.juego?.partida = partida.idPartida
                fragment = MainActivity.juego as Juego
            }
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, fragment)
                .commit()
        }
    }

    /**
     * Vuelve al fragmento principal de la aplicación.
     */
    override fun volver() {
        if (context is AppCompatActivity) {
            val inicio = Principal()
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, inicio)
                .commit()
        }
    }

    /**
     * Inicia el proceso para unirse a una partida en línea.
     *
     * @param idPartida Identificador de la partida a unirse.
     * @param contexto Contexto de la aplicación.
     */
    override fun unirseAPartida(idPartida: Int, contexto: Context) {
        if (context is AppCompatActivity) {
            val unirse = UnirseOnline(idPartida)
            val fragmentManager = context.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor, unirse)
                .commit()
        }
    }
}