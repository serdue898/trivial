package com.example.trivialnavidad.core.feature.seleccionJugadores.view

import android.app.AlertDialog
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida
import com.example.trivialnavidad.core.feature.seleccionJugadores.adapter.ListaAdapterSeleccion
import com.example.trivialnavidad.core.feature.seleccionJugadores.adapter.SpinnerAdapter
import com.example.trivialnavidad.core.feature.seleccionJugadores.viewModel.MetodosSeleccion

class SeleccionJugador : Fragment() {

    private var comunicador: MetodosSeleccion = MetodosSeleccion()
    private var contexto: Context? = null
    private var jugadoresEnPartida = mutableListOf<Jugador>()
    private var avatarImages: TypedArray? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        avatarImages = resources.obtainTypedArray(R.array.avatar_images)
        // Inflar el diseño de la vista
        val view = inflater.inflate(R.layout.seleccion_jugadores, container, false)
        contexto = container?.context

        // Configurar la barra de herramientas (toolbar)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar3)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflar el menú de opciones
        inflater.inflate(R.menu.menu_general_view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mItm_configuracion -> {
                // Abrir la configuración de la aplicación
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!)
                true
            }
            R.id.mItm_inicio -> {
                // Volver a la pantalla de inicio
                comunicador.volver(contexto!!)
                true
            }
            R.id.mItm_acerca -> {
                // Mostrar información acerca de la aplicación
                val msnEmergente = androidx.appcompat.app.AlertDialog.Builder(contexto as AppCompatActivity)
                msnEmergente.setMessage(getString(R.string.acercaDe))
                msnEmergente.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a las vistas necesarias
        val bGuardarjugador = view.findViewById<Button>(R.id.b_guardarJugador)
        val spinerAvatares = view.findViewById<Spinner>(R.id.sp_avatares)
        val btEmpezarpartida = view.findViewById<Button>(R.id.b_inciarJuego)
        avatarImages = resources.obtainTypedArray(R.array.avatar_images)
        val avatares = List(avatarImages!!.length()) { i -> i }
        val adapterspinner = SpinnerAdapter(contexto!!, avatares)


        // Configurar el adaptador en el Spinner
        spinerAvatares.adapter = adapterspinner

        // Configurar el evento de clic para el botón de guardar jugador
        bGuardarjugador?.setOnClickListener {
            // Ocultar el teclado virtual
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            if (jugadoresEnPartida.size >= 6) {
                // Mostrar un mensaje si se alcanza el límite de jugadores
                Toast.makeText(contexto, getString(R.string.limite_jugadores), Toast.LENGTH_SHORT).show()
            } else {
                // Agregar el jugador a la lista y actualizar la interfaz
                agregarJugadorLista()
                actualizarLista()

                if (jugadoresEnPartida.size >= 2) {
                    // Mostrar el botón de empezar partida si hay al menos 2 jugadores
                    btEmpezarpartida?.visibility = View.VISIBLE
                }
            }
        }

        // Configurar el evento de clic para el botón de empezar partida
        btEmpezarpartida?.setOnClickListener {
            // Iniciar la partida
            empezarPartida()
        }
    }

    // Método para actualizar el contenido del Spinner
    fun actualizarSpinner() {
        val spinerAvatares = view?.findViewById<Spinner>(R.id.sp_avatares)
        spinerAvatares?.adapter = listaspinner()
    }

    // Método para obtener un nuevo adaptador para el Spinner
    private fun listaspinner(): SpinnerAdapter {
        val avataresUsados = mutableListOf<Int>()
        jugadoresEnPartida.forEach { jugador -> avataresUsados.add(jugador.avatar.toInt()) }
        val avatares: MutableList<Int> = mutableListOf()
        for (i in 0 until avatarImages!!.length()) {
            if (!avataresUsados.contains(i)) {
                avatares.add(i)
            }
        }
        return SpinnerAdapter(contexto!!, avatares)
    }

