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
/**
 * Clase Juego: Fragmento que representa la interfaz principal del juego Trivial, mostrando el tablero,
 * la información del jugador actual y permitiendo interactuar con el dado para avanzar en el juego.
 *
 * @property comunicador Objeto que implementa la interfaz ComunicadorJuego para gestionar las acciones relacionadas con el juego.
 * @property contexto Contexto de la aplicación.
 * @property vista Vista del fragmento.
 * @property partida Identificador de la partida en curso.
 * @property jugador Índice del jugador actual en la lista de jugadores en la partida.
 * @property cargar Indica si se debe cargar la partida.
 * @property jugadorActual Información del jugador actual.
 * @property jugadoresEnPartida Lista de jugadores en la partida.
 * @property partidaActual Identificador de la partida actual.
 * @property metodosTablero Objeto para gestionar las interacciones con el tablero de juego.
 * @property avatarImages Recurso de imágenes para los avatares de los jugadores.
 * @property tipo Tipo de partida ("offline" u "online").
 * @property socket Objeto para la comunicación con el servidor en partidas online.
 * @property casillaTemp Almacena temporalmente la casilla actual del jugador.
 * @property jugadorTemp Almacena temporalmente la información del jugador.
 * @property cargarOnline Indica si se debe cargar la partida online.
 */
class Juego : Fragment() {

    private var comunicador: ComunicadorJuego? = MetodosJuego()
    private var contexto: Context? = null
    private var vista : View? = null
    var partida: Int? = null
    private var jugador: Int = 0
    private var cargar: Boolean = false
    private var jugadorActual: JugadorEnPartida? = null
    var jugadoresEnPartida: MutableList<JugadorEnPartida> = mutableListOf()
    var partidaActual: Int = 1
    private lateinit var metodosTablero: Tablero
    private var avatarImages : TypedArray? = null
    var tipo : String = "offline"
    private val socket = MainActivity.socket
    private var casillaTemp :String = ""
    private var jugadorTemp :JugadorEnPartida? = null
    private var cargarOnline : Boolean = false




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View?
        contexto = container?.context
        avatarImages = contexto?.resources!!.obtainTypedArray(R.array.avatar_images)
        val conexion = Conexion(contexto!!)

