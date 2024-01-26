package com.example.trivialnavidad.core.feature.minijuegos.test.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.test.viewmodel.ComunicadorTest
import com.example.trivialnavidad.core.feature.minijuegos.test.viewmodel.MetodosTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Test(private val preguntas: List<Pregunta>, val  jugador: JugadorEnPartida, private val final :Boolean, private val tipo:String): Fragment() {
    private var comunicador: ComunicadorTest? =MetodosTest()
    private var contexto: Context? = null
    private var puntos = 0
    private var opciones = mutableListOf(0, 1, 2, 3)
    private var botones = listOf<Button>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.minijuego_test, container, false)
        contexto = container?.context
        empezarJuego(preguntas[puntos],view)
        return view
    }

    private fun empezarJuego(pregunta: Pregunta, view: View) {

         botones = listOf(
            view.findViewById(R.id.bt_opcionA),
            view.findViewById(R.id.bt_opcionB),
            view.findViewById(R.id.bt_opcionC),
            view.findViewById(R.id.bt_opcionD)
        )

        val texto = view.findViewById<TextView>(R.id.tx_enunciado)
        texto.text = pregunta.pregunta

        botones.forEach { boton ->
            setRandomOption(boton, opciones, pregunta.respuestas)
            boton.setOnClickListener { verificarRespuesta(it as Button, pregunta,view) }
        }

    }


    private fun verificarRespuesta(boton: Button, pregunta: Pregunta, view: View) {
        val correcto = boton.text.equals(pregunta.correcta[0])
        if (correcto) puntos++

        if (final) {
            terminarJuego(resultado = false, final = true)
        } else {
            if (puntos == 5) {
                terminarJuego(correcto, false)
            } else if (!correcto) {
                lifecycleScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.Main) {
                        boton.setBackgroundColor(ContextCompat.getColor(contexto!!, R.color.rojo))

                        botones.forEach { boton1 ->
                            if (boton1.text.equals(pregunta.correcta[0])) {
                                boton1.setBackgroundColor(ContextCompat.getColor(contexto!!, R.color.verde))
                            }
                        }
                        delay(500)
                        terminarJuego(false, final = false)
                    }
                }

            } else {
                val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
                msnEmergente.setCancelable(false)
                msnEmergente.setMessage(getString(R.string.has_acertado_sigue_jugando))
                msnEmergente.setPositiveButton("Aceptar") { _, _ ->
                    opciones = mutableListOf(0, 1, 2, 3)
                    empezarJuego(preguntas[puntos], view)
                }
                msnEmergente.show()
            }
        }
    }
    private fun setRandomOption(button: Button, opciones: MutableList<Int>, respuestas: List<String>) {
        val opcion = opciones[Random.nextInt(0, opciones.size)]
        opciones.remove(opcion)
        button.text = respuestas[opcion]
    }
    private fun terminarJuego(resultado: Boolean,final: Boolean){
        var ganado = false
        val acercaDe: Int
        if (resultado) {
            if (final) {
                acercaDe = R.string.ganado

            } else {
                acercaDe = R.string.acierto
                jugador.juegos[0] = true
            }
            ganado = true
        } else {
            acercaDe = R.string.fallo
        }
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setMessage(getString(acercaDe))
        msnEmergente.setPositiveButton("Aceptar") { _, _ ->
            comunicador?.volver(contexto!!,ganado,final,jugador, tipo)
        }
        msnEmergente.show()
    }
}