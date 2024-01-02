package com.example.trivialnavidad.core.feature.juego

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R

class MinijuegoTest_fragment: Fragment() {
    private var comunicador: Comunicador? =null;

    override fun onAttach(context: Context) {
        super.onAttach(context)

        comunicador = context as? Comunicador
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val vista_test = inflater.inflate(R.layout.fragment_minijuego_test, container, false)

        var correcto = false

        val b_opcionA= vista_test.findViewById<Button>(R.id.b_opcionA)
        val b_opcionB= vista_test.findViewById<Button>(R.id.b_opcionB)
        val b_opcionC= vista_test.findViewById<Button>(R.id.b_opcionC)
        val b_opcionD= vista_test.findViewById<Button>(R.id.b_opcionD)

        // se declara y asigna un listener para todos los botones
        // entiendo que se tiene que hacer la comprobacion de la respuesta pero no se como, tomamos los datos de la base de datos?
        val listener = View.OnClickListener { boton->

            when (boton.id) {
                R.id.b_opcionA -> {
                   if(b_opcionA.text.equals("respuesta correcta")){
                       correcto= true
                   }

                }
                R.id.b_opcionB -> {
                    if(b_opcionB.text.equals("respuesta correcta")){
                        correcto= true
                    }
                }
                R.id.b_opcionC -> {
                    if(b_opcionC.text.equals("respuesta correcta")){
                        correcto= true
                    }
                }
                R.id.b_opcionD -> {
                    if(b_opcionD.text.equals("respuesta correcta")){
                        correcto= true
                    }
                }
            }
            comunicador?.enviarDatos("la respuesta ha sido " + correcto)
        }

        b_opcionA.setOnClickListener(listener)
        b_opcionB.setOnClickListener(listener)
        b_opcionC.setOnClickListener(listener)
        b_opcionD.setOnClickListener(listener)


        return view
    }
}