    // Método para editar la información de un jugador en la lista
    fun editarJugadorLista(jugador: Jugador) {
        val popup = AlertDialog.Builder(contexto)
        val view = layoutInflater.inflate(R.layout.editar_jugador, null)
        popup.setView(view)
        val nombreJugador = view.findViewById<EditText>(R.id.et_nombre)
        val avatarJugador = view.findViewById<Spinner>(R.id.sp_avatar)
        nombreJugador.setText(jugador.nombre)
        avatarJugador.adapter = listaspinner()
        avatarJugador.setSelection(jugador.avatar.toInt())

        popup.setPositiveButton(getString(R.string.guardar)) { _, _ ->
            if (nombreJugador.text.toString().isEmpty()) {
                // Mostrar un mensaje si el nombre está vacío
                Toast.makeText(contexto, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show()
            } else {
                // Obtener la vista seleccionada en el Spinner
                val vistaSeleccionada = avatarJugador?.selectedView
                val avatar = vistaSeleccionada?.findViewById<ImageView>(R.id.iv_avatar)
                val posicion = avatar?.tag.toString().toInt()

                // Crear un nuevo jugador con la información actualizada
                val nuevoJugador = Jugador(jugador.id_jugador, nombreJugador.text.toString(), posicion.toString())
                jugadoresEnPartida[jugadoresEnPartida.indexOf(jugador)] = nuevoJugador

                // Actualizar la interfaz
                actualizarLista()
                actualizarSpinner()
            }
        }

        popup.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
            dialog.dismiss()
        }

        popup.show()
    }

    // Método para actualizar la lista de jugadores en la interfaz
    fun actualizarLista() {
        val listajugadores = view?.findViewById<RecyclerView>(R.id.ryV_listaJugadores)

        val layoutManager = LinearLayoutManager(contexto)
        listajugadores?.layoutManager = layoutManager
        val adapter = ListaAdapterSeleccion(jugadoresEnPartida, contexto!!, this)
        listajugadores?.adapter = adapter

        // Actualizar el Spinner con la lista actualizada de avatares disponibles
        actualizarSpinner()
    }

    // Método para agregar un jugador a la lista
    private fun agregarJugadorLista() {
        val nombreJugador = view?.findViewById<EditText>(R.id.eT_nombreJugador)
        val avatarJugador = view?.findViewById<Spinner>(R.id.sp_avatares)

        if (nombreJugador?.text.toString().isEmpty()) {
            // Mostrar un mensaje si el nombre está vacío
            Toast.makeText(contexto, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show()
            return
        }

        if (nombreJugador?.text.toString() in jugadoresEnPartida.map { it.nombre }) {
            // Mostrar un mensaje si el nombre ya está en uso
            nombreJugador?.setText("")
            Toast.makeText(contexto, getString(R.string.nombre_repetido), Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener la vista seleccionada en el Spinner
        val view = avatarJugador?.selectedView
        val avatar = view?.findViewById<ImageView>(R.id.iv_avatar)
        val posicion = avatar?.tag.toString().toInt()

        // Crear un nuevo jugador
        val jugadorNuevo = Jugador(0, nombreJugador?.text.toString(), posicion.toString())

        // Agregar el nuevo jugador a la lista
        jugadoresEnPartida.add(jugadorNuevo)

        // Limpiar el campo de nombre y restablecer el Spinner a la primera posición
        nombreJugador?.setText("")
        avatarJugador?.setSelection(0)
    }

    // Método para comenzar la partida
    private fun empezarPartida() {
        val conexion = Conexion(contexto!!)
        val idPartida = conexion.agregarPartida(Partida(0, "partida", false))
        val juegos = MutableList(4) { _ -> false }

        for (jugador in jugadoresEnPartida) {
            // Agregar cada jugador a la base de datos
            conexion.agregarJugador(jugador)

            // Crear un objeto JugadorEnPartida
            val jugadorEnPartida = JugadorEnPartida(
                jugador.id_jugador,
                idPartida,
                "4_4",
                jugadoresEnPartida[0] == jugador,
                juegos,
                jugador.avatar
            )
            jugadorEnPartida.jugador = jugador

            // Agregar el jugador a la partida
            conexion.agregarJugadorEnPartida(jugadorEnPartida)
        }

        // Comunicar al fragmento padre que la partida ha comenzado
        comunicador.empezarPartida(contexto!!, idPartida)
    }
}
