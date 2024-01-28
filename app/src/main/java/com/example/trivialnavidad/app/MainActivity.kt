package com.example.trivialnavidad.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity.Companion.configuracion
import com.example.trivialnavidad.app.MainActivity.Companion.juego
import com.example.trivialnavidad.app.MainActivity.Companion.jugadorActual
import com.example.trivialnavidad.app.MainActivity.Companion.reproductor
import com.example.trivialnavidad.app.MainActivity.Companion.socket
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal
import io.socket.client.Socket


/**
 * MainActivity: Actividad principal de la aplicación Trivial Navidad.
 * Controla el ciclo de vida de la aplicación y gestiona la navegación entre fragmentos.
 *
 * @property juego Instancia de la clase Juego para la lógica del juego.
 * @property configuracion Instancia de la clase Configuracion para gestionar las preferencias de configuración.
 * @property reproductor Instancia de la clase Reproductor para la reproducción de música.
 * @property socket Instancia del socket para la comunicación en línea.
 * @property jugadorActual Jugador actual que está utilizando la aplicación.
 */
class MainActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var juego: Juego? = null
        var configuracion: Configuracion? = null
        @SuppressLint("StaticFieldLeak")
        var reproductor: Reproductor? = null
        var socket: Socket? = null
        var jugadorActual: Jugador? = null
    }

    /**
     * Método llamado cuando la actividad está creándose. Inicializa componentes y gestiona la navegación.
     *
     * @param savedInstanceState Estado anterior de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        configuracion = Configuracion(this)
        reproductor = Reproductor(this, R.raw.lobby_music)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Reemplaza con tu diseño

        if (savedInstanceState == null) {
            val principal = Principal()
            principal.empezar()
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }
    }

    /**
     * Método llamado cuando la actividad está siendo destruida. Libera recursos y realiza acciones de cierre.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos cuando la actividad se destruye
        reproductor?.liberarRecursos()
        if (jugadorActual != null) {
            if (jugadorActual?.partida != 0) {
                socket?.emit("desconectar", jugadorActual?.toJson())
            }

            socket?.emit("desloggear", jugadorActual?.id_jugador)
        }
    }
}
