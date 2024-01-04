package com.example.trivialnavidad.core.feature.principal.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.principal.viewModel.ComunicadorPrincipal
import com.example.trivialnavidad.core.feature.principal.viewModel.MetodosPrincipal

class Principal : Fragment() {
    private var comunicador: ComunicadorPrincipal? = MetodosPrincipal()
    private var contexto: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.principal, container, false)
        contexto = container?.context

        val botonOff = view.findViewById<Button>(R.id.bt_offline)
        val botonOn = view.findViewById<Button>(R.id.bt_online)
        val botonNuevaPartida = view.findViewById<Button>(R.id.bt_nueva)
        val botonCargarPartida = view.findViewById<Button>(R.id.bt_cargar)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (contexto as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)


        botonNuevaPartida.setOnClickListener {
            comunicador?.abrir_juego("nueva",contexto!!)
        }
        botonCargarPartida.setOnClickListener {
            comunicador?.abrir_juego("cargar",contexto!!)
        }
        botonOff.setOnClickListener {

            botonCargarPartida.visibility = View.VISIBLE
            botonNuevaPartida.visibility = View.VISIBLE
            botonOff.visibility = View.INVISIBLE
            botonOn.visibility = View.INVISIBLE

        }
        botonOn.setOnClickListener {
            comunicador?.abrir_juego("online",contexto!!)
        }

        // Se devuelve la vista inflada.
        return view
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.mItm_inicio)
        item.isVisible = false
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
            R.id.mItm_acerca -> {
                val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
                msnEmergente.setMessage(getString(R.string.acercaDe))
                msnEmergente.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




}