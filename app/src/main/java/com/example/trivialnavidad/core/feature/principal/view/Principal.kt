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

class Principal : Fragment() {
    private var comunicador: ComunicadorPrincipal? = MetodosPrincipal()
    private var contexto: Context? = null
    private var empezado = false
    var tipo = "offline"
    var volver = "inicio"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.principal, container, false)
        contexto = container?.context
        desloggear()


        val botonOff = view.findViewById<Button>(R.id.bt_offline)
        val botonOn = view.findViewById<Button>(R.id.bt_online)
        val botonNuevaPartida = view.findViewById<Button>(R.id.bt_nueva)
        val botonCargarPartida = view.findViewById<Button>(R.id.bt_cargar)
        val botonVolver = view.findViewById<Button>(R.id.bt_volverInicio)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null


        botonNuevaPartida.setOnClickListener {
            comunicador?.abrir_juego(botonNuevaPartida.tag.toString(),contexto!!)
        }
        botonCargarPartida.setOnClickListener {
            comunicador?.abrir_juego(botonCargarPartida.tag.toString(),contexto!!)
        }
        botonOff.setOnClickListener {

            botonCargarPartida.visibility = View.VISIBLE
            botonNuevaPartida.visibility = View.VISIBLE
            botonCargarPartida.tag = "cargar"
            botonNuevaPartida.tag = "nueva"
            botonOff.visibility = View.INVISIBLE
            botonOn.visibility = View.INVISIBLE
            botonVolver.visibility = View.VISIBLE

        }
        botonOn.setOnClickListener {
            if (MainActivity.socket == null || !MainActivity.socket?.connected()!!) {
                try {
                    MainActivity.socket = IO.socket("http://192.168.0.202:5000")
                    MainActivity.socket?.connect()
                    sleep(100)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (!MainActivity.socket?.connected()!!){
                val alerta = AlertDialog.Builder(contexto as AppCompatActivity)
                alerta.setTitle("Error")
                alerta.setMessage("No se ha podido conectar con el servidor")
                alerta.setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }
                alerta.show()
            }else {
                login()
            }


        }
        botonVolver.setOnClickListener {
            botonCargarPartida.visibility = View.INVISIBLE
            botonNuevaPartida.visibility = View.INVISIBLE
            botonOff.visibility = View.VISIBLE
            botonOn.visibility = View.VISIBLE
            botonVolver.visibility = View.INVISIBLE
            desloggear()
        }

        // Se devuelve la vista inflada.
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
    private fun login(){
        val popupLogin = AlertDialog.Builder(contexto as AppCompatActivity)
        popupLogin.setTitle("Login")
        val view = (contexto as AppCompatActivity).layoutInflater.inflate(R.layout.login, null)
        popupLogin.setView(view)
        popupLogin.setPositiveButton("Aceptar") { _, _ ->
            val nombre = view.findViewById<EditText>(R.id.ed_nombre).text.toString()
            val contrasena = view.findViewById<EditText>(R.id.et_contrasena).text.toString()
            val socket = MainActivity.socket
            val jugador = Jugador(0,nombre,"0")
            jugador.contraseña = contrasena
            socket?.emit("login",jugador.toJson())
            socket?.on("login_respuesta") { args ->
                val id = args[0]
                if (id == "null" || id=="error") {
                    (contexto as AppCompatActivity).runOnUiThread {
                        val errorPopup = AlertDialog.Builder(contexto as AppCompatActivity)
                        errorPopup.setTitle("Error")
                        errorPopup.setMessage("Usuario o contraseña incorrectos")
                        errorPopup.setPositiveButton("Aceptar") { dialog, which ->
                            dialog.dismiss()
                        }
                        errorPopup.show()
                    }

                }
                else if (id == "loggeado"){
                    (contexto as AppCompatActivity).runOnUiThread {
                        val errorPopup = AlertDialog.Builder(contexto as AppCompatActivity)
                        errorPopup.setTitle("Error")
                        errorPopup.setMessage("Usuario ya esta loggeado")
                        errorPopup.setPositiveButton("Aceptar") { dialog, which ->
                            dialog.dismiss()
                        }
                        errorPopup.show()
                    }
                }
                else {
                    MainActivity.jugadorActual= Jugador.fromJson(id.toString())
                    activity?.runOnUiThread {
                        mostrarOnline()
                    }

                }
            }
        }
        popupLogin.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        popupLogin.setNeutralButton ("Registrarse"){_, _ ->
            val nombre = view.findViewById<EditText>(R.id.ed_nombre).text.toString()
            val contrasena = view.findViewById<EditText>(R.id.et_contrasena).text.toString()
            val socket = MainActivity.socket
            val jugador = Jugador(0,nombre,"0")
            jugador.contraseña = contrasena
            socket?.emit("registrarJugador", jugador.toJson())
            socket?.on("registrarJugador") { args ->
                val jugador = args[0]

                if (jugador == "null") {
                    (contexto as AppCompatActivity).runOnUiThread {
                        val errorPopup = AlertDialog.Builder(contexto as AppCompatActivity)
                        errorPopup.setTitle("Error")
                        errorPopup.setMessage("Ya existe un usuario con ese nombre")
                        errorPopup.setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        errorPopup.show()
                    }
                } else {

                    MainActivity.jugadorActual= Jugador.fromJson(jugador.toString())
                    activity?.runOnUiThread {
                        mostrarOnline()
                    }


                }
            }
        }
        (contexto as AppCompatActivity).runOnUiThread {
            popupLogin.show()
        }

    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.mItm_inicio)
        item.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_general_view, menu)// OJO- se pasa la vista que se quiere inflar

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
            configuracion?.toggleDarkMode(contexto!!,configuracion.obtenerOpcionTemas())
        }
    }
    fun empezar(){
        empezado = true
    }




}