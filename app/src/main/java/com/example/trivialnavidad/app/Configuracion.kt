package com.example.trivialnavidad.app

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R

class Configuracion(context: Context) {

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
    fun guardarOcionTemas(tema: Int) {
        prefs.edit().putInt("Tema", tema).apply()
    }
    fun obtenerOpcionTemas(): Int {
        return prefs.getInt("Tema", 0) // Valor predeterminado: true
    }
    fun mostrarConfiguracion(context: Context) {
        val configuracion = MainActivity.configuracion
        val popup = AlertDialog.Builder(context as AppCompatActivity)
        val vista = LayoutInflater.from(context).inflate(R.layout.popup_configuracion, null)
        val musica = vista.findViewById<CheckBox>(R.id.ch_musica)
        val sonido = vista.findViewById<CheckBox>(R.id.ch_sonido)
        val vibracion = vista.findViewById<CheckBox>(R.id.ch_vibracion)
        musica.isChecked = configuracion?.obtenerOpcionMusica()!!
        sonido.isChecked = configuracion.obtenerOpcionSonido()
        vibracion.isChecked = configuracion.obtenerOpcionVibracion()
        musica.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                MainActivity.reproductor?.iniciarReproduccion()
            } else {
                MainActivity.reproductor?.detenerReproduccion()
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
        popup.show()
    }
}