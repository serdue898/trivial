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
import android.widget.Button
import android.widget.EditText
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
    private var avatarImages :TypedArray? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.seleccion_jugadores, container, false)
        contexto = container?.context
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar3)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null

        /*
        // codigo de clasificacion y no se si es necesario o no

      val b_guardar = view.findViewById<Button>(R.id.bt_volver)
        bt_volver.setOnClickListener {
            comunicador?.volver(contexto!!)
          }
    */
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = (contexto as AppCompatActivity).menuInflater
        inflater.inflate(R.menu.menu_general_view, menu)// OJO- se pasa la vista que se quiere inflar

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.mItm_configuracion -> {
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!)
                true
            }
            R.id.mItm_inicio -> {
                comunicador?.volver(contexto!!)
                true
            }
            R.id.mItm_acerca -> {
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

        val b_guardarJugador = view.findViewById<Button>(R.id.b_guardarJugador)
        val spinerAvatares = view.findViewById<Spinner>(R.id.sp_avatares)

        avatarImages = resources.obtainTypedArray(R.array.avatar_images)
        val avatares = List(avatarImages!!.length()) { i -> avatarImages!!.getDrawable(i) }
        val adapterspinner = SpinnerAdapter(contexto!!, avatares)


// Configurar el adaptador en el Spinner
        spinerAvatares.adapter = adapterspinner



        // el botton editar me sobra ya que si se quiere editar algo sera mas sencillo pinchar en el elemento y que se ponga en edittext y el spinner
        //val b_guardarModificar = view?.findViewById<Button>(R.id.b_editarJugador)
        val bt_empezarPartida = view.findViewById<Button>(R.id.b_inciarJuego)

        b_guardarJugador?.text = "Nuevo jugador"
        b_guardarJugador?.setOnClickListener {
            if (jugadoresEnPartida.size >= 6) {
                Toast.makeText(contexto, "No se pueden añadir mas jugadores", Toast.LENGTH_SHORT).show()
            }else{
                agregarJugadorLista( )
                actualizarLista()
                if (jugadoresEnPartida.size >=2) {
                    bt_empezarPartida?.visibility = View.VISIBLE
                }
            }

        }
        bt_empezarPartida?.setOnClickListener {
            empezarPartida()
        }

        /*
            agregarJugadorLista(jugadoresEnPartida, null)
            b_eliminarJugador?.isEnabled = true
            b_eliminarJugador?.setOnClickListener {

         */




    }
    fun editarJugadorLista(jugador: Jugador) {
        val popup = AlertDialog.Builder(contexto)
        val view = layoutInflater.inflate(R.layout.editar_jugador, null)
        popup.setView(view)
        val nombreJugador = view.findViewById<EditText>(R.id.et_nombre)
        val avatarJugador = view.findViewById<Spinner>(R.id.sp_avatar)
        nombreJugador.setText(jugador.nombre)
        val avatares = List(avatarImages!!.length()) { i -> avatarImages!!.getDrawable(i) }
        val adapterspinner = SpinnerAdapter(contexto!!, avatares)
        avatarJugador.adapter = adapterspinner
        avatarJugador.setSelection(jugador.avatar.toInt())

        popup.setPositiveButton("Guardar") { dialog, which ->
            if (nombreJugador.text.toString().isEmpty()) {
                Toast.makeText(contexto, "El nombre del jugador no puede estar vacío", Toast.LENGTH_SHORT).show()

            }else{
                val nuevoJugador = Jugador(jugador.id, nombreJugador.text.toString(), avatarJugador.selectedItemPosition.toString())
                jugadoresEnPartida[jugadoresEnPartida.indexOf(jugador)] = nuevoJugador
                actualizarLista()
            }
        }
        popup.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        popup.show()
    }
    fun actualizarLista(){
        val listajugadores = view?.findViewById<RecyclerView>(R.id.ryV_listaJugadores)

        val layoutManager = LinearLayoutManager(contexto)
        listajugadores?.layoutManager = layoutManager
        val adapter = ListaAdapterSeleccion(jugadoresEnPartida, contexto!!,this)
        listajugadores?.adapter = adapter

    }

    fun agregarJugadorLista() {
        val nombreJugador = view?.findViewById<EditText>(R.id.eT_nombreJugador)
        val avatarJugador = view?.findViewById<Spinner>(R.id.sp_avatares)
        if (nombreJugador?.text.toString().isEmpty()) {
            Toast.makeText(contexto, "El nombre del jugador no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el índice del avatar seleccionado
        val indiceAvatarSeleccionado = avatarJugador?.selectedItemPosition ?: 0

        // Crear un nuevo jugador
        val jugadorNuevo = Jugador(0, nombreJugador?.text.toString(), indiceAvatarSeleccionado.toString())

        // Agregar el nuevo jugador a la lista
          jugadoresEnPartida.add(jugadorNuevo)

        // Limpiar el campo de nombre y restablecer el Spinner a la primera posición
        nombreJugador?.setText("")
        avatarJugador?.setSelection(0)
    }
    private fun empezarPartida() {
        val conexion = Conexion(contexto!!)
        val idPartida = conexion.agregarPartida(Partida(0,"partida"))
        val juegos = MutableList(4){i -> false}
        for (jugador in jugadoresEnPartida) {
            conexion.agregarJugador(jugador)
            val jugadorEnPartida = JugadorEnPartida(jugador, idPartida, "4_4",
                jugadoresEnPartida[0]==jugador, juegos, jugador.avatar)
            conexion.agregarJugadorEnPartida(jugadorEnPartida)
        }
        comunicador?.empezarPartida(contexto!!,idPartida)
    }



}
