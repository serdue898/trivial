package com.example.t8_ej03_persistenciaapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class ListaAdapter(private val jugadores: List<JugadorEnPartida>) : RecyclerView.Adapter<ListaAdapter.PostViewHolder>() {
    // Creación de nuevas vistas (invocadas por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_clasificacion, parent, false)
        return PostViewHolder(view)
    }

    // Definición del ViewHolder que proporciona una referencia a las vistas para cada elemento de datos.
    class PostViewHolder(itemView: View) : ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.tx_nombre)
        var puntuacion: TextView = itemView.findViewById(R.id.tx_puntuacion)
    }

    // Reemplazo del contenido de una vista (invocado por el layout manager).
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if (position == 0) {
            // Configurar encabezados
            holder.nombre.text = "Nombre"
            holder.puntuacion.text = "Puntuacion"
        } else {
            val jugador = jugadores[position-1]
            holder.nombre.text = jugador.jugador?.nombre
            holder.puntuacion.text = jugador.puntosJugador().toString()
        }
    }

    // Devuelve el tamaño de tu conjunto de datos (invocado por el layout manager).
    override fun getItemCount() = jugadores.size+1
}
