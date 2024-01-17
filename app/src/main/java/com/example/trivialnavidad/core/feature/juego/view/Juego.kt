package com.example.trivialnavidad.core.feature.juego.view

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.app.Reproductor
import com.example.trivialnavidad.app.Vibracion
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.juego.viewModel.ComunicadorJuego
import com.example.trivialnavidad.core.feature.juego.viewModel.Dado
import com.example.trivialnavidad.core.feature.juego.viewModel.MetodosJuego
import com.example.trivialnavidad.core.feature.juego.viewModel.Tablero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Juego : Fragment() {

    private var comunicador: ComunicadorJuego? = MetodosJuego()
    private var contexto: Context? = null
    private var vista : View? = null
    var partida: Int? = null
    private var jugador: Int = 0
    private var cargar: Boolean = false
    private var jugadorActual: JugadorEnPartida? = null
    var jugadoresEnPartida: MutableList<JugadorEnPartida> = mutableListOf<JugadorEnPartida>()
    var partidaActual: Int = 1
    private lateinit var metodosTablero: Tablero
    private var avatarImages : TypedArray? = null
    var tipo : String = "offline"
    private val socket = MainActivity.socket
    private var casillaTemp :String = ""
    private var jugadorTemp :JugadorEnPartida? = null
    private var cargarOnline : Boolean = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View?
        contexto = container?.context
        avatarImages  = contexto?.resources!!.obtainTypedArray(R.array.avatar_images)
        val conexion = Conexion(contexto!!)
        if (vista == null){
            vista = inflater.inflate(R.layout.juego, container, false)
            view = vista
            if (partida != null) partidaActual = partida!!
            if (tipo == "offline")jugadoresEnPartida = conexion.obtenerJugadoresEnPartida(partidaActual).toMutableList()

            jugador = jugadoresEnPartida.indexOf(jugadoresEnPartida.find { it.jugadorActual })
            jugadorActual = jugadoresEnPartida[jugador]
            val tablero = view?.findViewById<GridLayout>(R.id.gr_tablero)
            metodosTablero=Tablero(tablero!!,contexto!!,jugadoresEnPartida,tipo)
            metodosTablero.crearTablero()
            metodosTablero.asignarJugadores()

        }
        view = vista


        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar2)
        val clasificacion = view?.findViewById<Button>(R.id.bt_clasificacion)
        val dado = view?.findViewById<Button>(R.id.bt_dado)
        if (tipo == "online" && jugadorActual?.id_jugador != MainActivity.jugadorActual?.id_jugador) dado?.isEnabled = false


        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Default) {
                socket?.on("moverJugadorOnline") { args ->
                    Log.d("DEBUG", "Evento moverJugadorOnline recibido")
                    val jugadorjson = args[0] as JSONObject
                    // Manejar la lista de jugadores en tu aplicación Android
                    // Por ejemplo, puedes actualizar la interfaz de usuario con la nueva lista
                    val jugador = JugadorEnPartida.fromJson(jugadorjson.toString())
                    jugador.jugador = jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador }?.jugador
                actualizarJugadorOnline(jugador)
            }

        }

        dado?.setOnClickListener {
            dado.isEnabled = false
            (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Default) {
                withContext(Dispatchers.Main) {
                    tirarDado()
                }}


        }
        clasificacion?.setOnClickListener {
            comunicador?.abrirClasificacion(jugadoresEnPartida, contexto!!)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jugadorActual = jugadoresEnPartida[jugador]
        actualizarJugador(jugadorActual)
        if (cargar){
            val dado = view.findViewById<Button>(R.id.bt_dado)
            dado?.isEnabled = true
            cargar = false
        }
        if (jugadorTemp != null && cargarOnline){
            actualizarJugadorOnline(jugadorTemp!!)
            jugadorTemp = null
            cargarOnline = false
        }else if (jugadorTemp != null ){
            actualizarJugadorOnline(jugadorTemp!!)
            jugadorTemp = null
        }
    }
    fun actualizarJugadorOnline(jugador: JugadorEnPartida){
        if (view == null) {
            jugadorTemp = jugador
            return
        }
        val dado = view?.findViewById<Button>(R.id.bt_dado)
        val posicionAntigua = jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador }?.casillaActual

        if (jugador.partida == partidaActual) {


            activity?.runOnUiThread {
                jugadoresEnPartida[jugadoresEnPartida.indexOf(jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador })] =
                    jugador
                if (!jugador.jugadorActual) {
                    if (jugadorActual?.id_jugador == jugador.id_jugador) {
                        this.jugador++
                        if (this.jugador >= jugadoresEnPartida.size) {
                            this.jugador = 0
                        }
                        val jugadorNuevo = jugadoresEnPartida[this.jugador]
                        jugadorNuevo.jugadorActual = true
                        jugadorActual = jugadorNuevo
                    }

                } else {
                    jugadorActual = jugador
                }
                if (jugador.id_jugador == MainActivity.jugadorActual?.id_jugador) {
                    metodosTablero.moverOnline(jugador, casillaTemp)
                } else {
                    metodosTablero.moverOnline(jugador, posicionAntigua!!)
                }
                dado?.isEnabled =
                    jugadorActual?.id_jugador == MainActivity.jugadorActual?.id_jugador
                actualizarJugador(jugadorActual)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.menu_juego_view, menu)// OJO- se pasa la vista que se quiere inflar

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.mItm_GuardarSalir -> {
                guardarDatos()
                comunicador?.salir(contexto!!)
                true
            }

            R.id.mItm_guardar -> {
                guardarDatos()
                true
            }
            R.id.mItm_configuracion -> {
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!)
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



    private fun guardarDatos() {
        val conexion = Conexion(contexto!!)
        for (jugador in jugadoresEnPartida){
            conexion.actualizarCasillaActual(jugador)
        }
        Toast.makeText(contexto, contexto?.getString(R.string.datos_guardados), Toast.LENGTH_SHORT).show()
    }

    private fun verInstrucciones() {
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setMessage(contexto?.getString(R.string.intrucciones))

        msnEmergente.show()
    }
    private fun acercaDe() {
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setMessage(contexto?.getString(R.string.acercaDe))
        msnEmergente.show()
    }

    private fun actualizarJugador( jugador: JugadorEnPartida? ){
        val nombre = view?.findViewById<TextView>(R.id.t_tunroJugador)
        val avatar = view?.findViewById<ImageView>(R.id.i_avatar)
        val texto =contexto?.getString(R.string.turno_del_jugador) +jugador?.jugador?.nombre
        nombre?.text = texto
        avatar?.setImageDrawable(avatarImages?.getDrawable(jugador?.avatar!!.toInt()))
        // Llama a la función y obtén el último número aleatorio

    }
    private suspend fun tirarDado(){
        val view = LayoutInflater.from(contexto).inflate(R.layout.dado_lyout, null)
        val dado = view.findViewById<ImageView>(R.id.dado)
        val boton = view.findViewById<Button>(R.id.bt_salir)
        casillaTemp = jugadorActual?.casillaActual!!

        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setView(view)
        msnEmergente.setTitle(contexto?.getString(R.string.tira_el_dado))
        val construido = msnEmergente.create()
        construido.show()

        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                var vibracion = null as Vibracion?
                val sonido = null as Reproductor?
                if (MainActivity.configuracion?.obtenerOpcionVibracion()!!) vibracion = Vibracion(contexto!!)
                //if (MainActivity.configuracion?.obtenerOpcionSonido()!!) sonido = Reproductor(contexto!!,R.raw.lobby_music)
                val movimientos = Dado(dado,vibracion,sonido).cambiarImagenCadaSegundo(view)
                boton.setOnClickListener {
                    tirada(movimientos,construido)
                }
            }
        }
    }
    private fun tirada(movimientos: Int, alertDialog: AlertDialog){
        alertDialog.dismiss()
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                if (tipo=="offline")jugadorActual = jugadoresEnPartida[jugador]
                metodosTablero.moverJugador(jugadorActual!!, movimientos.toString().toInt())
            }
        }
    }
    fun resultadoMiniJuego(ganado :Boolean){
        val conexion = Conexion(contexto!!)
        val jugadorActual = jugadoresEnPartida[jugador]
        val dado = view?.findViewById<Button>(R.id.bt_dado)
        cargarOnline = true
        if(tipo=="offline") dado?.isEnabled = true
        if (!ganado){
            jugadorActual.jugadorActual = false
            if (tipo=="offline") {
                jugador++
                if (jugador >= jugadoresEnPartida.size) {
                    jugador = 0
                }
                val jugadorNuevo = jugadoresEnPartida[jugador]
                actualizarJugador(jugadorNuevo)
                jugadorNuevo.jugadorActual = true
                conexion.actualizarCasillaActual(jugadorNuevo)
            }
        }
        cargar = true
        if (tipo == "online"){
            socket?.emit("moverJugador",jugadorActual.toJson())
        }
        if(tipo=="offline")conexion.actualizarCasillaActual(jugadorActual)



    }
}