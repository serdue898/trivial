package com.example.trivialnavidad.core.feature.juego

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.jugabilidad.Dado

class Juego : Fragment() {
    private var comunicador: Comunicador? = null
    private var contexto: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        comunicador = context as? Comunicador
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.juego, container, false)
        contexto = container?.context
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar2)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val boton = view.findViewById<Button>(R.id.bt_clasificacion)
        val bt_dado = view.findViewById<Button>(R.id.bt_dado)
        val dado = view.findViewById<ImageView>(R.id.dado)
        val handler = Dado(dado)





        bt_dado.setOnClickListener {
            handler.tiradaDado()
        }

        // Se devuelve la vista inflada.
        return view
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            val inflater: MenuInflater = (contexto as AppCompatActivity).menuInflater
            inflater.inflate(R.menu.menu_view, menu)// OJO- se pasa la vista que se quiere inflar

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.mItm_juegoNuevo -> {
                //juegoNuevo()
                true
            }

            R.id.mItm_guardar -> {
                //guardarDatos()
                true
            }

            R.id.mItm_instrucciones -> {
                verInstrucciones()
                true
            }

            R.id.mItm_acerca -> {
                acercaDe()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun verInstrucciones() {
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        val acercaDe = R.string.intrucciones
        msnEmergente.setMessage(getString(R.string.intrucciones))

        msnEmergente.show()
    }

    private fun acercaDe() {
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        val acercaDe = R.string.acercaDe
        msnEmergente.setMessage(getString(R.string.acercaDe))
        msnEmergente.show()
    }




}