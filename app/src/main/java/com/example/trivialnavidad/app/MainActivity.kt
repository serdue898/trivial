package com.example.trivialnavidad.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.principal.Principal

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            var principal = Principal()
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_view, menu)// OJO- se pasa la vista que se quiere inflar
        return true
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
        val msnEmergente = AlertDialog.Builder(this)
        val acercaDe = R.string.intrucciones
        msnEmergente.setMessage(getString(R.string.intrucciones))

        msnEmergente.show()
    }

    private fun acercaDe() {
        val msnEmergente = AlertDialog.Builder(this)
        val acercaDe = R.string.acercaDe
        msnEmergente.setMessage(getString(R.string.acercaDe))
        msnEmergente.show()
    }
}