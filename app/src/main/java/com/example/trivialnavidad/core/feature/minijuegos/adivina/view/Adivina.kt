package com.example.trivialnavidad.core.feature.juego.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.ComunicadorAdivina
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.MetodosAdivina

class Adivina(var pregunta :Pregunta,var jugador :JugadorEnPartida) : Fragment() {
    private var comunicador: ComunicadorAdivina? = MetodosAdivina()
    private var contexto: Context? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_adivina, container, false)
        contexto = container?.context


        val bt_clasificacion = view.findViewById<Button>(R.id.comprobar)
        val tx_pregunta = view.findViewById<TextView>(R.id.pregunta)
        val texto = view.findViewById<EditText>(R.id.respuesta)
        var ganado = false
        tx_pregunta.text = pregunta.pregunta
        bt_clasificacion.setOnClickListener {
            val respuesta = texto.text.toString()
            var acercaDe: Int
            if (respuesta == pregunta.correcta) {
                 acercaDe = R.string.acierto
                jugador.juegos[0] = true
                ganado = true
            } else {
                 acercaDe = R.string.fallo
            }
            val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
            msnEmergente.setCancelable(false)
            msnEmergente.setMessage(getString(acercaDe))
            msnEmergente.setPositiveButton("Aceptar") { dialog, which ->
                comunicador?.volver(contexto as AppCompatActivity , ganado)
            }
            msnEmergente.show()

        }
        return view
    }
}