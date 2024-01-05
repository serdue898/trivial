package com.example.trivialnavidad.core.feature.minijuegos.parejas.view

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.parejas.adapter.ListaAdapterParejas
import com.example.trivialnavidad.core.feature.minijuegos.parejas.viewModel.ComunicadorParejas
import com.example.trivialnavidad.core.feature.minijuegos.parejas.viewModel.MetodosParejas

class Parejas (val pregunta: Pregunta, val jugador: JugadorEnPartida): Fragment() {

    private var contexto: Context? = null
    private var comunicador: ComunicadorParejas? = null
    private var imagenes :TypedArray? = null
    var seleccionado : String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_parejas, container, false)
        contexto = container?.context
        comunicador = MetodosParejas()
        imagenes = contexto!!.resources.obtainTypedArray(R.array.imagenesParejas)
        val comprobar = view.findViewById<Button>(R.id.bt_comprobar)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar5)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        val fr1 = view.findViewById<FrameLayout>(R.id.fr_1)
        val fr2 = view.findViewById<FrameLayout>(R.id.fr_2)
        val fr3 = view.findViewById<FrameLayout>(R.id.fr_3)
        val fr4 = view.findViewById<FrameLayout>(R.id.fr_4)
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
        var opciones = pregunta.respuestas
        val lista = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_preguntas)
        val layoutManager = LinearLayoutManager(contexto)
        lista?.layoutManager = layoutManager
        val adaptador = ListaAdapterParejas(opciones, contexto!!, this)
        lista.adapter = adaptador
        comprobar.setOnClickListener {
            comprovar()
        }
        return view
    }
    fun comprovar (){

        val fr1 = view?.findViewById<FrameLayout>(R.id.fr_1)
        val fr2 = view?.findViewById<FrameLayout>(R.id.fr_2)
        val fr3 = view?.findViewById<FrameLayout>(R.id.fr_3)
        val fr4 = view?.findViewById<FrameLayout>(R.id.fr_4)
        var ganado = false
        val acercaDe: Int
        val correcta1 = pregunta.respuestas.get( pregunta.correcta[0].split("_")[0] .toInt())
        val correcta2 = pregunta.respuestas.get( pregunta.correcta[0].split("_")[1] .toInt())
        val correcta3 = pregunta.respuestas.get( pregunta.correcta[1].split("_")[0] .toInt())
        val correcta4 = pregunta.respuestas.get( pregunta.correcta[1].split("_")[1] .toInt())

        var respuesta1 =  fr1?.tag.toString() == correcta1 && fr2?.tag.toString() == correcta2
        val respuesta2 = fr3?.tag.toString() == correcta3 && fr4?.tag.toString() == correcta4
        if ( respuesta1 && respuesta2) {
            acercaDe = R.string.acierto
            jugador.juegos[3] = true
            ganado = true
        } else {
            acercaDe = R.string.fallo
        }
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setMessage(getString(acercaDe))
        msnEmergente.setPositiveButton("Aceptar") { dialog, which ->
            comunicador?.volver(contexto as AppCompatActivity , ganado)
        }
        msnEmergente.show()

    }

    fun actualizarCelda(frame : FrameLayout){

        if (seleccionado!=null){
            frame.removeAllViews()
            val contenido = seleccionado!!.toIntOrNull()

            if (contenido==null){
                val texto = TextView(contexto)
                texto.text = seleccionado
                frame.tag = seleccionado
                frame.addView(texto)

            }else{
                frame.tag = contenido
                val imagen = ImageView(contexto)
                imagen.setImageResource(imagenes!!.getResourceId(contenido,0))
                frame.addView(imagen)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = (contexto as AppCompatActivity).menuInflater
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
                val msnEmergente = androidx.appcompat.app.AlertDialog.Builder(contexto as AppCompatActivity)
                msnEmergente.setMessage(getString(R.string.acercaDe))
                msnEmergente.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




}
