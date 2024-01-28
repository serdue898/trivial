package com.example.trivialnavidad.core.feature.cargarPartida.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida
import com.example.trivialnavidad.core.feature.cargarPartida.adapter.PartidaAdapter
import com.example.trivialnavidad.core.feature.cargarPartida.viewModel.ComunicadorPartida
import com.example.trivialnavidad.core.feature.cargarPartida.viewModel.MetodosPartida
import io.socket.client.Ack
import org.json.JSONArray
import org.json.JSONException


/**
 * Clase Partidas: Fragmento que muestra la lista de partidas disponibles, permitiendo cargarlas y realizar acciones
 * como volver al inicio, ver la configuración o consultar información acerca de la aplicación.
 *
 * @property tipo Tipo de partida a mostrar ("offline" o "online").
 * @property contexto Contexto de la aplicación.
 * @property comunicador Objeto que implementa la interfaz ComunicadorPartida para gestionar las acciones relacionadas con las partidas.
 * @property partidas Lista de partidas a mostrar.
 */
class Partidas(private var tipo: String) : Fragment() {

    private var contexto: Context? = null
    private var comunicador: ComunicadorPartida? = null
    private var partidas: List<Partida>? = null

    /**
     * Inicializa la vista del fragmento.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.partidas, container, false)
        contexto = container?.context
        comunicador = MetodosPartida(contexto!!)
        val btVolver = view.findViewById<Button>(R.id.bt_volver)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar4)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null
        btVolver.setOnClickListener {
            comunicador?.volver()
        }
        return view
    }

    /**
     * Crea el menú de opciones en la barra de herramientas.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflaternuevo: MenuInflater = (contexto as AppCompatActivity).menuInflater
        inflaternuevo.inflate(R.menu.menu_general_view, menu)
    }

    /**
     * Maneja los eventos de selección de opciones del menú.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mItm_configuracion -> {
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!)
                true
            }
            R.id.mItm_inicio -> {
                comunicador?.volver()
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

    /**
     * Se llama después de que la vista ha sido creada.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Puedes llamar a actualizarLista aquí para garantizar que la vista ya se haya creado
        val conexion = Conexion(contexto!!)

        if (tipo == "offline") {
            partidas = conexion.obtenerPartidas().toMutableList()
        }
        actualizarLista(partidas!!)
    }

    /**
     * Obtiene las partidas disponibles en línea mediante la conexión al servidor.
     */
    fun cogerPartidas() {
        val partidasOnline: MutableList<Partida> = mutableListOf()
        val socket = MainActivity.socket
        // Cuando se conecta, solicita las partidas iniciales
        socket!!.emit("obtener_partidas", Ack { args1: Array<Any> ->
            val jsonArray = args1[0] as JSONArray

            try {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val nombre = jsonObject.getString("nombre")
                    val partida = Partida(id, nombre, false)
                    partidasOnline.add(partida)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
        partidas = partidasOnline
    }

    /**
     * Actualiza la lista de partidas en la vista.
     *
     * @param partidas Lista de partidas a mostrar.
     */
    private fun actualizarLista(partidas: List<Partida>) {
        val lista = view?.findViewById<RecyclerView>(R.id.rv_partidas)
        lista?.layoutManager = LinearLayoutManager(contexto)
        val dividerItemDecoration = DividerItemDecoration(
            lista?.context,
            (lista?.layoutManager as LinearLayoutManager).orientation
        )
        lista?.addItemDecoration(dividerItemDecoration)
        val adapter = PartidaAdapter(partidas, comunicador!!, tipo, contexto!!)
        lista?.adapter = adapter
    }
}

