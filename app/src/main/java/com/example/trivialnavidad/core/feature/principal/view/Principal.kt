package com.example.trivialnavidad.core.feature.principal.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.feature.principal.viewModel.ComunicadorPrincipal
import com.example.trivialnavidad.core.feature.principal.viewModel.MetodosPrincipal
import io.socket.client.IO
import java.lang.Thread.sleep
import java.util.regex.Pattern
/**
 * Fragmento que representa la pantalla de inicio de la aplicación.
 */
class Principal : Fragment() {
    private var comunicador: ComunicadorPrincipal? = MetodosPrincipal()
    private var contexto: Context? = null
    private var empezado = false
    var volver = "inicio"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar la vista del fragmento con el diseño definido en R.layout.principal
        val view = inflater.inflate(R.layout.principal, container, false)

        // Obtener el contexto del contenedor (puede ser nulo)
        contexto = container?.context

        // Realizar alguna acción, en este caso, desloguear al usuario
        desloggear()

        // Obtener referencias a los botones y la barra de herramientas
        val botonOff = view.findViewById<Button>(R.id.bt_offline)
        val botonOn = view.findViewById<Button>(R.id.bt_online)
        val botonNuevaPartida = view.findViewById<Button>(R.id.bt_nueva)
        val botonCargarPartida = view.findViewById<Button>(R.id.bt_cargar)
        val botonVolver = view.findViewById<Button>(R.id.bt_volverInicio)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        // Configurar la barra de herramientas
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null

