package com.example.trivialnavidad.core.feature.minijuegos.adivina.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.ComunicadorAdivina
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.MetodosAdivina
import java.text.Normalizer

class Adivina(var pregunta :Pregunta,var jugador :JugadorEnPartida) : Fragment() {
    private var comunicador: ComunicadorAdivina? = MetodosAdivina()
    private var contexto: Context? = null
    private var fallos = 0
    private var mostradoFinal = false




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_adivina, container, false)
        contexto = container?.context
        val imagenes = arrayOf(R.drawable.ahorcado_1, R.drawable.ahorcado_2, R.drawable.ahorcado_3, R.drawable.ahorcado_4, R.drawable.ahorcado_5, R.drawable.ahorcado_6)



        val imagen = view.findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.imagenAhorcado)
        val txPregunta = view.findViewById<TextView>(R.id.t_pregunta)
        val palabraAdivinar = Normalizer.normalize(pregunta.correcta[0], Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")
            .lowercase()
        val palabraDividida = view.findViewById<TextView>(R.id.t_palabraAdivina)
        txPregunta.text = pregunta.pregunta
        val grAbecedario = view.findViewById<GridLayout>(R.id.gr_botonesLetras)

        val abecedario = CharArray(27) { ('a' + it) }
        abecedario[26] = 'ñ'
        var palabraHuecos = desgranarPalabra(palabraAdivinar)
        // no entiendo porque no me esta dejando asignar el string al txtview de la palabra hecha trocitos
        palabraDividida.text= palabraHuecos
        grAbecedario.rowCount=9
        grAbecedario.columnCount=4
        for (letra in abecedario){
            val botonLetra = Button(contexto)
            botonLetra.text= letra.toString()
            botonLetra.background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra)


            val params = GridLayout.LayoutParams()
            params.width = GridLayout.LayoutParams.WRAP_CONTENT
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.setMargins(5,5,5,5)

            // Agrega el botón al GridLayout con los parámetros de diseño
            grAbecedario.addView(botonLetra, params)

            botonLetra.setOnClickListener{
                if (mostradoFinal)
                    return@setOnClickListener
                botonLetra.isEnabled = false
                if(comprobarLetra(botonLetra, palabraAdivinar )){
                    botonLetra.background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra_acertada)
                    palabraHuecos=rellenarPalabra(palabraHuecos.replace(" ",""), palabraAdivinar, botonLetra.text.toString())
                    palabraDividida.text= palabraHuecos
                }else {
                    botonLetra.background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra_fallada)
                    fallos++
                    if (fallos>6)
                        fallos=6
                    imagen.setImageResource(imagenes[fallos-1])
                }
                if (fallos>=6 ){
                    mostradoFinal=true
                    terminarJuego(false)
                }
                if(comprobarPlabra(palabraHuecos.replace(" ",""))){
                    mostradoFinal=true
                    terminarJuego(true)
                }
            }
        }

        return view
    }

    private fun desgranarPalabra(palabra: String) : String{
       var palabraDividida = ""
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for(hueco in arrayDeLetras){
            palabraDividida += "_ "
        }
        return palabraDividida
    }

    private fun comprobarLetra(botonPulsado :Button, palabra: String): Boolean {
        var correcto = false
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for(hueco in arrayDeLetras){
            if(botonPulsado.text== hueco){

                correcto= true
            }
        }
        return correcto
    }
    private fun comprobarPlabra(palabraDividida:String): Boolean{

        val arrayDeLetras = palabraDividida.toCharArray().map { it.toString() }.toTypedArray()
        for (hueco in arrayDeLetras){
            if (hueco == "_"){
                return false
            }
        }

        return true
    }
    private fun rellenarPalabra(palabraHuecos: String, palabraAdivinar: String, letra: String) : String{
        var palabraHuecosActualizados = ""
        val letrasAdivinar = palabraAdivinar.toCharArray().map { it.toString() }.toTypedArray()
        val letrasDividida = palabraHuecos.toCharArray().map { it.toString() }.toTypedArray()

        for ((posicion, hueco) in letrasDividida.withIndex()){
            palabraHuecosActualizados += if (letrasAdivinar[posicion]==letra){
                "$letra "
            }else{
                "$hueco "
            }
        }
        return  palabraHuecosActualizados
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
        msnEmergente.setPositiveButton((contexto as AppCompatActivity).getString(R.string.aceptar)) { _, _ ->
            comunicador?.volver(contexto!!,ganado)
        }
        msnEmergente.show()
    }
}