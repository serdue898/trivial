package com.example.trivialnavidad.core.feature.seleccionJugador.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.t8_ej03_persistenciaapi.ui.adapter.ListaAdapter
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class ListaAdapterSeleccion(private val jugadores: List<JugadorEnPartida>) : RecyclerView.Adapter<ListaAdapter.PostViewHolder>()  {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.tx_nombreJugador)
        var puntuacion: ImageView = itemView.findViewById(R.id.iV_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaAdapter.PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_seleccion_jugadores, parent, false)
        return ListaAdapter.PostViewHolder(view)
    }

    override fun getItemCount() = jugadores.size

    override fun onBindViewHolder(holder: ListaAdapter.PostViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.nombre.text = jugador.jugador.nombre
        holder.puntuacion.text = jugador.puntosJugador().toString()
    }
}