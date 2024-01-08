

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONException


class Partidas (var tipo:String): Fragment() {

    private var contexto: Context? = null
    private var comunicador: ComunicadorPartida? = null
    private var partidas: List<Partida>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.partidas, container, false)
        contexto = container?.context
        comunicador = MetodosPartida(contexto!!)
        val bt_volver = view.findViewById<Button>(R.id.bt_volver)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar4)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (contexto as AppCompatActivity).supportActionBar?.title = null
        bt_volver.setOnClickListener {
            comunicador?.volver()
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Puedes llamar a actualizarLista aquí para garantizar que la vista ya se haya creado
        val conexion = Conexion(contexto!!)

        if (tipo=="offline") {
            partidas = conexion.obtenerPartidas().toMutableList()

        }
        actualizarLista(partidas!!)


    }
    fun cogerPartidas(){
        val partidasOnline : MutableList<Partida> = mutableListOf()
        val socket = MainActivity.socket
            // Cuando se conecta, solicita las partidas iniciales
            socket!!.emit("obtener_partidas", Ack { args1: Array<Any> ->
                val jsonArray = args1[0] as JSONArray

                try {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        val nombre = jsonObject.getString("nombre")
                        val partida = Partida(id, nombre,false)
                        partidasOnline.add(partida)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                // Aquí puedes manejar la lista de partidas iniciales
                Log.d("SocketIO", "Partidas iniciales recibidas: $partidasOnline")

            })
        partidas= partidasOnline
    }

    fun actualizarLista(partidas: List<Partida>) {
        val lista = view?.findViewById<RecyclerView>(R.id.rv_partidas)
        lista?.layoutManager = LinearLayoutManager(contexto)
        val dividerItemDecoration = DividerItemDecoration(lista?.context, (lista?.layoutManager as LinearLayoutManager).orientation)
        lista?.addItemDecoration(dividerItemDecoration)
        val adapter = PartidaAdapter(partidas,comunicador!!)
        lista?.adapter = adapter


    }
}
