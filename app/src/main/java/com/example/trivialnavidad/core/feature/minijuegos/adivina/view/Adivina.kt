package com.example.trivialnavidad.core.feature.juego.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.ComunicadorAdivina
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.MetodosAdivina
import java.util.Locale

class Adivina(var pregunta :Pregunta,var jugador :JugadorEnPartida) : Fragment() {
    private var comunicador: ComunicadorAdivina? = MetodosAdivina()
    private var contexto: Context? = null



    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_adivina, container, false)
        contexto = container?.context



        val tx_pregunta = view.findViewById<TextView>(R.id.t_pregunta)
        var palabraAdivinar = pregunta.correcta[0].lowercase()
        palabraAdivinar="patata"
        val palabraDividida = view.findViewById<TextView>(R.id.t_palabraAdivina)
        var ganado = false
        tx_pregunta.text = pregunta.pregunta
        val gr_abecedario = view.findViewById<GridLayout>(R.id.gr_botonesLetras)

        val abecedario = CharArray(27) { ('a' + it).toChar() }
        abecedario[26] = 'ñ'
        var palabraHuecos = desgranarPalabra(palabraAdivinar)
        // no entiendo porque no me esta dejando asignar el string al txtview de la palabra hecha trocitos
        palabraDividida.text= palabraHuecos
        gr_abecedario.rowCount=9
        gr_abecedario.columnCount=4
        for (letra in abecedario){
            var botonLetra: Button = Button(contexto)
            botonLetra.text= letra.toString()
            botonLetra.background = contexto!!.getDrawable(R.drawable.fondo_letra)


            val params = GridLayout.LayoutParams()
            params.width = GridLayout.LayoutParams.WRAP_CONTENT
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.setMargins(5,5,5,5)

            // Agrega el botón al GridLayout con los parámetros de diseño
            gr_abecedario.addView(botonLetra, params)

            botonLetra.setOnClickListener(){
                botonLetra.isEnabled = false
                if(comprobarLetra(botonLetra, palabraAdivinar )){
                    botonLetra.background = contexto!!.getDrawable(R.drawable.fondo_letra_acertada)
                    palabraHuecos=rellenarPalabra(palabraHuecos.replace(" ",""), palabraAdivinar, botonLetra.text.toString())
                    palabraDividida.text= palabraHuecos
                }else {
                    botonLetra.background = contexto!!.getDrawable(R.drawable.fondo_letra_fallada)
                }
                if(comprobarPlabra(palabraHuecos.replace(" ",""))){
                    val mensaje = AlertDialog.Builder(contexto as AppCompatActivity)
                    mensaje.setCancelable(false)

                    mensaje.setMessage("Enhorabuena, has resuelto correctamente la prueba.")
                    mensaje.setPositiveButton("Aceptar") { dialog, which ->
                        terminarJuego(true)
                    }
                    mensaje.show()
                }
            }
        }

        return view
    }

    fun desgranarPalabra(palabra: String) : String{
       var palabraDividida : String= ""
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for(hueco in arrayDeLetras){
            palabraDividida += "_ "
        }
        return palabraDividida
    }

    fun comprobarLetra(botonPulsado :Button, palabra: String): Boolean {
        var correcto = false
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for(hueco in arrayDeLetras){
            if(botonPulsado.text== hueco){

                correcto= true
            }
        }
        return correcto
    }
    fun comprobarPlabra(palabraDividida:String): Boolean{

        val arrayDeLetras = palabraDividida.toCharArray().map { it.toString() }.toTypedArray()
        for (hueco in arrayDeLetras){
            if (hueco == "_"){
                return false
            }
        }

        return true
    }
    fun rellenarPalabra(palabraHuecos: String, palabraAdivinar: String, letra: String) : String{
        var palabraHuecosActualizados = ""
        val letrasAdivinar = palabraAdivinar.toCharArray().map { it.toString() }.toTypedArray()
        val letrasDividida = palabraHuecos.toCharArray().map { it.toString() }.toTypedArray()
        var posicion = 0

        for (hueco in letrasDividida){
            if (letrasAdivinar[posicion]==letra){
                palabraHuecosActualizados += "$letra "
            }else{
                palabraHuecosActualizados += "$hueco "
            }
            posicion++
        }
        return  palabraHuecosActualizados
    }
    private fun terminarJuego(resultado: Boolean){
        var ganado = false
        val acercaDe: Int
        if (resultado) {

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
            comunicador?.volver(contexto!!,ganado)
        }
        msnEmergente.show()
    }
}