package com.example.trivialnavidad.core.feature.minijuegos.parejas.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.parejas.viewModel.ComunicadorParejas
import com.example.trivialnavidad.core.feature.minijuegos.parejas.viewModel.MetodosParejas
import java.text.Normalizer

class Parejas (val pregunta: Pregunta, val jugador: JugadorEnPartida): Fragment() {

    private var contexto: Context? = null
    private var comunicador: ComunicadorParejas= MetodosParejas()
    private var imagenesBase :MutableList<Drawable> = mutableListOf()
    var seleccionado : FrameLayout? = null
    private var aciertos = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_parejas, container, false)
        contexto = container?.context
        pregunta.respuestas.forEach { i ->
            val nombre = Normalizer.normalize(i, Normalizer.Form.NFD)
                .replace("[^\\p{ASCII}]".toRegex(), "")
                .lowercase()
            val id = obtenerResourceId(nombre)
            val drawable = ContextCompat.getDrawable(contexto!!, id)
            imagenesBase.add(drawable!!)
        }
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar5)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        val fr1 = view.findViewById<FrameLayout>(R.id.fr_1)
        val fr2 = view.findViewById<FrameLayout>(R.id.fr_2)
        val fr3 = view.findViewById<FrameLayout>(R.id.fr_3)
        val fr4 = view.findViewById<FrameLayout>(R.id.fr_4)
        fr1.addView(ImageView(contexto).apply {
            setImageDrawable(imagenesBase[0])
        })
        fr1.tag = pregunta.respuestas[0]
        fr2.addView(ImageView(contexto).apply {
            setImageDrawable(imagenesBase[1])
        })
        fr2.tag = pregunta.respuestas[1]
        fr3.addView(TextView(contexto).apply {
            text = pregunta.respuestas[1]
            background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra)
            gravity = Gravity.CENTER
        })
        fr3.tag = pregunta.respuestas[1]
        fr4.addView(TextView(contexto).apply {
            text = pregunta.respuestas[0]
            background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra)
            gravity = Gravity.CENTER
        })
        fr4.tag = pregunta.respuestas[0]
        fr1.setOnClickListener {
            actualizarCelda(fr1)
        }
        fr2.setOnClickListener {
            actualizarCelda(fr2)
        }
        fr3.setOnClickListener {
            actualizarCelda(fr3)
        }
        fr4.setOnClickListener {
            actualizarCelda(fr4)
        }
        return view
    }
    private fun obtenerResourceId(imageName: String): Int {
        // Aquí deberías implementar la lógica para obtener el ID del recurso de la imagen
        // Puedes hacer esto de acuerdo a cómo tienes organizados tus recursos de imágenes
        // En este ejemplo, asumo que tienes imágenes con nombres como "dado1", "dado2", etc.
        return R.drawable::class.java.getField(imageName).get(null) as Int
    }


    private fun actualizarCelda(frame : FrameLayout){
        if (seleccionado!=null){
            if (seleccionado!!.tag.toString()==frame.tag.toString()){
                frame.visibility = View.INVISIBLE
                seleccionado?.visibility = View.INVISIBLE
                seleccionado = null
                aciertos++
                if (aciertos==2){
                    terminarJuego(true)
                }
            }
            else {
                terminarJuego(false)
            }

        }
        else{
            seleccionado = frame
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_general_view, menu)// OJO- se pasa la vista que se quiere inflar

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.mItm_configuracion -> {
                MainActivity.configuracion?.mostrarConfiguracion(contexto!!)
                true
            }
            R.id.mItm_inicio -> {

                true
            }
            R.id.mItm_acerca -> {
                val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
                msnEmergente.setMessage(getString(R.string.acercaDe))
                msnEmergente.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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
        msnEmergente.setPositiveButton("Aceptar") { _, _ ->
            comunicador.volver(contexto!!,ganado)
        }
        msnEmergente.show()
    }




}
