package com.example.trivialnavidad.core.feature.juego.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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

class Adivina(var pregunta :Pregunta,var jugador :JugadorEnPartida) : Fragment() {
    private var comunicador: ComunicadorAdivina? = MetodosAdivina()
    private var contexto: Context? = null


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_adivina, container, false)
        contexto = container?.context



        val tx_pregunta = view.findViewById<TextView>(R.id.t_pregunta)
        val palabraAdivinar = pregunta.correcta
        val palabraDividida = view.findViewById<EditText>(R.id.t_palabraAdivina)
        var ganado = false
        tx_pregunta.text = pregunta.pregunta
        val gr_abecedario = view.findViewById<GridLayout>(R.id.gr_botonesLetras)

        val abecedario = CharArray(27) { ('a' + it).toChar() }
        abecedario[26] = 'ñ'
        var palabraHuecos = desgranarPalabra(palabraAdivinar)
        // no entiendo porque no me esta dejando asignar el string al txtview de la palabra hecha trocitos
        palabraDividida.text= palabraHuecos

        for (letra in abecedario){
            var botonLetra: Button = Button(contexto)
            botonLetra.text= letra.toString()
            /* añadir los margenes a los botones para que esten separados pero me pide hacer un import que le pincho y no lo hace :S
            botonLetra.marginBottom(5)
            botonLetra.marginTop(5)
            botonLetra.marginLeft(5)
            botonLetra.marginRight(5)
            */
            val params = GridLayout.LayoutParams()
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT

            // Agrega el botón al GridLayout con los parámetros de diseño
            gr_abecedario.addView(botonLetra, params)

            botonLetra.setOnClickListener(){

                if(comprobarLetra(botonLetra, palabraAdivinar )){
                    botonLetra.setBackgroundColor(R.color.verde)
                    palabraHuecos=rellenarPalabra(palabraHuecos, palabraAdivinar, botonLetra.text.toString())
                }else {
                    botonLetra.setBackgroundColor(R.color.rojo)
                }
                if(comprobarPlabra(palabraHuecos)){
                    val mensaje = AlertDialog.Builder(contexto as AppCompatActivity)
                    mensaje.setMessage("Enhorabuena, has resuelto correctamente la prueba.")
                    mensaje.show()
                }else{

                }
            }
        }

        return view
    }

    fun desgranarPalabra(palabra: String) : String{
       var palabraDividida : String= ""
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for(hueco in arrayDeLetras){
           palabraDividida= palabraDividida +"_ "
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
        var completa= false
        val arrayDeLetras = palabraDividida.toCharArray().map { it.toString() }.toTypedArray()
        for (hueco in palabraDividida){
            if (hueco.equals("_")){
                return completa
            }else{
                completa= true
            }
        }

        return completa
    }
    fun rellenarPalabra(palabraDividida: String, palabraAdivinar: String, letra: String) : String{
        var palabraHuecosActualizados = ""
        val letrasAdivinar = palabraAdivinar.toCharArray().map { it.toString() }.toTypedArray()
        val letrasDividida = palabraDividida.toCharArray().map { it.toString() }.toTypedArray()
        var posicion = 0

        for(letraAdivinar in letrasAdivinar){
            var i =0
            if(letraAdivinar == letra){
                posicion= i
            }
            i++
        }
        for(letrahueco in letrasDividida){
            var i = 0
            if(i == posicion){
                palabraHuecosActualizados= palabraHuecosActualizados + letra
            }else{
                palabraHuecosActualizados= palabraHuecosActualizados + "_ "
            }

        }

        return  palabraHuecosActualizados
    }
}