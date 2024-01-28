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
    private val respuestascorrectas = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle? ): View {
        val view = inflater.inflate(R.layout.minijuego_repaso, container, false)
        contexto = container?.context

        // enunciado comodin para generar los huecos
        val enunciadoComodin = pregunta.pregunta

        // asignar las preguntas a los elementos de la vista
        val respuestasRepaso : List<String> = pregunta.respuestas
        val enunciado = view?.findViewById<TextView>(R.id.t_enunciadoRepaso)
        enunciado?.text =generarHuecosTexto(enunciadoComodin, respuestasRepaso)

        // falta coger las respuestas de la base de datos

        // declarar cada uno de spinner que va a contener la información aunque está seá repetida
        val spHueco1 = view.findViewById<Spinner>(R.id.sp_hueco1)
        val spHueco2 = view.findViewById<Spinner>(R.id.sp_hueco2)
        val spHueco3 = view.findViewById<Spinner>(R.id.sp_hueco3)
        val spHueco4 = view.findViewById<Spinner>(R.id.sp_hueco4)

        // para suplir el error que se muestra al poner this en el contexto hay que poner requireContext()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, respuestasRepaso)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spHueco1.adapter = adapter
        spHueco2.adapter = adapter
        spHueco3.adapter = adapter
        spHueco4.adapter = adapter



        val btCorregir = view.findViewById<Button>(R.id.bt_repasoCorregir)

        btCorregir.setOnClickListener{
            val seleccion1 = quitarTildes( spHueco1.selectedItem.toString().lowercase()).replace("[^a-zA-Z]".toRegex(), "")
            val seleccion2 = quitarTildes( spHueco2.selectedItem.toString().lowercase()).replace("[^a-zA-Z]".toRegex(), "")
            val seleccion3 = quitarTildes( spHueco3.selectedItem.toString().lowercase()).replace("[^a-zA-Z]".toRegex(), "")
            val seleccion4 = quitarTildes( spHueco4.selectedItem.toString().lowercase()).replace("[^a-zA-Z]".toRegex(), "")

            if (seleccion1 == respuestascorrectas[0] &&
                seleccion2 == respuestascorrectas[1] &&
                seleccion3 == respuestascorrectas[2] &&
                seleccion4 == respuestascorrectas[3]
            ) {
                terminarJuego(true)
            } else {
                terminarJuego(false)
            }

        }
        return view
    }

    private fun quitarTildes(palabra: String): String {
        val normalized = Normalizer.normalize(palabra, Normalizer.Form.NFD)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(normalized, "")
    }

    private fun generarHuecosTexto(enunciado: String, respuestas: List<String>): String {
        var i = 1
        val enunciadoTemporal = enunciado.split(" ")
        val enunciadoTemporal2: MutableList<String> = mutableListOf()

        for (palabra in enunciadoTemporal) {
            val palabraSinTilde = quitarTildes(palabra.lowercase()).replace("[^a-zA-Z]".toRegex(), "")
            if (palabraSinTilde in respuestas.map { quitarTildes(it.lowercase()) }) {
                if (i <=4) {
                    respuestascorrectas.add(palabraSinTilde)
                    enunciadoTemporal2.add("__($i)__")
                    i++
                }
            } else {
                enunciadoTemporal2.add(palabra)
            }
        }

        return enunciadoTemporal2.joinToString(" ")
    }
    private fun terminarJuego(resultado: Boolean){
        var ganado = false
        val acercaDe: Int
        if (resultado) {

            acercaDe = R.string.acierto
            jugador.juegos[2] = true

            ganado = true
        } else {
            acercaDe = R.string.fallo
        }
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setMessage(getString(acercaDe))
        msnEmergente.setPositiveButton((contexto as AppCompatActivity).getString(R.string.aceptar)) { _, _ ->
            comunicador?.volver(contexto!!,ganado)
        }
        msnEmergente.show()
    }

}