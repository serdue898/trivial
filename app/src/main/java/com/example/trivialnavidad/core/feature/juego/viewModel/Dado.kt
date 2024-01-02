package com.example.trivialnavidad.core.feature.juego.viewModel

import android.widget.ImageView
import com.example.trivialnavidad.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Dado(private val imageViewDado: ImageView) {

    private var lastRandomNumber = 0

    fun tiradaDado() {
        lastRandomNumber = 0
        cambiarImagenCadaSegundo()
    }

    private fun cambiarImagenCadaSegundo() {
        val job = GlobalScope.launch(Dispatchers.Main) {
            repeat(10) {
                val randomImageName = "dado${(1..6).random()}"
                // Asigna la imagen al ImageView
                val resourceId = obtenerResourceId(randomImageName)
                imageViewDado.setImageResource(resourceId)

                lastRandomNumber = randomImageName.last().toString().toInt()
                delay(300)
                imageViewDado.visibility = ImageView.VISIBLE
            }

            // Cuando termina el tiempo, devuelve el último número aleatorio
            // Puedes hacer lo que necesites con este número
            // En este ejemplo, simplemente lo imprimo
            println("Último número aleatorio: $lastRandomNumber")
            imageViewDado.visibility = ImageView.INVISIBLE
        }

        // Puedes cancelar el trabajo si es necesario (por ejemplo, si el usuario sale de la pantalla)
        // job.cancel()
    }

    private fun obtenerResourceId(imageName: String): Int {
        // Aquí deberías implementar la lógica para obtener el ID del recurso de la imagen
        // Puedes hacer esto de acuerdo a cómo tienes organizados tus recursos de imágenes
        // En este ejemplo, asumo que tienes imágenes con nombres como "dado1", "dado2", etc.
        return R.drawable::class.java.getField(imageName).get(null) as Int
    }

}