package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.app.Reproductor
import com.example.trivialnavidad.app.Vibracion
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Clase Dado: Representa un objeto dado utilizado en el juego, que cambia su imagen de forma aleatoria
 * durante un periodo de tiempo especificado.
 *
 * @property contexto Contexto de la aplicación.
 * @property imageViewDado Vista de la imagen del dado.
 * @property vibracion Objeto para gestionar la vibración del dispositivo.
 * @property reproductor Objeto para gestionar la reproducción de sonidos.
 * @property lastRandomNumber Último número aleatorio obtenido al cambiar la imagen del dado.
 */
class Dado(private val contexto: Context, private val imageViewDado: ImageView, private val vibracion: Vibracion?, private val reproductor: Reproductor?) {

    private var lastRandomNumber = 0

    /**
     * Método cambiarImagenCadaSegundo: Cambia la imagen del dado de forma aleatoria durante un periodo de tiempo,
     * con la opción de reproducir sonidos y vibrar el dispositivo.
     *
     * @param view Vista del fragmento que contiene el dado.
     * @return Número aleatorio obtenido al detener la animación del dado.
     */
    suspend fun cambiarImagenCadaSegundo(view: View): Int {
        val deferred = CompletableDeferred<Int>()

        // Inicia la reproducción de sonidos si está habilitada y pausa la música de fondo.
        if (reproductor != null) {
            reproductor.iniciarReproduccion()
            if (MainActivity.configuracion?.obtenerOpcionMusica()!!) MainActivity.reproductor?.pausarReproduccion()
        }

        // Utiliza una corrutina para cambiar la imagen del dado durante 10 ciclos.
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
            repeat(10) {
                val randomImageName = "dado${(1..6).random()}"
                val resourceId = obtenerResourceId(randomImageName)
                imageViewDado.setImageResource(resourceId)

                lastRandomNumber = randomImageName.last().toString().toInt()
                vibracion?.vibrar(100)
                delay(300)
            }

            // Obtiene el recurso de imagen correspondiente al último número aleatorio obtenido.
            val resourceId = obtenerResourceId("dado${lastRandomNumber}")
            imageViewDado.setImageResource(resourceId)

            // Hace visible el botón de salida en la vista.
            view.findViewById<Button>(R.id.bt_salir).visibility = View.VISIBLE

            // Completa la corrutina con el último número aleatorio obtenido.
            deferred.complete(lastRandomNumber)

            // Detiene la reproducción de sonidos y reanuda la música de fondo si está habilitada.
            if (reproductor != null) {
                reproductor.detenerReproduccion()
                if (MainActivity.configuracion?.obtenerOpcionMusica()!!) MainActivity.reproductor?.iniciarReproduccion()
            }
        }

        // Espera a que la corrutina termine y devuelve el último número aleatorio.
        return deferred.await()
    }

    /**
     * Método obtenerResourceId: Obtiene el identificador del recurso de imagen correspondiente al nombre de la imagen del dado.
     *
     * @param imageName Nombre de la imagen del dado.
     * @return Identificador del recurso de imagen.
     */
    private fun obtenerResourceId(imageName: String): Int {
        return R.drawable::class.java.getField(imageName).get(null) as Int
    }
}
