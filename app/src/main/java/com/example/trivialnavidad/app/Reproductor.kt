package com.example.trivialnavidad.app

import android.content.Context
import android.media.MediaPlayer

class Reproductor(private val context: Context, private val resourceId: Int) {

    private var mediaPlayer: MediaPlayer? = null
    private val configuracion = Configuracion(context)

    init {
        // Inicializar el MediaPlayer con el archivo de audio en res/raw
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.isLooping = true

        // Verificar la configuración antes de iniciar la reproducción
        if (configuracion.obtenerOpcionMusica()) {
            iniciarReproduccion()
        }
    }

    fun iniciarReproduccion() {
        mediaPlayer?.start()
    }

    fun detenerReproduccion() {
        mediaPlayer?.stop()
        mediaPlayer?.prepare()
        mediaPlayer?.seekTo(0)
    }
    fun pausarReproduccion() {
        mediaPlayer?.pause()
    }
    fun liberarRecursos() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // ... (resto de los métodos)
}