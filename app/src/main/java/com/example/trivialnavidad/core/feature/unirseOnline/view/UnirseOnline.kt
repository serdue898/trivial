package com.example.trivialnavidad.core.feature.unirseOnline.view

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
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.unirseOnline.adapter.ListaAdapterSeleccion
import com.example.trivialnavidad.core.feature.unirseOnline.adapter.SpinnerAdapter
import com.example.trivialnavidad.core.feature.unirseOnline.viewModel.MetodosUnirse
import org.json.JSONArray

class UnirseOnline(private var idPartida :Int) : Fragment() {

    private var comunicador: MetodosUnirse = MetodosUnirse()
    private var contexto: Context? = null
    private var jugadoresEnPartida = mutableListOf<Jugador>()
    private var avatarImages : TypedArray? = null
    private var emepzar = false
    var host = false
    private var eligiendo = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.seleccion_jugadores, container, false)
        contexto = container?.context
        MainActivity.jugadorActual?.partida = idPartida
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar3)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_general_view, menu)// OJO- se pasa la vista que se quiere inflar

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.mItm_configuracion -> {
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!)
                true
            }
            R.id.mItm_inicio -> {
                comunicador.volver(contexto!!)
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


        val bGuardarjugador = view.findViewById<Button>(R.id.b_guardarJugador)
        val nombreJugador = view.findViewById<EditText>(R.id.eT_nombreJugador)
        nombreJugador.isEnabled = false
        nombreJugador.setText(MainActivity.jugadorActual?.nombre)
        val spinerAvatares = view.findViewById<Spinner>(R.id.sp_avatares)

        avatarImages = resources.obtainTypedArray(R.array.avatar_images)
        val avatares = List(avatarImages!!.length()) { i -> i }
        val adapterspinner = SpinnerAdapter(contexto!!, avatares)
        // Configurar el adaptador en el Spinner
        spinerAvatares.adapter = adapterspinner
        val btEmpezarpartida = view.findViewById<Button>(R.id.b_inciarJuego)

        val socket = MainActivity.socket
        if (!host)socket?.emit("actualizarJugadores",idPartida)
        socket?.on("listaJugadores") { args ->
            val listaJugadores = args[0] as JSONArray
            // Manejar la lista de jugadores en tu aplicación Android
            // Por ejemplo, puedes actualizar la interfaz de usuario con la nueva lista

            jugadoresEnPartida.clear()
            for (i in 0 until listaJugadores.length()) {
                val jugador = Jugador.fromJson(listaJugadores[i].toString())
                if (jugador.partida == idPartida)jugadoresEnPartida.add(jugador)
            }
            val hostPreparado  = jugadoresEnPartida.find { it.nombre == MainActivity.jugadorActual?.nombre}?.host
            if (hostPreparado!=null) {
                host = hostPreparado
            }

            activity?.runOnUiThread {
                if (jugadoresEnPartida.size >=2 && host) {
                    btEmpezarpartida?.visibility = View.VISIBLE
                }else{
                    btEmpezarpartida?.visibility = View.INVISIBLE
                }
                actualizarLista()
            }

        }
        socket?.on("empezarPartida") { args ->
            val listaJugadores = args[0] as JSONArray
            emepzar = true
            // Manejar la lista de jugadores en tu aplicación Android
            // Por ejemplo, puedes actualizar la interfaz de usuario con la nueva lista
            val jugadores:MutableList<JugadorEnPartida> = mutableListOf()
            for (i in 0 until listaJugadores.length()) {
                val jugador = JugadorEnPartida.fromJson(listaJugadores[i].toString())
                jugador.jugador = jugadoresEnPartida.find { it.id_jugador == jugador.id_jugador }
                jugadores.add(jugador)
            }
            empezarPartida(jugadores)
        }
        bGuardarjugador?.setOnClickListener {
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            if (jugadoresEnPartida.size >= 6) {
                Toast.makeText(contexto, getString(R.string.limite_jugadores), Toast.LENGTH_SHORT).show()
            }else{
                eligiendo = true
                agregarJugadorLista( )

                bGuardarjugador.isEnabled = false
            }

        }
        btEmpezarpartida?.setOnClickListener {
            
            socket?.emit("empezarPartida", idPartida)
        }
    }



    private fun addJugadorOnline(jugador: Jugador){
        val socket = MainActivity.socket
        jugador.host = host
        val jugadorJson = jugador.toJson()
        socket?.emit("addJugador", jugadorJson)
    }


    private fun actualizarSpinner(){
        val spinerAvatares = view?.findViewById<Spinner>(R.id.sp_avatares)

        spinerAvatares?.adapter = listaspinner()
    }
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
    fun editarJugadorLista(jugador: Jugador) {
        val popup = AlertDialog.Builder(contexto)
        val view = layoutInflater.inflate(R.layout.editar_jugador, null)
        popup.setView(view)
        val nombreJugador = view.findViewById<EditText>(R.id.et_nombre)
        val avatarJugador = view.findViewById<Spinner>(R.id.sp_avatar)
        nombreJugador.isEnabled = false
        nombreJugador.setText(jugador.nombre)
        avatarJugador.adapter = listaspinner()
        avatarJugador.setSelection(jugador.avatar.toInt())

        popup.setPositiveButton(getString(R.string.guardar)) { _, _ ->
            if (nombreJugador.text.toString().isEmpty()) {
                Toast.makeText(contexto, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show()

            }else{

                val viewSeleccionada = avatarJugador?.selectedView
                val avatar=   viewSeleccionada?.findViewById<ImageView>(R.id.iv_avatar)
                val posicion =avatar?.tag.toString().toInt()
                val nuevoJugador = Jugador(jugador.id_jugador, nombreJugador.text.toString(), posicion.toString())
                nuevoJugador.partida = idPartida
                val socket = MainActivity.socket
                socket?.emit("actualizarJugador", nuevoJugador.toJson())
            }
        }
        popup.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
            dialog.dismiss()
        }
        popup.show()
    }
    private fun actualizarLista(){
        val listajugadores = view?.findViewById<RecyclerView>(R.id.ryV_listaJugadores)

        val layoutManager = LinearLayoutManager(contexto)
        listajugadores?.layoutManager = layoutManager
        val adapter = ListaAdapterSeleccion(jugadoresEnPartida, contexto!!,this)
        listajugadores?.adapter = adapter

        actualizarSpinner()

    }

    private fun agregarJugadorLista() {
        val nombreJugador = view?.findViewById<EditText>(R.id.eT_nombreJugador)
        val avatarJugador = view?.findViewById<Spinner>(R.id.sp_avatares)
        if (nombreJugador?.text.toString().isEmpty()) {
            Toast.makeText(contexto, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show()
            return
        }
        val view = avatarJugador?.selectedView
        val avatar=   view?.findViewById<ImageView>(R.id.iv_avatar)
        val posicion =avatar?.tag.toString().toInt()

        // Crear un nuevo jugador
        val jugadorNuevo = Jugador(0, nombreJugador?.text.toString(), posicion.toString())
        jugadorNuevo.partida = idPartida
        jugadoresEnPartida.add(jugadorNuevo)

        // Limpiar el campo de nombre y restablecer el Spinner a la primera posición
        nombreJugador?.setText("")
        avatarJugador?.setSelection(0)
        addJugadorOnline(jugadorNuevo)
    }
    private fun empezarPartida(jugadores: List<JugadorEnPartida>) {
        eligiendo = false
        comunicador.empezarPartida(contexto!!,idPartida,jugadores)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (eligiendo) {
            MainActivity.jugadorActual?.partida = idPartida
            MainActivity.socket?.emit("desconectar", MainActivity.jugadorActual?.toJson())
        }

    }



}