        // Configurar acciones para los botones
        botonNuevaPartida.setOnClickListener {
            // Abrir juego cuando se hace clic en el botón de nueva partida
            comunicador?.abrirJuego(botonNuevaPartida.tag.toString(), contexto!!)
        }
        botonCargarPartida.setOnClickListener {
            // Abrir juego cuando se hace clic en el botón de cargar partida
            comunicador?.abrirJuego(botonCargarPartida.tag.toString(), contexto!!)
        }
        botonOff.setOnClickListener {
            // Configuración cuando se hace clic en el botón de offline
            botonCargarPartida.visibility = View.VISIBLE
            botonNuevaPartida.visibility = View.VISIBLE
            botonCargarPartida.tag = "cargar"
            botonNuevaPartida.tag = "nueva"
            botonOff.visibility = View.INVISIBLE
            botonOn.visibility = View.INVISIBLE
            botonVolver.visibility = View.VISIBLE
        }
        botonOn.setOnClickListener {
            // Configuración cuando se hace clic en el botón de online
            if (MainActivity.socket == null || !MainActivity.socket?.connected()!!) {
                try {
                    // Intentar conectar el socket a la dirección especificada
                    MainActivity.socket = IO.socket("http://192.168.0.202:5000")
                    MainActivity.socket?.connect()
                    sleep(100)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (!MainActivity.socket?.connected()!!) {
                // Mostrar un diálogo de error si la conexión al servidor falla
                val alerta = AlertDialog.Builder(contexto as AppCompatActivity)
                alerta.setTitle(getString(R.string.error))
                alerta.setMessage(getString(R.string.error_server))
                alerta.setPositiveButton(getString(R.string.aceptar)) { dialog, _ ->
                    dialog.dismiss()
                }
                alerta.show()
            } else {
                // Realizar el inicio de sesión
                login()
            }
        }
        botonVolver.setOnClickListener {
            // Configuración cuando se hace clic en el botón de volver
            botonCargarPartida.visibility = View.INVISIBLE
            botonNuevaPartida.visibility = View.INVISIBLE
            botonOff.visibility = View.VISIBLE
            botonOn.visibility = View.VISIBLE
            botonVolver.visibility = View.INVISIBLE
            desloggear()
        }

        // Se devuelve la vista inflada
        return view
    }
    private fun mostrarOnline(){
        val botonOff = view?.findViewById<Button>(R.id.bt_offline)
        val botonOn = view?.findViewById<Button>(R.id.bt_online)
        val botonNuevaPartida = view?.findViewById<Button>(R.id.bt_nueva)
        val botonCargarPartida = view?.findViewById<Button>(R.id.bt_cargar)
        val botonVolver = view?.findViewById<Button>(R.id.bt_volverInicio)
        botonCargarPartida?.visibility = View.VISIBLE
        botonNuevaPartida?.visibility = View.VISIBLE
        botonCargarPartida?.tag = "online-cargar"
        botonNuevaPartida?.tag = "online-crear"
        botonOff?.visibility = View.INVISIBLE
        botonOn?.visibility = View.INVISIBLE
        botonVolver?.visibility = View.VISIBLE

    }
    private fun desloggear(){
        if (MainActivity.jugadorActual != null) {
            val socket = MainActivity.socket
            socket?.emit("desloggear", MainActivity.jugadorActual?.nombre)
        }
    }
    private fun login() {
        // Crear un diálogo de inicio de sesión
        val popupLogin = AlertDialog.Builder(contexto as AppCompatActivity)
        popupLogin.setTitle("Login")

        // Inflar la vista del diseño de inicio de sesión
        val view = (contexto as AppCompatActivity).layoutInflater.inflate(R.layout.login, null)
        popupLogin.setView(view)

        // Configurar el botón de aceptar en el diálogo
        popupLogin.setPositiveButton(getString(R.string.aceptar)) { _, _ ->
            // Obtener el nombre y la contraseña introducidos por el usuario
            val nombre = view.findViewById<EditText>(R.id.ed_nombre).text.toString()
            val contrasena = view.findViewById<EditText>(R.id.et_contrasena).text.toString()

            // Obtener el socket y crear un objeto Jugador con los datos ingresados
            val socket = MainActivity.socket
            val jugador = Jugador(0, nombre, "0")
            jugador.contraseña = contrasena

            // Emitir un evento "login" al servidor con los datos del jugador
            socket?.emit("login", jugador.toJson())

            // Escuchar la respuesta del servidor al evento "login_respuesta"
            socket?.on("login_respuesta") { args ->
                when (val id = args[0]) {
                    "null", "error" -> {
                        // Mostrar un mensaje de error si el inicio de sesión falla
                        (contexto as AppCompatActivity).runOnUiThread {
                            val errorPopup = AlertDialog.Builder(contexto as AppCompatActivity)
                            errorPopup.setTitle(getString(R.string.error))
                            errorPopup.setMessage(getString(R.string.fallo_login_usuario))
                            errorPopup.setPositiveButton(getString(R.string.aceptar)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            errorPopup.show()
                        }
                    }
                    "loggeado" -> {
                        // Mostrar un mensaje de error si el usuario ya está loggeado
                        (contexto as AppCompatActivity).runOnUiThread {
                            val errorPopup = AlertDialog.Builder(contexto as AppCompatActivity)
                            errorPopup.setTitle(getString(R.string.error))
                            errorPopup.setMessage(getString(R.string.usuario_loggeado))
                            errorPopup.setPositiveButton(getString(R.string.aceptar)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            errorPopup.show()
                        }
                    }
                    else -> {
                        // Si el inicio de sesión es exitoso, actualizar el jugador actual
                        MainActivity.jugadorActual = Jugador.fromJson(id.toString())
                        activity?.runOnUiThread {
                            // Mostrar la interfaz para usuarios loggeados
                            mostrarOnline()
                        }
                    }
                }
            }
        }

        // Configurar el botón de cancelar en el diálogo
        popupLogin.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        // Configurar el botón de registrarse en el diálogo
        popupLogin.setNeutralButton("Registrarse") { _, _ ->
            // Obtener el nombre y la contraseña introducidos por el usuario
            val nombre = view.findViewById<EditText>(R.id.ed_nombre).text.toString()
            val contrasena = view.findViewById<EditText>(R.id.et_contrasena).text.toString()

            if (nombre == "" || contrasena == "") {
                // Mostrar un mensaje si no se ingresan todos los campos
                Toast.makeText(contexto, getString(R.string.rellenar_campos), Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(contrasena)) {
                // Mostrar un mensaje si la contraseña no cumple con los requisitos
                Toast.makeText(
                    contexto,
                    getString(R.string.requisitos_contrasena),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Obtener el socket y crear un objeto Jugador con los datos ingresados
                val socket = MainActivity.socket
                val jugador = Jugador(0, nombre, "0")
                jugador.contraseña = contrasena

                // Emitir un evento "registrarJugador" al servidor con los datos del jugador
                socket?.emit("registrarJugador", jugador.toJson())

                // Escuchar la respuesta del servidor al evento "registrarJugador"
                socket?.on("registrarJugador") { args ->
                    val jugadorLLegado = args[0]

                    if (jugadorLLegado == "null") {
                        // Mostrar un mensaje de error si el registro falla (nombre de usuario ya existe)
                        (contexto as AppCompatActivity).runOnUiThread {
                            val errorPopup = AlertDialog.Builder(contexto as AppCompatActivity)
                            errorPopup.setTitle(getString(R.string.error))
                            errorPopup.setMessage(getString(R.string.error_registro))
                            errorPopup.setPositiveButton(getString(R.string.aceptar)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            errorPopup.show()
                        }
                    } else {
                        // Si el registro es exitoso, actualizar el jugador actual y mostrar la interfaz
                        MainActivity.jugadorActual = Jugador.fromJson(jugadorLLegado.toString())
                        activity?.runOnUiThread {
                            mostrarOnline()
                        }
                    }
                }
            }
        }

        // Mostrar el diálogo en el hilo principal
        (contexto as AppCompatActivity).runOnUiThread {
            popupLogin.show()
        }
    }
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z]).{5,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.mItm_inicio)
        item.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_general_view, menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.mItm_configuracion -> {
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!,true)
                true
            }
            R.id.mItm_acerca -> {
                val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
                msnEmergente.setMessage(getString(R.string.acercaDe))
                msnEmergente.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (empezado) {
            val configuracion = MainActivity.configuracion
            configuracion?.toggleDarkMode(configuracion.obtenerOpcionTemas())
        }
    }
    fun empezar(){
        empezado = true
    }




}