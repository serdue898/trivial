package com.example.trivialnavidad.core.feature.cargarPartida.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida
import com.example.trivialnavidad.core.feature.cargarPartida.viewModel.ComunicadorPartida

class PartidaAdapter(private val partidas: List<Partida>, var comunicador: ComunicadorPartida,
                     private var tipo :String, var contexto :Context) : RecyclerView.Adapter<PartidaAdapter.PostViewHolder>() {

    // Definición del ViewHolder que proporciona una referencia a las vistas para cada elemento de datos.
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.tx_nombrePartida)
        var id: TextView = itemView.findViewById(R.id.tx_id)
        var finalizada: TextView = itemView.findViewById(R.id.tx_numJugadores)

    }

    // Creación de nuevas vistas (invocadas por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_partidas, parent, false)
        return PostViewHolder(view)
    }

    // Reemplazo del contenido de una vista (invocado por el layout manager).
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if (position == 0) {
            // Configurar encabezados
            holder.id.text = "ID"
            holder.nombre.text = contexto.getString(R.string.partida)
            holder.finalizada.text = contexto.getString(R.string.finalizada)
        } else {
            val partida = partidas[position - 1]  // Resta 1 para compensar el encabezado
            holder.id.text = partida.idPartida.toString()
            holder.nombre.text = partida.nombre
            holder.finalizada.text = if (partida.finalizada)"Sí" else "No"

            // Agregar un clic listener para cargar la partida
            holder.itemView.setOnClickListener {
                if (tipo == "online"){
                    comunicador.unirseAPartida(partida.idPartida,contexto)
                }else {
                    comunicador.cargarPartida(partida)
                }
            }
        }
    }

    // Devuelve el tamaño de tu conjunto de datos (invocado por el layout manager).
    override fun getItemCount() = partidas.size+1
}