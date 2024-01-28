package com.example.trivialnavidad.core.feature.principal.viewModel

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.cargarPartida.view.Partidas
import com.example.trivialnavidad.core.feature.seleccionJugadores.view.SeleccionJugador
import com.example.trivialnavidad.core.feature.unirseOnline.view.UnirseOnline


class MetodosPrincipal: ComunicadorPrincipal {

    override fun abrirJuego(datos: String, context: Context) {
        if (context is AppCompatActivity) {
            when (datos) {
                "cargar" -> {
                    val partidas = Partidas("offline")
                    val fragmentManager = context.supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.contenedor, partidas)
                        .commit()
                }
                "nueva" -> {
                    // Código para el caso "online", si es necesario
                    val seleccion = SeleccionJugador()
                    val fragmentManager = context.supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.contenedor, seleccion)
                        .commit()
                }
                "online-crear" -> {
                    val popup = AlertDialog.Builder(context)
                    popup.setTitle("Crear partida")
                    val view = context.layoutInflater.inflate(R.layout.partida_online, null)
                    popup.setView(view)

                    popup.setPositiveButton("Crear") { _, _ ->
                        val nombre = view.findViewById<EditText>(R.id.et_nombre_partida).text.toString()
                        val socket = MainActivity.socket
                        socket?.emit("crearPartida", nombre)
                        socket?.on("partidaCreada") { args ->

                            val id = args[0].toString()
                            if (id == "null") {
                                context.runOnUiThread {
                                    val errorPopup = AlertDialog.Builder(context)
                                    errorPopup.setTitle("Error")
                                    errorPopup.setMessage("Ya existe una partida con ese nombre")
                                    errorPopup.setPositiveButton("Aceptar") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    errorPopup.show()
                                }
                            } else {
                                val unirse = UnirseOnline(id.toInt())
                                unirse.host = true
                                val fragmentManager = context.supportFragmentManager
                                fragmentManager.beginTransaction()
                                    .replace(R.id.contenedor, unirse)
                                    .commit()
                            }
                        }
                    }
                    context.runOnUiThread {
                        popup.show()
                    }


                }
                "online-cargar" -> {


                    val partidas = Partidas("online")
                    partidas.cogerPartidas()
                    val fragmentManager = context.supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.contenedor, partidas)
                        .commit()

                }
            }
        }
    }

}