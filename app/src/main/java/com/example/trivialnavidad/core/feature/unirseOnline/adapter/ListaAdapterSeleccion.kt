package com.example.trivialnavidad.core.feature.unirseOnline.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.feature.seleccionJugadores.view.SeleccionJugador
import com.example.trivialnavidad.core.feature.unirseOnline.view.UnirseOnline

class ListaAdapterSeleccion(
    private val jugadores: MutableList<Jugador>,
    var contexto: Context,
    val seleccionJugador: UnirseOnline
) : RecyclerView.Adapter<ListaAdapterSeleccion.PostViewHolder>()  {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.tx_nombreJugador)
        var avatar: ImageView = itemView.findViewById(R.id.iV_avatar)
        var eliminar : ImageView = itemView.findViewById(R.id.iv_papelera)
        var editar : ImageView = itemView.findViewById(R.id.iv_editar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_seleccion_jugadores, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = jugadores.size

    @SuppressLint("Recycle")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val avatarImages = contexto.resources.obtainTypedArray(R.array.avatar_images)
        val jugador = jugadores[position]
        holder.nombre.text = jugador.nombre
        holder.avatar.setImageDrawable(avatarImages.getDrawable(jugador.avatar.toInt()))
        holder.eliminar.visibility = View.GONE
        if (jugador.nombre == MainActivity.jugadorActual?.nombre) {
            holder.editar.visibility = View.VISIBLE
        } else {
            holder.editar.visibility = View.GONE
        }
        holder.editar.setOnClickListener {
            seleccionJugador.editarJugadorLista(jugador)
        }

    }
}