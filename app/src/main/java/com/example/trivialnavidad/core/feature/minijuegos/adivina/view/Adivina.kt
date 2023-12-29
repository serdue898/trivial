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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.ComunicadorAdivina
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.MetodosAdivina

class Adivina : Fragment() {
    private var comunicador: ComunicadorAdivina? = MetodosAdivina()
    private var contexto: Context? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_adivina, container, false)
        contexto = container?.context


        val bt_clasificacion = view.findViewById<Button>(R.id.comprobar)
        val texto = view.findViewById<EditText>(R.id.respuesta)
        bt_clasificacion.setOnClickListener {
            val respuesta = texto.text.toString()
            var acercaDe: Int
            if (respuesta == "3") {
                 acercaDe = R.string.acierto

            } else {
                 acercaDe = R.string.fallo
            }
            val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)

            msnEmergente.setMessage(getString(acercaDe))
            msnEmergente.show()

        }
        return view
    }
}