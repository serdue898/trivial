package com.example.trivialnavidad.app

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.trivialnavidad.R

/**
 * Clase Configuracion para gestionar las preferencias de configuración de la aplicación.
 * Permite controlar opciones como la reproducción de música, sonido, vibración y temas oscuros.
 *
 * @property prefs Preferencias compartidas para almacenar las opciones de configuración.
 * @property modoOscuroActual Almacena el modo oscuro actual para evitar cambios innecesarios.
 * @constructor Crea una instancia de la clase Configuracion.
 * @param context Contexto de la aplicación.
 */
class Configuracion(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("Configuracion", Context.MODE_PRIVATE)

    // Métodos para gestionar la opción de reproducir música
    private fun guardarOpcionMusica(reproducir: Boolean) {
        prefs.edit().putBoolean("ReproducirMusica", reproducir).apply()
    }

    fun obtenerOpcionMusica(): Boolean {
        return prefs.getBoolean("ReproducirMusica", true) // Valor predeterminado: true
    }

    // Métodos para gestionar la opción de reproducir sonido
    private fun guardarOpcionSonido(reproducir: Boolean) {
        prefs.edit().putBoolean("ReproducirSonido", reproducir).apply()
    }

    fun obtenerOpcionSonido(): Boolean {
        return prefs.getBoolean("ReproducirSonido", true) // Valor predeterminado: true
    }

    // Métodos para gestionar la opción de reproducir vibración
    private fun guardarOpcionVibracion(reproducir: Boolean) {
        prefs.edit().putBoolean("ReproducirVibracion", reproducir).apply()
    }

    fun obtenerOpcionVibracion(): Boolean {
        return prefs.getBoolean("ReproducirVibracion", true) // Valor predeterminado: true
    }

    // Métodos para gestionar la opción de temas oscuros
    private fun guardarOcionTemas(tema: Boolean) {
        prefs.edit().putBoolean("Tema", tema).apply()
    }

    fun obtenerOpcionTemas(): Boolean {
        return prefs.getBoolean("Tema", true) // Valor predeterminado: true
    }

    /**
     * Muestra un diálogo de configuración con opciones para música, sonido, vibración y temas oscuros.
     *
     * @param contexto Contexto de la aplicación.
     * @param tema Booleano que indica si se permite cambiar el tema oscuro.
     */
    fun mostrarConfiguracion(contexto: Context, tema: Boolean = false) {
        val configuracion = MainActivity.configuracion
        val popup = AlertDialog.Builder(contexto as AppCompatActivity)

        // Inflar la vista del diálogo de configuración
        val vista = LayoutInflater.from(contexto).inflate(R.layout.popup_configuracion, null)
        val musica = vista.findViewById<CheckBox>(R.id.ch_musica)
        val sonido = vista.findViewById<CheckBox>(R.id.ch_sonido)
        val vibracion = vista.findViewById<CheckBox>(R.id.ch_vibracion)
        val temas = vista.findViewById<CheckBox>(R.id.ch_temas)

        // Configurar el estado de los CheckBox según las preferencias actuales
        musica.isChecked = configuracion?.obtenerOpcionMusica()!!
        sonido.isChecked = configuracion.obtenerOpcionSonido()
        vibracion.isChecked = configuracion.obtenerOpcionVibracion()
        temas.isChecked = configuracion.obtenerOpcionTemas()

        // Manejar cambios en la opción de música
        musica.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                MainActivity.reproductor?.iniciarReproduccion()
            } else {
                MainActivity.reproductor?.detenerReproduccion()
            }
        }

        // Manejar cambios en la opción de temas oscuros
        temas.setOnCheckedChangeListener { _, isChecked ->
            if (tema) {
                toggleDarkMode(isChecked)
                guardarOcionTemas(isChecked)
            } else {
                Toast.makeText(contexto, contexto.getString(R.string.error_tema), Toast.LENGTH_LONG).show()
                temas.isChecked = configuracion.obtenerOpcionTemas()
            }
        }

        // Configurar y mostrar el diálogo de configuración
        popup.setView(vista)
        popup.setTitle(contexto.getString(R.string.configuracion))
        popup.setCancelable(false)
        popup.setPositiveButton(contexto.getString(R.string.aceptar)) { _, _ ->
            configuracion.guardarOpcionMusica(musica.isChecked)
            configuracion.guardarOpcionSonido(sonido.isChecked)
            configuracion.guardarOpcionVibracion(vibracion.isChecked)
        }

        val alert = popup.create()
        alert.show()
    }

    /**
     * Cambia el modo oscuro de la aplicación.
     *
     * @param encender Booleano que indica si se debe encender el modo oscuro.
     */
    private var modoOscuroActual: Int = -1
    fun toggleDarkMode(encender: Boolean) {
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

