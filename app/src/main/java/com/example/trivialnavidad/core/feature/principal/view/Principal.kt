package com.example.trivialnavidad.core.feature.principal.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.principal.viewModel.ComunicadorPrincipal
import com.example.trivialnavidad.core.feature.principal.viewModel.MetodosPrincipal

class Principal : Fragment() {
    private var comunicador: ComunicadorPrincipal? = MetodosPrincipal()
    private var contexto: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.principal, container, false)
        contexto = container?.context

        val botonOff = view.findViewById<Button>(R.id.bt_offline)
        val botonOn = view.findViewById<Button>(R.id.bt_online)
        val botonNuevaPartida = view.findViewById<Button>(R.id.bt_nueva)
        val botonCargarPartida = view.findViewById<Button>(R.id.bt_cargar)

        botonNuevaPartida.setOnClickListener {
            comunicador?.abrir_juego("nueva",contexto!!)
        }
        botonCargarPartida.setOnClickListener {
            comunicador?.abrir_juego("cargar",contexto!!)
        }
        botonOff.setOnClickListener {

            botonCargarPartida.visibility = View.VISIBLE
            botonNuevaPartida.visibility = View.VISIBLE
            botonOff.visibility = View.INVISIBLE
            botonOn.visibility = View.INVISIBLE

        }
        botonOn.setOnClickListener {
            comunicador?.abrir_juego("online",contexto!!)
        }

        // Se devuelve la vista inflada.
        return view
    }




}