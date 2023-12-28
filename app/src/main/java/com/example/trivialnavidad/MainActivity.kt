package com.example.trivialnavidad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val b_derecho = findViewById<Button>(R.id.b_derecho)

        val b_izquierdo = findViewById<Button>(R.id.b_izquierdo)

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
            R.id.mItm_numJugadores -> {
                numeroJugadores()
                true
            }

            R.id.mItm_instrucciones-> {
                verInstrucciones()
                true
            }

            R.id.mItm_acerca-> {
                acercaDe()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun numeroJugadores(){
        val radioGroup = RadioGroup(this)

        for( i in 2..6){

            val jugadores =RadioButton(this)
            jugadores.text= i.toString() + " jugadores"
            radioGroup.addView(jugadores)
        }


        val msnEmergente = AlertDialog.Builder(this)

        msnEmergente.setView(radioGroup)
        msnEmergente.setPositiveButton("Aceptar") {dialog, which->
            /*
            se crea una partida nueva con el numero de jugadores indicado
            en caso de tener una partida empezada se pregunta si se desea guardar la partida
             */
        }

        msnEmergente.show()
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