package com.example.trivialnavidad.core.feature.seleccionJugador.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.t8_ej03_persistenciaapi.ui.adapter.ListaAdapter
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class ListaAdapterSeleccion(private val jugadores: List<Jugador> , var contexto :Context) : RecyclerView.Adapter<ListaAdapterSeleccion.PostViewHolder>()  {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.tx_nombreJugador)
        var avatar: ImageView = itemView.findViewById(R.id.iV_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_seleccion_jugadores, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = jugadores.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.nombre.text = jugador.nombre
        val resourceId = contexto.resources.getIdentifier(jugador.avatar, "drawable", contexto.packageName)
        holder.avatar.setImageResource(resourceId)
    }
}