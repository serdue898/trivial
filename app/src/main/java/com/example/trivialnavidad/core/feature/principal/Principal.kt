package com.example.trivialnavidad.core.feature.principal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R

class Principal : Fragment() {
    private var comunicador: ComunicadorPrincipal? = Metodos()
    private var contexto: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.principal, container, false)
        contexto = container?.context

        val botonOff = view.findViewById<Button>(R.id.bt_offline)
        val botonOn = view.findViewById<Button>(R.id.bt_online)


        botonOff.setOnClickListener {
            comunicador?.abrir_juego("offline",contexto!!)

        }
        botonOn.setOnClickListener {
            comunicador?.abrir_juego("online",contexto!!)
        }

        // Se devuelve la vista inflada.
        return view
    }




}