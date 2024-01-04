package com.example.trivialnavidad.app

import android.content.Context
import android.content.SharedPreferences

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
}