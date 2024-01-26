package com.example.trivialnavidad.core.feature.clasificacion.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class ListaAdapter(private val jugadores: List<JugadorEnPartida>,val contexto :Context) : RecyclerView.Adapter<ListaAdapter.PostViewHolder>() {
    // Creación de nuevas vistas (invocadas por el layout manager).
    private val colores = contexto.resources.getIntArray(R.array.colores)
     private var textos = mutableListOf<TextView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_clasificacion, parent, false)
        return PostViewHolder(view)
    }

    // Definición del ViewHolder que proporciona una referencia a las vistas para cada elemento de datos.
    class PostViewHolder(itemView: View) : ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.tx_nombre)
        var minijuego1: TextView = itemView.findViewById(R.id.tx_minijuego)
        var minijuego2:TextView = itemView.findViewById(R.id.tx_minijuego1)
        var minijuego3:TextView = itemView.findViewById(R.id.tx_minijuego2)
        var minijuego4:TextView = itemView.findViewById(R.id.tx_minijuego3)
        val textos2 = mutableListOf(nombre,minijuego1,minijuego2,minijuego3,minijuego4)
    }

    // Reemplazo del contenido de una vista (invocado por el layout manager).
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.minijuego1.text = ""
        holder.minijuego2.text = ""
        holder.minijuego3.text = ""
        holder.minijuego4.text = ""
        if (position == 0) {
            // Configurar encabezados
            holder.nombre.text = contexto.getString(R.string.nombre)
            holder.minijuego1.setBackgroundColor(colores[1])
            holder.minijuego2.setBackgroundColor(colores[2])
            holder.minijuego3.setBackgroundColor(colores[3])
            holder.minijuego4.setBackgroundColor(colores[4])
        } else {
            textos = holder.textos2
            val jugador = jugadores[position-1]
            holder.nombre.text = jugador.jugador?.nombre
            jugador.juegos.forEachIndexed { index, b ->
                if (b) {
                    textos[index+1].setBackgroundColor(ContextCompat.getColor(contexto, R.color.verde))
                } else {
                    textos[index+1].setBackgroundColor(ContextCompat.getColor(contexto, R.color.rojo))
                }
            }

        }
    }

    // Devuelve el tamaño de tu conjunto de datos (invocado por el layout manager).
    override fun getItemCount() = jugadores.size+1
}
