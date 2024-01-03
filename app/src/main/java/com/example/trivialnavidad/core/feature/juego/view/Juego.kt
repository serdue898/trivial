package com.example.trivialnavidad.core.feature.juego.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.juego.viewModel.ComunicadorJuego
import com.example.trivialnavidad.core.feature.juego.viewModel.Dado
import com.example.trivialnavidad.core.feature.juego.viewModel.MetodosJuego
import com.example.trivialnavidad.core.feature.juego.viewModel.Tablero
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Juego : Fragment() {
    private var comunicador: ComunicadorJuego? = MetodosJuego()
    private var contexto: Context? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.juego, container, false)
        contexto = container?.context
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar2)
        val tablero = view.findViewById<GridLayout>(R.id.gr_tablero)
        val bt_clasificacion = view.findViewById<Button>(R.id.bt_clasificacion)
        val bt_dado = view.findViewById<Button>(R.id.bt_dado)
        val dado = view.findViewById<ImageView>(R.id.dado)

        val conexion = Conexion(contexto!!)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val handler = Dado(dado)
        var jugadoresEnPartida = conexion.obtenerJugadoresEnPartida(1)
        val metodosTablero=Tablero(tablero,contexto!!,jugadoresEnPartida)
        metodosTablero.crearTablero()
        metodosTablero.asignarJugadores()

        bt_dado.setOnClickListener {
            GlobalScope.launch {
                // Llama a la función y obtén el último número aleatorio
                val movimientos = handler.cambiarImagenCadaSegundo()
                metodosTablero.moverJugador(jugadoresEnPartida[0],movimientos.toString().toInt())

            }


        }
        bt_clasificacion.setOnClickListener {
            comunicador?.abrirClasificacion(jugadoresEnPartida, contexto!!)
        }
        return view
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            val inflater: MenuInflater = (contexto as AppCompatActivity).menuInflater
            inflater.inflate(R.menu.menu_view, menu)// OJO- se pasa la vista que se quiere inflar

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.mItm_juegoNuevo -> {
                //juegoNuevo()
                true
            }

            R.id.mItm_guardar -> {
                //guardarDatos()
                true
            }

            R.id.mItm_instrucciones -> {
                verInstrucciones()
                true
            }

            R.id.mItm_acerca -> {
                acercaDe()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun verInstrucciones() {
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        val acercaDe = R.string.intrucciones
        msnEmergente.setMessage(getString(R.string.intrucciones))

        msnEmergente.show()
    }

    private fun acercaDe() {
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        val acercaDe = R.string.acercaDe
        msnEmergente.setMessage(getString(R.string.acercaDe))
        msnEmergente.show()
    }




}