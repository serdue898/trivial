package com.example.trivialnavidad.core.feature.juego.viewModel

import android.widget.ImageView
import com.example.trivialnavidad.R
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Dado(private val imageViewDado: ImageView) {

    private var lastRandomNumber = 0

    fun tiradaDado() {
        lastRandomNumber = 0

    }

    suspend fun cambiarImagenCadaSegundo(): Int {
        val deferred = CompletableDeferred<Int>()

        GlobalScope.launch(Dispatchers.Main) {
            repeat(10) {
                val randomImageName = "dado${(1..6).random()}"
                val resourceId = obtenerResourceId(randomImageName)
                imageViewDado.setImageResource(resourceId)

                val lastRandomNumber = randomImageName.last().toString().toInt()
                delay(300)
                imageViewDado.visibility = ImageView.VISIBLE

                deferred.complete(lastRandomNumber)
            }

            imageViewDado.visibility = ImageView.INVISIBLE
        }

        // Espera a que la corrutina termine y obtén el último número aleatorio
        return deferred.await()
    }

    private fun obtenerResourceId(imageName: String): Int {
        // Aquí deberías implementar la lógica para obtener el ID del recurso de la imagen
        // Puedes hacer esto de acuerdo a cómo tienes organizados tus recursos de imágenes
        // En este ejemplo, asumo que tienes imágenes con nombres como "dado1", "dado2", etc.
        return R.drawable::class.java.getField(imageName).get(null) as Int
    }

}