package com.example.trivialnavidad.core.feature.juego

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.test.viewmodel.ComunicadorTest
import com.example.trivialnavidad.core.feature.minijuegos.test.viewmodel.MetodosTest

class MinijuegoTest_fragment(val pregunta: Pregunta ,val  jugador: JugadorEnPartida ,val final :Boolean): Fragment() {
    private var comunicador: ComunicadorTest? =MetodosTest();
    private var contexto: Context? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_minijuego_test, container, false)
        contexto = container?.context
        var correcto = false

        val b_opcionA= view.findViewById<Button>(R.id.bt_opcionA)
        val b_opcionB= view.findViewById<Button>(R.id.bt_opcionB)
        val b_opcionC= view.findViewById<Button>(R.id.bt_opcionC)
        val b_opcionD= view.findViewById<Button>(R.id.bt_opcionD)
        val texto = view.findViewById<TextView>(R.id.tx_enunciado)
        b_opcionA.background = contexto!!.getDrawable(R.color.negro)
        texto.text = pregunta.pregunta
        b_opcionA.text = pregunta.respuestas[0]
        b_opcionB.text = pregunta.respuestas[1]
        b_opcionC.text = pregunta.respuestas[2]
        b_opcionD.text = pregunta.respuestas[3]

        // se declara y asigna un listener para todos los botones
        // entiendo que se tiene que hacer la comprobacion de la respuesta pero no se como, tomamos los datos de la base de datos?
        val listener = View.OnClickListener { boton->

            when (boton.id) {
                R.id.bt_opcionA -> {
                   if(b_opcionA.text.equals(pregunta.correcta[0])){
                       correcto= true
                   }

                }
                R.id.bt_opcionB -> {
                    if(b_opcionB.text.equals(pregunta.correcta[0])){
                        correcto= true
                    }
                }
                R.id.bt_opcionC -> {
                    if(b_opcionC.text.equals(pregunta.correcta[0])){
                        correcto= true
                    }
                }
                R.id.bt_opcionD -> {
                    if(b_opcionD.text.equals(pregunta.correcta[0])){
                        correcto= true
                    }
                }
            }
           // comunicador?.enviarDatos("la respuesta ha sido " + correcto)
            terminarJuego(correcto,final)
        }

        b_opcionA.setOnClickListener(listener)
        b_opcionB.setOnClickListener(listener)
        b_opcionC.setOnClickListener(listener)
        b_opcionD.setOnClickListener(listener)


        return view
    }
    private fun terminarJuego(resultado: Boolean,final: Boolean){
        var ganado = false
        val acercaDe: Int
        if (resultado) {
            if (final) {
                acercaDe = R.string.ganado

            } else {
                acercaDe = R.string.acierto
                jugador.juegos[2] = true
            }
            ganado = true
        } else {
            acercaDe = R.string.fallo
        }
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setMessage(getString(acercaDe))
        msnEmergente.setPositiveButton("Aceptar") { dialog, which ->
            comunicador?.volver(contexto!!,ganado,final)
        }
        msnEmergente.show()
    }
}