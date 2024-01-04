package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.trivialnavidad.R
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog.Builder
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.app.Reproductor
import com.example.trivialnavidad.app.Vibracion

class Dado(private val imageViewDado: ImageView, private val vibracion: Vibracion?,private val reproductor: Reproductor? ) {

    private var lastRandomNumber = 0



    suspend fun cambiarImagenCadaSegundo(view: View): Int {
        val deferred = CompletableDeferred<Int>()
        if (reproductor != null){
            reproductor.iniciarReproduccion()
            if (MainActivity.configuracion?.obtenerOpcionMusica()!!)MainActivity.reproductor?.pausarReproduccion()

        }

        GlobalScope.launch(Dispatchers.Main) {
            repeat(10) {
                val randomImageName = "dado${(1..6).random()}"
                val resourceId = obtenerResourceId(randomImageName)
                imageViewDado.setImageResource(resourceId)

                lastRandomNumber = randomImageName.last().toString().toInt()
                if (vibracion != null) vibracion.vibrar(100)
                delay(300)
            }
            val resourceId = obtenerResourceId("dado${lastRandomNumber}")
            imageViewDado.setImageResource(resourceId)

            view.findViewById<Button>(R.id.bt_salir).visibility = View.VISIBLE
            deferred.complete(lastRandomNumber)

        }
        reproductor?.detenerReproduccion()
        if (MainActivity.configuracion?.obtenerOpcionMusica()!!)MainActivity.reproductor?.iniciarReproduccion()


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