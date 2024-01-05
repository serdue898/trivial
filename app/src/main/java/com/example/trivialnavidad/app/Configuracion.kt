package com.example.trivialnavidad.app

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.principal.view.Principal

class Configuracion( context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("Configuracion", Context.MODE_PRIVATE)
    //musica
    fun guardarOpcionMusica(reproducir: Boolean) {
        prefs.edit().putBoolean("ReproducirMusica", reproducir).apply()
    }

    fun obtenerOpcionMusica(): Boolean {
        return prefs.getBoolean("ReproducirMusica", true) // Valor predeterminado: true
    }
    //sonido
    fun guardarOpcionSonido(reproducir: Boolean) {
        prefs.edit().putBoolean("ReproducirSonido", reproducir).apply()
    }
    fun obtenerOpcionSonido(): Boolean {
        return prefs.getBoolean("ReproducirSonido", true) // Valor predeterminado: true
    }
    //vibracion
    fun guardarOpcionVibracion(reproducir: Boolean) {
        prefs.edit().putBoolean("ReproducirVibracion", reproducir).apply()
    }
    fun obtenerOpcionVibracion(): Boolean {
        return prefs.getBoolean("ReproducirVibracion", true) // Valor predeterminado: true
    }
    //temas
    fun guardarOcionTemas(tema: Boolean) {
        prefs.edit().putBoolean("Tema", tema).apply()
    }
    fun obtenerOpcionTemas(): Boolean {
        return prefs.getBoolean("Tema", true) // Valor predeterminado: true
    }
    fun mostrarConfiguracion(contexto: Context,tema: Boolean = false) {
        val configuracion = MainActivity.configuracion
        val popup = AlertDialog.Builder(contexto as AppCompatActivity)

        val vista = LayoutInflater.from(contexto).inflate(R.layout.popup_configuracion, null)
        val musica = vista.findViewById<CheckBox>(R.id.ch_musica)
        val sonido = vista.findViewById<CheckBox>(R.id.ch_sonido)
        val vibracion = vista.findViewById<CheckBox>(R.id.ch_vibracion)
        val temas = vista.findViewById<CheckBox>(R.id.ch_temas)
        musica.isChecked = configuracion?.obtenerOpcionMusica()!!
        sonido.isChecked = configuracion.obtenerOpcionSonido()
        vibracion.isChecked = configuracion.obtenerOpcionVibracion()
        temas.isChecked = configuracion.obtenerOpcionTemas()
        musica.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    MainActivity.reproductor?.iniciarReproduccion()
                } else {
                    MainActivity.reproductor?.detenerReproduccion()
                }


        }
        temas.setOnCheckedChangeListener { _, isChecked ->
            if (tema ) {
                toggleDarkMode(contexto,isChecked)
                guardarOcionTemas(isChecked)
            }else{
                Toast.makeText(contexto,"Solo se cambia el tema en la pantalla de inicio",Toast.LENGTH_LONG).show()
                temas.isChecked = configuracion.obtenerOpcionTemas()
            }
        }
        popup.setView(vista)
        popup.setTitle("Configuración")
        popup.setCancelable(false)
        popup.setPositiveButton("Aceptar") { _, _ ->
            configuracion.guardarOpcionMusica(musica.isChecked)
            configuracion.guardarOpcionSonido(sonido.isChecked)
            configuracion.guardarOpcionVibracion(vibracion.isChecked)
        }
        val alert =popup.create()
        alert.show()
    }
    private var modoOscuroActual: Int = -1
     fun toggleDarkMode(  context: Context,encender: Boolean) {
         val nuevoModo = if (encender) {
             AppCompatDelegate.MODE_NIGHT_YES
         } else {
             AppCompatDelegate.MODE_NIGHT_NO
         }
         if (modoOscuroActual != nuevoModo) {
             AppCompatDelegate.setDefaultNightMode(nuevoModo) // Reiniciar la actividad solo si ha cambiado el modo
             modoOscuroActual = nuevoModo
         }
// Reiniciar la actividad para aplicar el cambio
    }

}