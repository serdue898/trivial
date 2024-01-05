package com.example.trivialnavidad.core.feature.minijuegos.parejas.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.feature.minijuegos.parejas.view.Parejas
import com.example.trivialnavidad.core.feature.seleccionJugadores.view.SeleccionJugador

class ListaAdapterParejas(
    private val opciones: List<String>,
    var contexto: Context,
    val Parejas: Parejas
) : RecyclerView.Adapter<ListaAdapterParejas.PostViewHolder>()  {
    private var imagenes = contexto.resources.obtainTypedArray(R.array.imagenesParejas)
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contenedor: FrameLayout = itemView.findViewById(R.id.fl_contenedor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_parejas, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = opciones.size

    @SuppressLint("Recycle")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val opcion = opciones[position]
        val contenido = opcion.toIntOrNull()
        if (contenido==null){
            val texto = TextView(contexto)
            texto.text = opcion
            holder.contenedor.addView(texto)

        }else{
            val imagen = ImageView(contexto)
            imagen.setImageResource(imagenes.getResourceId(contenido,0))
            holder.contenedor.addView(imagen)
        }
        holder.itemView.setOnClickListener {
            Parejas.seleccionado = opcion
        }


    }
}