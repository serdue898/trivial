package com.example.trivialnavidad.app

import android.content.Context
import android.media.MediaPlayer

/**
 * Clase Reproductor: Gestiona la reproducción de música en la aplicación Trivial Navidad.
 *
 * @property mediaPlayer Instancia de MediaPlayer para reproducir archivos de audio.
 * @property configuracion Instancia de la clase Configuracion para gestionar las preferencias de configuración.
 * @constructor Crea una instancia de la clase Reproductor.
 * @param context Contexto de la aplicación.
 * @param resourceId Identificador del recurso de audio en res/raw.
 */
class Reproductor(context: Context, resourceId: Int) {

    private var mediaPlayer: MediaPlayer? = null
    private val configuracion = Configuracion(context)

    /**
     * Inicializa el Reproductor y configura la reproducción según las preferencias de música del usuario.
     */
    init {
        // Inicializar el MediaPlayer con el archivo de audio en res/raw
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.isLooping = true

        // Verificar la configuración antes de iniciar la reproducción
        if (configuracion.obtenerOpcionMusica()) {
            iniciarReproduccion()
        }
    }

    /**
     * Inicia la reproducción de música.
     */
    fun iniciarReproduccion() {
        mediaPlayer?.start()
    }

    /**
     * Detiene la reproducción de música, prepara el MediaPlayer para reiniciar y lo coloca al principio.
     */
    fun detenerReproduccion() {
        mediaPlayer?.stop()
        mediaPlayer?.prepare()
        mediaPlayer?.seekTo(0)
    }

    /**
     * Pausa la reproducción de música.
     */
    fun pausarReproduccion() {
        mediaPlayer?.pause()
    }

    /**
     * Libera los recursos utilizados por el MediaPlayer al finalizar su uso.
     */
    fun liberarRecursos() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
