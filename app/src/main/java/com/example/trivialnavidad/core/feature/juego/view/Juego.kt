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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.juego.viewModel.ComunicadorJuego
import com.example.trivialnavidad.core.feature.juego.viewModel.Dado
import com.example.trivialnavidad.core.feature.juego.viewModel.MetodosJuego
import com.example.trivialnavidad.core.feature.juego.viewModel.Tablero
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Juego : Fragment() {
    private var comunicador: ComunicadorJuego? = MetodosJuego()
    private var contexto: Context? = null
    private var jugador:Int = 0
    private var jugadorActual : JugadorEnPartida? = null
    private var jugadoresEnPartida = listOf<JugadorEnPartida>()
    private lateinit var metodosTablero: Tablero


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.juego, container, false)
        contexto = container?.context
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar2)
        val tablero = view.findViewById<GridLayout>(R.id.gr_tablero)
        val bt_clasificacion = view.findViewById<Button>(R.id.bt_clasificacion)
        val bt_dado = view.findViewById<Button>(R.id.bt_dado)


        val conexion = Conexion(contexto!!)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)


        jugadoresEnPartida = conexion.obtenerJugadoresEnPartida(1)
         metodosTablero=Tablero(tablero,contexto!!,jugadoresEnPartida)
        metodosTablero.crearTablero()
        metodosTablero.asignarJugadores()


        bt_dado.setOnClickListener {
            bt_dado.isEnabled = false
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    tirarDado()
                }}


        }
        bt_clasificacion.setOnClickListener {
            comunicador?.abrirClasificacion(jugadoresEnPartida, contexto!!)
        }
        return view
    }
    fun tirada(movimientos: Int, alertDialog: AlertDialog){
        alertDialog.dismiss()
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                jugadorActual = jugadoresEnPartida[jugador]
                metodosTablero.moverJugador(jugadorActual!!, movimientos.toString().toInt())
            }

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jugadorActual = jugadoresEnPartida[jugador]
        actualizarJugador(jugadorActual)

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
     fun actualizarJugador( jugador: JugadorEnPartida? ){
        val nombre = view?.findViewById<TextView>(R.id.t_tunroJugador)
        val avatar = view?.findViewById<ImageView>(R.id.i_avatar)

            nombre?.text = "turno del jugador:"+jugador?.jugador?.nombre

        val jugadorAvatar = ImageView(contexto)
        val resourceId = contexto!!.resources.getIdentifier(
            jugador!!.jugador.avatar,
            "drawable",
            contexto!!.packageName
        )
        jugadorAvatar.setImageResource(resourceId)
        avatar?.setImageDrawable(jugadorAvatar.drawable)
        // Llama a la función y obtén el último número aleatorio

    }
    suspend fun tirarDado(){
        val view = LayoutInflater.from(contexto).inflate(R.layout.dado_lyout, null)
        val dado = view.findViewById<ImageView>(R.id.dado)
        val boton = view.findViewById<Button>(R.id.bt_salir)

        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setView(view)
        msnEmergente.setTitle("Tira el dado")
        val construido = msnEmergente.create()
        construido.show()

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val movimientos = Dado(dado).cambiarImagenCadaSegundo(view)
                boton.setOnClickListener {
                    tirada(movimientos,construido)
                }
            }
        }
    }
    fun resultadoMiniJuego(ganado :Boolean){
        val bt_dado = view?.findViewById<Button>(R.id.bt_dado)
        bt_dado?.isEnabled = true
        if (!ganado){
            jugador++
            val jugadorActual = jugadoresEnPartida[jugador]
            actualizarJugador(jugadorActual)

        }

    }




}