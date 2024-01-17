package com.example.trivialnavidad.core.feature.minijuegos.repaso.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.repaso.viewModel.ComunicadorRepaso
import com.example.trivialnavidad.core.feature.minijuegos.repaso.viewModel.MetodosRepaso
import java.text.Normalizer

class Repaso(var pregunta : Pregunta, var jugador : JugadorEnPartida) : Fragment() {

    private var comunicador: ComunicadorRepaso? = MetodosRepaso()
    private var contexto: Context? = null
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle? ): View {
        val view = inflater.inflate(R.layout.minijuego_repaso, container, false)
        contexto = container?.context

        // enunciado comodin para generar los huecos
        val enunciadoComodin = pregunta.pregunta

        // asignar las preguntas a los elementos de la vista
        var respuestasRepaso : List<String> = listOf()
        respuestasRepaso= pregunta.respuestas
        val enunciado = view?.findViewById<TextView>(R.id.t_enunciadoRepaso)
        enunciado?.text= generarHuecosTexto(enunciadoComodin, respuestasRepaso)

        // falta coger las respuestas de la base de datos

        // declarar cada uno de spinner que va a contener la información aunque está seá repetida
        val sp_hueco1 = view.findViewById<Spinner>(R.id.sp_hueco1)
        val sp_hueco2 = view.findViewById<Spinner>(R.id.sp_hueco2)
        val sp_hueco3 = view.findViewById<Spinner>(R.id.sp_hueco3)
        val sp_hueco4 = view.findViewById<Spinner>(R.id.sp_hueco4)

        // para suplir el error que se muestra al poner this en el contexto hay que poner requireContext()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, respuestasRepaso)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_hueco1.adapter = adapter
        sp_hueco2.adapter = adapter
        sp_hueco3.adapter = adapter
        sp_hueco4.adapter = adapter



        val bt_corregir = view.findViewById<Button>(R.id.bt_repasoCorregir)

        bt_corregir.setOnClickListener(){

            val seleccion1 = sp_hueco1.selectedItem.toString()
            val seleccion2 = sp_hueco2.selectedItem.toString()
            val seleccion3 = sp_hueco3.selectedItem.toString()
            val seleccion4 = sp_hueco4.selectedItem.toString()

            if (seleccion1 == pregunta.correcta[0] &&
                seleccion2 == pregunta.correcta[1] &&
                seleccion3 == pregunta.correcta[2] &&
                seleccion4 == pregunta.correcta[3]
            ) {
                terminarJuego(true)
            } else {
                terminarJuego(false)
            }

        }
        return view
    }

    fun quitarTildes(palabra: String): String {
        val normalized = Normalizer.normalize(palabra, Normalizer.Form.NFD)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(normalized, "")
    }

    fun generarHuecosTexto(enunciado: String, respuestas: List<String>): String {
        var i = 1
        val enunciado_temporal = enunciado.split(" ")
        val enunciado_temporal2: MutableList<String> = mutableListOf()

        for (palabra in enunciado_temporal) {
            val palabraSinTilde = quitarTildes(palabra.lowercase()).replace("[^a-zA-Z]".toRegex(), "")
            if (palabraSinTilde in respuestas.map { quitarTildes(it.lowercase()) }) {
                enunciado_temporal2.add("hueco$i")
                i++
            } else {
                enunciado_temporal2.add(palabra)
            }
        }

        return enunciado_temporal2.joinToString(" ")
    }
    private fun terminarJuego(resultado: Boolean){
        var ganado = false
        val acercaDe: Int
        if (resultado) {

            acercaDe = R.string.acierto
            jugador.juegos[1] = true

            ganado = true
        } else {
            acercaDe = R.string.fallo
        }
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setMessage(getString(acercaDe))
        msnEmergente.setPositiveButton("Aceptar") { dialog, which ->
            comunicador?.volver(contexto!!,ganado)
        }
        msnEmergente.show()
    }

}