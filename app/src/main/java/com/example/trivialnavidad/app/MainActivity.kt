package com.example.trivialnavidad.app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.BaseDatos
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal

class MainActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var juego=null as Juego?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        BaseDatos(this)
        var conexion = Conexion(this)
        /*
        val juegos = MutableList(4){i -> false}
        var jug1 = Jugador(1,"prueba","dado1")
        var jug2 = Jugador(2,"prueba2","dado1")

        conexion.agregarJugador(jug1)
        conexion.agregarJugador(jug2)
        var partida = Partida(1,"partida1")
        conexion.agregarPartida(partida)
        var jugador1 = JugadorEnPartida(jug1,1,"1_1",false,juegos,"dado1")
        conexion.agregarJugadorEnPartida(jugador1)
        var jugador2 = JugadorEnPartida(jug2,1,"1_1",false,juegos,"dado2")
        conexion.agregarJugadorEnPartida(jugador2)
        */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            var principal = Principal()
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }

    }



}