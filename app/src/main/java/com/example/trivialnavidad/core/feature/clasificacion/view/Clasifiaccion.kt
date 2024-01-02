package com.example.trivialnavidad.core.feature.clasificacion.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.t8_ej03_persistenciaapi.ui.adapter.ListaAdapter
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.clasificacion.viewModel.ComunicadorClasificacion
import com.example.trivialnavidad.core.feature.clasificacion.viewModel.MetodosClasifiacion

class Clasifiaccion (var jugadoresEnPartida: List<JugadorEnPartida>): Fragment() {
    private var comunicador: ComunicadorClasificacion? = MetodosClasifiacion()
    private var contexto: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.clasificacion, container, false)
        contexto = container?.context

        val bt_volver = view.findViewById<Button>(R.id.bt_volver)
        bt_volver.setOnClickListener {
            comunicador?.volver(contexto!!)
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Puedes llamar a actualizarLista aquí para garantizar que la vista ya se haya creado

            actualizarLista(jugadoresEnPartida)

    }

    fun actualizarLista(jugadoresEnPartida: List<JugadorEnPartida>) {
        val lista = view?.findViewById<RecyclerView>(R.id.rv_jugadores)
        val adapter = ListaAdapter(jugadoresEnPartida)
        lista?.layoutManager = LinearLayoutManager(contexto)
        lista?.adapter = adapter
    }
}
