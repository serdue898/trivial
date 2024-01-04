

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida
import com.example.trivialnavidad.core.feature.cargarPartida.adapter.PartidaAdapter
import com.example.trivialnavidad.core.feature.cargarPartida.viewModel.ComunicadorPartida
import com.example.trivialnavidad.core.feature.cargarPartida.viewModel.MetodosPartida

class Partidas : Fragment() {

    private var contexto: Context? = null
    private var comunicador: ComunicadorPartida? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.partidas, container, false)
        contexto = container?.context
        comunicador = MetodosPartida(contexto!!)
        val bt_volver = view.findViewById<Button>(R.id.bt_volver)
        bt_volver.setOnClickListener {
            comunicador?.volver()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Puedes llamar a actualizarLista aquí para garantizar que la vista ya se haya creado
        val conexion = Conexion(contexto!!)
        val partidas = conexion.obtenerPartidas()
        actualizarLista(partidas)


    }

    fun actualizarLista(jugadoresEnPartida: List<Partida>) {
        val lista = view?.findViewById<RecyclerView>(R.id.rv_partidas)
        lista?.layoutManager = LinearLayoutManager(contexto)
        val dividerItemDecoration = DividerItemDecoration(lista?.context, (lista?.layoutManager as LinearLayoutManager).orientation)
        lista?.addItemDecoration(dividerItemDecoration)
        val adapter = PartidaAdapter(jugadoresEnPartida,comunicador!!)
        lista?.adapter = adapter


    }
}