        // Si la vista es nula, crea la interfaz del juego y asigna jugadores.
        if (vista == null) {
            MainActivity.jugadorActual?.partida = partida!!
            vista = inflater.inflate(R.layout.juego, container, false)
            view = vista
            if (partida != null) partidaActual = partida!!

            // Si es una partida offline, carga la lista de jugadores en la partida.
            if (tipo == "offline") jugadoresEnPartida = conexion.obtenerJugadoresEnPartida(partidaActual).toMutableList()

            jugador = jugadoresEnPartida.indexOf(jugadoresEnPartida.find { it.jugadorActual })
            jugadorActual = jugadoresEnPartida[jugador]

            val tablero = view?.findViewById<GridLayout>(R.id.gr_tablero)
            metodosTablero = Tablero(tablero!!, contexto!!, jugadoresEnPartida, tipo)
            metodosTablero.crearTablero()
            metodosTablero.asignarJugadores()
        }
        view = vista

        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar2)
        val clasificacion = view?.findViewById<Button>(R.id.bt_clasificacion)
        val dado = view?.findViewById<Button>(R.id.bt_dado)

        // Si es una partida online y no es el turno del jugador actual, deshabilita el botón del dado.
        if (tipo == "online" && jugadorActual?.id_jugador != MainActivity.jugadorActual?.id_jugador) dado?.isEnabled = false

        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null

        // Configura el listener para el evento "moverJugadorOnline" del socket.
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
            socket?.on("moverJugadorOnline") { args ->
                Log.d("DEBUG", "Evento moverJugadorOnline recibido")
                val jugadorjson = args[0]

                // Maneja la actualización del jugador en la interfaz gráfica.
                val jugador = JugadorEnPartida.fromJson(jugadorjson.toString())
                jugador.jugador = jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador }?.jugador
                actualizarJugadorOnline(jugador)
            }
        }

        // Configura el listener para el evento "ganador" del socket.
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                socket?.on("ganador") { args ->
                    val jugadorjson = args[0]
                    val jugador = JugadorEnPartida.fromJson(jugadorjson.toString())
                    jugador.jugador = jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador }?.jugador

                    // Muestra un popup informando del ganador y permite salir de la partida.
                    val popup = AlertDialog.Builder(contexto as AppCompatActivity)
                    popup.setTitle(contexto?.getString(R.string.partida_ganada))
                    popup.setCancelable(false)
                    popup.setMessage(contexto?.getString(R.string.jugador) + jugador.jugador?.nombre + contexto?.getString(
                        R.string.ha_ganado_la_partida
                    ))
                    popup.setPositiveButton(contexto?.getString(R.string.aceptar)) { dialog, _ ->
                        MainActivity.jugadorActual?.partida = 0
                        socket.emit("desloggear", jugadorActual?.toJson())
                        comunicador?.salir(contexto!!)
                        dialog.dismiss()
                    }
                    (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            popup.show()
                        }
                    }
                }
            }
        }

        // Configura el listener para el botón del dado.
        dado?.setOnClickListener {
            dado.isEnabled = false
            (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
                withContext(Dispatchers.Main) {
                    tirarDado()
                }
            }
        }

        // Configura el listener para el botón de clasificación.
        clasificacion?.setOnClickListener {
            comunicador?.abrirClasificacion(jugadoresEnPartida, contexto!!)
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (tipo == "offline") {
            jugadorActual = jugadoresEnPartida[jugador]
        }
        actualizarJugador(jugadorActual)
        if (cargar && tipo == "offline"){
            val dado = view.findViewById<Button>(R.id.bt_dado)
            dado?.isEnabled = true
            cargar = false
        }
        if (jugadorTemp != null){
            metodosTablero.actualizartablero(jugadoresEnPartida)
            cargarOnline = true
            actualizarJugadorOnline(jugadorTemp!!)
            jugadorTemp = null

        }
    }
    /**
     * Método actualizarJugadorOnline: Actualiza la información del jugador en el juego cuando se recibe una actualización
     * del estado del jugador desde el servidor en partidas online.
     *
     * @param jugador Objeto JugadorEnPartida que contiene la información actualizada del jugador.
     */
    private fun actualizarJugadorOnline(jugador: JugadorEnPartida) {
        // Si hay una actualización pendiente y no se debe cargar la partida online, retorna.
        if (jugadorTemp != null && !cargarOnline) {
            return
        }

        // Si la vista es nula, almacena temporalmente la información del jugador.
        if (view == null) {
            jugadorTemp = jugador
            return
        }

        cargarOnline = false
        val dado = view?.findViewById<Button>(R.id.bt_dado)

        // Obtiene la posición antigua del jugador.
        val posicionAntigua = jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador }?.casillaActual

        // Ejecuta la actualización en el hilo principal de la interfaz de usuario.
        activity?.runOnUiThread {
            // Si el jugador es el actual, realiza la animación de movimiento en el tablero.
            if (jugador.id_jugador == jugadorActual?.id_jugador) {
                metodosTablero.moverOnline(jugador, posicionAntigua!!)
            } else {
                jugadorActual = jugador
            }

            // Actualiza la lista de jugadores en partida y el tablero.
            jugadoresEnPartida[jugadoresEnPartida.indexOf(jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador })] = jugador
            metodosTablero.actualizartablero(jugadoresEnPartida)

            // Habilita el botón del dado solo si es el turno del jugador local.
            dado?.isEnabled = jugadorActual?.id_jugador == MainActivity.jugadorActual?.id_jugador

            // Actualiza la información del jugador actual en la interfaz.
            actualizarJugador(jugadorActual)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.menu_juego_view, menu)
            if (tipo == "online"){
                val item = menu.findItem(R.id.mItm_guardar)
                item.isVisible = false
                val item2 = menu.findItem(R.id.mItm_GuardarSalir)
                item2.title = "Salir"
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.mItm_GuardarSalir -> {
                if (tipo == "online"){
                    socket?.emit("desconectar",jugadorActual?.toJson())
                }else {
                    guardarDatos()
                }
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
    /**
     * Método tirarDado: Realiza la animación de tirada de dado, mostrando un diálogo con la animación del dado
     * y permitiendo al jugador lanzar el dado para avanzar en el juego.
     */
    private suspend fun tirarDado() {
        // Infla la vista del diálogo del dado.
        val view = LayoutInflater.from(contexto).inflate(R.layout.dado_lyout, null)

        // Obtiene las referencias a la imagen del dado y al botón de salir del diálogo.
        val dado = view.findViewById<ImageView>(R.id.dado)
        val boton = view.findViewById<Button>(R.id.bt_salir)

        // Almacena temporalmente la casilla actual del jugador.
        casillaTemp = jugadorActual?.casillaActual!!

        // Crea el diálogo de tirada de dado.
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setView(view)
        msnEmergente.setTitle(contexto?.getString(R.string.tira_el_dado))
        val construido = msnEmergente.create()
        construido.show()

        // Utiliza un coroutine para manejar las operaciones asíncronas en el hilo principal de la interfaz de usuario.
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                var vibracion = null as Vibracion?
                var sonido = null as Reproductor?

                // Verifica y crea instancias de Vibracion y Reproductor según las preferencias del usuario.
                if (MainActivity.configuracion?.obtenerOpcionVibracion()!!) {
                    vibracion = Vibracion(contexto!!)
                }
                if (MainActivity.configuracion?.obtenerOpcionSonido()!!) {
                    sonido = Reproductor(contexto!!, R.raw.tirada)
                }

                // Obtiene el número de movimientos resultantes de la tirada de dado.
                val movimientos = Dado(contexto as AppCompatActivity, dado, vibracion, sonido).cambiarImagenCadaSegundo(view)

                // Asigna el evento click al botón de salir para procesar la tirada.
                boton.setOnClickListener {
                    tirada(movimientos, construido)
                }
            }
        }
    }

    /**
     * Método tirada: Realiza las acciones necesarias después de realizar una tirada de dado, como actualizar la posición
     * del jugador en el tablero y gestionar el turno.
     *
     * @param movimientos Número de movimientos resultantes de la tirada de dado.
     * @param alertDialog Diálogo de tirada de dado a cerrar.
     */
    private fun tirada(movimientos: Int, alertDialog: AlertDialog) {
        // Cierra el diálogo de tirada de dado.
        alertDialog.dismiss()

        // Utiliza un coroutine para manejar las operaciones asíncronas en el hilo principal de la interfaz de usuario.
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                // Actualiza la posición del jugador en el tablero según los movimientos obtenidos.
                if (tipo == "offline") {
                    jugadorActual = jugadoresEnPartida[jugador]
                }
                metodosTablero.moverJugador(jugadorActual!!, movimientos.toString().toInt())
            }
        }
    }

    /**
     * Método resultadoMiniJuego: Procesa el resultado de un mini juego, gestionando el avance del jugador en el tablero
     * y actualizando su estado en la partida.
     *
     * @param ganado Indica si el jugador ha ganado el mini juego.
     */
    fun resultadoMiniJuego(ganado: Boolean) {
        val conexion = Conexion(contexto!!)
        val jugadorenviar: JugadorEnPartida?
        val dado = view?.findViewById<Button>(R.id.bt_dado)
        cargarOnline = true

        // Determina si la partida es offline u online y ajusta la lógica en consecuencia.
        if (tipo == "offline") {
            jugadorenviar = jugadoresEnPartida[jugador]
            dado?.isEnabled = true
        } else {
            jugadorenviar = jugadorActual
        }

        // Realiza acciones específicas según el resultado del mini juego.
        if (!ganado) {
            jugadorenviar?.jugadorActual = false

            // Si la partida es offline, avanza al siguiente jugador en turno.
            if (tipo == "offline") {
                jugador++
                if (jugador >= jugadoresEnPartida.size) {
                    jugador = 0
                }

                // Obtiene el jugador siguiente y actualiza la información del turno.
                val jugadorNuevo = jugadoresEnPartida[jugador]
                actualizarJugador(jugadorNuevo)
                jugadorNuevo.jugadorActual = true
                conexion.actualizarCasillaActual(jugadorNuevo)
            }
        }

        cargar = true

        // Realiza acciones específicas si la partida es online.
        if (tipo == "online") {
            jugadorenviar?.casillaActual = metodosTablero.jugadorActual?.casillaActual!!
            jugadorenviar?.jugadorActual = ganado

            // Envia la información actualizada del jugador al servidor.
            socket?.emit("moverJugador", jugadorenviar?.toJson())
        }

        // Actualiza la casilla actual del jugador en caso de partida offline.
        if (tipo == "offline") {
            conexion.actualizarCasillaActual(jugadorenviar!!)
        }
    }
    override fun onResume() {
        super.onResume()
        metodosTablero.preguntas.verificarConexionYRecargarPreguntas()
    }
}